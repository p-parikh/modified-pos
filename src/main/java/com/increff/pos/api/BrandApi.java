package com.increff.pos.api;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.exception.ApiException;
import com.increff.pos.pojo.BrandPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BrandApi {

    @Autowired
    private BrandDao brandRepo;

    @Transactional(readOnly = true)
    public List<BrandPojo> getAllEntries() {
        return brandRepo.viewAll();
    }

    @Transactional(readOnly = true)
    public BrandPojo getById(Integer id) throws ApiException{
        BrandPojo db_bp = brandRepo.getById(id);
        if(db_bp == null){
            throw new ApiException("Brand doesn't exists with given Id");
        }
        return db_bp;
    }

    @Transactional(rollbackFor = ApiException.class)
    public void create(BrandPojo bp){
        brandRepo.insert(bp);
    }

    @Transactional(rollbackFor = ApiException.class)
    public void update(Integer id, BrandPojo new_bp) throws ApiException{
        brandRepo.update(id, new_bp);
    }

    @Transactional(readOnly = true)
    public BrandPojo selectWithBrandAndCategory(String brand, String category) {
        return brandRepo.selectWithBrandAndCategory(brand, category);
    }

    public List<Integer> selectAllIDs(){
        return brandRepo.selectAllId();
    }



}
