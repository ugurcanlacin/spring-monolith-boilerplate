package com.monolith.boilerplate.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorDTO implements Serializable {
    private String referenceCode;
    private String message;
    private String requestMethod;
    private String requestUrl;
    private String userId;
}
