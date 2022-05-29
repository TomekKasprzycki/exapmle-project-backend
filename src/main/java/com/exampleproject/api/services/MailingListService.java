package com.exampleproject.api.services;

import com.exampleproject.api.model.MailingList;
import com.exampleproject.api.model.User;
import com.exampleproject.api.repositories.MailingListRepository;
import org.springframework.stereotype.Service;


import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MailingListService {

    private final MailingListRepository mailingListRepository;

    public MailingListService(MailingListRepository mailingListRepository) {
        this.mailingListRepository=mailingListRepository;
    }

    public Map<String, String> getMailingList(Long id) {

        Optional<MailingList> optional = mailingListRepository.findById(id);

        if(optional.isPresent()) {
            MailingList mailingListById = optional.get();
            return mailingListById.getUsers().stream().collect(Collectors.toMap(User::getEmail, User::getName));
        }

        return null;
    }

}
