package com.increff.pos.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "Inventory")
public class InventoryPojo extends AbstractPojo {

    @Id
    @Column(nullable = false, unique = true)
    private Integer productId;

    @NotNull
    private Long qty;
}
