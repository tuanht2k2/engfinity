package com.kma.engfinity.service;

import com.kma.engfinity.DTO.request.CommonDeleteRequest;
import com.kma.engfinity.DTO.request.CommonSearchRequest;
import com.kma.engfinity.DTO.request.EditAccountRequest;
import com.kma.engfinity.DTO.request.SearchAccountByPhoneNumbersRequest;
import com.kma.engfinity.DTO.response.*;
import com.kma.engfinity.enums.EError;
import com.kma.engfinity.exception.CustomException;
import com.kma.engfinity.entity.Account;
import com.kma.engfinity.repository.AccountRepository;
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
    ModelMapper modelMapper;

    @Autowired
    AuthService authService;



    private BCryptPasswordEncoder passwordEncoder;

    AccountService() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public ResponseEntity<CommonResponse<String>> createAccount (EditAccountRequest editAccountRequest) {
        if (accountRepository.existsByEmail(editAccountRequest.getEmail()))
            throw new CustomException(EError.EXISTED_BY_EMAIL);
        if (accountRepository.existsByPhoneNumber(editAccountRequest.getPhoneNumber()))
            throw  new CustomException(EError.EXISTED_BY_PHONE_NUMBER);
        String encodedPassword = passwordEncoder.encode(editAccountRequest.getPassword());

        Account newAccount = modelMapper.map(editAccountRequest, Account.class);
        newAccount.setCreatedAt(new Date());
        newAccount.setPassword(encodedPassword);


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
            PublicAccountResponse accountResponse = modelMapper.map(account, PublicAccountResponse.class);

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
