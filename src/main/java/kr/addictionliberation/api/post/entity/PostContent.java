package kr.addictionliberation.api.post.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kr.addictionliberation.api.user.entity.User;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "post_content")
@Getter
@Setter
public class PostContent {

    @Id
    @UuidGenerator // UUID 자동 생성
    @Column(length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY) // EAGER 로딩 대신 LAZY 로딩 명시 (성능상 이점)
    @JoinColumn(name = "post_id", nullable = false) // FK 컬럼명 및 not null 제약
    private Post post;

    @Column(nullable = false)
    private String locale;

    @Column(nullable = false)
    private String title;

    private String subtitle;

    @Lob // 긴 텍스트 컨텐츠를 위한 JPA 표준 어노테이션
    @Column(nullable = false)
    private String content;

    @Column(nullable = false) // boolean (primitive) 타입 사용, null 불가
    private Boolean published;

    @ManyToOne(fetch = FetchType.LAZY) // EAGER 로딩 대신 LAZY 로딩 명시
    @JoinColumn(name = "author_id", nullable = false) // FK 컬럼명 및 not null 제약
    private User author;

    private LocalDateTime publishedDate;

    @Column(columnDefinition = "integer default 0")
    private int viewCount = 0; // Java 레벨 기본값 설정

    private String thumbnailImageURL;

    @CreationTimestamp // Hibernate: 엔티티 생성 시 현재 시각 자동 저장
    @Column(name = "created_at", nullable = false, updatable = false) // 컬럼명, null 불가, 업데이트 불가
    private LocalDateTime createdAt;

    @UpdateTimestamp // Hibernate: 엔티티 업데이트 시 현재 시각 자동 저장
    @Column(name = "updated_at", nullable = false) // 컬럼명, null 불가
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    // 생성자, getter, setter (Lombok 사용)

}