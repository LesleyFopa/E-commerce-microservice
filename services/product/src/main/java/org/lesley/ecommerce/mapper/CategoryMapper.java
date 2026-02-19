package org.lesley.ecommerce.mapper;

import org.lesley.ecommerce.dtos.CategoryRequest;
import org.lesley.ecommerce.dtos.CategoryResponse;
import org.lesley.ecommerce.entity.Category;
import org.springframework.stereotype.Service;

@Service
public class CategoryMapper {

    public Category toCategory(CategoryRequest request) {
        if (request == null) {
            return null;
        }
        return Category.builder()
                .name(request.name())
                .description(request.description())
                .build();
    }

    public CategoryResponse fromCategory(Category category) {
        if (category == null) {
            return null;
        }
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }
}