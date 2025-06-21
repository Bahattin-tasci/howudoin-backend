package com.howudoin.friendmessaging.grouppack;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Document(collection = "groups")
public class Group {
    @Id
    private String id;

    private String name;

    private String adminId;

    @Builder.Default
    private List<String> members = new ArrayList<>();

    @Builder.Default
    private List<String> messages = new ArrayList<>();

    @Builder.Default
    private Date createdAt = new Date();

    @Builder.Default
    private Date updatedAt = new Date();
}
