package com.increff.pos.model.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InventoryReportData {
    private String brand;
    private String category;
    private String barcode;
    private Integer qty;
}
