package com.increff.pos.dto.helper;

import com.increff.pos.api.InventoryApi;
import com.increff.pos.api.ProductApi;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.forms.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

public class InventoryDtoHelper {

    public static InventoryPojo convertToInventoryPojo(InventoryForm inventoryForm, Integer productId){
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setProductId(productId);
        inventoryPojo.setQty(inventoryForm.getQty());
        return inventoryPojo;
    }

    public static InventoryData convertToInventoryData(InventoryPojo inventoryPojo, ProductPojo productPojo){
        InventoryData inventoryData = new InventoryData();
        inventoryData.setBarcode(productPojo.getBarcode());
        inventoryData.setQty(inventoryPojo.getQty());
        return inventoryData;
    }

}
