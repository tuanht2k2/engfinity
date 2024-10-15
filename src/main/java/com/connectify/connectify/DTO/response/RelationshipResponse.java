package com.connectify.connectify.DTO.response;

import com.connectify.connectify.enums.ERelationshipStatus;
import lombok.Data;
import java.util.Date;

@Data
public class RelationshipResponse {
    private String id;
    private Date createdAt;
    private Date updatedAt;
    private PublicAccountResponse updatedBy;
    private PublicAccountResponse createdBy;
    private PublicAccountResponse receiver;
    private ERelationshipStatus status;
}
