package com.kma.engfinity.DTO.response;

import com.kma.engfinity.enums.EGender;
import lombok.Data;

import java.util.Date;

@Data
public class PrivateAccountResponse extends PublicAccountResponse{
    private String email;
    private String phoneNumber;
    private Long balance;
}
