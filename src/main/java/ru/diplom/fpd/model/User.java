package ru.diplom.fpd.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import jakarta.persistence.*;


@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "ID", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE")
    @SequenceGenerator(name = "SEQUENCE", sequenceName = "user_id_sequence", allocationSize = 1)
    private long id;

    private String userName;

    @Column
    private String password;

    private String role;

    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

    @Column(name = "LAST_NAME", nullable = false)
    private String lastName;

    @Column(name = "PHONE", nullable = false, unique = true)
    private String phone;

    @Column(name = "EMAIL", unique = true)
    private String email;

    @Column
    @JsonIgnore
    private boolean isDeleted;

}
