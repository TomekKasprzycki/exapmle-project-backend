package com.exampleproject.api.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String secondName;
    private String lastName;
    @ManyToMany(mappedBy = "authors")
    private List<Book> books;


}
