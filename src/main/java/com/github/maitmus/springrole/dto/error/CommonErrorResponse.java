package com.github.maitmus.springrole.dto.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommonErrorResponse {
    private Integer statusCode;
    private String message;
}
