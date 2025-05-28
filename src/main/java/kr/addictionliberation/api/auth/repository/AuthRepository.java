package kr.addictionliberation.api.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.addictionliberation.api.auth.entity.AuthEntity;

public interface AuthRepository extends JpaRepository<AuthEntity, Long> {
    Optional<AuthEntity> findByEmail(String email);

    boolean existsByEmail(String email); // 이메일 중복 확인
}