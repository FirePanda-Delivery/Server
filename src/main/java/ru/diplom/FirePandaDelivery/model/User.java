package ru.diplom.FirePandaDelivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import jakarta.persistence.*;
import java.util.Locale;


@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "ID", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonIgnore
    private String userName;

    @JsonIgnore
    @Column
    private String password;

    @JsonIgnore
    private String role;

    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

    @Column(name = "LAST_NAME", nullable = false)
    private String lastName;

    @Column(name = "PHONE", nullable = false, unique = true)
    private String phone;

    @Column(name = "EMAIL", unique = true)
    private String email;

    @JsonIgnore
    @Column(unique = true)
    private String normalizedEmail;

    public void setEmail(String email) {

        if (email == null || email.isEmpty()) {
            return;
        }

        this.email = email;
        this.normalizedEmail = email.toUpperCase(Locale.ROOT);
    }

    @Column
    @JsonIgnore
    private boolean isDeleted;

}
