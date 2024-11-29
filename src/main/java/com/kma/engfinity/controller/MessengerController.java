package com.kma.engfinity.controller;

import com.kma.engfinity.DTO.request.EditMessengerRequest;
import com.kma.engfinity.DTO.request.SearchMessengerOfAccountRequest;
import com.kma.engfinity.DTO.request.SearchPersonalMessengerByMember;
import com.kma.engfinity.service.MessengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/messengers")
public class MessengerController {
    @Autowired
    MessengerService messengerService;

    @GetMapping("{id}")
    public ResponseEntity<?> get (@PathVariable String id) {
        return messengerService.get(id);
    }

    @PostMapping
    public ResponseEntity<?> create (@RequestBody EditMessengerRequest request) {
        return messengerService.create(request);
    }

    @PostMapping("find-personal-by-members")
    public ResponseEntity<?> findByMembers (@RequestBody SearchPersonalMessengerByMember request) {
        return messengerService.findPersonalByMember(request);
    }

    @PostMapping("search-by-account")
    public ResponseEntity<?> searchMessengerOfAccount (@RequestBody SearchMessengerOfAccountRequest request) {
        return messengerService.searchMessengerOfAccount(request);
    }

    @PostMapping("add-members")
    public ResponseEntity<?> addMember (@RequestBody EditMessengerRequest request) {
        return messengerService.addMember(request);
    }
}
