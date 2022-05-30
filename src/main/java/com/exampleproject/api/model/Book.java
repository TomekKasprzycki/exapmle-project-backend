package com.exampleproject.api.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Book {

    @Id
    private Long id;
    private String title;
    @ManyToMany
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private List<Author> authors;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private User owner;
    private boolean bookLended;
    @OneToMany(mappedBy = "book")
    private List<LendingRegister> lendingRegister;
    @OneToMany(mappedBy = "book")
    private List<BookScore> scores;




}
