package com.howudoin.friendmessaging.friendreqsys;

import com.howudoin.friendmessaging.security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendService {

    @Autowired
    private FriendUserRepository friendUserRepository;

    public void sendFriendRequest(String senderId, String receiverEmail) {
        User sender = friendUserRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));
        User receiver = friendUserRepository.findByEmail(receiverEmail)
                .orElseThrow(() -> new IllegalArgumentException("Receiver not found"));

        if (receiver.getFriendRequests().contains(sender.getId())) {
            throw new IllegalArgumentException("Friend request already sent");
        }

        receiver.getFriendRequests().add(sender.getId());
        friendUserRepository.save(receiver);
    }

    public void acceptFriendRequest(String userId, String senderId) {
        User user = friendUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        User sender = friendUserRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));

        if (!user.getFriendRequests().contains(senderId)) {
            throw new IllegalArgumentException("No friend request from this user");
        }

        // Remove the friend request from the receiver's list
        user.getFriendRequests().remove(senderId);
        user.getFriends().add(senderId);

        // Add each other to the friends list
        sender.getFriends().add(userId);

        // Save the updated users back to the repository
        friendUserRepository.save(user);
        friendUserRepository.save(sender);
    }

    public void rejectFriendRequest(String userId, String senderId) {
        User user = friendUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        User sender = friendUserRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));

        if (!user.getFriendRequests().contains(senderId)) {
            throw new IllegalArgumentException("No friend request from this user");
        }

        // Remove the friend request from the receiver's list
        user.getFriendRequests().remove(senderId);

        // Save the updated user back to the repository
        friendUserRepository.save(user);
    }

    public List<FriendDTO> getFriends(String userId) {
        User user = friendUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Map to FriendDTO for cleaner response
        return user.getFriends().stream()
                .map(friendId -> friendUserRepository.findById(friendId)
                        .map(friend -> new FriendDTO(
                                friend.getId(),
                                friend.getFirstName() + " " + friend.getLastName()
                        ))
                        .orElseThrow(() -> new IllegalArgumentException("Friend not found: " + friendId))
                )
                .collect(Collectors.toList());
    }
    public List<FriendRequestDTO> getFriendRequests(String userId) {
        User user = friendUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Map friend request IDs to FriendRequestDTO objects
        return user.getFriendRequests().stream()
                .map(senderId -> friendUserRepository.findById(senderId)
                        .map(sender -> new FriendRequestDTO(
                                sender.getId(),
                                sender.getEmail(),
                                sender.getFirstName(),  // Fetch first name
                                sender.getLastName()    // Fetch last name
                        ))
                        .orElseThrow(() -> new IllegalArgumentException("Sender not found: " + senderId))
                )
                .collect(Collectors.toList());
    }
}
