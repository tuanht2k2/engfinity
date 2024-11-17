package com.connectify.connectify.controller;

import com.connectify.connectify.DTO.request.EditCommentRequest;
import com.connectify.connectify.DTO.request.EditReportCommentRequest;
import com.connectify.connectify.DTO.request.SearchCommentRequest;
import com.connectify.connectify.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/comments")
public class CommentController {
    @Autowired
    CommentService commentService;

    @PostMapping("")
    public ResponseEntity<?> create (@RequestBody EditCommentRequest request) throws IOException {
        return commentService.create(request);
    }

    @PostMapping("/search")
    public ResponseEntity<?> search (@RequestBody SearchCommentRequest request) {
        return commentService.search(request);
    }
}
