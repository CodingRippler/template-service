package com.codingrippler.template_service.service;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.codingrippler.template_service.context.ConfigServiceContext;
import com.codingrippler.template_service.dto.ConfigServiceResponse;

@Service
public class ConfigServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(ConfigServiceClient.class);

    private RestTemplate restTemplate;

    private static final String API_KEY_HEADER = "x-api-key";
    private static final String ENV_API_KEY = "API_KEY";
    private static final String CONFIG_SERVICE_URL = "CONFIG_SERVICE_URL";

    public ConfigServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ConfigServiceResponse fetchEnvConfig(ConfigServiceContext context) {

        logger.info("Fetching environment configuration");

        ConfigServiceResponse responseBody = null;

        try {

            String apiKey = System.getenv(ENV_API_KEY);
            String configServiceUrl = System.getenv(CONFIG_SERVICE_URL);

            HttpHeaders headers = new HttpHeaders();
            headers.set(API_KEY_HEADER, apiKey);

            URI uri = UriComponentsBuilder.fromUriString(configServiceUrl + context.getEndpoint())
                    .queryParam("environmentName", context.getRequest().getEnvironmentName())
                    .queryParam("serviceNames", String.join(",", context.getRequest().getServiceNames()))
                    .build()
                    .toUri();

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<ConfigServiceResponse> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    entity,
                    ConfigServiceResponse.class);

            responseBody = response.getBody();

        } catch (Exception e) {
            logger.error("Failed to fetch environment configuration", e);
        }

        logger.info("Environment configuration fetched successfully");
        return responseBody;
    }
}
