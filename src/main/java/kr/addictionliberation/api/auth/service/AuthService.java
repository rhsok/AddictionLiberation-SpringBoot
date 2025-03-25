package kr.addictionliberation.api.auth.service;

import kr.addictionliberation.api.auth.dto.AuthDto;
import kr.addictionliberation.api.auth.dto.LoginRequestDto;
import kr.addictionliberation.api.auth.dto.SignupRequestDto;
import kr.addictionliberation.api.auth.dto.TokenResponseDto;
import kr.addictionliberation.api.auth.entity.AuthEntity;
import kr.addictionliberation.api.auth.repository.AuthRepository;
import kr.addictionliberation.api.jwt.JwtTokenProvider;
import kr.addictionliberation.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections; // 이 import 문 추가
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    //회원가입 
    public AuthDto signup(SignupRequestDto request) {
        // 이메일 중복 체크 
        if (authRepository.existsByEmail(request.getEmail())) { // existsByEmail 메서드 추가 필요
            throw new AppException(HttpStatus.CONFLICT, "Email already exists");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        AuthEntity user = AuthEntity.builder()
                .username(request.getUsername())
                .password(encodedPassword)
                .email(request.getEmail()) // 이메일 설정
                .roles(Set.of("ROLE_USER"))
                .build();

        AuthEntity savedUser = authRepository.save(user);

        return AuthDto.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail()) // 이메일 설정
                .build();
    }

    //로그인 
    public TokenResponseDto login(LoginRequestDto loginRequestDto) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword())
            );
            // Access Token 생성
            String accessToken = jwtTokenProvider.createAccessToken(authentication);
            // Refresh Token 생성
            String refreshToken = jwtTokenProvider.createRefreshToken(authentication);
            
            // Refresh Token을 DB에 저장 (User 엔티티에)
            AuthEntity user = authRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + loginRequestDto.getEmail()));
            user.setRefreshToken(refreshToken);
            authRepository.save(user); // 변경사항 저장
            
            return TokenResponseDto.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken) // Refresh Token도 함께 반환
                    .build();
            
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username or password");
        } catch (UsernameNotFoundException e){
            throw new UsernameNotFoundException("Invalid username or password");
        }
    }
    
    //리프레시토큰으로 액세스토큰 재발
    @Transactional
    public TokenResponseDto refreshAccessToken(String refreshToken) {
        // 1. Refresh Token 유효성 검사
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
        }

        // 2. Refresh Token으로 사용자 정보 가져오기
        Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);
        String email = authentication.getName();

        // 3. DB에서 Refresh Token 가져오기 (데이터베이스에 저장하는 경우)
        AuthEntity user = authRepository.findByEmail(email) 
               .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String storedRefreshToken = user.getRefreshToken();


        // 4. DB의 Refresh Token과 일치하는지 확인
        if (!refreshToken.equals(storedRefreshToken)) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "Invalid refresh token"); // 또는 로그아웃 처리
        }

        // 5. 새로운 Access Token 생성
        String newAccessToken = jwtTokenProvider.createAccessToken(authentication);

        // (선택 사항) Refresh Token rotation (새로운 Refresh Token 생성 및 저장)
        // String newRefreshToken = jwtTokenProvider.createRefreshToken(authentication);
        // user.setRefreshToken(newRefreshToken);
        // authRepository.save(user);

        // 6. 새로운 Access Token 반환
        return TokenResponseDto.builder().accessToken(newAccessToken)/* .refreshToken(newRefreshToken) */.build(); // 새 Refresh Token도 반환할지 선택
    }
}