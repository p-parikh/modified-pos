package com.increff.pos.model.forms;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class ProductForm {
    @NotBlank
    private String barcode;
    @NotNull
    private Double mrp;
    @NotBlank
    private String name;
    @NotBlank
    private String brand;
    @NotBlank
    private String Category;
}
