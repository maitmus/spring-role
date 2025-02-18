package com.github.maitmus.springrole.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteCurrentUserResponse {
    private Long deletedUserId;
}
