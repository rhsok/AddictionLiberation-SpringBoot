
package kr.addictionliberation.api.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostTypeUpdateRequest {
  @NotBlank(message = "게시글 유형 이름은 필수입니다.")
  @Size(max = 50, message = "이름은 50자를 넘을 수 없습니다.")
  private String name;

  private Integer order;

  @Size(max = 255, message = "설명은 255자를 넘을 수 없습니다.")
  private String description;
}
