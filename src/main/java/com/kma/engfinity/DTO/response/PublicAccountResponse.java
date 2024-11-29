package com.kma.engfinity.DTO.response;

import com.kma.engfinity.enums.EGender;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class PublicAccountResponse {
    private String id;
    private String profileImage;
    private String name;
}
