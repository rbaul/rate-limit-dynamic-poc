package com.example.ratelimitbyheader;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@Getter
@Setter
@ConfigurationProperties("rate-limit")
public class RateLimitByHeaderProperties {
	
	/**
	 * This master microservice
	 */
	private String header = "X-Rate-Limit-Key";
	
	private Integer capacity = 5;
	
	private Duration duration = Duration.ofMinutes(1);
	
}
