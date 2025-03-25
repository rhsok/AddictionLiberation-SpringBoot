package kr.addictionliberation.api.category.service;

import kr.addictionliberation.api.category.entity.Category;
import kr.addictionliberation.api.category.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // 기본적으로 읽기 전용 트랜잭션
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional // 쓰기에는 별도로 @Transactional 어노테이션 사용
    public Category createCategory(Category category) {
        // (선택 사항) 이미 존재하는 카테고리 이름인지 확인
        if (categoryRepository.findByName(category.getName()).isPresent()) {
            throw new IllegalArgumentException("Category with name '" + category.getName() + "' already exists.");
        }
        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategory(Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID: " + id));
    }

    @Transactional
    public Category updateCategory(Integer id, Category updatedCategory) {
        Category category = getCategory(id); // ID로 기존 카테고리 가져오기 (getCategory() 메서드 사용)
        category.setName(updatedCategory.getName());
        category.setDescription(updatedCategory.getDescription());
        // (선택 사항) 다른 필드도 업데이트
        return categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Integer id) {
        categoryRepository.deleteById(id);
    }
}