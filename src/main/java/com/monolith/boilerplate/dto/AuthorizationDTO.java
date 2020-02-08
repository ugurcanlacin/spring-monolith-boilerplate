package com.monolith.boilerplate.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizationDTO {
    private String accessToken;
    private String tokenType = "Bearer";
}
