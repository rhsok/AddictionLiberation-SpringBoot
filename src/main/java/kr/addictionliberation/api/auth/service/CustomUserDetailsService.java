package kr.addictionliberation.api.auth.service;

import kr.addictionliberation.api.auth.entity.AuthEntity;
import kr.addictionliberation.api.auth.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service // Spring Bean으로 등록 (매우 중요!)
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AuthRepository authRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AuthEntity user = authRepository.findByEmail(email) // 이메일로 사용자 조회
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return user; // AuthEntity가 UserDetails를 구현하므로 바로 반환
    }
}