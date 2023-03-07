package com.increff.pos.dao;

import com.increff.pos.exception.ApiException;
import com.increff.pos.pojo.ProductPojo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@Transactional
public class ProductDao extends AbstractDao {

    private static final String SELECT_BY_ID = "select p from Product p where id=:id";

    private static final String SELECT_ALL = "select p from Product p";

    private static final String SELECT_BY_BARCODE = "select p from Product p where barcode=:barcode";



    public List<ProductPojo> viewAll() {
        TypedQuery<ProductPojo> query = em().createQuery(SELECT_ALL, ProductPojo.class);
        return query.getResultList();
    }

    public ProductPojo viewById(Integer id) {
        TypedQuery<ProductPojo> query = em().createQuery(SELECT_BY_ID, ProductPojo.class);
        query.setParameter("id", id);
        return getSingleRow(query);
    }

    public void insert(ProductPojo productPojo) {
        em().persist(productPojo);
    }

    public void update(Integer id, ProductPojo productPojo) throws ApiException {
        ProductPojo productPojo_db = viewById(id);

        if (productPojo_db == null) {
            throw new ApiException("Product does not exists with given id");
        }
        productPojo_db.setBarcode(productPojo.getBarcode());
        productPojo_db.setMrp(productPojo.getMrp());
        productPojo_db.setName(productPojo.getName());
        productPojo_db.setBrandCategory(productPojo.getBrandCategory());
    }

    public ProductPojo selectWithBarCode(String barcode) {
        TypedQuery<ProductPojo> query = em().createQuery(SELECT_BY_BARCODE, ProductPojo.class);
        query.setParameter("barcode", barcode);
        return getSingleRow(query);
    }
}
