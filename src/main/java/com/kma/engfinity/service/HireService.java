package com.kma.engfinity.service;

import com.kma.engfinity.DTO.request.EditHireRequest;
import com.kma.engfinity.DTO.response.CommonResponse;
import com.kma.engfinity.DTO.response.HireResponse;
import com.kma.engfinity.entity.Account;
import com.kma.engfinity.entity.Hire;
import com.kma.engfinity.enums.EAccountStatus;
import com.kma.engfinity.enums.EError;
import com.kma.engfinity.exception.CustomException;
import com.kma.engfinity.repository.HireRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HireService {
    @Autowired
    private HireRepository hireRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthService authService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private WebSocketService webSocketService;

    public ResponseEntity<?> create (EditHireRequest request) {
        Account account = authService.getCurrentAccount();
        Hire hire = modelMapper.map(request, Hire.class);
        hire.setCreatedBy(account);
        Account teacher = accountService.getAccountById(request.getTeacherId());
        if (teacher.getStatus().equals(EAccountStatus.IN_CALL)) throw new CustomException(EError.USER_IN_CALL);
        hire.setTeacher(teacher);
        hire.setCost(request.getTotalTime() * teacher.getCost());
        Hire createdHire = hireRepository.save(hire);
        HireResponse hireResponse = modelMapper.map(hire, HireResponse.class);
        String destination = "/topic/teachers/" + request.getTeacherId();
        webSocketService.sendData(destination, hireResponse);
        CommonResponse<?> response = new CommonResponse<>(200, createdHire, "Create hire successfully!");
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> updateStatus (EditHireRequest request) {
        Optional<Hire> optionalHire = hireRepository.findById(request.getId());
        if (optionalHire.isEmpty()) throw new CustomException(EError.BAD_REQUEST);
        String destination = "/topic/teachers/" + request.getTeacherId();
        webSocketService.sendData(destination, hireResponse);
        CommonResponse<?> response = new CommonResponse<>(200, createdHire, "Create hire successfully!");
        return ResponseEntity.ok(response);
    }
}
