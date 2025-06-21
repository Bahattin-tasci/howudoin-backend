package com.howudoin.friendmessaging.messagingsys;

import lombok.Getter;

@Getter
public class MessageQueryLimitedDTO {
    // Getter for senderId
    private String senderId;
    // Getter for receiverId
    private String receiverId;

    // Default constructor
    public MessageQueryLimitedDTO() {
    }

    // Parameterized constructor
    public MessageQueryLimitedDTO(String senderId, String receiverId) {
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    // Setter for senderId
    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    // Setter for receiverId
    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }
}
