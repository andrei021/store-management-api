package io.github.andrei021.store.config;

import io.github.andrei021.store.common.property.LoggingProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(LoggingProperties.class)
public class LoggingConfig {

}
