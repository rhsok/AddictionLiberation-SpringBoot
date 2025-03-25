package kr.addictionliberation.api.post.entity;

import kr.addictionliberation.api.category.entity.Category;

import kr.addictionliberation.api.post.entity.PostType;
import kr.addictionliberation.api.user.entity.User; // User 엔티티 import
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "post")
@Getter
@Setter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @ManyToOne
    @JoinColumn(name = "postTypeId")
    private PostType postType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostContent> contents;

    @ManyToMany
    @JoinTable(
            name = "post_category",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;

    @Column(name = "is_main")
    private Boolean isMain;

    // 생성자, getter, setter (Lombok 사용)
}