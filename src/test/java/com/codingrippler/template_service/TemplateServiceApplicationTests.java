package com.codingrippler.template_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest(properties = "spring.profiles.active=test")
@ActiveProfiles("test") // Activate the 'test' profile
class TemplateServiceApplicationTests {

	@DynamicPropertySource
	static void dynamicProperties(DynamicPropertyRegistry registry) {
		registry.add("CR_ENV", () -> "test");
	}

	@Test
	void contextLoads() {
	}

}
