package com.connectify.connectify.service;

import org.springframework.stereotype.Service;

@Service
public class CommonService {
    public String getAccountNotificationUrl (String accountId) {
        return "/topic/" + accountId + "/notifications";
    }
}
