package kr.addictionliberation.api.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component // Spring Component로 등록
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        // 인증 실패 시 (토큰이 없거나, 유효하지 않거나, 만료되었거나 등) 401 Unauthorized 에러를 반환
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}