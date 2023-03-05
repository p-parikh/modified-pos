package com.increff.pos.dto.helper;

import com.increff.pos.api.InventoryApi;
import com.increff.pos.api.ProductApi;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.forms.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

public class InventoryDtoHelper {
    @Autowired
    private static InventoryApi inventoryApi;

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

    public static boolean validateInput(InventoryPojo inventoryPojo) throws ApiException{
        List<Integer> allProductId = inventoryApi.getAllId();
        if(allProductId.contains(inventoryPojo.getProductId())){
            throw new ApiException("Inventory for given product already exists");
        }
        return true;
    }

}
