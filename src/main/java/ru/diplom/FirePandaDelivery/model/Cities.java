package ru.diplom.FirePandaDelivery.model;

import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Locale;

@Entity
@Data
public class Cities {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @JsonValue
    @Column
    @ApiModelProperty
    private String citi;

    @Column
    @ApiModelProperty
    private String normalizedCiti;

    public void setCiti(String citi) {
        this.citi = citi;
        normalizedCiti = citi.toUpperCase(Locale.ROOT);
    }
//    @ElementCollection
//    @CollectionTable()

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Column(nullable = false, unique = true)
    @JoinColumn(name = "cityId")
    @ApiModelProperty
    private List<CitiesCoordinates> Cords;

}
