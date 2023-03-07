package com.increff.pos.dao;

import com.increff.pos.exception.ApiException;
import com.increff.pos.pojo.InventoryPojo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@Transactional
public class InventoryDao extends AbstractDao{

    private static final String SELECT_BY_ID = "select i from Inventory i where productId=:id";

    private static final String SELECT_ALL_ID = "select i.productId from Inventory i";

    private static final String SELECT_ALL = "select i from Inventory i";

    public List<InventoryPojo> viewAll(){
        TypedQuery<InventoryPojo> query = em().createQuery(SELECT_ALL, InventoryPojo.class);
        return query.getResultList();
    }

    public InventoryPojo viewById(Integer productId){
        TypedQuery<InventoryPojo> query = em().createQuery(SELECT_BY_ID, InventoryPojo.class);
        query.setParameter("id", productId);
        return getSingleRow(query);
    }

    public void insert(InventoryPojo inventoryPojo){
        em().persist(inventoryPojo);
    }

    public void update(Integer productId, InventoryPojo inventoryPojo) throws ApiException {
        InventoryPojo inventoryPojo_db = viewById(productId);

        if(inventoryPojo_db == null){
            throw new ApiException("Inventory for the given product does not exists");
        }

        inventoryPojo_db.setQty(inventoryPojo.getQty());
    }

    public List<Integer> getAllProductId(){
        TypedQuery<Integer> query = em().createQuery(SELECT_ALL_ID, Integer.class);
        return query.getResultList();
    }
}
