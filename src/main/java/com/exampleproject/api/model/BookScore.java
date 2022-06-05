package com.exampleproject.api.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class BookScore {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(columnDefinition = "varchar(2000)")
    private String opinion;
    private int score;
    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
