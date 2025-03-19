package kr.addictionliberation.api.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenResponseDto {
    private String accessToken; // Access Token 필드 이름 변경
    private String refreshToken; // Refresh Token 필드 추가
    // 선택 사항: 토큰 타입 (Bearer), 만료 시간 등 추가 가능
}