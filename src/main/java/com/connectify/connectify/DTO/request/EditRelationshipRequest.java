package com.connectify.connectify.DTO.request;

import com.connectify.connectify.enums.ERelationshipStatus;
import lombok.Data;

@Data
public class EditRelationshipRequest {
    private String id;
    private String receiverId;
    private ERelationshipStatus status;
}
