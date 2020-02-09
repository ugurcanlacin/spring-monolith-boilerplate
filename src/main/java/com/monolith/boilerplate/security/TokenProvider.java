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

import java.util.Date;

@Service
@Slf4j
public class TokenProvider {

    private AppProperties appProperties;
    private Algorithm algorithm;
    private JWTVerifier verifier;

    public TokenProvider(AppProperties appProperties) {
        this.appProperties = appProperties;
        this.algorithm = Algorithm.HMAC512(appProperties.getAuth().getTokenSecret());
        this.verifier = JWT.require(algorithm).build();
    }

    public String create(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMsec());
        String token = null;
        try {
            token = JWT.create().withSubject(Long.toString(userPrincipal.getId()))
                    .withIssuedAt(now)
                    .withExpiresAt(expiryDate)
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            log.error("Claims couldn't be converted to JSON.");
        }
        return token;
    }

    public Long getUserId(String token) {
        DecodedJWT jwt = null;
        try {
            jwt = JWT.decode(token);
        } catch (JWTDecodeException exception){
            log.error("Invalid token.");
        }
        return Long.parseLong(jwt.getSubject());
    }

    public boolean verify(String token) {
        try {
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException exception){
            log.error("Invalid signature or claims.");
        }
        return false;
    }

}
