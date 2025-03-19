package kr.addictionliberation.api.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 1. Request Header에서 JWT 토큰 추출
        String token = resolveToken(request);

        // 2. 토큰 유효성 검사 (validateToken)
        if (token != null && jwtTokenProvider.validateToken(token)) {
            try {
                // 3. 토큰에서 인증 정보 가져오기 (getAuthentication)
                Authentication authentication = jwtTokenProvider.getAuthentication(token);

                // 4. SecurityContext에 Authentication 객체 저장
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) { // 만료, 잘못된 토큰 등..
                SecurityContextHolder.clearContext(); // 인증 정보 제거
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed");
                return;  // 중요: 더 이상 필터 체인 진행 중단.
            }
        }

        // 5. 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }

    // Request Header에서 토큰 정보 추출 ("Bearer <token>" 형식)
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 접두사 제거
        }
        return null;
    }
}