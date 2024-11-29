package com.kma.engfinity.DTO.request;

import com.kma.engfinity.enums.EGender;
import lombok.Data;

import java.util.Date;

@Data
public class EditAccountRequest {
    private String email;
    private String phoneNumber;
    private String password;
    private String profileImage;
    private String name;
}
