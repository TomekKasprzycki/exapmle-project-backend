package com.exampleproject.api.repositories;

import com.exampleproject.api.model.Book;
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
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.email=:email")
    Optional<User> findByEmail(@Param("email")String email);

    @Query(nativeQuery = true, value = "select * from neighborlibrary.user limit :limit offset :offset")
    Optional<List<User>> findAllWithLimitAndOffset(@Param("limit") int limit,@Param("offset") int offset);

    @Query("update User u set u.active=false where u=:user")
    void setUserNotActive(@Param("user") User user);

    @Query("update User u set u.active=true where u=:user")
    void setUserActive(@Param("user") User user);

    @Query("select count(u) from User u where u.name=:name")
    int isUserNameTaken(String name);

}
