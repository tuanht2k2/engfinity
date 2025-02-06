package com.kma.engfinity.service;

import com.kma.common.entity.Account;
import com.kma.engfinity.DTO.request.EditMessengerRequest;
import com.kma.engfinity.DTO.request.SearchMessengerOfAccountRequest;
import com.kma.engfinity.DTO.request.SearchPersonalMessengerByMember;
import com.kma.engfinity.DTO.response.CommonResponse;
import com.kma.engfinity.DTO.response.MessengerResponse;
import com.kma.engfinity.entity.Messenger;
import com.kma.engfinity.enums.EError;
import com.kma.engfinity.enums.EMessengerType;
import com.kma.engfinity.exception.CustomException;
import com.kma.engfinity.repository.AccountRepository;
import com.kma.engfinity.repository.MessengerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MessengerService {

    @Autowired
    MessengerRepository messengerRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AuthService authService;

    @Autowired
    ModelMapper mapper;

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    private String p_create(EditMessengerRequest request) {
        if (request.getType().equals(EMessengerType.GROUP) && request.getMembers().size() < 2) throw new CustomException(EError.BAD_REQUEST);
        Account currentAccount = authService.getCurrentAccount();
        request.getMembers().add(currentAccount.getId());
        List<Account> accounts =  accountRepository.findAllById(request.getMembers());
        Set<Account> accountSet = new HashSet<>(accounts);

        if (accountSet.size() < 2 || request.getType() == null) throw new CustomException(EError.BAD_REQUEST);
        Messenger messenger = new Messenger();
        messenger.setCreatedAt(new Date());
        messenger.setCreatedBy(currentAccount);
        messenger.setMembers(accountSet);
        if (request.getName() != null && request.getName().isEmpty()) {
            messenger.setName("Cuộc trò chuyện mới");
        } else {
            messenger.setName(request.getName());
        }
        messenger.setType(request.getType());
        messenger.setName(request.getName());
        Messenger createdMessenger = messengerRepository.save(messenger);
        return createdMessenger.getId();
    }

    public ResponseEntity<?> create(EditMessengerRequest request) {
        String createdMessengerId = p_create(request);

        CommonResponse<?> response = new CommonResponse<>(200, createdMessengerId, "Create successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> get(String id) {
        Optional<Messenger> optionalMessenger = messengerRepository.findById(id);
        if (optionalMessenger.isEmpty()) throw new CustomException(EError.BAD_REQUEST);
        MessengerResponse messengerResponse = mapper.map(optionalMessenger.get(), MessengerResponse.class);
        CommonResponse<?> response = new CommonResponse<>(200, messengerResponse, "Get messenger successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> findPersonalByMember (SearchPersonalMessengerByMember searchRequest) {
        Account currentAccount = authService.getCurrentAccount();
        Optional<Messenger> optionalMessenger = messengerRepository.findPersonalByMembers(currentAccount.getId(), searchRequest.getTargetMember());
        if (optionalMessenger.isPresent()) {
            CommonResponse<String> response = new CommonResponse<>(200, optionalMessenger.get().getId(), "Messenger is existed!");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        EditMessengerRequest request = new EditMessengerRequest();
        List<String> members = new ArrayList<>();
        members.add(searchRequest.getTargetMember());
        request.setMembers(members);
        request.setType(EMessengerType.PERSONAL);
        String createdMessengerId = p_create(request);
        CommonResponse<String> response = new CommonResponse<>(200, createdMessengerId, "Create messenger successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> searchMessengerOfAccount (SearchMessengerOfAccountRequest request) {
        List<Messenger> messengers = messengerRepository.findMessengersOfAccount(request.getAccountId());
        List<MessengerResponse> messengerResponses = messengers.stream().map(this::messengerToMessengerResponse).toList();
        CommonResponse<?> response = new CommonResponse<>(200, messengerResponses, "Search messenger successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> addMember (EditMessengerRequest request) {
        Optional<Messenger> optionalMessenger = messengerRepository.findById(request.getId());
        if (optionalMessenger.isEmpty()) throw new CustomException(EError.BAD_REQUEST);
        Messenger messenger = optionalMessenger.get();
        if (messenger.getType().equals(EMessengerType.PERSONAL)) throw new CustomException(EError.BAD_REQUEST);
        List<Account> members = accountRepository.findAllById(request.getMembers());
        messenger.getMembers().addAll(members);
        messengerRepository.save(messenger);
        CommonResponse<?> response = new CommonResponse<>(200, null, "Add members successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public MessengerResponse messengerToMessengerResponse (Messenger messenger) {
        return mapper.map(messenger, MessengerResponse.class);
    }

    //internal use
    public Messenger s_get (String id) {
        Optional<Messenger> optionalMessenger = messengerRepository.findById(id);
        if (optionalMessenger.isEmpty()) throw new CustomException(EError.BAD_REQUEST);
        return optionalMessenger.get();
    }

}
