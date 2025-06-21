package com.howudoin.friendmessaging.messagingsys;

import com.howudoin.friendmessaging.grouppack.GroupMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

import org.springframework.data.mongodb.repository.Query;

public interface MessageRepository extends MongoRepository<Message, String> {

    List<Message> findByReceiverId(String receiverId);

    List<Message> findBySenderIdAndReceiverId(String senderId, String receiverId);

    @Query("{ '$or': [ { 'senderId': ?0, 'receiverId': ?1 }, { 'senderId': ?1, 'receiverId': ?0 } ], 'groupId': { '$exists': false } }")
    List<Message> findDirectMessages(String senderId, String receiverId);
    @Query("{ 'senderId': ?0, 'receiverId': ?1, 'groupId': { '$exists': false } }")
    List<Message> findDirectMessagesSentBySender(String senderId, String receiverId);

    @Query("{ 'receiverId': ?0, 'senderId': ?1, 'groupId': { '$exists': false } }")
    List<Message> findDirectMessagesSentToReceiver(String receiverId, String senderId);

}

