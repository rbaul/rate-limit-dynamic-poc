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
  api:
    - key: api-1
      paths:
        - /api/v1/random-data/numeric
      config:
        capacity: 5
        duration: 1m
    - key: api-2
      paths:
        - /api/v1/random-data/alphabetic
      config:
        capacity: 2
        duration: 30s
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
