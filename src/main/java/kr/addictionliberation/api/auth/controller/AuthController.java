package kr.addictionliberation.api.auth.controller;

import kr.addictionliberation.api.auth.dto.AuthDto;
import kr.addictionliberation.api.auth.dto.LoginRequestDto;
import kr.addictionliberation.api.auth.dto.SignupRequestDto;
import kr.addictionliberation.api.auth.dto.TokenResponseDto;
import kr.addictionliberation.api.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthDto> signup(@Valid @RequestBody SignupRequestDto request) {
        AuthDto createdUser = authService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        TokenResponseDto tokenResponse = authService.login(request);
        return ResponseEntity.ok(tokenResponse);
    }
    
    // Refresh Token으로 Access Token 재발급
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDto> refresh(@RequestBody String refreshToken) { // Refresh Token은 요청 본문 또는 헤더로 받음
        TokenResponseDto tokenResponse = authService.refreshAccessToken(refreshToken); // AuthService에 메서드 추가 필요
        return ResponseEntity.ok(tokenResponse);
    }
}