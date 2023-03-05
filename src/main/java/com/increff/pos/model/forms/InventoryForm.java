package com.increff.pos.model.forms;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InventoryForm {
    private Long qty;
    private String barcode;
}
