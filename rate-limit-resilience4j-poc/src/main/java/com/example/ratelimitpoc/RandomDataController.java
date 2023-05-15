package com.example.ratelimitpoc;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;

@RestController
@RequestMapping("/api/v1/random-data")
@RequiredArgsConstructor
@Slf4j
public class RandomDataController {
	
	@GetMapping("/alphabetic")
	public String getRandomAlphabetic() {
		return RandomStringUtils.randomAlphabetic(10);
	}
	
	@GetMapping("/numeric")
	public String getRandomNumeric() {
		return RandomStringUtils.randomNumeric(10);
	}
	
	@RateLimiter(name = "backendA")
	@GetMapping("/alphanumeric")
	public String getRandomAlphanumeric() {
		return RandomStringUtils.randomAlphanumeric(10);
	}
	
	@GetMapping("/echo/{word}")
	public String echo(@PathVariable String word) {
		return word;
	}
	
	@PostMapping("/echo")
	public String create(@RequestBody String body) {
		return body;
	}
	
	@PutMapping("/echo/{word}")
	public String update(@PathVariable String word, @RequestBody String body) {
		return MessageFormat.format("{0} -> {1}", word, body);
	}
	
	@DeleteMapping("/echo/{word}")
	public String delete(@PathVariable String word) {
		return MessageFormat.format("Deleted: `{0}`", word);
	}
}
