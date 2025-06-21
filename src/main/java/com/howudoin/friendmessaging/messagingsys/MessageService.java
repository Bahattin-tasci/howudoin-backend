package com.howudoin.friendmessaging.messagingsys;

import com.howudoin.friendmessaging.friendreqsys.FriendUserRepository;
import com.howudoin.friendmessaging.security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private FriendUserRepository friendUserRepository;

    public Message sendMessage(String senderId, String receiverId, String content) {

        if (!areFriends(senderId, receiverId)) {
            throw new IllegalArgumentException("You can only send messages to friends.");
        }


        Message message = new Message(senderId, receiverId, content);
        message.setSentAt(new Date());
        return messageRepository.save(message);
    }

    public List<Message> getMessages(String receiverId) {
        return messageRepository.findByReceiverId(receiverId);
    }


    private boolean areFriends(String senderId, String receiverId) {
        User sender = friendUserRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));
        return sender.getFriends().contains(receiverId);
    }

    public List<MessageDTO> getRecentMessages(String userId) {
        List<Message> messages = messageRepository.findByReceiverId(userId);

        // Group messages by senderId and map to DTOs
        return messages.stream()
                .collect(Collectors.groupingBy(Message::getSenderId))
                .entrySet()
                .stream()
                .map(entry -> {
                    String senderId = entry.getKey();
                    List<Message> userMessages = entry.getValue();
                    Message lastMessage = userMessages.get(userMessages.size() - 1);

                    User sender = friendUserRepository.findById(senderId)
                            .orElseThrow(() -> new IllegalArgumentException("User not found"));

                    return new MessageDTO(
                            sender.getId(),
                            sender.getFirstName() + " " + sender.getLastName(),
                            lastMessage.getMessageText(),
                            userId, // Set receiverId to userId
                            lastMessage.getSentAt()
                    );
                })
                .sorted(Comparator.comparing(MessageDTO::getSentAt).reversed())
                .collect(Collectors.toList());
    }


    public List<Message> getMessagesBySenderId(String senderId, String receiverId) {
        User receiver = friendUserRepository.findById(receiverId)
                .orElseThrow(() -> new IllegalArgumentException("Receiver not found"));

        return messageRepository.findBySenderIdAndReceiverId(senderId, receiverId);
    }

    public List<MessageDTO> getMessagesWithUsers(String senderId, String receiverId) {
        // Fetch all messages sent between the sender and receiver
        List<Message> messages = messageRepository.findBySenderIdAndReceiverId(senderId, receiverId);

        // Map to MessageDTO
        return messages.stream()
                .map(message -> {
                    User sender = friendUserRepository.findById(message.getSenderId())
                            .orElseThrow(() -> new IllegalArgumentException("Sender not found"));
                    return new MessageDTO(
                            message.getSenderId(),
                            sender.getFirstName() + " " + sender.getLastName(),
                            message.getMessageText(),
                            message.getReceiverId(),
                            message.getSentAt()
                    );
                })
                .collect(Collectors.toList());
    }
    public List<MessageDTO> getDirectMessagesWithUser(String senderId, String receiverId) {
        // Fetch direct messages sent by the current user to the specified receiver
        List<Message> messages = messageRepository.findDirectMessagesSentBySender(senderId, receiverId);

        // Map to MessageDTO
        return messages.stream()
                .map(message -> {
                    User sender = friendUserRepository.findById(senderId)
                            .orElseThrow(() -> new IllegalArgumentException("Sender not found"));
                    User receiver = friendUserRepository.findById(receiverId)
                            .orElseThrow(() -> new IllegalArgumentException("Receiver not found"));

                    return new MessageDTO(
                            senderId,
                            sender.getFirstName() + " " + sender.getLastName(), // Sender's full name
                            message.getMessageText(),
                            receiverId,
                            message.getSentAt()
                    );
                })
                .collect(Collectors.toList());
    }


    public List<MessageDTO> getDirectMessagesToUser(String receiverId, String senderId) {
        // Fetch direct messages sent to the current user from a specific sender
        List<Message> messages = messageRepository.findDirectMessagesSentToReceiver(receiverId, senderId);

        // Filter messages with non-null messageText and map to MessageDTO
        return messages.stream()
                .filter(message -> message.getMessageText() != null) // Exclude messages with null messageText
                .map(message -> {
                    User sender = friendUserRepository.findById(message.getSenderId())
                            .orElseThrow(() -> new IllegalArgumentException("Sender not found"));
                    return new MessageDTO(
                            message.getSenderId(),
                            sender.getFirstName() + " " + sender.getLastName(), // Sender's full name
                            message.getMessageText(),
                            receiverId,
                            message.getSentAt()
                    );
                })
                .collect(Collectors.toList());
    }
}
