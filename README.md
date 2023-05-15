# Rate limit Dynamic - POC
* Main idea limit for internal requests
> Dynamic limit request by header

> No header no limit

### Build on
> Java 17.0.x

> Spring Boot 3.0.x

> Gradle 7.6 

## Rate limit by Header

### Properties
See default in `RateLimitByHeaderProperties.java`

#### Change (default presented)
``` yaml
rate-limit:
  header: X-Rate-Limit-Key
  capacity: 5
  duration: 1m
```

## Rate limit by API
```yaml
rate-limit:
  #  header: X-Rate-Limit-Key
  #  capacity: 10
  #  duration: 1m
  api:
    - key: limit-1
      paths:
        - GET /api/v1/random-data/numeric
        - GET /api/v1/random-data/echo/{word}
        - POST /api/v1/random-data/echo
        - PUT /api/v1/random-data/echo/{word}
      config:
        capacity: 5
        duration: 1m
    - key: limit-2
      paths:
        - GET /api/v1/random-data/alphabetic
      config:
        capacity: 2
        duration: 30s
    - key: limit-3
      config:
        capacity: 2
        duration: 10s
```

### Annotation support `@RateLimit`
``` java
	@RateLimit("limit-3")
	@GetMapping("/alphanumeric")
	public String getRandomAlphanumeric() {
		return RandomStringUtils.randomAlphanumeric(10);
	}
```

### Additional response headers
In order to enhance the client experience of the API, we'll use the following additional response headers to send information about the rate limit:

* X-Rate-Limit-Remaining: number of tokens remaining in the current time window 
* X-Rate-Limit-Retry-After-Seconds: remaining time, in seconds, until the bucket is refilled

### How to run?
Run `RateLimitPocApplication.java`

Follow Open Api link `http://localhost:8080/swagger-ui.html`

Execute any request with specific key (default 5 times)
#### Success
![](/demo/rate_limit_open_api.PNG)

#### Failed
![](/demo/rate_limit_open_api_failed.PNG)

# Reference
[baeldung](https://www.baeldung.com/spring-bucket4j)
