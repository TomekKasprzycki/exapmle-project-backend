package com.exampleproject.api.repositories;

import com.exampleproject.api.model.MailingList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface MailingListRepository extends JpaRepository<MailingList, Long> {

    @Query("select ml from MailingList ml where ml.id=:id")
    Optional<MailingList> findById(@Param("id") Long id);

}
