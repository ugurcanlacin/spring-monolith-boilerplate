package com.monolith.boilerplate.payload;

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
