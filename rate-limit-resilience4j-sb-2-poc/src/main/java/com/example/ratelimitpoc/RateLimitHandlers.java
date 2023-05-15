package com.example.ratelimitpoc;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RateLimitHandlers {
	
	@ResponseStatus(code = HttpStatus.TOO_MANY_REQUESTS)
	@ExceptionHandler(RequestNotPermitted.class)
	public void handleRequestNotPermitted(RequestNotPermitted ex) {
		log.error("Request Not Permitted: {}", ex.getMessage());
	}
	
}