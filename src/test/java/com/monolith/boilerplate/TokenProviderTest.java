package com.monolith.boilerplate;

import com.monolith.boilerplate.security.TokenProvider;
import com.monolith.boilerplate.security.UserPrincipal;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BoilerplateApplication.class)
public class TokenProviderTest {

    @Autowired
    TokenProvider tokenProvider;

    @Test
    public void test_generate(){
        UserPrincipal userPrincipal = UserPrincipal.builder()
                .id("id")
                .email("mail@test.com")
                .password("123")
                .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))).build();
        String token = tokenProvider.create(userPrincipal);
        Assert.assertNotNull(token);
        Assert.assertNotEquals(token, "");
    }

    @Test
    public void test_validate(){
        UserPrincipal userPrincipal = UserPrincipal.builder()
                .id("id")
                .email("mail@test.com")
                .password("123")
                .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))).build();
        String token = tokenProvider.create(userPrincipal);
        tokenProvider.verify(token);
    }

    @Test
    public void test_getUserId(){
        UserPrincipal userPrincipal = UserPrincipal.builder()
                .id("id")
                .email("mail@test.com")
                .password("123")
                .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))).build();
        String token = tokenProvider.create(userPrincipal);
        String userId = tokenProvider.getUserId(token);
        Assert.assertEquals("id", userId);
    }

}
