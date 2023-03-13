package com.increff.invoice.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan("com.increff.invoice")
@EnableTransactionManagement
@PropertySources({ //
        @PropertySource(value = "file:invoice.properties", ignoreResourceNotFound = true) //
})
public class SpringConfig {
}
