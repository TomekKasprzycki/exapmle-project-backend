package com.exampleproject.api.controllers;

import com.exampleproject.api.converters.UserDtoConverter;
import com.exampleproject.api.dto.UserDto;
import com.exampleproject.api.model.Role;
import com.exampleproject.api.model.User;
import com.exampleproject.api.services.RoleService;
import com.exampleproject.api.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;
    private final UserDtoConverter userDtoConverter;
    private final RoleService roleService;

    public UserController(UserService userService, UserDtoConverter userDtoConverter, RoleService roleService) {

        this.userService = userService;
        this.userDtoConverter = userDtoConverter;
        this.roleService = roleService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/getAll")
    public List<UserDto> getAllUsers(@RequestParam int limit, @RequestParam int offset, HttpServletResponse response) {
        Optional<List<User>> optionalUsers = userService.getAllLimited(limit, offset);

        if (optionalUsers.isPresent()) {
            return userDtoConverter.convertToDto(optionalUsers.get());
        } else {
            response.setStatus(404);
            response.setHeader("ERROR", "No users found!");
            return new ArrayList<>();
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/edit")
    public UserDto editUser(@RequestParam UserDto userDto, HttpServletResponse response) {

        final String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        UserDto newUserDto = new UserDto();

        if (email.equals(userDto.getLogin())) {

            Optional<User> optionalUser = userService.findUserById(userDto.getId());

            if(optionalUser.isPresent()) {
                User user = optionalUser.get();

                user.setName(userDto.getName());

                User editedUser = userService.save(user);
                newUserDto = userDtoConverter.convertToDto(editedUser);
                response.setHeader("EDIT", "Success");
            }

        } else {
            response.setStatus(405);
            response.setHeader("ERROR", "Something went wrong...");
        }

        return newUserDto;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/editByAdmin")
    public UserDto editUserByAdmin(@RequestParam UserDto userDto, HttpServletResponse response) {

        UserDto newUserDto = new UserDto();

        Optional<User> optionalUser = userService.findUserById(userDto.getId());

        if (optionalUser.isEmpty()) {
            response.setStatus(405);
            response.setHeader("ERROR", "Something went wrong...");
            return newUserDto;
        }

        Optional<Role> optionalRole = roleService.getRoleByName(userDto.getRole());
        Role role;
        User user = optionalUser.get();
        user.setActive(userDto.isActive());
        if(optionalRole.isPresent()){
            role = optionalRole.get();
            user.setRole(role);
        }
        user.setName(userDto.getName());

        User newUser = userService.save(user);

        return userDtoConverter.convertToDto(newUser);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/deactivate")
    public void deactivate(@RequestBody UserDto userDto, HttpServletResponse response) {

        Optional<User> optional = userService.findUserByEmail(userDto.getLogin());
        if (optional.isPresent()) {
            userService.deactivate(optional.get());
        } else {
            response.setHeader("ERROR", "USER NOT FOUND");
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/activate")
    public void setActive(@RequestBody UserDto userDto, HttpServletResponse response) {


        Optional<User> optional = userService.findUserByEmail(userDto.getLogin());
        if (optional.isPresent()) {
            userService.activate(optional.get());
        } else {
            response.setHeader("ERROR", "USER NOT FOUND");
        }
    }

    @PostMapping("/anonymous/checkName")
    public boolean checkName(@RequestParam String name) {
        return userService.isNameTaken(name);
    }

}
