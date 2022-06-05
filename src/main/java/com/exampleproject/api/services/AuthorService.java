package com.exampleproject.api.services;

import com.exampleproject.api.model.Author;
import com.exampleproject.api.model.Book;
import com.exampleproject.api.repositories.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {

        this.authorRepository = authorRepository;
    }

    public Optional<List<Book>> getAuthorBooks(Long id) {
        return authorRepository.findAuthorBooks(id);
    }

    public void addBookToAuthor(Book book) {
        List<Author> authors = book.getAuthors();
        for (Author author : authors) {
            Optional<List<Book>> authorBooks = getAuthorBooks(author.getId());
            List<Book> books;
            if (authorBooks.isEmpty()) {
                books = new ArrayList<>();
            } else {
                books = authorBooks.get();
            }
            if (!books.contains(book)) {
                books.add(book);
                author.setBooks(books);
            }
        }
    }

    public void removeBookFromAuthor(Book book) {
        List<Author> authors = book.getAuthors();
        for (Author author : authors) {
            Optional<List<Book>> authorBooks = getAuthorBooks(author.getId());
            List<Book> books;
            if (authorBooks.isPresent()) {
                books = authorBooks.get();
                if (books.contains(book)) {
                    books.remove(book);
                    author.setBooks(books);
                }
            }
        }
    }

    public Optional<List<Author>> getAllAuthors() {
        return authorRepository.findAllAuthors();
    }

    public void addAuthor(Author author) {
        authorRepository.save(author);
    }

    public int areThereAnyBooks(Long id) {
        return authorRepository.countBooks(id);
    }

    public void removeAuthor(Author author) {
        authorRepository.delete(author);
    }
}
