package com.howudoin.friendmessaging.grouppack;

import com.howudoin.friendmessaging.messagingsys.Message;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "messages")
public class GroupMessage {
    @Id
    @Getter
    private String id;
    private String groupId;
    private String senderId;
    private List<String> receiverIds; // Changed to a list of receiver IDs
    private String messageText;
    private Date sentAt;
}



