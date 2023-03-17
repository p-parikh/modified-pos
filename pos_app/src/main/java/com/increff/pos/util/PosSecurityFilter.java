package com.increff.pos.util;

import com.increff.pos.dto.UserDto;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.UserData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class PosSecurityFilter extends GenericFilterBean {

    @Autowired
    private UserDto userDto;
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse myResponse= (HttpServletResponse) response;
        String email = httpRequest.getHeader("username");
        String password = httpRequest.getHeader("password");
        if(StringUtils.isEmpty(email) || StringUtils.isEmpty(password)){
            myResponse.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED);
            chain.doFilter(request, response);
        }
        else {
            UserData userData = null;
            try {
                userData = userDto.getByEmail(email);
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
            boolean authenticated = (userData != null) && (userData.getPassword().equals(password));
            if(!authenticated){
                myResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
            else{
                Authentication authentication = userDto.convertUserPojoToAuthentication(userData);
                HttpSession session = httpRequest.getSession(true);
                SecurityUtil.createContext(session);
                SecurityUtil.setAuthentication(authentication);
            }
            chain.doFilter(request, response);
        }
    }
}
