package com.monolith.boilerplate.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "app")
@Data
public class AppProperties {

    private OAuth2 oAuth2;
    private Auth auth;

    @Data
    public static class Auth {
        private String tokenSecret;
        private long tokenExpirationMsec;
        private String issuer;
    }

    @Data
    public static class OAuth2 {
        private List<String> authorizedRedirectUris;
    }
}
