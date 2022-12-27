package com.example.ratelimitbyheader;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class RateLimitService {
	
	private final Map<String, Bucket> cache = new ConcurrentHashMap<>();
	
	private final RateLimitByHeaderProperties properties;
	
	public RateLimitService(RateLimitByHeaderProperties properties) {
		this.properties = properties;
	}
	
	public Bucket resolveBucket(String apiKey) {
		return cache.computeIfAbsent(apiKey, this::newBucket);
	}
	
	private Bucket newBucket(String apiKey) {
		return Bucket.builder()
				.addLimit(Bandwidth.simple(properties.getCapacity(), properties.getDuration()))
				.build();
	}
}