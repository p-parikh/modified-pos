package com.increff.pos.model.forms;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class InventoryForm {
    @NotNull
    @Min(value = 0)
    private Integer qty;
    @NotBlank
    private String barcode;
}
