package ru.diplom.FirePandaDelivery.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Cities {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    @ApiModelProperty
    private String citi;

//    @ElementCollection
//    @CollectionTable()
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Column(nullable = false, unique = true)
    @JoinColumn(name = "cityId")
    @ApiModelProperty
    private List<CitiesCoordinates> Cords;
}
