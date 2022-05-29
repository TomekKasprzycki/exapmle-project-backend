package com.exampleproject.api.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Data
public class Category {

    @Id
    private Long id;
    private String name;
    @OneToMany(mappedBy = "category")
    private List<Book> books;
}
