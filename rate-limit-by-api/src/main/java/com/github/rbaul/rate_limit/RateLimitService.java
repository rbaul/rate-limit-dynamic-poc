package com.github.rbaul.rate_limit;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class RateLimitService {
	
	private final Map<String, Bucket> cache = new ConcurrentHashMap<>();
	
	private final Map<String, String> apiKeyToUrl = new HashMap<>();
	
	private final RateLimitProperties properties;
	
	public RateLimitService(RateLimitProperties properties) {
		this.properties = properties;
		
		properties.getApi().forEach(rateLimitApiProperties -> {
			RateLimitProperties.RateLimitConfigProperties config = rateLimitApiProperties.getConfig();
			rateLimitApiProperties.getPaths().forEach(path -> apiKeyToUrl.put(path, rateLimitApiProperties.getKey()));
			cache.put(rateLimitApiProperties.getKey(), Bucket.builder()
					.addLimit(Bandwidth.simple(config.getCapacity(), config.getDuration()))
					.build());
		});
	}
	
	public Bucket resolveBucket(String apiKey) {
		return cache.get(apiKey);
//		return cache.computeIfAbsent(apiKey, this::newBucket);
	}
	
	public Optional<Bucket> resolveBucketByUrl(String url) {
		String key = apiKeyToUrl.get(url);
		return Optional.ofNullable(StringUtils.hasText(key) ? cache.get(key) : null);
//		return cache.computeIfAbsent(apiKey, this::newBucket);
	}
	
//	private Bucket newBucket(String apiKey) {
//		return Bucket.builder()
//				.addLimit(Bandwidth.simple(properties.getCapacity(), properties.getDuration()))
//				.build();
//	}
}