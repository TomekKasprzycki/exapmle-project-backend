package com.exampleproject.api.services;

import com.exampleproject.api.filters.SecurityConstants;
import com.exampleproject.api.model.Token;
import com.exampleproject.api.model.User;
import com.exampleproject.api.repositories.TokenRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;
    public TokenService(TokenRepository tokenRepository) {

        this.tokenRepository = tokenRepository;
    }

    public Optional<Token> findByToken(String tokenToDeactivation) {
        return tokenRepository.findByToken(tokenToDeactivation);
    }

    public Token addToken(Token token) {
        return tokenRepository.save(token);
    }

    public String createToken(User user){

        long currentDateMilliseconds = System.currentTimeMillis();
        long expirationTime = 20 * 60 * 1000; //20 minutes

        byte[] signingKey = SecurityConstants.JWT_SECRET.getBytes();

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("id",user.getId())
                .claim("name", user.getName())
                .claim("email", user.getEmail())
                .claim("role", user.getRole().getName())
                .setIssuedAt(new Date(currentDateMilliseconds))
                .setExpiration(new Date(currentDateMilliseconds + expirationTime))
                .signWith(SignatureAlgorithm.HS256, signingKey)
                .compact();
    }

    public Optional<Token> findByUser(User user) {
        return tokenRepository.findByUser(user);
    }
}
