package com.increff.pos.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "Brand")
@Table(indexes = {@Index(name = "brand_category_index", columnList = "brand, category")},
                            uniqueConstraints = {@UniqueConstraint(name = "brand_category_uk", columnNames = {"brand","category"})})
public class BrandPojo extends AbstractPojo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private String brand;

    @NotNull
    private String category;
}
