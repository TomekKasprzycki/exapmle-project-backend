package com.exampleproject.api.repositories;

import com.exampleproject.api.model.Author;
import com.exampleproject.api.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query("select a.books from Author a where a.id=:id ")
    Optional<List<Book>> findAuthorBooks(Long id);

    @Query(nativeQuery = true, value = "select * from neighborlibrary.author limit :limit offset :offset")
    Optional<List<Author>> findAllAuthors(@Param("limit") int limit,@Param("offset") int offset);

    @Query("select count(a.books) from Author a where a.id=:id")
    int countBooks(Long id);
}
