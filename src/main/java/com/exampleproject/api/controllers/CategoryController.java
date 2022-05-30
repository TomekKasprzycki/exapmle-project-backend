package com.exampleproject.api.controllers;

import com.exampleproject.api.converters.CategoryDtoConverter;
import com.exampleproject.api.dto.CategoryDto;
import com.exampleproject.api.model.Category;
import com.exampleproject.api.services.BookService;
import com.exampleproject.api.services.CategoryService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryDtoConverter categoryDtoConverter;
    private final BookService bookService;

    public CategoryController(CategoryService categoryService,
                              CategoryDtoConverter categoryDtoConverter, BookService bookService){
        this.categoryService = categoryService;
        this.categoryDtoConverter = categoryDtoConverter;
        this.bookService = bookService;
    }

    @GetMapping("/getAll")
    public List<CategoryDto> getAll(HttpServletResponse response) {

        Optional<List<Category>> optionalCategories = categoryService.getAll();
        if(optionalCategories.isPresent()){
            return categoryDtoConverter.convertToDto(optionalCategories.get());
        } else {
            response.setStatus(404);
            response.setHeader("ERROR","There are no categories...");
            return new ArrayList<>();
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @PostMapping("/addcategory")
    public void addCategory(@RequestBody CategoryDto categoryDto) {

        Category category = categoryDtoConverter.convertFromDto(categoryDto);
        categoryService.addCategory(category);

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/editcategory")
    public void editCategory(@RequestBody CategoryDto categoryDto) {

        Category category = categoryDtoConverter.convertFromDto(categoryDto);
        categoryService.addCategory(category);

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/deletecategory")
    public void deleteCategory(@RequestBody CategoryDto categoryDto, HttpServletResponse response) {

        Category category = categoryDtoConverter.convertFromDto(categoryDto);
        if(bookService.hasAnyBookThisCategory(category)){
            response.setStatus(405);
            response.setHeader("ERROR","There are books with this category...");
        } else {
            categoryService.removeCategory(category);
        }


    }



}
