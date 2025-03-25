package kr.addictionliberation.api.user.entity;

import kr.addictionliberation.api.post.entity.Post;
import kr.addictionliberation.api.post.entity.PostContent;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // 또는 다른 적절한 전략
    private String id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password; // 실제로는 암호화된 비밀번호를 저장해야 합니다.

    private String refreshToken; //(선택사항)

    @OneToMany(mappedBy = "author") // PostContent의 author 필드
    private List<PostContent> postContents; // 내가 작성한 번역본(PostContent) 목록

    @Enumerated(EnumType.STRING) // enum 타입을 String 형태로 저장
    private Role role; // 사용자 역할 (예: USER, ADMIN)

    // 기타 필드 (프로필 사진, 닉네임 등) 및 getter/setter, 생성자 (Lombok 사용 가능)
}