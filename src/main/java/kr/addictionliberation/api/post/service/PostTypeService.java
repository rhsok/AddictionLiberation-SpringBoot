package kr.addictionliberation.api.post.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.addictionliberation.api.post.dto.PostTypeCreateRequest;
import kr.addictionliberation.api.post.dto.PostTypeResponse;
import kr.addictionliberation.api.post.dto.PostTypeUpdateRequest;
import kr.addictionliberation.api.post.entity.PostType;
import kr.addictionliberation.api.post.repository.PostTypeRepository;
import kr.addictionliberation.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostTypeService {

  private final PostTypeRepository postTypeRepository;

  // 생성
  @Transactional
  public PostTypeResponse createPostType(PostTypeCreateRequest request) {
    postTypeRepository.findByName(request.getName()).ifPresent(pt -> {
      throw new IllegalArgumentException("이미 존재하는 게시글 유형 이름입니다: " + request.getName());
    });

    PostType postType = new PostType();
    postType.setName(request.getName());
    postType.setOrder(request.getOrder());
    postType.setDescription(request.getDescription());

    PostType savedPostType = postTypeRepository.save(postType);
    return PostTypeResponse.from(savedPostType);
  }

  // 전체 조회 (순서 및 이름으로 정렬)
  public List<PostTypeResponse> getAllPostTypes() {
    return postTypeRepository.findAllByOrderByOrderAscNameAsc().stream()
        .map(PostTypeResponse::from)
        .collect(Collectors.toList());
  }

  // ID로 단일 조회
  public PostTypeResponse getPostTypeById(Integer id) {
    PostType postType = postTypeRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("ID " + id + "에 해당하는 게시글 유형을 찾을 수 없습니다."));
    return PostTypeResponse.from(postType);
  }

  // 수정
  @Transactional
  public PostTypeResponse updatePostType(Integer id, PostTypeUpdateRequest request) {
    PostType postType = postTypeRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("ID " + id + "에 해당하는 게시글 유형을 찾을 수 없습니다."));

    // 이름 변경 시 중복 체크
    postTypeRepository.findByName(request.getName()).ifPresent(pt -> {
      if (!pt.getId().equals(id)) {
        throw new IllegalArgumentException("이미 존재하는 게시글 유형 이름입니다: " + request.getName());
      }
    });

    postType.setName(request.getName());
    postType.setOrder(request.getOrder());
    postType.setDescription(request.getDescription());

    PostType updatedPostType = postTypeRepository.save(postType);
    return PostTypeResponse.from(updatedPostType);
  }

  // 삭제
  @Transactional
  public void deletePostType(Integer id) {
    PostType postType = postTypeRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("ID " + id + "에 해당하는 게시글 유형을 찾을 수 없습니다."));

    // 해당 유형을 사용하는 게시글이 있는지 확인 (중요)
    if (postType.getPost() != null && !postType.getPost().isEmpty()) {
      throw new IllegalStateException("해당 게시글 유형을 사용하는 게시글이 있어 삭제할 수 없습니다.");
    }

    postTypeRepository.delete(postType);
  }
}