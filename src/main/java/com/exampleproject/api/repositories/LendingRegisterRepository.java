package com.exampleproject.api.repositories;

import com.exampleproject.api.model.LendingRegister;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface LendingRegisterRepository extends JpaRepository<LendingRegister, Long> {
}
