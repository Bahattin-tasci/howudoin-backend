package com.howudoin.friendmessaging.grouppack;

import com.howudoin.friendmessaging.security.User;
import com.howudoin.friendmessaging.security.UserService;
import com.howudoin.friendmessaging.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupMessageRepository groupMessageRepository;

    @Autowired
    private UserService userService; // Assuming you have methods to validate users

    @Autowired
    private SecurityUtils securityUtils;

    public Group createGroup(String name, List<String> members, String creatorId) {
        if (!members.contains(creatorId)) {
            members.add(creatorId);
        }

        Group group = Group.builder()
                .name(name)
                .adminId(creatorId)
                .members(members)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
        return groupRepository.save(group);
    }

    public void addMemberToGroup(String groupId, String memberId, String adminId) {
        System.out.println("GroupService: Attempting to add member with ID: " + memberId + " to group: " + groupId + " by admin: " + adminId);

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));

        if (!group.getAdminId().equals(adminId)) {
            throw new IllegalArgumentException("Only the group admin can add members");
        }

        if (!group.getMembers().contains(memberId)) {
            group.getMembers().add(memberId);
            group.setUpdatedAt(new Date());
            groupRepository.save(group);
            System.out.println("GroupService: Member " + memberId + " added to group " + groupId);
        } else {
            throw new IllegalArgumentException("Member already exists in the group");
        }
    }
    public GroupMessage sendGroupMessage(String groupId, String content, String senderId) {
        // Fetch the group from the repository
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));

        // Check if the sender is a member of the group
        if (!group.getMembers().contains(senderId)) {
            throw new IllegalArgumentException("Sender is not a member of the group");
        }

        // Get all members except the sender
        List<String> receiverIds = group.getMembers().stream()
                .filter(memberId -> !memberId.equals(senderId)) // Exclude the sender
                .collect(Collectors.toList());

        // Create a single group message
        GroupMessage message = GroupMessage.builder()
                .groupId(groupId)
                .senderId(senderId)
                .receiverIds(receiverIds) // Use all group members except the sender
                .messageText(content)
                .sentAt(new Date())
                .build();

        // Save the message to the database
        GroupMessage savedMessage = groupMessageRepository.save(message);

        // Add the message to the group's messages list
        group.getMessages().add(savedMessage.getId());
        group.setUpdatedAt(new Date());
        groupRepository.save(group);

        return savedMessage;
    }

    public List<String> getGroupMessages(String groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));
        return group.getMessages();
    }

    public List<MemberDTO> getGroupMembers(String groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));

        return group.getMembers().stream()
                .map(memberId -> {
                    User user = userService.getUserById(memberId)
                            .orElseThrow(() -> new IllegalArgumentException("Member not found: " + memberId));
                    return new MemberDTO(memberId, user.getFirstName() + " " + user.getLastName());
                })
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all groups that the authenticated user is a member of.
     *
     * @return A list of groups the user belongs to.
     */
    public List<GroupDTO> getGroupsForAuthenticatedUser() {
        String userId = securityUtils.getCurrentUserId();
        System.out.println("Extracted userId: " + userId);
        List<Group> groups = groupRepository.findByMembersContaining(userId);

        return groups.stream()
                .map(group -> {
                    // Fetch members
                    List<MemberDTO> members = group.getMembers().stream()
                            .map(memberId -> {
                                User user = userService.getUserById(memberId)
                                        .orElseThrow(() -> new IllegalArgumentException("Member not found: " + memberId));
                                return new MemberDTO(memberId, user.getFirstName() + " " + user.getLastName());
                            })
                            .collect(Collectors.toList());
                    if (group.getAdminId() == null) {
                        throw new IllegalArgumentException("Admin ID is null for group: " + group.getId());
                    }
                    if (group.getMembers().contains(null)) {
                        throw new IllegalArgumentException("Null member ID found in group: " + group.getId());
                    }
                    // Fetch admin details
                    String adminName = userService.getUserById(group.getAdminId())
                            .map(admin -> admin.getFirstName() + " " + admin.getLastName())
                            .orElse("Unknown");

                    // Create and return the GroupDTO
                    return new GroupDTO(group.getId(), group.getName(), group.getAdminId(), adminName, members);
                })
                .collect(Collectors.toList());
    }

    public List<GroupMessageDTO> getMessagesSentByUserToGroup(String userId, String groupId) {
        // Fetch all group messages sent by the user to the specified group
        List<GroupMessage> groupMessages = groupMessageRepository.findBySenderIdAndGroupId(userId, groupId);

        // Map to DTO
        return groupMessages.stream()
                .map(message -> {
                    // Fetch the sender's name
                    String senderName = userService.getUserById(message.getSenderId())
                            .map(user -> user.getFirstName() + " " + user.getLastName())
                            .orElse("Unknown");

                    // Fetch the names of all receivers
                    List<String> receiverNames = message.getReceiverIds().stream()
                            .map(receiverId -> userService.getUserById(receiverId)
                                    .map(user -> user.getFirstName() + " " + user.getLastName())
                                    .orElse("Unknown"))
                            .collect(Collectors.toList());

                    // Join receiver names into a single string (optional, for display purposes)
                    String receivers = String.join(", ", receiverNames);

                    return new GroupMessageDTO(
                            message.getId(),
                            groupId, // Use the specific groupId
                            message.getMessageText(),
                            message.getSenderId(),
                            senderName, // Include senderName
                            message.getReceiverIds(), // Include the list of receiver IDs
                            message.getSentAt()
                    );
                })
                .collect(Collectors.toList());
    }

    public List<GroupMessageDTO> getMessagesForGroup(String userId, String groupId) {
        // Fetch the group and validate membership
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));

        if (!group.getMembers().contains(userId)) {
            throw new IllegalArgumentException("User is not a member of this group");
        }

        // Fetch all messages for the group sorted by time
        List<GroupMessage> groupMessages = groupMessageRepository.findByGroupIdOrderBySentAtAsc(groupId);

        // Map to DTO
        return groupMessages.stream()
                .map(message -> {
                    // Fetch the sender's name
                    String senderName = userService.getUserById(message.getSenderId())
                            .map(user -> user.getFirstName() + " " + user.getLastName())
                            .orElse("Unknown");

                    return new GroupMessageDTO(
                            message.getId(),
                            message.getGroupId(),
                            message.getMessageText(),
                            message.getSenderId(),
                            senderName,
                            message.getReceiverIds(), // Include the list of receiver IDs
                            message.getSentAt()
                    );
                })
                .collect(Collectors.toList());
    }


    public GroupDTO getGroupDetails(String userId, String groupId) {
        // Fetch the group
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));

        // Validate if the current user is a member of the group
        if (!group.getMembers().contains(userId)) {
            throw new IllegalArgumentException("User is not a member of this group");
        }

        // Fetch the admin's name
        String adminName = userService.getUserById(group.getAdminId())
                .map(user -> user.getFirstName() + " " + user.getLastName())
                .orElse("Unknown");

        // Map members to MemberDTO
        List<MemberDTO> members = group.getMembers().stream()
                .map(memberId -> {
                    String name = userService.getUserById(memberId)
                            .map(user -> user.getFirstName() + " " + user.getLastName())
                            .orElse("Unknown");
                    return new MemberDTO(memberId, name);
                })
                .collect(Collectors.toList());

        // Return the group details as a DTO
        return new GroupDTO(group.getId(), group.getName(), group.getAdminId(), adminName, members);
    }
}
