package com.howudoin.friendmessaging.grouppack;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends MongoRepository<Group, String> {
    // Existing methods...

    /**
     * Finds all groups that include the specified member ID.
     *
     * @param memberId The ID of the member.
     * @return A list of groups the member belongs to.
     */
    List<Group> findByMembersContaining(String memberId);
}
