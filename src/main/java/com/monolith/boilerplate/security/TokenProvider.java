package com.monolith.boilerplate.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.monolith.boilerplate.config.AppProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

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
            logger.error("Claims couldn't be converted to JSON.");
        }
        return token;
    }

    public Long getUserId(String token) {
        DecodedJWT jwt = null;
        try {
            jwt = JWT.decode(token);
        } catch (JWTDecodeException exception){
            logger.error("Invalid token.");
        }
        return Long.parseLong(jwt.getSubject());
    }

    public boolean verify(String token) {
        try {
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException exception){
            logger.error("Invalid signature or claims.");
        }
        return false;
    }

}
