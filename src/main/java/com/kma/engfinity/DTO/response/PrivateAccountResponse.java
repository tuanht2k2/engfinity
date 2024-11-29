package com.kma.engfinity.DTO.response;

import com.kma.engfinity.enums.EGender;
import lombok.Data;

import java.util.Date;

@Data
public class PrivateAccountResponse {
    private String id;
    private String email;
    private String phoneNumber;
    private String profileImage;
    private String name;
    private Long balance;
}
