package com.example.demo.auth.domain;

import com.example.demo.model.common.auth.AuthUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuthUserTest {
    private AuthUser expected;

    @BeforeEach
    void setUp() {
        expected = AuthUser.builder()
                .id(1L)
                .email("devteller123@gmail.com")
                .password("$2a$10$B5tKf/PyHEzhpImNkwHKyONoPyqCcXY68DVLQB6PIGsZARUrsGxdq")
                .build();
    }

    @Test
    @DisplayName("AuthUser 도메인 인증 메소드 성공 테스트")
    void match_password_success_test() {
        String password = "test123!";
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        boolean result = expected.matchPassword(passwordEncoder, password);

        assertTrue(result);
    }

    @Test
    @DisplayName("AuthUser 도메인 인증 메소드 실패 테스트")
    void match_password_fail_test() {
        String password = "test234!";
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        boolean result = expected.matchPassword(passwordEncoder, password);

        assertFalse(result);
    }
}
