package com.connectify.connectify.DTO.request;

import lombok.Data;

import java.util.List;

@Data
public class SearchAccountByPhoneNumbersRequest {
    private List<String> phoneNumbers;
}
