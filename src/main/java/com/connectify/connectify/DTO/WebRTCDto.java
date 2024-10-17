package com.connectify.connectify.DTO;

import lombok.Data;

@Data
public class WebRTCDto {
    private String type; // offer | answer | candidate
    private String sdp;
    private String candidate;
}
