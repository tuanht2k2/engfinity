package com.connectify.connectify.DTO.request;

import lombok.Data;

import java.util.List;

@Data
public class CommonDeleteRequest {
    private List<String> ids;
}
