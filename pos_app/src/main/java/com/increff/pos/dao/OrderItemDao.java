package com.increff.pos.dao;

import com.increff.pos.exception.ApiException;
import com.increff.pos.pojo.OrderItemPojo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@Transactional
public class OrderItemDao extends AbstractDao{

    private static final String SELECT_ALL = "select oi from Orderitem oi";

    private static final String SELECT_BY_ID = "select oi from Orderitem oi where id=:id";

    private static final String SELECT_BY_ORDER_AND_PRODUCT_ID = "select oi from Orderitem oi where orderId=:orderId and productId=:productId";

    private static final String SELECT_BY_ORDER_ID = "select oi from Orderitem oi where orderId=:orderId";

    private static final String DELETE_BY_ID = "delete from Orderitem where id=:id";
    public List<OrderItemPojo> viewAll(){
        TypedQuery<OrderItemPojo> query = em().createQuery(SELECT_ALL, OrderItemPojo.class);
        return query.getResultList();
    }

    public OrderItemPojo viewById(Integer id){
        TypedQuery<OrderItemPojo> query = em().createQuery(SELECT_BY_ID, OrderItemPojo.class);
        query.setParameter("id", id);
        return getSingleRow(query);
    }

    public void insert(OrderItemPojo orderItemPojo){
        em().persist(orderItemPojo);
    }

    public void update(Integer id, OrderItemPojo orderItemPojo) throws ApiException{
        OrderItemPojo orderItemPojo_db = viewById(id);

        if(orderItemPojo_db == null){
            throw new ApiException("Order does not exists with given id");
        }
        orderItemPojo_db.setProductId(orderItemPojo.getProductId());
        orderItemPojo_db.setQuantity(orderItemPojo.getQuantity());
        orderItemPojo_db.setSellingPrice(orderItemPojo.getSellingPrice());
    }

    public OrderItemPojo selectWithOrderAndProductId(Integer orderId, Integer productId){
        TypedQuery<OrderItemPojo> query = em().createQuery(SELECT_BY_ORDER_AND_PRODUCT_ID, OrderItemPojo.class);
        query.setParameter("orderId", orderId);
        query.setParameter("productId", productId);
        return getSingleRow(query);
    }

    public List<OrderItemPojo> selectWithOrderId(Integer orderId){
        TypedQuery<OrderItemPojo> query = em().createQuery(SELECT_BY_ORDER_ID, OrderItemPojo.class);
        query.setParameter("orderId", orderId);
        return query.getResultList();
    }

    public void delete(Integer id){
        OrderItemPojo orderItemPojo = viewById(id);
        em().remove(orderItemPojo);
    }
}
