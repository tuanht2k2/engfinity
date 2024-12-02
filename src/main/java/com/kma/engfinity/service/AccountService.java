package com.kma.engfinity.service;

import com.kma.engfinity.DTO.request.*;
import com.kma.engfinity.DTO.response.*;
import com.kma.engfinity.enums.EError;
import com.kma.engfinity.enums.ERole;
import com.kma.engfinity.enums.ETransferType;
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
        newAccount.setRole(ERole.USER);
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

    public ResponseEntity<?> update (EditAccountRequest request) {
        Optional<Account> optionalAccount = accountRepository.findById(request.getId());
        if (optionalAccount.isEmpty()) throw new CustomException(EError.USER_NOT_EXISTED);
        Account account = optionalAccount.get();
        if (request.getName() != null) account.setName(request.getName());
        if (request.getPhoneNumber() != null) account.setPhoneNumber(request.getPhoneNumber());
        if (request.getEmail() != null) account.setEmail(request.getEmail());
        accountRepository.save(account);
        CommonResponse<?> response = new CommonResponse<>(200, null, "Update account successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> updateRole (EditAccountRequest request) {
        Account currentAccount = authService.getCurrentAccount();
        if (!currentAccount.getRole().equals(ERole.ADMIN)) throw new CustomException(EError.UNAUTHORIZED);
        Optional<Account> optionalAccount = accountRepository.findById(request.getId());
        if (optionalAccount.isEmpty()) throw new CustomException(EError.USER_NOT_EXISTED);
        Account account = optionalAccount.get();
        account.setRole(request.getRole());
        accountRepository.save(account);
        CommonResponse<?> response = new CommonResponse<>(200, null, "Update role successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public void updateBalance(EditAccountBalanceRequest request) {
        Optional<Account> optionalAccount = accountRepository.findById(request.getId());
        if (optionalAccount.isEmpty()) throw new CustomException(EError.USER_NOT_EXISTED);
        Account account = optionalAccount.get();
        if (request.getType().equals(ETransferType.INCOMING)) {
            account.setBalance(account.getBalance() + request.getAmount());
        } else {
            if (account.getBalance() < request.getAmount()) throw new CustomException(EError.BAD_REQUEST);
            account.setBalance(account.getBalance() - request.getAmount());
        }
        accountRepository.save(account);
    }
}
