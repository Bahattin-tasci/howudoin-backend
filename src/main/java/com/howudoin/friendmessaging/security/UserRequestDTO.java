package com.howudoin.friendmessaging.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
