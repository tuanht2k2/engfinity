package com.connectify.connectify.service;

import com.connectify.connectify.DTO.request.CommonDeleteRequest;
import com.connectify.connectify.DTO.request.CommonSearchRequest;
import com.connectify.connectify.DTO.request.EditAccountRequest;
import com.connectify.connectify.DTO.request.SearchAccountByPhoneNumbersRequest;
import com.connectify.connectify.DTO.response.*;
import com.connectify.connectify.entity.Role;
import com.connectify.connectify.enums.EError;
import com.connectify.connectify.enums.ERole;
import com.connectify.connectify.exception.CustomException;
import com.connectify.connectify.entity.Account;
import com.connectify.connectify.repository.AccountRepository;
import com.connectify.connectify.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.*;

@Service
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AccountService {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AuthService authService;

    @Autowired
    RelationshipService relationshipService;


    private BCryptPasswordEncoder passwordEncoder;

    AccountService() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public ResponseEntity<CommonResponse<String>> createAccount (EditAccountRequest editAccountRequest) {
        if (accountRepository.existsByEmail(editAccountRequest.getEmail()))
            throw new CustomException(EError.EXISTED_BY_EMAIL);
        if (accountRepository.existsByPhoneNumber(editAccountRequest.getPhoneNumber()))
            throw  new CustomException(EError.EXISTED_BY_PHONE_NUMBER);
        if (accountRepository.existsByIdentificationNumber(editAccountRequest.getIdentificationNumber()))
            throw  new CustomException(EError.EXISTED_BY_IDENTIFICATION_NUMBER);
        String encodedPassword = passwordEncoder.encode(editAccountRequest.getPassword());

        Account newAccount = modelMapper.map(editAccountRequest, Account.class);
        newAccount.setCreatedAt(new Date());
        newAccount.setPassword(encodedPassword);

        // set default role: user
        Role accountRole = roleRepository.findByName(ERole.USER);

        accountRepository.save(newAccount);

        CommonResponse<String> response = new CommonResponse<>(200, "", "Create account successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<CommonResponse<?>> getAccount (String id) {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isEmpty()) throw new CustomException(EError.BAD_REQUEST);
        Account account = optionalAccount.get();
        Account currentAccount = authService.getCurrentAccount();
        CommonResponse<?> response;
        if (account.getId().equals(currentAccount.getId())) {
            PrivateAccountResponse accountResponse = modelMapper.map(account, PrivateAccountResponse.class);
            response = new CommonResponse<>(200, accountResponse, "Get account successfully!");
        } else {
            PublicAccountDetailResponse accountResponse = modelMapper.map(account, PublicAccountDetailResponse.class);
            RelationshipResponse relationshipResponse = relationshipService.getRelationshipByAccounts(id);
            if (relationshipResponse != null) {
                accountResponse.setRelationship(relationshipResponse);
            }
            response = new CommonResponse<>(200, accountResponse, "Get account successfully!");
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private PublicAccountResponse accountToPublicAccountResponse(Account account) {
        return modelMapper.map(account, PublicAccountResponse.class);
    }

    public ResponseEntity<?> search (CommonSearchRequest request) {
        List<Account> accounts = accountRepository.search(request.getPage() * request.getPageSize(), request.getPageSize(), request.getSortBy(), request.getSortDir(), request.getKeyword());
        List<PublicAccountResponse> accountResponses = accounts.stream().map(this::accountToPublicAccountResponse).toList();
        CommonResponse<?> response = new CommonResponse<>(200, accountResponses, "Search accounts successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> updateRole (ERole roleName, String accountId) {
        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        if (optionalAccount.isEmpty()) throw  new CustomException(EError.USER_NOT_EXISTED);
        Account currentAccount = authService.getCurrentAccount();
        if (!authService.hasRole(currentAccount.getId(), ERole.ADMIN)) throw new CustomException(EError.UNAUTHORIZED);
        Account targetAccount = optionalAccount.get();
        Set<Role> targetAccountRoles = targetAccount.getRoles().isEmpty() ? new HashSet<Role>() : targetAccount.getRoles();

        Role role = roleRepository.findByName(roleName);
        targetAccountRoles.add(role);
        targetAccount.setRoles(targetAccountRoles);
        accountRepository.save(targetAccount);

        CommonResponse<?> response = new CommonResponse<>(200, null, "Update role successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> delete (CommonDeleteRequest request) {
        accountRepository.deleteAllById(request.getIds());
        CommonResponse<?> response = new CommonResponse<>(200, null, "Delete accounts successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> searchByPhoneNumbers (SearchAccountByPhoneNumbersRequest request) {
        List<Account> accounts = accountRepository.searchByPhoneNumbers(request.getPhoneNumbers());
        List<PublicAccountResponse> accountResponses = accounts.stream().map(this::accountToPublicAccountResponse).toList();
        CommonResponse<?> response = new CommonResponse<>(200, accountResponses, "Search accounts successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
