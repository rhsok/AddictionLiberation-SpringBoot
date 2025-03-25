// src/main/java/kr/addictionliberation/api/category/dto/CategoryResponse.java
package kr.addictionliberation.api.category.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryResponse {
    private Integer id;
    private String name;
    private String description;
}