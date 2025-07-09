// 경로: src/main/java/kr/addictionliberation/api/post/controller/PostTypeController.java
package kr.addictionliberation.api.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.addictionliberation.api.post.dto.PostTypeCreateRequest;
import kr.addictionliberation.api.post.dto.PostTypeResponse;
import kr.addictionliberation.api.post.dto.PostTypeUpdateRequest;
import kr.addictionliberation.api.post.service.PostTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "Post Types", description = "게시글 유형 관리 API")
@RestController
@RequestMapping("/api/post-types")
@RequiredArgsConstructor
public class PostTypeController {

  private final PostTypeService postTypeService;

  @Operation(summary = "게시글 유형 생성")
  @PostMapping
  public ResponseEntity<PostTypeResponse> createPostType(@Valid @RequestBody PostTypeCreateRequest request) {
    PostTypeResponse createdPostType = postTypeService.createPostType(request);
    return ResponseEntity.created(URI.create("/api/post-types/" + createdPostType.getId())).body(createdPostType);
  }

  @Operation(summary = "모든 게시글 유형 조회")
  @GetMapping
  public ResponseEntity<List<PostTypeResponse>> getAllPostTypes() {
    return ResponseEntity.ok(postTypeService.getAllPostTypes());
  }

  @Operation(summary = "특정 게시글 유형 조회")
  @GetMapping("/{id}")
  public ResponseEntity<PostTypeResponse> getPostTypeById(@PathVariable Integer id) {
    return ResponseEntity.ok(postTypeService.getPostTypeById(id));
  }

  @Operation(summary = "게시글 유형 수정")
  @PutMapping("/{id}")
  public ResponseEntity<PostTypeResponse> updatePostType(@PathVariable Integer id,
      @Valid @RequestBody PostTypeUpdateRequest request) {
    return ResponseEntity.ok(postTypeService.updatePostType(id, request));
  }

  @Operation(summary = "게시글 유형 삭제")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletePostType(@PathVariable Integer id) {
    postTypeService.deletePostType(id);
    return ResponseEntity.noContent().build();
  }
}