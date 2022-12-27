package com.example.ratelimitbyheader;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import(RateLimitInterceptorConfig.class)
@EnableConfigurationProperties(RateLimitByHeaderProperties.class)
public class RateLimitByHeaderConfiguration {
	
	public RateLimitService rateLimitService(RateLimitByHeaderProperties properties) {
		return new RateLimitService(properties);
	}
	
	@Bean
	public RateLimitInterceptor rateLimitInterceptor(RateLimitByHeaderProperties properties) {
		return new RateLimitInterceptor(rateLimitService(properties), properties);
	}
}
