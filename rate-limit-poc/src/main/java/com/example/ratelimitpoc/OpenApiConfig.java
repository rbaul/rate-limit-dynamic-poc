package com.example.ratelimitpoc;

import com.example.ratelimitbyheader.RateLimitByHeaderProperties;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

@Configuration
public class OpenApiConfig {
	
	@Bean
	public GroupedOpenApi publicApi(RateLimitByHeaderProperties properties) {
		
		return GroupedOpenApi.builder()
				.group("public")
//				.pathsToMatch("/public/**")
				.addOperationCustomizer(new OperationCustomizer() {
					@Override
					public Operation customize(Operation operation, HandlerMethod handlerMethod) {
						HeaderParameter parametersItem = new HeaderParameter();
						parametersItem.setName(properties.getHeader());
						parametersItem.setDescription("Rate limit key");
						operation.addParametersItem(parametersItem);
						return operation;
					}
				})
				.build();
	}
}
