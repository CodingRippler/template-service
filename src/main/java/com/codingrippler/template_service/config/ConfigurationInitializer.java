package com.codingrippler.template_service.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.codingrippler.template_service.context.ConfigServiceContext;
import com.codingrippler.template_service.dto.ConfigServiceRequest;
import com.codingrippler.template_service.dto.ConfigServiceResponse;
import com.codingrippler.template_service.service.ConfigServiceClient;

import jakarta.annotation.PostConstruct;

@Configuration
public class ConfigurationInitializer {

    private static final Logger logger = LoggerFactory.getLogger(ConfigurationInitializer.class);

    private ConfigServiceClient configServiceClient;

    @Value("${CR_ENV}")
    private String environment;

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    public ConfigurationInitializer(ConfigServiceClient configServiceClient) {

        this.configServiceClient = configServiceClient;

    }

    @PostConstruct
    public void init() {
        logger.info("Initializing configuration cache");

        if ("test".equals(activeProfile)) {
            // Skip initialization for tests
            System.out.println("Skipping configuration initialization in test profile.");
            return;
        }

        if (environment == null || environment.isEmpty()) {
            logger.error("Environment variable CR_ENV is not set");
            throw new RuntimeException("Environment variable CR_ENV is not set");
        }

        ConfigServiceRequest request = ConfigServiceRequest.builder()
                .environmentName(environment)
                .serviceNames(List.of("email-service", "global"))
                .build();

        Map<String, String> envConfig = fetchConfig("/api/config/env-config", request);
        Map<String, String> envSecrets = fetchConfig("/api/config/env-secrets", request);

        // Merge envConfig and envSecrets
        Map<String, String> mergedConfig = new HashMap<>(envConfig);
        mergedConfig.putAll(envSecrets);

        ConfigurationCache.setConfig(mergedConfig);
    }

    private Map<String, String> fetchConfig(String endpoint, ConfigServiceRequest request) {
        ConfigServiceContext context = ConfigServiceContext.builder()
                .request(request)
                .endpoint(endpoint)
                .build();

        ConfigServiceResponse response = configServiceClient.fetchEnvConfig(context);
        if (response == null || !response.isSuccess()) {
            throw new RuntimeException("Failed to fetch configuration from " + endpoint);
        }

        return response.getConfig();
    }

}
