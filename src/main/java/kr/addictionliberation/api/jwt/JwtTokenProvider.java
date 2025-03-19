package kr.addictionliberation.api.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j // Lombok: 로깅 (Slf4j) 사용
public class JwtTokenProvider {

    @Value("${jwt.secret}") // application.properties의 jwt.secret 값을 가져옴
    private String secretKey;

    @Value("${jwt.expiration}") // application.properties의 jwt.expiration 값을 가져옴
    private long accessValidityInMilliseconds;
    
    @Value("${jwt.refresh-expiration}") // Refresh Token 만료 시간 (application.properties)
    private long refreshTokenValidityInMilliseconds;

    private Key key; // HMAC-SHA 알고리즘에 사용할 키

    private final UserDetailsService userDetailsService; // Spring Security의 UserDetailsService

    // 생성자 주입 (Constructor Injection)
    public JwtTokenProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct // 빈(Bean) 초기화 시 실행되는 메서드
    protected void init() {
        // secretKey를 Base64 인코딩하여 HMAC-SHA 알고리즘에 사용할 키 생성
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    // JWT 토큰 생성
    public String createAccessToken(Authentication authentication) {
        String username = authentication.getName(); // 사용자 이름 (주체)
        List<String> roles = authentication.getAuthorities().stream()  //권한 가져오기
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        Claims claims = Jwts.claims().setSubject(username); // JWT Claims 생성 (payload)
        claims.put("roles", roles); // 권한 정보 추가

        Date now = new Date();
        Date validity = new Date(now.getTime() + accessValidityInMilliseconds); // 토큰 만료 시간

        return Jwts.builder()
                .setClaims(claims) // Claims 설정
                .setIssuedAt(now) // 발행 시간
                .setExpiration(validity) // 만료 시간
                .signWith(key, SignatureAlgorithm.HS256) // 서명 (HMAC-SHA256 알고리즘 사용)
                .compact(); // JWT 생성
    }
    
    // Refresh Token 생성 (새로운 메서드)
    public String createRefreshToken(Authentication authentication) {
        String username = authentication.getName();
        Claims claims = Jwts.claims().setSubject(username);
        // Refresh Token에는 보통 권한 정보(roles)를 넣지 않습니다.
        // 필요하다면 추가할 수 있습니다.

        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshTokenValidityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // JWT 토큰에서 인증 정보 가져오기
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsername(token));
        if (userDetails == null) {
            throw new IllegalArgumentException("User details not found for token."); // 또는 커스텀 예외
        }
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // JWT 토큰에서 사용자 이름 (주체) 추출
    public String getUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }


    // JWT 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            log.info("Invalid JWT signature.");  // 서명 검증 실패
            log.trace("Invalid JWT signature trace: {}", e);
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token."); // 토큰 구조 문제
            log.trace("Invalid JWT token trace: {}", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token."); // 토큰 만료
            log.trace("Expired JWT token trace: {}", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token."); // 지원되지 않는 토큰 형식
            log.trace("Unsupported JWT token trace: {}", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid."); // 토큰 Claims가 비어있는 경우 등
            log.trace("JWT token compact of handler are invalid trace: {}", e);
        }
        return false; // 유효하지 않은 토큰
    }
}