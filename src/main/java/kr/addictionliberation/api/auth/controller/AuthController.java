package kr.addictionliberation.api.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.addictionliberation.api.auth.dto.AuthDto;
import kr.addictionliberation.api.auth.dto.LoginRequestDto;
import kr.addictionliberation.api.auth.dto.SignupRequestDto;
import kr.addictionliberation.api.auth.dto.TokenResponseDto;
import kr.addictionliberation.api.auth.service.AuthService;
import lombok.RequiredArgsConstructor;

@Tag(name = "Auth", description = "인증 관련 API") // API 그룹 설정
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "회원 가입", description = "새로운 사용자를 등록합니다.") // API 요약, 설명
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "회원 가입 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping("/signup")
    public ResponseEntity<AuthDto> signup(@Valid @RequestBody SignupRequestDto request) {
        AuthDto createdUser = authService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @Operation(summary = "로그인", description = "사용자 인증 후 토큰을 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(authService.login(request)); // 수정된 부분
    }

    @Operation(summary = "토큰 갱신", description = "Refresh Token을 사용하여 Access Token을 갱신합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "토큰 갱신 성공"),
            @ApiResponse(responseCode = "401", description = "Refresh Token 만료 또는 유효하지 않음")
    })
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDto> refresh(@RequestBody String refreshToken) { // Refresh Token은 요청 본문 또는 헤더로 받음
        TokenResponseDto tokenResponse = authService.refreshAccessToken(refreshToken); // AuthService에 메서드 추가 필요
        return ResponseEntity.ok(tokenResponse);
    }
}