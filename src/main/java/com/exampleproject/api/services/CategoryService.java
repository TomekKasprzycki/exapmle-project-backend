package com.exampleproject.api.services;

import com.exampleproject.api.model.Category;
import com.exampleproject.api.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    public CategoryService(CategoryRepository categoryRepository){

        this.categoryRepository = categoryRepository;
    }


    public Optional<List<Category>> getAll() {
        return categoryRepository.findAllCategories();
    }

    public void addCategory(Category category) {
        categoryRepository.save(category);
    }

    public void removeCategory(Category category) {
        categoryRepository.delete(category);
    }
}
