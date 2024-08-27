package com.connectify.connectify.DTO.response;

import com.connectify.connectify.enums.EGender;
import lombok.Data;

import java.util.Date;

@Data
public class AccountResponse {
    private String username;
    private String email;
    private String phoneNumber;
    private String password;
    private String displayName;
    private String profileImage;
    private String identificationNumber;
    private String fullName;
    private Date dob;
    private String address;
    private EGender gender;
}
