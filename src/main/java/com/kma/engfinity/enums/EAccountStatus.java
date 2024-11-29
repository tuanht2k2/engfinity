package com.kma.engfinity.enums;

public enum EAccountStatus {

    ACTIVE("active"),
    INACTIVE("inactive"),
    BLOCKED("blocked");

    private final String status;

    private EAccountStatus(String status) {
        this.status = status;
    }

    private String getStatus () {
        return status;
    }

}
