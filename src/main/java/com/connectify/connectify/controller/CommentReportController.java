package com.connectify.connectify.controller;

import com.connectify.connectify.DTO.request.CommonSearchRequest;
import com.connectify.connectify.DTO.request.EditReportCommentRequest;
import com.connectify.connectify.service.CommentReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/comment-reports")
public class CommentReportController {
    @Autowired
    CommentReportService commentReportService;

    @PostMapping("")
    public ResponseEntity<?> create (@RequestBody EditReportCommentRequest request) {
        return commentReportService.create(request);
    }

    @PostMapping("search")
    public ResponseEntity<?> search (@RequestBody CommonSearchRequest request) {
        return commentReportService.search(request);
    }
}
