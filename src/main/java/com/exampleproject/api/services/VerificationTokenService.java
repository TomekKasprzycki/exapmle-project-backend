package com.exampleproject.api.services;

import com.exampleproject.api.model.User;
import com.exampleproject.api.model.VerificationToken;
import com.exampleproject.api.repositories.VerificationTokenRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;


    public VerificationTokenService(VerificationTokenRepository verificationTokenRepository) {
        this.verificationTokenRepository=verificationTokenRepository;
    }

    public VerificationToken createToken() {


        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken();
        verificationToken.setActive(true);
        verificationToken.setExpirationDate();

        return verificationToken;
    }

    public VerificationToken save(VerificationToken verificationToken) {
        return verificationTokenRepository.save(verificationToken);
    }

    public Optional<VerificationToken> getByToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }

    public void remove(VerificationToken verificationToken) {
        verificationTokenRepository.delete(verificationToken);
    }

    public Optional<VerificationToken> getByUser(User user) {
        return verificationTokenRepository.findByUser(user);
    }
}
