package com.github.rbaul.rate_limit;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Import(RateLimitInterceptorConfig.class)
@EnableConfigurationProperties(RateLimitProperties.class)
public class RateLimitConfiguration {
	
	@Bean
	public RateLimitService rateLimitService(RateLimitProperties properties, @Lazy RequestMappingHandlerMapping requestMappingHandlerMapping) {
		return new RateLimitService(properties, requestMappingHandlerMapping);
	}
	
	@Bean
	public RateLimitInterceptor rateLimitInterceptor(RateLimitService rateLimitService) {
		return new RateLimitInterceptor(rateLimitService);
	}
}
