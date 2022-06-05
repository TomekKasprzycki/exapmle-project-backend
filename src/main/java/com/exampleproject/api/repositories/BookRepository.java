package com.exampleproject.api.repositories;

import com.exampleproject.api.model.Book;
import com.exampleproject.api.model.Category;
import com.exampleproject.api.model.User;
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

    @Query(nativeQuery = true, value = "select * from neighborlibrary.book " +
            "inner join neighborlibrary.user on neighborlibrary.book.user_id=neighborlibrary.user.id " +
            "where book.book_lended=false and not user.email=:login limit :limit offset :offset")
    Optional<List<Book>> findAllNotLended(@Param("login") String login, @Param("limit") int limit,@Param("offset") int offset);

    @Query("select count(b) from Book b")
    int countBooks();

    @Query("select count(b) from Book b where b.owner=:user")
    int countAllUserBooks(@Param("user") User user);

    @Query("select count(b) from Book b where b.owner not in (:user)")
    int countOtherBooks(@Param("user") User user);

    @Query("select count(b) from Book b where b.bookLended=false and b.owner not in(:user)")
    int countBooksForLend(@Param("user") User user);
}
