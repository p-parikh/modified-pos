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
@Table(indexes = {@Index(name = "barcode_index", columnList = "barcode")},
        uniqueConstraints = {@UniqueConstraint(name = "barcode_uk", columnNames = {"barcode"})})
public class ProductPojo extends AbstractPojo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private String barcode;

    @NotNull
    private String name;

    @NotNull
    private Integer brandCategory;

    @NotNull
    private Double mrp;
}
