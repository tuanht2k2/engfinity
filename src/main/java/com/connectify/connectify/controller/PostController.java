package com.connectify.connectify.controller;

import com.connectify.connectify.DTO.request.EditPostRequest;
import com.connectify.connectify.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/posts")
public class PostController {
    @Autowired
    PostService postService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> create (@ModelAttribute EditPostRequest request) {
        return postService.create(request);
    }
}
