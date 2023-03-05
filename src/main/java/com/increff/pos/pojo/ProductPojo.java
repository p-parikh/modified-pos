package com.increff.pos.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "Product")
@Table(indexes = {@Index(name = "barcode_index", columnList = "barcode")})
public class ProductPojo extends AbstractPojo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String barcode;

    @NotNull
    private String name;

    @NotNull
    private Integer brandCategory;

    @NotNull
    private Double mrp;
}
