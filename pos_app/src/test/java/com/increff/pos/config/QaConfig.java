package com.increff.pos.config;

import com.increff.pos.spring.SpringConfig;
import org.springframework.context.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@ComponentScan(//
        basePackages = { "com.increff.pos" }, //
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = { SpringConfig.class })//
)
@PropertySources({ //
        @PropertySource(value = "classpath:./test.properties", ignoreResourceNotFound = true) //
})
public class QaConfig {

    @Bean(name = "mvcHandlerMappingIntrospector")
    public HandlerMappingIntrospector mvcHandlerMappingIntrospector(final WebApplicationContext servletAppContext) {
        return new HandlerMappingIntrospector(servletAppContext);
    }
}
