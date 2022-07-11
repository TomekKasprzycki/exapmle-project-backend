package com.exampleproject.api.controllers;

import com.exampleproject.api.model.User;
import com.exampleproject.api.services.FileService;
import com.exampleproject.api.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("api/files")
public class FileController {

    private final FileService fileService;
    private final UserService userService;

    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("/upload")
    public void uploadFile(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        Part filePart = request.getPart("file");

        boolean result = fileService.importAuthorsFromFile(filePart);

    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/download")
    public void downloadFile(HttpServletResponse response) throws IOException {

        final String login = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Optional<User> optional = userService.findUserByEmail(login);
        if(optional.isPresent()){
            User user = optional.get();
            String headerValue = "attachment; filename=Rejestr.xlsx";
            response.setHeader("Content-Disposition",headerValue);
            fileService.getRegister(response, user);

        } else {
            response.setStatus(404);
            response.setHeader("Info", "Brak rejestru w bazie!");

        }


    }

}
