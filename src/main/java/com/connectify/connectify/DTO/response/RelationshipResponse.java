package com.connectify.connectify.DTO.response;

import com.connectify.connectify.enums.ERelationshipStatus;
import lombok.Data;
import java.util.Date;

@Data
public class RelationshipResponse {
    private String id;
    private Date createdAt;
    private Date updatedAt;
    private String updatedBy;
    private String createdBy;
    private String receiverId;
    private ERelationshipStatus status;
}
