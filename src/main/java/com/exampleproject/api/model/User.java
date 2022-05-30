package com.exampleproject.api.model;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(columnDefinition = "varchar(50)")
    private String name;
    @NotNull
    @Column(columnDefinition = "varchar(50)", unique = true)
    private String email;
    @NotNull
    private String password;
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;
    @NotNull
    private Date created;
    private Date passwordChange;
    private boolean active;
    @OneToOne(fetch = FetchType.EAGER)
    private Token token;
    @ManyToMany
    @JoinColumn(name = "mailingList_id", referencedColumnName = "id")
    private List<MailingList> mailingList;
    @OneToOne(fetch = FetchType.EAGER)
    private VerificationToken verificationToken;
    @OneToMany(mappedBy = "owner")
    private List<Book> books;
    @OneToMany(mappedBy = "user")
    private List<LendingRegister> lendingRegister;
    @OneToMany(mappedBy = "user")
    private List<BookScore> bookScores;


    public void setPassword(String unHashedPassword) {
        this.password = BCrypt.hashpw(unHashedPassword, BCrypt.gensalt());
    }

    public Boolean checkPassword(String unHashedPassword) {
        return BCrypt.checkpw(unHashedPassword, this.password);
    }

    @Override
    public String toString() {
        return email;
    }
}
