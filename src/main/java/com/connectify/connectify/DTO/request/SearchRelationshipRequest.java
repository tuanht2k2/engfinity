package com.connectify.connectify.DTO.request;

import com.connectify.connectify.enums.ERelationshipStatus;
import lombok.Data;

@Data
public class SearchRelationshipRequest {
    private String receiver_id;
    private String createdBy;
    private ERelationshipStatus status;
}
