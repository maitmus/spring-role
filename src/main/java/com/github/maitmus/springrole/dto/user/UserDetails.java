package com.github.maitmus.springrole.dto.user;

import com.github.maitmus.springrole.constant.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetails {
    private Long id;
    private String username;
    private List<Role> roles;
}
