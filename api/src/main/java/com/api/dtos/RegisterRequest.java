package com.api.dtos;

import com.api.entities.User;
import com.api.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private Role role;

    public User toUser() {
        User user = new User();

        user.setName(this.name);
        user.setEmail(this.email);
        user.setRole(this.role);

        return user;
    }
}
