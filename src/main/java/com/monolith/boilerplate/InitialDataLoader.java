package com.monolith.boilerplate;

import com.monolith.boilerplate.config.LocaleConfiguration;
import com.monolith.boilerplate.model.*;
import com.monolith.boilerplate.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Profile("development")
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    @Autowired
    UserRepository userRepository;

    @Autowired
    VerificationTokenRepository verificationTokenRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    TextRepository textRepository;

    @Autowired
    TranslationRepository translationRepository;

    @Autowired
    LocaleRepository localeRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (alreadySetup)
            return;
        PermissionEntity p1 = PermissionEntity.builder().name("P1").build();
        PermissionEntity p2 = PermissionEntity.builder().name("P2").build();
        Set<PermissionEntity> privileges1 = new HashSet<>();
        privileges1.add(p1);
        Set<PermissionEntity> privileges2 = new HashSet<>();
        privileges2.add(p2);
        RoleEntity roleEntity1 = RoleEntity.builder().name("ROLE1").permissionEntities(privileges1).build();
        RoleEntity roleEntity2 = RoleEntity.builder().name("ROLE2").permissionEntities(privileges2).build();
        Set<RoleEntity> roleEntities = new HashSet<>();
        roleEntities.add(roleEntity1);
        roleEntities.add(roleEntity2);
        roleRepository.saveAll(roleEntities);

        UserEntity user = UserEntity.builder().email("test@email.com")
                .emailVerified(true)
                .name("name")
                .surname("surname")
                .password(passwordEncoder.encode("123"))
                .imageUrl("url")
                .provider(AuthProvider.app)
                .verificationTokenEntities(new HashSet<>())
                .build();
        List<RoleEntity> rolesList = roleRepository.findAll();
        user.setRoleEntities(new HashSet<>(rolesList));
        UserEntity userSaved = userRepository.save(user);

        VerificationTokenEntity token = VerificationTokenEntity.builder().verified(false).token("token").userEntity(user).expiresAt(LocalDateTime.now().plusDays(1)).build();
        verificationTokenRepository.save(token);

        LocaleEntity localeEntity1 = LocaleEntity.builder().country(LocaleConfiguration.SV_SE_LOCALE.getCountry()).language(LocaleConfiguration.SV_SE_LOCALE.getLanguage()).build();
        LocaleEntity localeEntity2 = LocaleEntity.builder().country(LocaleConfiguration.EN_EN_LOCALE.getCountry()).language(LocaleConfiguration.EN_EN_LOCALE.getLanguage()).build();
        LocaleEntity localeSv = localeRepository.save(localeEntity1);
        LocaleEntity localeEn = localeRepository.save(localeEntity2);


        TextEntity hello_text = TextEntity.builder().textKey("hello_text").build();
        TextEntity textEntity = textRepository.save(hello_text);

        TranslationEntity helloTranslation = TranslationEntity.builder().translation("Hello").locale(localeEn).textEntity(textEntity).build();
        TranslationEntity hejTranslation = TranslationEntity.builder().translation("Hej").locale(localeSv).textEntity(textEntity).build();
        Set<TranslationEntity> translationEntities = new HashSet<>();
        translationEntities.add(helloTranslation);
        translationEntities.add(hejTranslation);
        translationRepository.saveAll(translationEntities);


        alreadySetup = true;
    }
}