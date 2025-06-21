package com.howudoin.friendmessaging.grouppack;


import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface GroupMessageRepository extends MongoRepository<GroupMessage, String> {
    List<GroupMessage> findBySenderId(String senderId);
    List<GroupMessage> findBySenderIdAndGroupId(String senderId, String groupId);
    // Fetch all messages by group ID sorted by time
    List<GroupMessage> findByGroupIdOrderBySentAtAsc(String groupId);

}
