package com.exampleproject.api.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String login;
    private String name;
    private String Password;
    private String Password2;
    private String role;
    private boolean active;
}
