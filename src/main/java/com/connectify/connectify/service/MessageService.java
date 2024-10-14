package com.connectify.connectify.service;

import com.connectify.connectify.DTO.request.EditMessageRequest;
import com.connectify.connectify.DTO.request.SearchMessageRequest;
import com.connectify.connectify.DTO.response.CommonResponse;
import com.connectify.connectify.DTO.response.MessageResponse;
import com.connectify.connectify.DTO.response.PublicAccountResponse;
import com.connectify.connectify.entity.Account;
import com.connectify.connectify.entity.Message;
import com.connectify.connectify.entity.Messenger;
import com.connectify.connectify.enums.EError;
import com.connectify.connectify.exception.CustomException;
import com.connectify.connectify.repository.MessageRepository;
import com.connectify.connectify.repository.MessengerRepository;
import com.google.firebase.database.DatabaseReference;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    @Autowired
    MessageRepository messageRepository;

    @Autowired
    MessengerRepository messengerRepository;

    @Autowired
    AuthService authService;

    @Autowired
    ModelMapper mapper;

    @Autowired
    FirebaseService firebaseService;

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    public ResponseEntity<?> create (EditMessageRequest request) {
        Optional<Messenger> optionalMessenger = messengerRepository.findById(request.getMessengerId());
        if (optionalMessenger.isEmpty()) throw new CustomException(EError.BAD_REQUEST);

        Messenger messenger = optionalMessenger.get();
        messenger.setUpdatedAt(new Date());
        messengerRepository.save(messenger);

        Message message = new Message();
        Account currentAccount = authService.getCurrentAccount();
        message.setCreatedBy(currentAccount);
        message.setCreatedAt(new Date());
        message.setMessenger(messenger);
        message.setContent(request.getContent());
        Message createdMessage = messageRepository.save(message);

        String destination = "/topic/messengers/" + request.getMessengerId() + "/messages";
        messagingTemplate.convertAndSend(destination, messageToMessageResponse(createdMessage));

        CommonResponse<?> response = new CommonResponse<>(200, null, "Send message successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private MessageResponse messageToMessageResponse (Message message) {
        MessageResponse messageResponse = mapper.map(message, MessageResponse.class);
        messageResponse.setMessengerId(message.getMessenger().getId());
        PublicAccountResponse accountResponse = mapper.map(message.getCreatedBy(), PublicAccountResponse.class);
        messageResponse.setCreatedBy(accountResponse);
        return messageResponse;
    }

    public ResponseEntity<?> search(SearchMessageRequest request) {
        List<Message> messages = messageRepository.findAllByMessengerIdOrderByCreatedAtAsc(request.getMessengerId());
        List<MessageResponse> messageResponses = messages.stream().map(this::messageToMessageResponse).toList();
        CommonResponse<?> response = new CommonResponse<>(200, messageResponses, "Search messages successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
