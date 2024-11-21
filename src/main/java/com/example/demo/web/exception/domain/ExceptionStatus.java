package com.example.demo.web.exception.domain;

import lombok.Getter;

@Getter
public enum ExceptionStatus {
    CREDENTIAL_NOT_MATCH("CNM"),
    JWT_AUTH_EXCEPTION("TK001"),
    TOKEN_EXPIRED("TK002"),
    TOKEN_NOT_FOUND("TK003");

    private final String code;

    ExceptionStatus(String code) {
        this.code = code;
    }
}