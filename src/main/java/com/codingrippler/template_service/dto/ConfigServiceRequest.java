package com.codingrippler.template_service.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ConfigServiceRequest {

    String environmentName;
    List<String> serviceNames;

}
