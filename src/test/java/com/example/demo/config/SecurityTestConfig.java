package com.example.demo.config;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

// 보안은 테스트 할 것이 매우 많다! 때문에 선택과 집중이 필요하다
// 해당 부분의 경우 JWT를 통한 인증을 진행할 것이기에, 기존 HTTP 로그인 경로 삭제 및 세션 상태없음을 택하였다
// 때문에 이 2가지를 테스트하도록 한다
@WebMvcTest
@ExtendWith(SpringExtension.class)
@Disabled
public class SecurityTestConfig {
}
