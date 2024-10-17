package com.connectify.connectify.DTO.request;

import com.connectify.connectify.enums.EVideoCallStatus;
import lombok.Data;

@Data
public class EditVideoCallRequest {
    private String messengerId;
    private EVideoCallStatus status;
    private String sdp;
    private String candidate;
}
