package com.exampleproject.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookDto {

    private Long id;
    private String title;
    private List<AuthorDto> authors;
    private CategoryDto category;
    private double score;
    private UserDto owner;
    private boolean bookLended;


}
