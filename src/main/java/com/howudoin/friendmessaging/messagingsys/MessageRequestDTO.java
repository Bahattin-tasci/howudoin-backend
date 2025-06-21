package com.howudoin.friendmessaging.messagingsys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequestDTO {
    private String senderId;
    private String receiverId;
    private String content;
}
