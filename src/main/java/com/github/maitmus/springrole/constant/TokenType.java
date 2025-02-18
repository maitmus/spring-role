package com.github.maitmus.springrole.constant;

import lombok.Getter;

@Getter
public enum TokenType {
    ACCESS("accessToken"),
    REFRESH("refreshToken");

    private final String value;

    TokenType(String value) {
        this.value = value;
    }
}
