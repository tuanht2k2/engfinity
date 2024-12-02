package com.kma.engfinity.DTO.request;

import com.kma.engfinity.enums.EGender;
import com.kma.engfinity.enums.ERole;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class EditAccountRequest {
    private String id;
    private String email;
    private String phoneNumber;
    private String password;
    private String profileImage;
    private String name;
    private Long balance;
    private ERole role;
}
