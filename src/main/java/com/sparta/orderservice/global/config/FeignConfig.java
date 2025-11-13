package com.sparta.orderservice.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.Logger;
import feign.codec.Decoder;
import feign.jackson.JacksonDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

	@Bean
	public Logger.Level feignLoggerLevel() {
		return Logger.Level.FULL;
	}

	@Bean
	public ObjectMapper feignObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule()); // LocalDateTime 매핑 지원
		return mapper;
	}

	@Bean
	public Decoder feignDecoder(ObjectMapper objectMapper) {
		return new JacksonDecoder(objectMapper); // Feign 응답 역직렬화용
	}
}

