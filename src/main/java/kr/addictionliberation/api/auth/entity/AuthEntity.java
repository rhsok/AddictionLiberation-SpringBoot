package kr.addictionliberation.api.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Set;          // Set import 추가
import java.util.HashSet;     // HashSet import 추가

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthEntity implements UserDetails { // Spring Security UserDetails 상속
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //데이터베이스가 기본 키를 자동으로 생성하도록 합니다 (auto-increment).
    private Long id;

    @Column(nullable = false)
    private String username; // Email or other unique identifier
    
    @Column(nullable = false, unique = true) 
    //unique = true: 해당 컬럼에 고유성 제약 조건(UNIQUE constraint)을 추가합니다. 
    //(email은 중복될 수 없음)
    private String email;

    @Column(nullable = false)
    private String password;
    
    @Column(name = "refresh_token") // refresh_token 컬럼 추가
    private String refreshToken;

    @Column(nullable = false, length = 2) 
    // 사용자 언어 설정 (예: "ko", "en", "ja")
    private String language; 

    // (Optional) Add other user fields like name, nickname, etc.
    

    // Roles/Authorities (권한)
    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private Set<String> roles = new HashSet<>(); // Set<String>으로 변경, HashSet으로 초기화 // 기본값으로 빈 리스트 설정

//    // Post와의 관계 설정 (One-to-Many)
//    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true) // 추가
//    private List<Post> posts = new ArrayList<>();  // 또는 Set<Post>

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정 만료 여부 (true: 만료되지 않음)
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정 잠금 여부 (true: 잠기지 않음)
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 비밀번호 만료 여부 (true: 만료되지 않음)
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정 활성화 여부 (true: 활성화됨)
    }
}