package com.moyeorait.moyeoraitspring.commons;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(ignoreResourceNotFound = true, value = "classpath:application-${spring.profiles.active}.yml",
    factory = YamlPropertySourceFactory.class)
public class DatabaseConfiguration {
}
