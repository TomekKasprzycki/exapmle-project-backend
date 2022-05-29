package com.exampleproject.api.converters;

import com.exampleproject.api.dto.CategoryDto;
import com.exampleproject.api.model.Category;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryDtoConverter {

    public CategoryDto convertToDto (Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        return categoryDto;
    }

    public Category convertFromDto (CategoryDto categoryDto) {
        Category category = new Category();
        category.setId(categoryDto.getId());
        category.setName(categoryDto.getName());
        return category;
    }

    public List<CategoryDto> convertToDto(List<Category> categories) {
        return categories.stream().map(this::convertToDto).collect(Collectors.toList());
    }

}
