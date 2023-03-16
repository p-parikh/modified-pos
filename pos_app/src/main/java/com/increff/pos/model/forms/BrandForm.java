package com.increff.pos.model.forms;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class BrandForm {

    @NotNull
    @NotBlank
    private String brand;
    @NotNull
    @NotBlank
    private String category;
}
