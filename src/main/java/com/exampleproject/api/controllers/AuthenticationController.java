package com.exampleproject.api.controllers;

import com.exampleproject.api.dto.UserDto;
import com.exampleproject.api.filters.SecurityConstants;
import com.exampleproject.api.model.Token;
import com.exampleproject.api.model.User;
import com.exampleproject.api.services.TokenService;
import com.exampleproject.api.services.UserService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;


@RestController
@RequestMapping("/api/authentication")
public class AuthenticationController {

    private final TokenService tokenService;
    private final UserService userService;

    public AuthenticationController(TokenService tokenService,
                                    UserService userService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

//    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_PUBLISHER')")
//    @GetMapping("/refresh")
//    public void refresh(HttpServletResponse response) throws UserNotFoundException {
//
//        //Is this safe?
//
//        final String userName = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
//        User user = userService.findUserByEmail(userName).orElseThrow(() -> new UserNotFoundException("User not found"));
//
//        final String token = tokenService.createToken(user);
//        Token createdToken = tokenService.findByUser(user);
//        createdToken.setToken(token);
//        tokenService.addToken(createdToken);
//        response.addHeader(SecurityConstants.TOKEN_HEADER, SecurityConstants.TOKEN_PREFIX + token);
//
//    }

    @GetMapping("/anonymous/confirm")
    public RedirectView confirmUser(@RequestParam String token, HttpServletResponse response) {

        //todo set correct url...
        if (userService.verifyToken(token)) {
            response.setHeader("STATUS", "OK");
            return new RedirectView("http://localhost:3000/login");
        } else {
            response.setHeader("STATUS", "DENIED");
            return new RedirectView("http://localhost:3000/");
        }
    }

    @PostMapping("/anonymous/login")
    public String login(@RequestBody UserDto userDto, HttpServletResponse response) {

        Optional<User> optional = userService.findUserByEmail(userDto.getLogin());
        User user;

        if (optional.isEmpty()) {
            response.setStatus(404);
            response.setHeader("ERROR", "Invalid username or password");
            return null;
        }

        user = optional.get();
        String token = null;
        Token createdToken;

        if (user.isActive() && user.checkPassword(userDto.getPassword())) {

            token = tokenService.createToken(user);

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
        }


            response.addHeader(SecurityConstants.TOKEN_HEADER, SecurityConstants.TOKEN_PREFIX + token);

        return token;
    }

    @PostMapping("/anonymous/registration")
    public void registration(@RequestBody UserDto userDto, HttpServletResponse response) {

        if(userDto.getId() == 0) {
            userDto.setId(null);
        }

        if (userService.registerUser(userDto)) {
            response.setStatus(200);
            response.setHeader("Status", "REGISTERED");
        } else {
            response.setHeader("Status", "DENIED");
        }
    }

    @GetMapping("/logout")
    public void logout(HttpServletResponse response, HttpServletRequest request) {
        String tokenToDeactivation = request.getHeader("Authorization").replace("Bearer ", "");
        Optional<Token> optionalToken = tokenService.findByToken(tokenToDeactivation);
        if (optionalToken.isPresent()) {
            Token token = optionalToken.get();
            token.setActive(false);
            tokenService.addToken(token);
            response.setHeader("Status", "LOGOUT");
        } else {
            response.setHeader("Status", "DENIED");
        }
    }

    @GetMapping("/anonymous/passwordRecovery")
    public RedirectView passwordRecovery(@RequestParam String email, HttpServletResponse response) {

        //todo set correct url...
        if (userService.sendVerificationToken(email)) {
            return new RedirectView("/success");
        } else {
            return new RedirectView("/error");
        }
    }

    @GetMapping("/anonymous/confirmPasswordRecovery")
    public RedirectView confirmPasswordRecovery(@RequestParam String token, @RequestParam String email) {

        //todo set correct url...
        if (userService.recoverPassword(token, email)) {
            return new RedirectView("/success");
        } else {
            return new RedirectView("/error");
        }
    }

    @PostMapping("/passwordChange")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_PUBLISHER')")
    public void passwordChange(@RequestBody UserDto userDto, HttpServletResponse response) {

        final String userName = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        if (userName.equals(userDto.getLogin())) {
            if (userService.changePassword(userDto)) {
                response.setHeader("TYPE", "Success");
            } else {
                response.setHeader("TYPE", "Declined");
            }
        } else {
            response.setHeader("ERROR", "Something went wrong...");
        }
    }


}
