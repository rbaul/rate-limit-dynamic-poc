package com.example.ratelimitpoc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	
	@GetMapping("/alphanumeric")
	public String getRandomAlphanumeric() {
		return RandomStringUtils.randomAlphanumeric(10);
	}
}
