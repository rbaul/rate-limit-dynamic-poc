package com.github.rbaul.rate_limit;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
@Getter
@Setter
@ConfigurationProperties("rate-limit")
public class RateLimitProperties {
	
	private List<RateLimitApiProperties> api = List.of();
	
	@Getter
	@Setter
	public static class RateLimitApiProperties {
		
		private String key;
		
		private List<String> paths = List.of();
		
		private RateLimitConfigProperties config = new RateLimitConfigProperties();
	}
	
	@Getter
	@Setter
	public static class RateLimitConfigProperties {
		private Integer capacity = 5;
		
		private Duration duration = Duration.ofMinutes(1);
	}
	
}
