package kr.addictionliberation.api.config;

import kr.addictionliberation.api.jwt.JwtAuthenticationEntryPoint;
import kr.addictionliberation.api.jwt.JwtAuthenticationFilter;
import kr.addictionliberation.api.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // 이 클래스가 Spring 설정 클래스임을 나타냄
@EnableWebSecurity // Spring Security 활성화
@RequiredArgsConstructor // Lombok: final 필드에 대한 생성자 자동 생성
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider; // JWT 토큰 제공자 (의존성 주입)
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint; // JWT 인증 예외 처리 (의존성 주입)

    @Bean // Spring 컨테이너에 빈(Bean)으로 등록
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 비밀번호 암호화를 위한 BCryptPasswordEncoder 빈 등록
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // CSRF 보호 비활성화 (REST API에서는 일반적으로 비활성화)
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 사용 안 함 (JWT는 stateless)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/signup", "/api/auth/login","/api/auth/refresh").permitAll() // 회원가입, 로그인, refresh API는 인증 없이 허용
                        .anyRequest().authenticated() // 나머지 요청은 인증 필요
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint) // 인증 예외 처리 (401 Unauthorized)
                )

                // JWT 인증 필터 추가 (UsernamePasswordAuthenticationFilter 전에 실행)
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}