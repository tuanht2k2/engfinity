package com.connectify.connectify.DTO.request;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String phoneNumber;
    private String password;
}
