package com.exampleproject.api.controllers;

import com.exampleproject.api.converters.LendingRegisterDtoConverter;
import com.exampleproject.api.dto.LendingRegisterDto;
import com.exampleproject.api.model.LendingRegister;
import com.exampleproject.api.model.User;
import com.exampleproject.api.services.LendingRegisterService;
import com.exampleproject.api.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lendingregister")
public class LendingRegisterController {

    private final LendingRegisterService lendingRegisterService;
    private final LendingRegisterDtoConverter lendingRegisterDtoConverter;
    private final UserService userService;

    public LendingRegisterController(LendingRegisterService lendingRegisterService,
                                     LendingRegisterDtoConverter lendingRegisterDtoConverter, UserService userService) {
        this.lendingRegisterService = lendingRegisterService;
        this.lendingRegisterDtoConverter = lendingRegisterDtoConverter;
        this.userService = userService;
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @GetMapping("/showregister")
    public List<LendingRegisterDto> showRegister(HttpServletResponse response) {

        final String login = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Optional<User> optionalUser = userService.findUserByEmail(login);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Optional<List<LendingRegister>> optionalLendingRegister = lendingRegisterService.getRegisterForUser(user);
            if(optionalLendingRegister.isPresent()){
                return lendingRegisterDtoConverter.lendingRegisterDtoList(optionalLendingRegister.get());
            } else {
                return new ArrayList<>();
            }
        }

        response.setStatus(404);
        response.setHeader("ERROR","Register not found...");
        return null;
    }



}
