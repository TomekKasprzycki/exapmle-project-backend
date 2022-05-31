package com.exampleproject.api.controllers;

import com.exampleproject.api.converters.AuthorDtoConverter;
import com.exampleproject.api.dto.AuthorDto;
import com.exampleproject.api.model.Author;
import com.exampleproject.api.services.AuthorService;
import com.exampleproject.api.services.BookService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService authorService;
    private final AuthorDtoConverter authorDtoConverter;
    private final BookService bookService;

    public AuthorController(AuthorService authorService,
                            AuthorDtoConverter authorDtoConverter,
                            BookService bookService){
        this.authorService = authorService;
        this.authorDtoConverter = authorDtoConverter;
        this.bookService = bookService;
    }

    @GetMapping("/anonymous/getAllAuthors")
    public List<AuthorDto> getAllAuthors(@RequestParam int limit,
                                         @RequestParam int offset,
                                         HttpServletResponse response){

        Optional<List<Author>> optionalAuthors = authorService.getAllAuthors(limit, offset);
        if(optionalAuthors.isEmpty()) {
            response.setStatus(404);
            response.setHeader("INFO", "There are no authors here...");
            return new ArrayList<>();
        }
        return authorDtoConverter.convertToDto(optionalAuthors.get());
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @PostMapping("/addauthor")
    public void addAuthor(@RequestBody AuthorDto authorDto){
        Author author = authorDtoConverter.convertFromDto(authorDto);
        authorService.addAuthor(author);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/editauthor")
    public void editAuthor(@RequestBody AuthorDto authorDto){
        Author author = authorDtoConverter.convertFromDto(authorDto);
        authorService.addAuthor(author);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/deleteauthor")
    public void deleteAuthor(@RequestBody AuthorDto authorDto, HttpServletResponse response){
        Author author = authorDtoConverter.convertFromDto(authorDto);
        if(bookService.areThereAnyBooksOfAuthor(author.getId())) {
            response.setStatus(405);
            response.setHeader("ERROR","Author may not be deleted, because they has books...");
        } else {
            authorService.removeAuthor(author);
        }
    }

}
