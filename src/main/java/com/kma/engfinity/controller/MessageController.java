package com.kma.engfinity.controller;

import com.kma.engfinity.DTO.request.EditMessageRequest;
import com.kma.engfinity.DTO.request.SearchMessageRequest;
import com.kma.engfinity.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/messages")
public class MessageController {
    @Autowired
    MessageService messageService;

    @PostMapping("")
    public ResponseEntity<?> create (@RequestBody EditMessageRequest request) {
        return messageService.create(request);
    }

    @PostMapping("/search")
    public ResponseEntity<?> search (@RequestBody SearchMessageRequest request) {
        return messageService.search(request);
    }
}
