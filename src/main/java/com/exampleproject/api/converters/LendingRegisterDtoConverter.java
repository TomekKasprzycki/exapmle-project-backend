package com.exampleproject.api.converters;

import com.exampleproject.api.dto.LendingRegisterDto;
import com.exampleproject.api.model.LendingRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LendingRegisterDtoConverter {

    private final UserDtoConverter userDtoConverter;
    private final BookDtoConverter bookDtoConverter;

    public LendingRegisterDtoConverter(UserDtoConverter userDtoConverter,
                                       BookDtoConverter bookDtoConverter){
        this.userDtoConverter = userDtoConverter;
        this.bookDtoConverter = bookDtoConverter;
    }

    public LendingRegisterDto convertToDto(LendingRegister lendingRegister) {
        LendingRegisterDto lendingRegisterDto = new LendingRegisterDto();
        lendingRegisterDto.setId(lendingRegister.getId());
        lendingRegisterDto.setBook(bookDtoConverter.convertToDto(lendingRegister.getBook()));
        lendingRegisterDto.setUser(userDtoConverter.convertToDto(lendingRegister.getUser()));
        lendingRegisterDto.setDateOfLend(lendingRegister.getDateOfLend());
        lendingRegisterDto.setDateOfReturn(lendingRegister.getDateOfReturn());
        return lendingRegisterDto;
    }

    public LendingRegister convertFromDto(LendingRegisterDto lendingRegisterDto) {
        LendingRegister lendingRegister = new LendingRegister();
        lendingRegister.setId(lendingRegisterDto.getId());
        lendingRegister.setBook(bookDtoConverter.convertFromDto(lendingRegisterDto.getBook()));
        lendingRegister.setUser(userDtoConverter.convertFromDto(lendingRegisterDto.getUser()));
        lendingRegister.setDateOfLend(lendingRegisterDto.getDateOfLend());
        lendingRegister.setDateOfReturn(lendingRegisterDto.getDateOfReturn());
        return lendingRegister;
    }

    public List<LendingRegisterDto> lendingRegisterDtoList(List<LendingRegister> lendingRegisterList) {
        return lendingRegisterList.stream().map(this::convertToDto).collect(Collectors.toList());
    }

}
