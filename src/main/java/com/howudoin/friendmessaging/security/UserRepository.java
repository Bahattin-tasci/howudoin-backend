package com.howudoin.friendmessaging.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    // Custom query to find by 'id' field
    @Query("{ 'id': ?0 }")
    Optional<User> findOneById(String userId);
}
