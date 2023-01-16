package com.github.rbaul.rate_limit;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import(RateLimitInterceptorConfig.class)
@EnableConfigurationProperties(RateLimitProperties.class)
public class RateLimitConfiguration {
	
	public RateLimitService rateLimitService(RateLimitProperties properties) {
		return new RateLimitService(properties);
	}
	
	@Bean
	public RateLimitInterceptor rateLimitInterceptor(RateLimitProperties properties) {
		return new RateLimitInterceptor(rateLimitService(properties), properties);
	}
}
