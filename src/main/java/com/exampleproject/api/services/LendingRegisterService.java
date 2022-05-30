package com.exampleproject.api.services;

import com.exampleproject.api.model.LendingRegister;
import com.exampleproject.api.model.User;
import com.exampleproject.api.repositories.LendingRegisterRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LendingRegisterService {

    private final LendingRegisterRepository lendingRegisterRepository;

    public LendingRegisterService(LendingRegisterRepository lendingRegisterRepository){
        this.lendingRegisterRepository = lendingRegisterRepository;
    }

    public Optional<List<LendingRegister>> getRegisterForUser(User user) {
        return lendingRegisterRepository.findAllByUser(user);
    }
}
