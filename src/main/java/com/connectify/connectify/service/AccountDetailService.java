package com.connectify.connectify.service;

import com.connectify.connectify.enums.EError;
import com.connectify.connectify.exception.CustomException;
import com.connectify.connectify.model.Account;
import com.connectify.connectify.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountDetailService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        Optional<Account> optionalAccount = accountRepository.findByPhoneNumber(phoneNumber);
        if (optionalAccount.isEmpty()) throw new CustomException(EError.USER_NOT_EXISTED);
        Account account = optionalAccount.get();
        return new org.springframework.security.core.userdetails.User(
                account.getPhoneNumber(),
                account.getPassword(),
                AuthorityUtils.createAuthorityList(String.valueOf(account.getRoles()))
        );
    }

}
