package com.connectify.connectify.DTO.request;

import lombok.Data;

import java.util.List;

@Data
public class EditReportCommentRequest {
    private String commentId;
    private String reason;
}
