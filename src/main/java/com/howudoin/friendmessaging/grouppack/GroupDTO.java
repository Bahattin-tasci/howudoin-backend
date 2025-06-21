package com.howudoin.friendmessaging.grouppack;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.howudoin.friendmessaging.grouppack.MemberDTO;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupDTO {
    private String id;
    private String name;
    private String adminId;
    private String adminName; // Include admin name
    private List<MemberDTO> members;
}

