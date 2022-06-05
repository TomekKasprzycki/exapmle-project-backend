package com.exampleproject.api.controllers;

import com.exampleproject.api.converters.BookDtoConverter;
import com.exampleproject.api.converters.LendingRegisterDtoConverter;
import com.exampleproject.api.dto.BookDto;
import com.exampleproject.api.dto.LendingRegisterDto;
import com.exampleproject.api.model.Book;
import com.exampleproject.api.model.LendingRegister;
import com.exampleproject.api.model.User;
import com.exampleproject.api.services.BookService;
import com.exampleproject.api.services.LendingRegisterService;
import com.exampleproject.api.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lendingregister")
public class LendingRegisterController {

    private final LendingRegisterService lendingRegisterService;
    private final LendingRegisterDtoConverter lendingRegisterDtoConverter;
    private final UserService userService;
    private final BookDtoConverter bookDtoConverter;
    private final BookService bookService;

    public LendingRegisterController(LendingRegisterService lendingRegisterService,
                                     LendingRegisterDtoConverter lendingRegisterDtoConverter, UserService userService, BookDtoConverter bookDtoConverter, BookService bookService) {
        this.lendingRegisterService = lendingRegisterService;
        this.lendingRegisterDtoConverter = lendingRegisterDtoConverter;
        this.userService = userService;
        this.bookDtoConverter = bookDtoConverter;
        this.bookService = bookService;
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @GetMapping("/showregister")
    public List<LendingRegisterDto> showRegister(HttpServletResponse response) {

        final String login = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Optional<User> optionalUser = userService.findUserByEmail(login);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Optional<List<LendingRegister>> optionalLendingRegister = lendingRegisterService.getRegisterForUser(user);
            if (optionalLendingRegister.isPresent()) {
                return lendingRegisterDtoConverter.convertToDto(optionalLendingRegister.get());
            } else {
                return new ArrayList<>();
            }
        }

        response.setStatus(404);
        response.setHeader("ERROR", "Register not found...");
        return null;
    }

    @PreAuthorize("hasAnyRole('ROLE_USE','ROLE_ADMIN')")
    @PostMapping("/lendBook")
    public void lendBook(@RequestBody BookDto bookDto) {

        final String login = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Optional<User> optionalUser = userService.findUserByEmail(login);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Book book = bookDtoConverter.convertFromDto(bookDto);

            Optional<User> optionalOwner = userService.findUserByEmail(bookDto.getOwner().getLogin());
            User owner = new User();
            if (optionalOwner.isPresent()) {
                owner = optionalOwner.get();
            }
            book.setOwner(owner);
            book.setBookLended(true);

            LendingRegister newLend = new LendingRegister();
            long currentDateMilliseconds = System.currentTimeMillis();
            Date currentDate = new Date(currentDateMilliseconds);
            newLend.setBook(book);
            newLend.setUser(user);
            newLend.setDateOfLend(currentDate);

            lendingRegisterService.saveNewRegister(newLend);
            bookService.saveBook(book);
        }
    }


    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @PostMapping("/returnbook")
    public void returnBook(@RequestBody LendingRegisterDto lendingRegisterDto, HttpServletResponse response) {

        final String login = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Optional<User> optionalUser = userService.findUserByEmail(login);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            LendingRegister lendingRegister = lendingRegisterService.getRegister(lendingRegisterDto.getId());

            Optional<Book> optionalBook = bookService.getBookById(lendingRegister.getBook().getId());
            Book book = new Book();
            if (optionalBook.isPresent()) book = optionalBook.get();
            book.setBookLended(false);

            bookService.saveBook(book);
            lendingRegister.setDateOfReturn(new Date(System.currentTimeMillis()));
            lendingRegister.setUser(user);
            lendingRegisterService.saveNewRegister(lendingRegister);
        } else {
            response.setStatus(404);
            response.setHeader("ERROR", "Data not found");
        }
    }


}
