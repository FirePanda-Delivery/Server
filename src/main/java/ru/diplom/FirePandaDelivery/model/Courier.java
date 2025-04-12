package ru.diplom.FirePandaDelivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import ru.diplom.FirePandaDelivery.service.CitiesServices;

import jakarta.persistence.*;
import java.util.Locale;

@Entity
@Data
@Table
public class Courier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

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

    @Column
    private float rating;


    @ManyToOne
    @JoinColumn(nullable = false)
    private Cities city;

    @Column
    @JsonIgnore
    private boolean isDeleted;

    public void setEmail(String email) {
        this.email = email;
        this.normalizedEmail = email.toUpperCase(Locale.ROOT);
    }
}
