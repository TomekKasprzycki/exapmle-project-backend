package com.exampleproject.api.services;

import com.exampleproject.api.model.Author;
import com.exampleproject.api.model.Book;
import com.exampleproject.api.model.Category;
import com.exampleproject.api.model.User;
import com.exampleproject.api.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    @Autowired
    private final BookRepository bookRepository;
    public BookService(BookRepository bookRepository) {

        this.bookRepository = bookRepository;
    }

    public Optional<List<Book>> getAllBooksWithLimitAndOffset(int limit, int offset) {
        return bookRepository.findAllBooksWithLimitAndOffset(limit,offset);
    }

    public void saveBook(Book book) {
        bookRepository.save(book);
    }

    public void removeBook(Book book) {
        bookRepository.delete(book);
    }

    public boolean hasAnyBookThisCategory(Category category) {
        return bookRepository.findBooksWithCategory(category) != 0;
    }

    public boolean areThereAnyBooksOfAuthor(Long id) {
        return bookRepository.countBookWithAuthor(id) != 0;
    }

    public Optional<List<Book>> getAllUserBooksWithLimit(Long id, int limit, int offset) {
        return bookRepository.findAllUserBooksWithLimit(id, limit, offset);
    }

    public Optional<List<Book>> getAllNotUserBooksWithLimit(Long id, int limit, int offset) {
        return bookRepository.findAllNotUserBooksWithLimit(id, limit, offset);
    }

    public Optional<List<Book>> getBooksForLend(String login, int limit, int offset) {
        return bookRepository.findAllNotLended(login, limit, offset);
    }

    public int countBooks() {
        return bookRepository.countBooks();
    }

    public int countUserBooks(User user) {
        return bookRepository.countAllUserBooks(user);
    }

    public int countOtherBooks(User user) {
        return bookRepository.countOtherBooks(user);
    }

    public int countBooksForLend(User user) {
        return bookRepository.countBooksForLend(user);
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }
}
