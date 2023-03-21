package com.increff.pos.model.forms;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
public class OrderItemForm {

    @NotBlank
    private String barcode;

    @NotNull
    @Min(value = 1)
    private Integer quantity;

    @NotNull
    @Min(value = 0)
    private Double sellingPrice;
}
