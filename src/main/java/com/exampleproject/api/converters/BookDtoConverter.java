package com.exampleproject.api.converters;

import com.exampleproject.api.dto.BookDto;
import com.exampleproject.api.model.Book;
import com.exampleproject.api.model.BookScore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookDtoConverter {

    private final AuthorDtoConverter authorDtoConverter;
    private final CategoryDtoConverter categoryDtoConverter;
    private final UserDtoConverter userDtoConverter;

    public BookDtoConverter(AuthorDtoConverter authorDtoConverter,
                            CategoryDtoConverter categoryDtoConverter, UserDtoConverter userDtoConverter) {
        this.authorDtoConverter = authorDtoConverter;
        this.categoryDtoConverter = categoryDtoConverter;
        this.userDtoConverter = userDtoConverter;
    }

    public BookDto convertToDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthors(authorDtoConverter.convertToDto(book.getAuthors()));
        bookDto.setCategory(categoryDtoConverter.convertToDto(book.getCategory()));
        bookDto.setOwner(userDtoConverter.convertToDto(book.getOwner()));
        bookDto.setBookLended(book.isBookLended());
        bookDto.setScore(book.getScores().stream().collect(Collectors.averagingDouble(BookScore::getScore)));
        return bookDto;
    }

    public Book convertFromDto(BookDto bookDto) {
        Book book = new Book();
        book.setId(bookDto.getId());
        book.setTitle(bookDto.getTitle());
        book.setAuthors(bookDto.getAuthors().stream().map(authorDtoConverter::convertFromDto).collect(Collectors.toList()));
        book.setCategory(categoryDtoConverter.convertFromDto(bookDto.getCategory()));
        book.setOwner(userDtoConverter.convertFromDto(bookDto.getOwner()));
        book.setBookLended(bookDto.isBookLended());
        return book;
    }

    public List<BookDto> convertToDto(List<Book> books) {
        return books.stream().map(this::convertToDto).collect(Collectors.toList());
    }

}
