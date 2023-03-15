package com.increff.pos.model.forms;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
public class OrderItemForm {

    @NotBlank
    private String barcode;

    @NotNull
    private Integer quantity;

    @NotNull
    private Double sellingPrice;
}
