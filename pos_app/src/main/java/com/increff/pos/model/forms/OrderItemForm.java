package com.increff.pos.model.forms;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class OrderItemForm {

    private String barcode;

    private Integer quantity;

    private Double sellingPrice;
}
