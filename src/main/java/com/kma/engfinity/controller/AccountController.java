package com.kma.engfinity.controller;

import com.kma.engfinity.DTO.request.CommonDeleteRequest;
import com.kma.engfinity.DTO.request.CommonSearchRequest;
import com.kma.engfinity.DTO.request.SearchAccountByPhoneNumbersRequest;
import com.kma.engfinity.DTO.response.CommonResponse;
import com.kma.engfinity.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
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
