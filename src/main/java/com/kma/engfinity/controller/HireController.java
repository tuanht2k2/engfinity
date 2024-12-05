package com.kma.engfinity.controller;

import com.kma.engfinity.DTO.request.EditHireRequest;
import com.kma.engfinity.service.HireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/hires")
public class HireController {
    @Autowired
    private HireService hireService;

    @PostMapping
    public ResponseEntity<?> create (@RequestBody EditHireRequest request) {
        return hireService.create(request);
    }
}
