package com.howudoin.friendmessaging.grouppack;

import com.howudoin.friendmessaging.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.howudoin.friendmessaging.security.JwtHelper;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestHeader;
import java.util.stream.Collectors;
import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private JwtHelper jwtHelper;

    /**
     * Create a new group. The creator is automatically set as the admin and added to the members list.
     *
     * @param token   The JWT token from the Authorization header.
     * @param name    The name of the group.
     * @param members The initial list of member IDs to add to the group.
     * @return The created Group object.
     */
    @PostMapping("/create")
    public ResponseEntity<Group> createGroup(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestParam String name,
            @RequestBody List<String> members) {
        String creatorId = jwtHelper.getUserIdFromToken(token.replace("Bearer ", ""));
        Group createdGroup = groupService.createGroup(name, members, creatorId);
        return ResponseEntity.ok(createdGroup);
    }

    /**
     * Add a member to an existing group.
     *
     * @param token    The JWT token from the Authorization header.
     * @param groupId  The ID of the group.
     * @param memberId The ID of the member to add.
     * @return A success message.
     */
    @PostMapping("/{groupId}/add-member")
    public ResponseEntity<String> addMemberToGroup(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @PathVariable String groupId,
            @RequestParam String memberId) {
        memberId = memberId.trim();
        String adminId = jwtHelper.getUserIdFromToken(token.replace("Bearer ", ""));
        groupService.addMemberToGroup(groupId, memberId, adminId);
        return ResponseEntity.ok("Member added to group");
    }

    /**
     * Send a message to a group.
     *
     * @param token   The JWT token from the Authorization header.
     * @param groupId The ID of the group.
     * @param content The message content.
     * @return The sent GroupMessage object.
     */
    @PostMapping("/{groupId}/send")
    public ResponseEntity<GroupMessage> sendGroupMessage(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @PathVariable String groupId,
            @RequestBody String content) {
        // Extract sender ID from token
        String senderId = jwtHelper.getUserIdFromToken(token.replace("Bearer ", ""));
        // Call the service method and get the saved message
        GroupMessage savedMessage = groupService.sendGroupMessage(groupId, content, senderId);
        // Return the saved message in the response
        return ResponseEntity.ok(savedMessage);
    }


    /**
     * Retrieve all messages from a group.
     *
     * @param token   The JWT token from the Authorization header.
     * @param groupId The ID of the group.
     * @return A list of message IDs.
     */
    @GetMapping("/{groupId}/messages")
    public ResponseEntity<List<String>> getGroupMessages(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @PathVariable String groupId) {
        String userId = jwtHelper.getUserIdFromToken(token.replace("Bearer ", ""));
        List<String> messages = groupService.getGroupMessages(groupId);
        return ResponseEntity.ok(messages);
    }

    /**
     * Retrieve all members of a group.
     *
     * @param token   The JWT token from the Authorization header.
     * @param groupId The ID of the group.
     * @return A list of member IDs.
     */
    @GetMapping("/{groupId}/members")
    public ResponseEntity<List<MemberDTO>> getGroupMembers(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @PathVariable String groupId) {
        String userId = jwtHelper.getUserIdFromToken(token.replace("Bearer ", ""));
        List<MemberDTO> members = groupService.getGroupMembers(groupId);
        return ResponseEntity.ok(members);
    }
    /**
     * Retrieve all groups the authenticated user belongs to.
     *
     * @param token The JWT token from the Authorization header.
     * @return A list of groups the user belongs to.
     */
    @GetMapping("/my-groups")
    public ResponseEntity<List<GroupDTO>> getMyGroups(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        String userId = jwtHelper.getUserIdFromToken(token.replace("Bearer ", ""));
        List<GroupDTO> groups = groupService.getGroupsForAuthenticatedUser();
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/sent-messages/{groupId}")
    public ResponseEntity<List<GroupMessageDTO>> getMessagesSentByUserToGroup(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @PathVariable String groupId) {
        String userId = jwtHelper.getUserIdFromToken(token.replace("Bearer ", ""));
        List<GroupMessageDTO> sentMessages = groupService.getMessagesSentByUserToGroup(userId, groupId);
        return ResponseEntity.ok(sentMessages);
    }

    @GetMapping("/{groupId}/all-messages")
    public ResponseEntity<List<GroupMessageDTO>> getMessagesForGroup(
            @PathVariable String groupId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        String userId = jwtHelper.getUserIdFromToken(token.replace("Bearer ", ""));
        // Validate and fetch messages
        List<GroupMessageDTO> messages = groupService.getMessagesForGroup(userId, groupId);
        return ResponseEntity.ok(messages);
    }
    @GetMapping("/{groupId}/details")
    public ResponseEntity<GroupDTO> getGroupDetails(
            @PathVariable String groupId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        String userId = jwtHelper.getUserIdFromToken(token.replace("Bearer ", ""));
        GroupDTO groupDetails = groupService.getGroupDetails(userId, groupId);
        return ResponseEntity.ok(groupDetails);
    }

}

