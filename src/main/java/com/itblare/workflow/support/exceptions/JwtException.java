package com.itblare.workflow.support.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Jwt异常信息
 *
 * @author Blare
 * @version 1.0.0
 * @since 2021/7/8 17:31
 */
public class JwtException extends BaseException {

    public JwtException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
    }

    public JwtException(int code, String message) {
        super(code, message);
    }

    public JwtException(int code, String message, Throwable ex) {
        super(code, message, ex);
    }

    public JwtException(Throwable exception) {
        super(exception);
    }
}