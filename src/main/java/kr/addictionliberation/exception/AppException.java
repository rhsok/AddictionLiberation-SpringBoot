package kr.addictionliberation.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor // 모든 필드를 파라미터로 받는 생성자 자동 생성 (Lombok)
@Getter // getter 메서드 자동 생성 (Lombok)
public class AppException extends RuntimeException { // RuntimeException 상속
    private HttpStatus status; // HTTP 상태 코드 (예: 404, 500 등)
    private String message;    // 예외 메시지
}