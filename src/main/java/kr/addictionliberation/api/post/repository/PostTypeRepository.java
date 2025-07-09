// 경로: src/main/java/kr/addictionliberation/api/post/repository/PostTypeRepository.java
package kr.addictionliberation.api.post.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.addictionliberation.api.post.entity.PostType;

public interface PostTypeRepository extends JpaRepository<PostType, Integer> {
  // 이름으로 PostType 찾기 (중복 방지 등에 사용)
  Optional<PostType> findByName(String name);

  // 순서(order)와 이름으로 정렬하여 모두 조회
  List<PostType> findAllByOrderByOrderAscNameAsc();
}