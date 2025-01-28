package com.github.maitmus.springrole.exceptionHandler;

import com.github.maitmus.springrole.dto.error.CommonErrorResponse;
import com.github.maitmus.springrole.exception.ForbiddenException;
import com.github.maitmus.springrole.exception.NotFoundException;
import com.github.maitmus.springrole.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<CommonErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        HttpStatus status = HttpStatus.CONFLICT;
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new CommonErrorResponse(status.value(), "Entity duplicated"), status);
    }

    @ExceptionHandler({AuthorizationDeniedException.class})
    public ResponseEntity<CommonErrorResponse> handleAuthorizationDeniedException(AuthorizationDeniedException e) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new CommonErrorResponse(status.value(), "Unauthorized"), status);
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<CommonErrorResponse> handleNotFoundException(NotFoundException e) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new CommonErrorResponse(status.value(), "Not found"), status);
    }

    @ExceptionHandler({ForbiddenException.class})
    public ResponseEntity<CommonErrorResponse> handleForbiddenException(ForbiddenException e) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new CommonErrorResponse(status.value(), "Forbidden"), status);
    }

    @ExceptionHandler({UnauthorizedException.class})
    public ResponseEntity<CommonErrorResponse> handleUnauthorizedException(UnauthorizedException e) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new CommonErrorResponse(status.value(), "Unauthorized"), status);
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<CommonErrorResponse> handleRuntimeException(RuntimeException e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new CommonErrorResponse(status.value(), "Internal server error"), status);
    }
}
