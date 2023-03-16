package com.increff.pos.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@EnableWebMvc
@Configuration
@EnableTransactionManagement
@EnableAsync
@EnableScheduling
@ComponentScan("com.increff.pos")
@PropertySources({
        @PropertySource(value = "file:pos.properties", ignoreResourceNotFound = true)
})
public class SpringConfig {

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(100000);
        return multipartResolver;
    }
    @Bean(name = "mvcHandlerMappingIntrospector")
    public HandlerMappingIntrospector mvcHandlerMappingIntrospector( final WebApplicationContext servletAppContext) {
        return new HandlerMappingIntrospector(servletAppContext);
    }
}