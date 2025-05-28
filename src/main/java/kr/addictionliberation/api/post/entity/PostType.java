package kr.addictionliberation.api.post.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter; // 추가
import lombok.ToString;

@Entity
@Table(name = "post_types")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor // 기본 생성자 추가
@ToString(exclude = { "post" }) // posts 필드는 toString에서 제외
@EqualsAndHashCode(of = "id") // id 필드 기준으로 equals/hashCode 생성
public class PostType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(name = "`order`")
    private Integer order;

    private String description;

    @OneToMany(mappedBy = "postType")
    @Builder.Default // 빌더 사용 시 기본값 설정
    private List<Post> post = new ArrayList<>(); // NPE 방지를 위해 초기화

    // 특정 필드만 받는 생성자가 필요하다면 직접 정의할 수 있습니다.
    // @Builder 어노테이션이 있으면 대부분의 경우 이 생성자는 필요 없을 수 있습니다.
    // public PostType(String name, Integer order, String description) {
    // this.name = name;
    // this.order = order;
    // this.description = description;
}