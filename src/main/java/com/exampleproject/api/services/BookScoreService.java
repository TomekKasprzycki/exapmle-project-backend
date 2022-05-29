package com.exampleproject.api.services;

import com.exampleproject.api.repositories.BookScoreRepository;
import org.springframework.stereotype.Service;

@Service
public class BookScoreService {

    private final BookScoreRepository bookScoreRepository;

    public BookScoreService(BookScoreRepository bookScoreRepository) {
        this.bookScoreRepository = bookScoreRepository;
    }



}
