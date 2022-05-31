package com.exampleproject.api.filters;

import com.exampleproject.api.dto.UserDto;
import com.exampleproject.api.model.Token;
import com.exampleproject.api.model.User;
import com.exampleproject.api.services.TokenService;
import com.exampleproject.api.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final TokenService tokenService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
                                   TokenService tokenService,
                                   UserService userService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.userService = userService;
        setFilterProcessesUrl(SecurityConstants.AUTH_LOGIN_URL);
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

//        String header = request.getHeader("TYPE");
//        if (header == null) {
//            return null;
//        }
        UsernamePasswordAuthenticationToken authenticationToken = null;
//        UserDto userDto = parseUserDto(request);
//        if(userDto.getId() == 0) {
//            userDto.setId(null);
//        }
//
//        switch (header) {
//            case "Registration":
//
//                if (userService.registerUser(userDto)) {
//                    response.setStatus(200);
//                    response.setHeader("Status", "REGISTERED");
//                } else {
//                    response.setHeader("Status", "DENIED");
//                }
//                break;
//
//            case "Login":
//
//                Optional<User> optional = userService.findUserByEmail(userDto.getLogin());
//                User user;
//
//                if (optional.isPresent()) {
//                    user = optional.get();
//                } else {
//                    break;
//                }
//
//                if (user.isActive() && user.checkPassword(userDto.getPassword())) {
//                    authenticationToken =
//                            new UsernamePasswordAuthenticationToken(userDto.getLogin(), userDto.getPassword());
//                }
//
//                break;
//
//            case "Logout":
//                String tokenToDeactivation = request.getHeader("Authorization").replace("Bearer ", "");
//                Optional<Token> optionalToken = tokenService.findByToken(tokenToDeactivation);
//                if (optionalToken.isPresent()) {
//                    Token token = optionalToken.get();
//                    token.setActive(false);
//                    tokenService.addToken(token);
//                    response.setHeader("Status", "LOGOUT");
//                } else {
//                    response.setHeader("Status", "DENIED");
//                }
//                break;
//        }

        return authenticationManager.authenticate(authenticationToken);
    }

    @SneakyThrows
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain filterChain, Authentication authentication) {


        final org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

        Optional<User> optionalUser = userService.findUserByEmail(principal.getUsername());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String token = tokenService.createToken(user);
            Token createdToken;

            Optional<Token> optionalToken = tokenService.findByUser(user);
            if (optionalToken.isEmpty()) {

                createdToken = new Token();
                createdToken.setToken(token);
                createdToken.setActive(true);
                createdToken.setUser(user);
                createdToken = tokenService.addToken(createdToken);
                user.setToken(createdToken);
                userService.save(user);
            } else {
                createdToken = optionalToken.get();
                createdToken.setToken(token);
                createdToken.setActive(true);
            }
            tokenService.addToken(createdToken);


            response.addHeader(SecurityConstants.TOKEN_HEADER, SecurityConstants.TOKEN_PREFIX + token);
        }
    }


    private UserDto parseUserDto(HttpServletRequest request) {
        if (request.getMethod().equalsIgnoreCase("post")) {
            try {
                final String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
                return objectMapper.readValue(requestBody, UserDto.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new UserDto();
    }

}
