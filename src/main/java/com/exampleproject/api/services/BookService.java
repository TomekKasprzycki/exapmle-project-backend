package com.exampleproject.api.services;

import com.exampleproject.api.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    @Autowired
    private final BookRepository bookRepository;
    public BookService(BookRepository bookRepository) {

        this.bookRepository = bookRepository;
    }

}
