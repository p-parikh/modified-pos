package com.increff.pos.api;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.exception.ApiException;
import com.increff.pos.pojo.InventoryPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryApi {

    @Autowired
    private InventoryDao inventoryDao;

    public List<InventoryPojo> getAllEntries(){
        return inventoryDao.viewAll();
    }

    public InventoryPojo getById(Integer productId) throws ApiException{
        InventoryPojo inventoryPojo_db = inventoryDao.viewById(productId);

        if(inventoryPojo_db == null){
            throw new ApiException("product with given id does not exists");
        }

        return inventoryPojo_db;
    }

    public void create(InventoryPojo inventoryPojo){
        inventoryDao.insert(inventoryPojo);
    }

    public void update(Integer productId, InventoryPojo inventoryPojo) throws ApiException{
        inventoryDao.update(productId, inventoryPojo);
    }

    public List<Integer> getAllId(){
        return  inventoryDao.getAllProductId();
    }
}
