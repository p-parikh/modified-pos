package com.increff.pos.dto;

import com.increff.pos.api.UserApi;
import com.increff.pos.dto.helper.UserDtoHelper;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.UserData;
import com.increff.pos.model.data.UserPrincipal;
import com.increff.pos.model.forms.UserForm;
import com.increff.pos.pojo.UserPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import com.increff.pos.util.ValidationUtil;

import java.util.ArrayList;
import java.util.List;
@Component
public class UserDto {
    private static final String SUPERVISOR = "supervisor";
    private static final String OPERATOR = "operator";
    @Autowired
    private UserApi userApi;

    @Value("$supervisor.email")
    private String supervisorEmail;

    public void addUser(UserForm userForm) throws ApiException, IllegalAccessException{
        ValidationUtil.validate(userForm);
        UserPojo userPojo = UserDtoHelper.convertToUserPojo(userForm);
        userPojo = UserDtoHelper.normalise(userPojo);
        if(userPojo.getEmail().equals(supervisorEmail)){
            userPojo.setRole(SUPERVISOR);
        }
        else{
            userPojo.setRole(OPERATOR);
        }
        userApi.addUser(userPojo);
    }

    public List<UserData> getAllUsers(){
        List<UserPojo> userPojoList = userApi.getAllEntries();
        List<UserData> userDataList = new ArrayList<>();
        for(UserPojo userPojo : userPojoList){
            userDataList.add(UserDtoHelper.convertToUserData(userPojo));
        }
        return userDataList;
    }

    public UserData getById(Integer id) throws ApiException{
        return UserDtoHelper.convertToUserData(userApi.getById(id));
    }

    public UserData getByEmail(String email) throws ApiException{
        return UserDtoHelper.convertToUserData(userApi.getByEmail(email));
    }

    public void updateUser(Integer id, UserForm userForm) throws ApiException{
        ValidationUtil.validate(userForm);
        UserPojo userPojo = UserDtoHelper.convertToUserPojo(userForm);
        userPojo = UserDtoHelper.normalise(userPojo);
        if(userPojo.getEmail().equals(supervisorEmail)){
            userPojo.setRole(SUPERVISOR);
        }
        else{
            userPojo.setRole(OPERATOR);
        }
        userApi.update(id, userPojo);
    }

    public Authentication convertUserPojoToAuthentication(UserData userData) {
        // Create principal
        UserPrincipal principal = new UserPrincipal();
        principal.setEmail(userData.getEmail());
        principal.setId(userData.getId());
        principal.setRole(userData.getRole());

        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(userData.getRole()));

        return new UsernamePasswordAuthenticationToken(principal, null,
                authorities);
    }
}
