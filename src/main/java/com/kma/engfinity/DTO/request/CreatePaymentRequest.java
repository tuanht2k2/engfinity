package com.kma.engfinity.DTO.request;

import lombok.Data;

@Data
public class CreatePaymentRequest {
    private Long amount;
    private String orderInfo;
}
