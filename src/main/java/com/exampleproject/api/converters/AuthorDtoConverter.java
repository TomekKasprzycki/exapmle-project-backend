package com.exampleproject.api.converters;

import com.exampleproject.api.dto.AuthorDto;
import com.exampleproject.api.model.Author;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorDtoConverter {

    public AuthorDto convertToDto(Author author) {

        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(author.getId());
        authorDto.setFirstName(author.getFirstName());
        authorDto.setSecondName(author.getSecondName());
        authorDto.setLastName(author.getLastName());
        return authorDto;
    }

    public Author convertFromDto(AuthorDto authorDto){
        Author author = new Author();
        author.setId(authorDto.getId());
        author.setFirstName(authorDto.getFirstName());
        author.setSecondName(authorDto.getSecondName());
        author.setLastName(authorDto.getLastName());
        return author;
    }

    public List<AuthorDto> convertToDto(List<Author> authors) {
        return authors.stream().map(this::convertToDto).collect(Collectors.toList());
    }
}
