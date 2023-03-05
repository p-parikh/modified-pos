package com.increff.pos.dao;


import com.increff.pos.dto.BrandDto;
import com.increff.pos.exception.ApiException;
import com.increff.pos.pojo.BrandPojo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@Transactional
public class BrandDao extends AbstractDao{

    private static final String SELECT_ALL= "select b from Brand b";
    private static final String SELECT_BY_ID = "select b from Brand b where id=:id";

    private static final String SELECT_ALL_ID = "select b.id from Brand b";
    private static final String SELECT_BY_BRAND_AND_CATEGORY = "select b from Brand b where b.brand=:brand and b.category=:category";

    public List<BrandPojo> viewAll(){
        TypedQuery<BrandPojo> query = em().createQuery(SELECT_ALL, BrandPojo.class);
        return query.getResultList();
    }

    public BrandPojo getById(Integer id){
        TypedQuery<BrandPojo> query = em().createQuery(SELECT_BY_ID, BrandPojo.class);
        query.setParameter("id", id);
        return getSingleRow(query);
    }

    public void insert(BrandPojo bp){
        em().persist(bp);
    }

    public void update(Integer id, BrandPojo bp) throws ApiException{
       BrandPojo db_bp = getById(id);
       if(db_bp == null){
           throw new ApiException("Brand doesn't exists with given Id");
       }
       db_bp.setBrand(bp.getBrand());
       db_bp.setCategory((bp.getCategory()));
    }

    public BrandPojo selectWithBrandAndCategory(String brand, String category) {
        TypedQuery<BrandPojo> query = em().createQuery(SELECT_BY_BRAND_AND_CATEGORY, BrandPojo.class);
        query.setParameter("brand", brand);
        query.setParameter("category", category);
        return getSingleRow(query);
    }

    public List<Integer> selectAllId(){
        TypedQuery<Integer> query = em().createQuery(SELECT_ALL_ID, Integer.class);
        return query.getResultList();
    }
}

