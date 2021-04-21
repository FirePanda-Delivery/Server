package ru.diplom.FirePandaDelivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import ru.diplom.FirePandaDelivery.service.CitiesServices;

import javax.persistence.*;
import java.util.Locale;

@Entity
@Data
@Table
public class Courier {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ApiModelProperty
    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

    @ApiModelProperty
    @Column(name = "LAST_NAME", nullable = false)
    private String lastName;

    @ApiModelProperty
    @Column(name = "PHONE", nullable = false, unique = true)
    private String phone;

    @ApiModelProperty
    @Column(name = "EMAIL", unique = true)
    private String email;

    @JsonIgnore
    @ApiModelProperty
    @Column(unique = true)
    private String normalizedEmail;

    @Column
    @ApiModelProperty
    private float rating;


    @ManyToOne
    @JoinColumn(nullable = false)
    @ApiModelProperty
    private Cities city;

    @Column
    @JsonIgnore
    private boolean isDeleted;

    public void setEmail(String email) {
        this.email = email;
        this.normalizedEmail = email.toUpperCase(Locale.ROOT);
    }
}
