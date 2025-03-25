package kr.addictionliberation.api.category.repository;

import kr.addictionliberation.api.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
	
     Optional<Category> findByName(String name); //이름으로 카테고리 찾기
}