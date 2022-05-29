package com.exampleproject.api.dto;

import lombok.Data;

import java.util.Date;

@Data
public class LendingRegisterDto {

    private Long id;
    private UserDto user;
    private BookDto book;
    private Date dateOfLend;
    private Date dateOfReturn;

}
