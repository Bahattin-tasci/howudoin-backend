package com.howudoin.friendmessaging.grouppack;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class GroupMessageDTO {
    private String messageId;
    private String groupId;
    private String messageText;
    private String senderId; // Added senderId
    private String senderName; // Added senderName
    private List<String> receiverIds; // Changed to a list of receiver IDs
    private Date sentAt;
}

