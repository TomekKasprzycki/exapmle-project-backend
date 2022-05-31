package com.exampleproject.api.repositories;

import com.exampleproject.api.model.Author;
import com.exampleproject.api.model.Book;
import com.exampleproject.api.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query(nativeQuery = true, value = "select * from neighborlibrary.book limit :limit offset :offset")
    Optional<List<Book>> findAllBooksWithLimitAndOffset(@Param("limit") int limit, @Param("offset") int offset);

    @Query("select count(b) from Book b where b.category=:category")
    int findBooksWithCategory(@Param("category") Category category);

    @Query(nativeQuery = true, value=
            "select count(*) from book inner join book_authors on books_id = book.id inner join author " +
                    "on authors_id = author.id where author.id=:authorId ")
    int countBookWithAuthor(@Param("authorId") Long authorId);

    @Query(nativeQuery = true, value = "select * from neighborlibrary.book where book.user_id=:id limit :limit offset :offset")
    Optional<List<Book>> findAllUserBooksWithLimit(@Param("id") Long id, @Param("limit") int limit, @Param("offset") int offset);

    @Query(nativeQuery = true, value = "select * from neighborlibrary.book where book.user_id not in(:id) limit :limit offset :offset")
    Optional<List<Book>> findAllNotUserBooksWithLimit(@Param("id") Long id, @Param("limit") int limit, @Param("offset") int offset);
}
