package com.exampleproject.api.repositories;

import com.exampleproject.api.model.Token;
import com.exampleproject.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query("select t from Token t where t.token=:tokenToDeactivation")
    Optional<Token> findByToken(@Param("tokenToDeactivation")String tokenToDeactivation);

    @Query("select t from Token t where t.user=:user")
    Optional<Token> findByUser(@Param("user") User user);
}
