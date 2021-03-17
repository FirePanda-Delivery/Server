package ru.diplom.FirePandaDelivery.model;

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

    @Column
    @ApiModelProperty
    private String name;

    @Column
    @ApiModelProperty
    @JoinColumn(name = "cat_id")
    @OneToMany
    private List<Product> products;
}
