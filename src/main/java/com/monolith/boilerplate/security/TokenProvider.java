package com.monolith.boilerplate.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.monolith.boilerplate.config.AppProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TokenProvider {

    private AppProperties appProperties;
    private Algorithm algorithm;

    private static final String ROLE_SEPARATOR = ";";
    private static final String CLAIM_KEY_ROLE = "role";
    private static final String CLAIM_KEY_SESSION_ID = "sid";
    private static final String CLAIM_KEY_DEVICE_ID = "did";

    private static final Long DEFAULT_JWT_LEEWAY = 60L;

    public TokenProvider(AppProperties appProperties) {
        this.appProperties = appProperties;
        this.algorithm = Algorithm.HMAC512(appProperties.getAuth().getTokenSecret());
    }

    public String create(UserPrincipal userPrincipal) {
        String roles = userPrincipal.getAuthorities().stream().map(authority -> authority.getAuthority()).collect(Collectors.joining(ROLE_SEPARATOR));
        Date now = new Date(); // TODO: is it better to use LocalDateTime?
        Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMsec());
        String token = null;
        try {
            token = JWT.create().withSubject(userPrincipal.getId())
                    .withIssuedAt(now)
                    .withExpiresAt(expiryDate)
                    .withIssuer(appProperties.getAuth().getIssuer())
                    .withClaim(CLAIM_KEY_ROLE, roles)
                    .withClaim(CLAIM_KEY_DEVICE_ID, "") // TODO Set device id
                    .withClaim(CLAIM_KEY_SESSION_ID, "") // TODO Set session id
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            log.error("Claims couldn't be converted to JSON.");
        }
        return token;
    }

    public String getUserId(String token) {
        DecodedJWT jwt = null;
        try {
            jwt = JWT.decode(token);
        } catch (JWTDecodeException exception){
            log.error("Invalid token.");
        }
        return jwt.getSubject();
    }

    public boolean verify(String token) {
        try {
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(appProperties.getAuth().getIssuer())
                    .acceptLeeway(DEFAULT_JWT_LEEWAY)
                    .build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException exception){
            log.error("Invalid signature or claims.");
        }
        return false;
    }

}
