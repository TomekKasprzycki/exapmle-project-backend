package com.exampleproject.api.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
@Data
public class Author {

    @Id
    private Long id;
    private String firstName;
    private String secondName;
    private String lastName;
    @ManyToMany(mappedBy = "authors")
    private List<Book> books;


}
