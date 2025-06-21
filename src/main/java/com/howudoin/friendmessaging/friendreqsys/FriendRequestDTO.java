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
public class FriendRequestDTO implements Serializable {
    private String senderId;
    private String receiverEmail;
    private String senderFirstName;
    private String senderLastName;
}