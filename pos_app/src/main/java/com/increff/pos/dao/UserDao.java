package com.increff.pos.dao;

import com.increff.pos.exception.ApiException;
import com.increff.pos.pojo.UserPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDao extends AbstractDao{
    private static final String SELECT_BY_EMAIL = "select u from Userpojo u where email=:email";

    private static final String SELECT_ALL = "select u from Userpojo u";

    private static final String SELECT_BY_ID = "select u from Userpojo u where id=:id";

    public List<UserPojo> viewAll(){
        TypedQuery<UserPojo> query = em().createQuery(SELECT_ALL, UserPojo.class);
        return query.getResultList();
    }

    public UserPojo viewById(Integer id){
        TypedQuery<UserPojo> query = em().createQuery(SELECT_BY_ID, UserPojo.class);
        query.setParameter("id", id);
        return getSingleRow(query);
    }

    public void insert(UserPojo userPojo){
        em().persist(userPojo);
    }

    public UserPojo selectByEmail(String email) {
        TypedQuery<UserPojo> query = em().createQuery(SELECT_BY_EMAIL, UserPojo.class);
        query.setParameter("email", email);
        return getSingleRow(query);
    }

}
