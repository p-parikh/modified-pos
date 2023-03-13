package com.increff.pos.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "Orderitem")
@Table(uniqueConstraints = {@UniqueConstraint(name="orderId_productId_uk", columnNames = {"orderId", "productId"})})
public class OrderItemPojo extends AbstractPojo{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private Integer orderId;

    @NotNull
    private Integer productId;

    @NotNull
    private Integer quantity;

    @NotNull
    private Double sellingPrice;

}
