package com.kma.engfinity.DTO.request;

import com.kma.engfinity.entity.Account;
import com.kma.engfinity.enums.EPaymentType;
import lombok.Data;

import java.util.Date;

@Data
public class EditPaymentRequest {
    private String id;
    private Account createdBy;
    private Date createdAt;
    private Account updatedBy;
    private Date updatedAt;
    private EPaymentType type;
    private Long amount;
    private String description;
}
