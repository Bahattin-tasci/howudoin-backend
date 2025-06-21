package com.howudoin.friendmessaging.messagingsys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {
    private String senderId;
    private String senderName;
    private String lastMessage;
    private String receiverId;
    private Date sentAt;
}

