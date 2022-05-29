package com.exampleproject.api.converters;

import com.exampleproject.api.dto.UserDto;
import com.exampleproject.api.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDtoConverter {

    public UserDto convertToDto(User user) {

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setLogin(user.getEmail());
        userDto.setRole(user.getRole().getName());
        userDto.setActive(user.isActive());

        return userDto;
    }

    public User convertFromDto(UserDto userDto) {

        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getLogin());
        user.setActive(userDto.isActive());

        return user;
    }

    public List<UserDto> convertToDto(List<User> users) {
        return users.stream().map(this::convertToDto).collect(Collectors.toList());
    }

}
