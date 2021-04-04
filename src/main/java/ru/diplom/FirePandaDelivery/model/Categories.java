package ru.diplom.FirePandaDelivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table
public class Categories {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty
    private long id;

    @Column(nullable = false)
    @ApiModelProperty
    private String name;

    @ApiModelProperty
    @JoinColumn(name = "cat_id")
    @OneToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.DETACH
    })
    private List<Product> products;

    @Column
    @JsonIgnore
    private boolean isDeleted;
}
