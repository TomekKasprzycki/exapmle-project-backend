package com.exampleproject.api.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class LendingRegister {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name="book_id", referencedColumnName = "id")
    private Book book;
    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private User user;
    private Date dateOfLend;
    private Date dateOfReturn;
}
