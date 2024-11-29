package com.kma.engfinity.DTO.response;

import lombok.Data;

@Data
public class MomoPaymentResultResponse {
    private String orderId;
    private String resultCode;
    private String message;
    private String signature;
}
