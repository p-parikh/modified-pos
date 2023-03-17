package com.increff.pos.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.increff.pos.util.PosSecurityFilter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;
import java.time.ZonedDateTime;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger logger = Logger.getLogger(SecurityConfig.class);

    @Autowired
    private PosSecurityFilter posSecurityFilter;
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http//
                // Match only these URLs
                .authorizeRequests()//
                .antMatchers("/api/supervisor/**").hasAuthority("supervisor")//
                .antMatchers("/api/**").hasAnyAuthority("supervisor", "operator")//
                // Ignore CSRF
                .and().csrf().disable()
                .addFilterBefore(posSecurityFilter, BasicAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.cors();
        http
                .exceptionHandling()
                .authenticationEntryPoint((request, response, e) ->
                {
                    response.setContentType("application/json;charset=UTF-8");
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getOutputStream().write(new ObjectMapper().createObjectNode()
                            .put("timestamp", ZonedDateTime.now().toString())
                            .put("message", "Access denied")
                            .toString().getBytes());
//                    response.getWriter().write(new ObjectMapper().createObjectNode()
//                            .put("timestamp", ZonedDateTime.now().toString())
//                            .put("message", "Access denied")
//                            .toString());
                });
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security",
                "/swagger-ui.html", "/webjars/**");
    }

}
