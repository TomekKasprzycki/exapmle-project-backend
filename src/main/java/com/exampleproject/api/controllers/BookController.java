package com.exampleproject.api.controllers;

import com.exampleproject.api.converters.BookDtoConverter;
import com.exampleproject.api.dto.BookDto;
import com.exampleproject.api.model.Book;
import com.exampleproject.api.model.User;
import com.exampleproject.api.services.AuthorService;
import com.exampleproject.api.services.BookService;
import com.exampleproject.api.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;
    private final BookDtoConverter bookDtoConverter;
    private final AuthorService authorService;
    private final UserService userService;

    public BookController(BookService bookService,
                          BookDtoConverter bookDtoConverter, AuthorService authorService, UserService userService) {
        this.bookService = bookService;
        this.bookDtoConverter = bookDtoConverter;
        this.authorService = authorService;
        this.userService = userService;
    }

    @GetMapping("/anonymous/showbooks")
    public List<BookDto> getAllBooks(@RequestParam int limit, @RequestParam int offset, HttpServletResponse response) {

        Optional<List<Book>> optionalBookList = bookService.getAllBooksWithLimitAndOffset(limit, offset);

        if (optionalBookList.isEmpty()) {
            response.setStatus(404);
            response.setHeader("ERROR", "Books are missing...");
            return new ArrayList<>();
        }

        List<Book> books = optionalBookList.get();

        return bookDtoConverter.convertToDto(books);
    }

    @GetMapping("/anonymous/showbooksnumber")
    public int showBookNumber() {

        return bookService.countBooks();
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @GetMapping("/mybooks")
    public List<BookDto> getMyBooks(@RequestParam int limit, @RequestParam int offset, HttpServletResponse response) {

        final String login = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Optional<User> optionalUser = userService.findUserByEmail(login);
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            Optional<List<Book>> optionalBooks = bookService.getAllUserBooksWithLimit(user.getId(), limit, offset);
            if(optionalBooks.isPresent()){
                List<Book> bookList = optionalBooks.get();
                return bookDtoConverter.convertToDto(bookList);
            } else {
                response.setStatus(404);
                response.setHeader("INFO","Ups, Your books are missing...");
            }

        }
        return new ArrayList<>();
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @GetMapping("/showuserbooksnumber")
    public int showUserBookNumber() {

        final String login = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Optional<User> optionalUser = userService.findUserByEmail(login);
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            return bookService.countUserBooks(user);
        }
        return 0;
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @GetMapping("/otherbooks")
    public List<BookDto> getOtherBooks(@RequestParam int limit, @RequestParam int offset, HttpServletResponse response) {

        final String login = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Optional<User> optionalUser = userService.findUserByEmail(login);
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            Optional<List<Book>> optionalBooks = bookService.getAllNotUserBooksWithLimit(user.getId(), limit, offset);
            if(optionalBooks.isPresent()){
                return bookDtoConverter.convertToDto(optionalBooks.get());
            } else {
                response.setStatus(404);
                response.setHeader("INFO","Ups, Your books are missing...");
            }
        }
        return new ArrayList<>();
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @GetMapping("/showotherbooksnumber")
    public int showOtherBookNumber() {

        final String login = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Optional<User> optionalUser = userService.findUserByEmail(login);
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            return bookService.countOtherBooks(user);
        }
        return 0;
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @GetMapping("/booksforlend")
    public List<BookDto> getBookForLend(@RequestParam int limit, @RequestParam int offset, HttpServletResponse response){

        final String login = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        Optional<List<Book>> optionalBookList = bookService.getBooksForLend(login, limit, offset);

        if (optionalBookList.isEmpty()) {
            response.setStatus(404);
            response.setHeader("ERROR", "Books are missing...");
            return new ArrayList<>();
        }

        List<Book> books = optionalBookList.get();

        return bookDtoConverter.convertToDto(books);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @GetMapping("/showbooksforlendnumber")
    public int showBookForLendNumber() {

        final String login = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        Optional<User> optionalUser = userService.findUserByEmail(login);
        User user = new User();
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        }
        return bookService.countBooksForLend(user);


    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @PostMapping("/addbook")
    public void addBook(@RequestBody BookDto bookDto) {

        bookDto.setId(null);
        Book book = bookDtoConverter.convertFromDto(bookDto);
        Optional<User> optionalUser = userService.findUserByEmail(bookDto.getOwner().getLogin());
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            book.setOwner(user);
            bookService.saveBook(book);
            authorService.addBookToAuthor(book);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @PostMapping("/editbook")
    public void editBook(@RequestBody BookDto bookDto) {

        final String login = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        final String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();

        Optional<User> optionalUser = userService.findUserByEmail(login);
        User user = new User();
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        }

        if (user.getEmail().equals(bookDto.getOwner().getLogin()) || Objects.equals(role, "ROLE_ADMIN")) {
            Book book = bookDtoConverter.convertFromDto(bookDto);
            bookService.saveBook(book);
            authorService.addBookToAuthor(book);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @DeleteMapping("/deletebook")
    public void deleteBook(@RequestBody BookDto bookDto) {

        final String login = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        final String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();

        Optional<User> optionalUser = userService.findUserByEmail(login);
        User user = new User();
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        }

        if (user.getEmail().equals(bookDto.getOwner().getLogin()) || Objects.equals(role, "ROLE_ADMIN")) {
            Book book = bookDtoConverter.convertFromDto(bookDto);
            authorService.removeBookFromAuthor(book);
            bookService.removeBook(book);
        }
    }
}
