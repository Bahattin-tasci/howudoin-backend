package com.howudoin.friendmessaging.friendreqsys;

import com.howudoin.friendmessaging.security.User;
import com.howudoin.friendmessaging.security.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/api/friends")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @Autowired
    private JwtHelper jwtHelper;

    @PostMapping("/add")
    public ResponseEntity<String> sendFriendRequest(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody FriendRequestDTO request) {
        String userId = jwtHelper.getUserIdFromToken(token.replace("Bearer ", ""));
        friendService.sendFriendRequest(userId, request.getReceiverEmail());
        return ResponseEntity.ok("Friend request sent");
    }

    @PostMapping("/accept")
    public ResponseEntity<String> acceptFriendRequest(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody AcceptFriendRequestDTO request) {
        String userId = jwtHelper.getUserIdFromToken(token.replace("Bearer ", ""));
        friendService.acceptFriendRequest(userId, request.getSenderId());
        return ResponseEntity.ok("Friend request accepted");
    }

    @PostMapping("/reject")
    public ResponseEntity<String> rejectFriendRequest(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody RejectFriendRequestDTO request) {
        String userId = jwtHelper.getUserIdFromToken(token.replace("Bearer ", ""));
        friendService.rejectFriendRequest(userId, request.getSenderId());
        return ResponseEntity.ok("Friend request rejected");
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<FriendDTO>> getFriends(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        String userId = jwtHelper.getUserIdFromToken(token.replace("Bearer ", ""));
        List<FriendDTO> friends = friendService.getFriends(userId);
        return ResponseEntity.ok(friends);
    }

    @GetMapping("/friend-requests")
    public ResponseEntity<List<FriendRequestDTO>> getFriendRequests(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        String userId = jwtHelper.getUserIdFromToken(token.replace("Bearer ", ""));
        List<FriendRequestDTO> friendRequests = friendService.getFriendRequests(userId);
        return ResponseEntity.ok(friendRequests);
    }

}
