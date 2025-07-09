// 경로: src/main/java/kr/addictionliberation/api/post/dto/PostTypeResponse.java
package kr.addictionliberation.api.post.dto;

import kr.addictionliberation.api.post.entity.PostType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostTypeResponse {
  private Integer id;
  private String name;
  private Integer order;
  private String description;

  // Entity를 DTO로 변환하는 정적 팩토리 메서드
  public static PostTypeResponse from(PostType postType) {
    PostTypeResponse response = new PostTypeResponse();
    response.setId(postType.getId());
    response.setName(postType.getName());
    response.setOrder(postType.getOrder());
    response.setDescription(postType.getDescription());
    return response;
  }
}