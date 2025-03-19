package kr.addictionliberation.api.auth.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthDto {
    private Long id;
    private String username;
    private String email; // 이메일 필드 추가
}