package com.increff.pos.dto;

import com.increff.pos.api.AbstractUnitTest;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.UserData;
import com.increff.pos.model.forms.UserForm;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class UserDtoTest extends AbstractUnitTest {
    @Autowired
    private UserDto userDto;

    @Test
    public void testAddUser() throws ApiException, IllegalAccessException {
        create();
        UserData userData = userDto.getAllUsers().get(0);
        assertEquals("test@gmail.com", userData.getEmail());
        assertEquals("operator", userData.getRole());
        assertEquals("12345678", userData.getPassword());
    }


    @Test(expected = ApiException.class)
    public void testUpdateUser() throws ApiException, IllegalAccessException {
        UserForm userForm = create();
        UserData userData = userDto.getAllUsers().get(0);
        userForm.setPassword("88888888");
        try {
            userDto.updateUser(userData.getId(), userForm);
        }
        catch (ApiException e){
            assertEquals("Unable to update user", e.getMessage());
            throw new ApiException("Unable to update user");
        }
    }

    @Test
    public void testGetByEmail() throws ApiException, IllegalAccessException {
        UserForm userForm = create();
        userDto.getByEmail(userForm.getEmail());
        assertEquals("12345678", userForm.getPassword());
    }

    @Test(expected = ApiException.class)
    public void testCreateTwice() throws ApiException, IllegalAccessException {
        create();
        try{
            create();
        }
        catch (ApiException e){
            assertEquals("User with provided email id already exists.", e.getMessage());
            throw new ApiException("User with provided email id already exists.");
        }
    }

    @Test(expected = ApiException.class)
    public void testGetByWrongEmail() throws ApiException, IllegalAccessException {
        create();
        try{
            userDto.getByEmail("1234@gmail.com");
        }
        catch (ApiException e){
            assertEquals("User with provided email id does not exists", e.getMessage());
            throw new ApiException("User with provided email id does not exists");
        }
    }


    private UserForm create() throws ApiException, IllegalAccessException {
        UserForm userForm = new UserForm();
        userForm.setEmail("test@gmail.com");
        userForm.setPassword("12345678");
        userDto.addUser(userForm);
        return userForm;
    }


}
