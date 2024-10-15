package com.connectify.connectify.service;

import com.connectify.connectify.DTO.request.EditRelationshipRequest;
import com.connectify.connectify.DTO.request.SearchRelationshipRequest;
import com.connectify.connectify.DTO.response.CommonResponse;
import com.connectify.connectify.DTO.response.RelationshipResponse;
import com.connectify.connectify.entity.Account;
import com.connectify.connectify.entity.Relationship;
import com.connectify.connectify.enums.EError;
import com.connectify.connectify.enums.ERelationshipStatus;
import com.connectify.connectify.exception.CustomException;
import com.connectify.connectify.repository.AccountRepository;
import com.connectify.connectify.repository.RelationshipRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RelationshipService {
    @Autowired
    RelationshipRepository relationshipRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AuthService authService;

    @Autowired
    ModelMapper mapper;

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    public ResponseEntity<?> create(EditRelationshipRequest request) {
        Optional<Account> optionalAccount = accountRepository.findById(request.getReceiverId());
        if (optionalAccount.isEmpty()) throw new CustomException(EError.BAD_REQUEST);
        Account currentAccount = authService.getCurrentAccount();
        Relationship relationship = new Relationship();
        relationship.setCreatedAt(new Date());
        relationship.setReceiver(optionalAccount.get());
        relationship.setCreatedBy(currentAccount);
        relationship.setStatus(ERelationshipStatus.PENDING);
        Relationship createdRelationship = relationshipRepository.save(relationship);
        CommonResponse<?> response = new CommonResponse<>(200, null, "Send request successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> delete(List<String> ids) {
        relationshipRepository.deleteAllById(ids);
        CommonResponse<?> response = new CommonResponse<>(200, null, "Delete relationship successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> accept(String id) {
        Optional<Relationship> optionalRelationship = relationshipRepository.findById(id);
        if (optionalRelationship.isEmpty()) throw new CustomException(EError.BAD_REQUEST);
        Account currentAccount = authService.getCurrentAccount();
        Relationship relationship = optionalRelationship.get();
        relationship.setUpdatedBy(currentAccount);
        relationship.setUpdatedAt(new Date());
        relationship.setStatus(ERelationshipStatus.FRIEND);
        relationshipRepository.save(relationship);
        CommonResponse<?> response = new CommonResponse<>(200, null, "Accept relationship successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public RelationshipResponse getRelationshipByAccounts (String targetAccount) {
        Account currentAccount = authService.getCurrentAccount();
        Optional<Relationship> optionalRelationship = relationshipRepository.findRelationshipByAccounts(currentAccount.getId(), targetAccount);
        if (optionalRelationship.isEmpty()) return null;
        Relationship relationship = optionalRelationship.get();
        return mapper.map(relationship, RelationshipResponse.class);
    }

    public ResponseEntity<?> getFriendRequests () {
        Account currentAccount = authService.getCurrentAccount();
        List<Relationship> relationships = relationshipRepository.findByReceiverIdAndStatus(currentAccount.getId(), ERelationshipStatus.PENDING);
        List<RelationshipResponse> relationshipResponses = relationships.stream().map(this::relationshipToRelationshipResponse).toList();
        CommonResponse<?> response = new CommonResponse<>(200, relationshipResponses, "Get friend requests successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public RelationshipResponse relationshipToRelationshipResponse (Relationship relationship) {
        return mapper.map(relationship, RelationshipResponse.class);
    }
}
