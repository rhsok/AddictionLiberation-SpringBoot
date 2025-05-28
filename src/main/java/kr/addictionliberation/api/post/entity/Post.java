package kr.addictionliberation.api.post.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.CascadeType; // User 엔티티 import
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import kr.addictionliberation.api.category.entity.Category;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "post")
@Getter
@Setter
public class Post {
    @Id
    @UuidGenerator // UUID 자동 생성 (Hibernate 6+)
    @Column(length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY) // PostType은 필요할 때만 로딩 (LAZY)
    @JoinColumn(name = "post_type_id")
    private PostType postType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostContent> contents;

    @ManyToMany
    @JoinTable(name = "post_category", joinColumns = @JoinColumn(name = "post_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categories;

    @Column(name = "is_main")
    private Boolean isMain;

    // 생성자, getter, setter (Lombok 사용)
}