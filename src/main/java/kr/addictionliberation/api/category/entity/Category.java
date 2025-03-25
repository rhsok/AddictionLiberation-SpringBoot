package kr.addictionliberation.api.category.entity;

import jakarta.persistence.*;
import java.util.List;
import kr.addictionliberation.api.post.entity.Post; 

@Entity
@Table(name = "categories") // 데이터베이스 테이블 이름
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String name;

    private String description;

    @ManyToMany(mappedBy = "categories") // Post 엔티티의 categories 필드에 의해 매핑됨
    private List<Post> posts;  //Post는 임포트 해야됨.


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}