package com.connectify.connectify.controller;

import com.connectify.connectify.DTO.VideoCallDto;
import com.connectify.connectify.service.WebRTCService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/web-rtc")
public class WebRTCController {
    @Autowired
    WebRTCService webRTCService;

    @PostMapping("start-video-call")
    public ResponseEntity<?> startVideoCall (VideoCallDto request) {
        return webRTCService.startVideoCall(request);
    }

    @PostMapping("accept-video-call")
    public ResponseEntity<?> acceptVideoCall (VideoCallDto request) {
        return webRTCService.acceptVideoCall(request);
    }
}
