package kr.addictionliberation.api.category.controller;

import kr.addictionliberation.api.category.dto.CategoryCreateRequest;
import kr.addictionliberation.api.category.dto.CategoryResponse;
import kr.addictionliberation.api.category.dto.CategoryUpdateRequest;
import kr.addictionliberation.api.category.entity.Category;
import kr.addictionliberation.api.category.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody CategoryCreateRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        Category createdCategory = categoryService.createCategory(category);

        // Entity -> DTO 변환
        CategoryResponse response = new CategoryResponse();
        response.setId(createdCategory.getId());
        response.setName(createdCategory.getName());
        response.setDescription(createdCategory.getDescription());

        return ResponseEntity.created(URI.create("/api/categories/" + createdCategory.getId()))
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        // Entity List -> DTO List 변환
        List<CategoryResponse> responseList = categories.stream()
                .map(category -> {
                    CategoryResponse response = new CategoryResponse();
                    response.setId(category.getId());
                    response.setName(category.getName());
                    response.setDescription(category.getDescription());
                    return response;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategory(@PathVariable Integer id) {
        Category category = categoryService.getCategory(id);

        // Entity -> DTO 변환
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setDescription(category.getDescription());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable Integer id, @RequestBody CategoryUpdateRequest request) {
        Category updatedCategory = new Category();
    updatedCategory.setName(request.getName()); // DTO에서 엔티티로 필요한 데이터 복사
        updatedCategory.setDescription(request.getDescription());

        Category category = categoryService.updateCategory(id, updatedCategory);

        // Entity -> DTO 변환
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setDescription(category.getDescription());

        return ResponseEntity.ok(response);
    }
    //delete는 변경사항 없음
     @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

}