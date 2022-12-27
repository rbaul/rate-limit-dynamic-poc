package com.example.ratelimitbyheader;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
@Slf4j
public class RateLimitInterceptor implements HandlerInterceptor {
	
	private final RateLimitService rateLimitService;
	
	private final RateLimitByHeaderProperties properties;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String apiKey = request.getHeader(properties.getHeader());
		if (apiKey == null || apiKey.isEmpty()) {
			return true; // No need to limit rate
		}
		
		Bucket tokenBucket = rateLimitService.resolveBucket(apiKey);
		ConsumptionProbe probe = tokenBucket.tryConsumeAndReturnRemaining(1);
		if (probe.isConsumed()) {
			response.addHeader("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()));
			return true;
		} else {
			long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;
			response.addHeader("X-Rate-Limit-Retry-After-Seconds", String.valueOf(waitForRefill));
			response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(),
					"You have exhausted your API Request Quota");
			return false;
		}
	}
}