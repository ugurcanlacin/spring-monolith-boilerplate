package com.monolith.boilerplate.security.oauth2;

import com.monolith.boilerplate.exception.OAuth2AuthenticationProcessingException;
import com.monolith.boilerplate.model.AuthProvider;
import com.monolith.boilerplate.model.PermissionEntity;
import com.monolith.boilerplate.model.RoleEntity;
import com.monolith.boilerplate.model.UserEntity;
import com.monolith.boilerplate.repository.RoleRepository;
import com.monolith.boilerplate.repository.UserRepository;
import com.monolith.boilerplate.security.UserPrincipal;
import com.monolith.boilerplate.security.oauth2.user.OAuth2UserInfo;
import com.monolith.boilerplate.security.oauth2.user.OAuth2UserInfoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashSet;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        if(StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        UserEntity user = userRepository.findByEmail(oAuth2UserInfo.getEmail());
        if(user != null && user.getEmailVerified() == true) {
            if(!user.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                        user.getProvider() + " account. Please use your " + user.getProvider() +
                        " account to login.");
            }
            user = updateExistingUser(user, oAuth2UserInfo);
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }

        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private UserEntity registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        PermissionEntity permission = PermissionEntity.builder().name("ROLE_USER").build();
        RoleEntity roleEntity = RoleEntity.builder()
                .name("DEFAULT_ROLE")
                .permissionEntities(new HashSet<>(Arrays.asList(permission)))
                .build();
        permission.setRoleEntities(new HashSet<>(Arrays.asList(roleEntity)));
        RoleEntity defaultRoleEntity = roleRepository.findByName("DEFAULT_ROLE");
        if(defaultRoleEntity == null){
            roleRepository.save(roleEntity);
        }
        defaultRoleEntity = roleRepository.findByName("DEFAULT_ROLE");

        UserEntity user = new UserEntity();
        user.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        user.setProviderId(oAuth2UserInfo.getId());
        user.setName(oAuth2UserInfo.getName());
        user.setEmail(oAuth2UserInfo.getEmail());
        user.setEmailVerified(true);
        user.setImageUrl(oAuth2UserInfo.getImageUrl());
        user.setRoleEntities(new HashSet<>(Arrays.asList(defaultRoleEntity)));
        return userRepository.save(user);
    }

    private UserEntity updateExistingUser(UserEntity existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setName(oAuth2UserInfo.getName());
        existingUser.setImageUrl(oAuth2UserInfo.getImageUrl());
        return userRepository.save(existingUser);
    }

}
