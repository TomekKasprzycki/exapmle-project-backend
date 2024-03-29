package com.exampleproject.api.repositories;

import com.exampleproject.api.model.LendingRegister;
import com.exampleproject.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface LendingRegisterRepository extends JpaRepository<LendingRegister, Long> {

    @Query("select lr from LendingRegister lr where lr.user=:user")
    Optional<List<LendingRegister>> findAllByUser(User user);



}
