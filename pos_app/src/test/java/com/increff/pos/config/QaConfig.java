package com.increff.pos.config;

import com.increff.pos.spring.SpringConfig;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(//
        basePackages = { "com.increff.pos" }, //
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = { SpringConfig.class })//
)
@PropertySources({ //
        @PropertySource(value = "classpath:./test.properties", ignoreResourceNotFound = true) //
})
public class QaConfig {
}
