package com.connectify.connectify.DTO.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PublicAccountDetailResponse extends PublicAccountResponse{
    private RelationshipResponse relationship;
}
