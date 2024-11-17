package com.connectify.connectify.service;

import com.connectify.connectify.DTO.request.CommonSearchRequest;
import com.connectify.connectify.DTO.request.EditReportCommentRequest;
import com.connectify.connectify.DTO.response.CommonResponse;
import com.connectify.connectify.entity.Account;
import com.connectify.connectify.entity.Comment;
import com.connectify.connectify.entity.CommentReport;
import com.connectify.connectify.enums.EError;
import com.connectify.connectify.exception.CustomException;
import com.connectify.connectify.repository.CommentReportRepository;
import com.connectify.connectify.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CommentReportService {
    @Autowired
    CommentReportRepository commentReportRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    AuthService authService;

    public ResponseEntity<?> create (EditReportCommentRequest request) {
        Optional<Comment> optionalComment = commentRepository.findById(request.getCommentId());
        Account currentAccount = authService.getCurrentAccount();
        if (optionalComment.isEmpty()) throw new CustomException(EError.BAD_REQUEST);
        Comment comment = optionalComment.get();
        CommentReport commentReport = new CommentReport();
        commentReport.setCreatedAt(new Date());
        commentReport.setCreatedBy(currentAccount);
        commentReport.setComment(comment);
        commentReport.setReason(request.getReason());
        commentReportRepository.save(commentReport);
        CommonResponse<?> commonResponse = new CommonResponse<>(200, null, "Report comment successfully!");
        return new ResponseEntity<>(commonResponse, HttpStatus.OK);
    }

    public ResponseEntity<?> search (CommonSearchRequest request) {
        List<CommentReport> commentReports = commentReportRepository.findAll();
        CommonResponse<?> response = new CommonResponse<>(200, commentReports, "Search comments successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
