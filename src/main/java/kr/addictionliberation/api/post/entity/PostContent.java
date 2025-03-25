package kr.addictionliberation.api.post.entity;

import kr.addictionliberation.api.user.entity.User; // User import
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "post_content")
@Getter
@Setter
public class PostContent {

    @Id
     @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(nullable = false)
    private String locale;

    @Column(nullable = false)
    private String title;

    private String subtitle;

    @Column(columnDefinition = "TEXT")
    private String content;

    private Boolean published;

    @ManyToOne
    @JoinColumn(name = "authorId", nullable = false)
    private User author;

    private LocalDateTime publishedDate;

    @Column(columnDefinition = "integer default 0")
    private Integer viewCount;

    private String thumbnailImageURL;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    // 생성자, getter, setter (Lombok 사용)

     public void setPost(Post post) {
        this.post = post;
    }
}