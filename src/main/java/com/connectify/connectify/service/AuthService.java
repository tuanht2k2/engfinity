package com.connectify.connectify.service;

import com.connectify.connectify.DTO.request.AuthenticationRequest;
import com.connectify.connectify.DTO.response.CommonResponse;
import com.connectify.connectify.DTO.response.LoginResponse;
import com.connectify.connectify.DTO.response.PrivateAccountResponse;
import com.connectify.connectify.JWT.JWTUtil;
import com.connectify.connectify.enums.EError;
import com.connectify.connectify.exception.CustomException;
import com.connectify.connectify.model.Account;
import com.connectify.connectify.repository.AccountRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.experimental.NonFinal;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ModelMapper mapper;

    @Autowired
    JWTUtil jwtUtil;

    public ResponseEntity<CommonResponse<?>> authenticate (AuthenticationRequest request) {
        Optional<Account> optionalAccount = accountRepository.findByPhoneNumber(request.getPhoneNumber());
        if (optionalAccount.isEmpty()) throw new CustomException(EError.USER_NOT_EXISTED);
        Account account = optionalAccount.get();

        boolean isAuthenticated = passwordEncoder.matches(request.getPassword(), account.getPassword());
        if (!isAuthenticated) throw new CustomException(EError.INCORRECT_PASSWORD);

        try {
            String token = jwtUtil.generateToken(account);
            PrivateAccountResponse accountResponse = mapper.map(account, PrivateAccountResponse.class);
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setToken(token);
            loginResponse.setData(accountResponse);
            CommonResponse<LoginResponse> response = new CommonResponse<>(200, loginResponse, "Login successfully!");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

}
