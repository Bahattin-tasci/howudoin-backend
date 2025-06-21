package com.howudoin.friendmessaging.friendreqsys;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RejectFriendRequestDTO implements Serializable {

    private String userId;    // The ID of the receiver (who rejects the request)
    private String senderId;  // The ID of the sender (who sent the request)
}
