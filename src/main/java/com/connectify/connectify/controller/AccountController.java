package com.connectify.connectify.controller;

import com.connectify.connectify.DTO.request.CommonDeleteRequest;
import com.connectify.connectify.DTO.request.CommonSearchRequest;
import com.connectify.connectify.DTO.request.SearchAccountByPhoneNumbersRequest;
import com.connectify.connectify.DTO.response.CommonResponse;
import com.connectify.connectify.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {
    @Autowired
    AccountService accountService;

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<?>> getAccount (@PathVariable String id) {
        return accountService.getAccount(id);
    }

    @PostMapping("search")
    public ResponseEntity<?> search (@RequestBody CommonSearchRequest request) {
        return accountService.search(request);
    }

    @PostMapping("delete")
    public ResponseEntity<?> delete (@RequestBody CommonDeleteRequest request) {
        return accountService.delete(request);
    }

    @PostMapping("search-by-phone-numbers")
    public ResponseEntity<?> searchByPhoneNumbers (@RequestBody SearchAccountByPhoneNumbersRequest request) {
        return accountService.searchByPhoneNumbers(request);
    }
}
