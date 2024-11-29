package com.kma.engfinity.service;

import org.springframework.stereotype.Service;

@Service
public class CommonService {
    public String getAccountNotificationUrl (String accountId) {
        return "/topic/" + accountId + "/notifications";
    }

    public String getMessengerWebRTCUrl (String accountId) {
        return "/topic/accounts/" + accountId + "/messengers";
    }
}
