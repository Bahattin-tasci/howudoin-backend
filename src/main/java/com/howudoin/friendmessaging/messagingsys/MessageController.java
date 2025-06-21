package com.howudoin.friendmessaging.messagingsys;

import org.springframework.beans.factory.annotation.Autowired;
import com.howudoin.friendmessaging.security.JwtHelper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;
    @Autowired
    private JwtHelper jwtHelper;
    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(@RequestBody MessageRequestDTO request, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        String senderId = jwtHelper.getUserIdFromToken(token.replace("Bearer ", ""));
        return ResponseEntity.ok(messageService.sendMessage(senderId, request.getReceiverId(), request.getContent()));
    }

    @PostMapping("/get")
    public ResponseEntity<List<Message>> getMessages(@RequestBody MessageQueryDTO query, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        String receiverId = jwtHelper.getUserIdFromToken(token.replace("Bearer ", ""));
        return ResponseEntity.ok(messageService.getMessages(receiverId));
    }
    @PostMapping("/get-from-sender")
    public ResponseEntity<List<Message>> getMessagesFromSender(
            @RequestBody MessageQueryLimitedDTO query,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        String receiverId = jwtHelper.getUserIdFromToken(token.replace("Bearer ", ""));
        return ResponseEntity.ok(messageService.getMessagesBySenderId(query.getSenderId(), receiverId));
    }

    @PostMapping("/recent")
    public ResponseEntity<List<MessageDTO>> getRecentMessages(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        String userId = jwtHelper.getUserIdFromToken(token.replace("Bearer ", ""));
        return ResponseEntity.ok(messageService.getRecentMessages(userId));
    }
    @PostMapping("/get-with-users")
    public ResponseEntity<List<MessageDTO>> getMessagesWithUsers(
            @RequestBody MessageQueryLimitedDTO query,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        String userId = jwtHelper.getUserIdFromToken(token.replace("Bearer ", ""));
        return ResponseEntity.ok(messageService.getMessagesWithUsers(userId, query.getReceiverId()));
    }

    @PostMapping("/get-direct-messages")
    public ResponseEntity<List<MessageDTO>> getDirectMessagesWithUser(
            @RequestBody MessageQueryLimitedDTO query,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        String userId = jwtHelper.getUserIdFromToken(token.replace("Bearer ", ""));
        return ResponseEntity.ok(messageService.getDirectMessagesWithUser(userId, query.getReceiverId()));
    }

    @PostMapping("/get-direct-messages-to-user")
    public ResponseEntity<List<MessageDTO>> getDirectMessagesToUser(
            @RequestBody MessageQueryLimitedDTO query,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        String userId = jwtHelper.getUserIdFromToken(token.replace("Bearer ", ""));
        return ResponseEntity.ok(messageService.getDirectMessagesToUser(userId, query.getSenderId()));
    }

}

