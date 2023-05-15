package com.github.rbaul.rate_limit;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class RateLimitService implements ApplicationListener<ContextRefreshedEvent> {
	
	public static final String SPACE = " ";
	private final Map<String, Bucket> bucketsCache = new ConcurrentHashMap<>();
	
	private final Map<RequestMappingInfo, String> requestMappingInfoToBucketName = new HashMap<>();
	
	private final RateLimitProperties properties;
	
	private final RequestMappingHandlerMapping requestMappingHandlerMapping;
	
	public RateLimitService(RateLimitProperties properties, RequestMappingHandlerMapping requestMappingHandlerMapping) {
		this.properties = properties;
		this.requestMappingHandlerMapping = requestMappingHandlerMapping;
		
		properties.getApi().forEach(rateLimitApiProperties -> {
			RateLimitProperties.RateLimitConfigProperties config = rateLimitApiProperties.getConfig();
			bucketsCache.put(rateLimitApiProperties.getKey(), Bucket.builder()
					.addLimit(Bandwidth.simple(config.getCapacity(), config.getDuration()))
					.build());
		});
	}
	
	public Optional<Bucket> resolveBucket(String bucketName) {
		return Optional.ofNullable(StringUtils.hasText(bucketName) ? bucketsCache.get(bucketName) : null);
	}
	
	/**
	 * Convert GET /api/v1/random-data/numeric -> RequestMappingInfo
	 */
	public Optional<RequestMappingInfo> convertStringToRequestMappingInfo(String string) {
		String[] splitUrl = string.split(SPACE);
		try {
			String url = splitUrl[1];
			RequestMethod requestMethod = RequestMethod.valueOf(splitUrl[0]);
			return Optional.of(RequestMappingInfo.paths(url).methods(requestMethod).build());
		} catch (Exception e) {
			log.error("Failed convert '{}'", string);
			return Optional.empty();
		}
	}
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		Map<RequestMappingInfo, HandlerMethod> handlerMethods = this.requestMappingHandlerMapping.getHandlerMethods();
		
		// Properties support
		properties.getApi().forEach(rateLimitApiProperties -> rateLimitApiProperties.getPaths()
				.forEach(path -> convertStringToRequestMappingInfo(path)
						.ifPresent(convertedRequestMappingInfo -> {
							RequestMappingInfo requestMappingInfo = null;
							for (RequestMappingInfo rmi : handlerMethods.keySet()) {
								boolean methodTypeExist = convertedRequestMappingInfo.getMethodsCondition().equals(rmi.getMethodsCondition());
								boolean urlExist = convertedRequestMappingInfo.getPatternValues().equals(rmi.getPatternValues());
								if (methodTypeExist && urlExist) {
									requestMappingInfo = rmi;
									break;
								}
								
							}
							if (requestMappingInfo == null) {
								log.warn("Not found request mapping for '{}'", path);
							} else {
								requestMappingInfoToBucketName.put(requestMappingInfo, rateLimitApiProperties.getKey());
							}
						})));
		
		// Annotation support
		handlerMethods.forEach((requestMappingInfo, handlerMethod) -> {
			RateLimit annotation = AnnotationUtils.getAnnotation(handlerMethod.getMethod(), RateLimit.class);
			if (annotation != null) {
				final String bucketName = annotation.value();
				resolveBucket(bucketName).ifPresentOrElse(bucket -> requestMappingInfoToBucketName.put(requestMappingInfo, bucketName),
						() -> log.warn("Not found request mapping for '{}'", bucketName));
			}
		});
	}
	
	/**
	 * Resolve request by bucket
	 */
	public Optional<Bucket> resolveBucket(HttpServletRequest request) {
		RequestMappingInfo matchingCondition = null;
		for (RequestMappingInfo requestMappingInfo : this.requestMappingInfoToBucketName.keySet()) {
			matchingCondition = requestMappingInfo.getMatchingCondition(request);
			if (matchingCondition != null) {
				String bucketName = this.requestMappingInfoToBucketName.get(requestMappingInfo);
				return resolveBucket(bucketName);
			}
		}
		return Optional.empty();
	}
	
}