package kr.addictionliberation.api.auth.repository;

import kr.addictionliberation.api.auth.entity.AuthEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<AuthEntity, Long> {
    Optional<AuthEntity> findByEmail(String email); // findByUsername -> findByEmail 변경

    boolean existsByEmail(String email); // 이메일 중복 확인 
}