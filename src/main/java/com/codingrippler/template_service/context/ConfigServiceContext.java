package com.codingrippler.template_service.context;

import com.codingrippler.template_service.dto.ConfigServiceRequest;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ConfigServiceContext {

    ConfigServiceRequest request;
    String endpoint;

}
