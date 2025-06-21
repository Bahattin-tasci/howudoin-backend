package com.howudoin.friendmessaging.friendreqsys;

import com.howudoin.friendmessaging.security.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendUserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    List<User> findAllById(Iterable<String> ids);
}
