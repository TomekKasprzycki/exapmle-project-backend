package com.exampleproject.api.repositories;

import com.exampleproject.api.model.BookScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface BookScoreRepository extends JpaRepository<BookScore, Long> {

}
