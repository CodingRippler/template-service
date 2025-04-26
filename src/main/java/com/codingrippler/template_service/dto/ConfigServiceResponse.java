package com.codingrippler.template_service.dto;

import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConfigServiceResponse {

    private boolean success;
    private Map<String, String> config;

}
