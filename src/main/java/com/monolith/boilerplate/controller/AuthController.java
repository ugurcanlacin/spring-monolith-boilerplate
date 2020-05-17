package com.monolith.boilerplate.controller;

import com.monolith.boilerplate.dto.*;
import com.monolith.boilerplate.exception.BadRequestException;
import com.monolith.boilerplate.model.*;
import com.monolith.boilerplate.repository.RoleRepository;
import com.monolith.boilerplate.repository.UserRepository;
import com.monolith.boilerplate.repository.VerificationTokenRepository;
import com.monolith.boilerplate.security.TokenProvider;
import com.monolith.boilerplate.security.UserPrincipal;
import com.monolith.boilerplate.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private EmailService emailService;

    @Autowired
    VerificationTokenRepository verificationTokenRepository;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginDTO loginDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getEmail(),
                        loginDTO.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.create((UserPrincipal) authentication.getPrincipal());
        AuthorizationDTO authorizationDTO = new AuthorizationDTO().builder().accessToken(token).build();
        return ResponseEntity.ok(authorizationDTO);
    }

    @GetMapping("/verify/{token}")
    public ResponseEntity<?> verify(@PathVariable("token") String token) {
        VerificationTokenEntity byToken = verificationTokenRepository.findByToken(token);
        UserEntity user = byToken.getUserEntity();
        if(byToken.getVerified() == true){
            log.info("Token is already verified. Token: {}", token);
            return (ResponseEntity<?>) ResponseEntity.badRequest();
        }

        if(user.getEmailVerified() == true){
            log.info("Email is already verified. Email: {}", user.getEmail());
            return (ResponseEntity<?>) ResponseEntity.badRequest();
        }

        if(byToken != null){
            byToken.setVerified(true);
            user.setEmailVerified(true);
            verificationTokenRepository.save(byToken);
            userRepository.save(user);
            log.info("Email is verified. Email: {}", user.getEmail());
            return ResponseEntity.ok("Email is verified.");
        } else {
            return (ResponseEntity<?>) ResponseEntity.notFound();
        }
    }


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpDTO signUpDTO) {
        if(userRepository.existsByEmail(signUpDTO.getEmail())) {
            throw new BadRequestException("Email address already in use.");
        }

        PermissionEntity permission = PermissionEntity.builder().name("ROLE_USER").build();
        RoleEntity roleEntity = RoleEntity.builder()
                .name("DEFAULT_ROLE")
                .permissionEntities(new HashSet<>(Arrays.asList(permission)))
                .build();
        permission.setRoleEntities(new HashSet<>(Arrays.asList(roleEntity)));
        roleRepository.save(roleEntity);
        RoleEntity default_roleEntity = roleRepository.findByName("DEFAULT_ROLE");

        // Creating user's account
        UserEntity user = new UserEntity();
        user.setName(signUpDTO.getName());
        user.setEmail(signUpDTO.getEmail());
        user.setPassword(signUpDTO.getPassword());
        user.setProvider(AuthProvider.app);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoleEntities(new HashSet<>(Arrays.asList(default_roleEntity)));
        user.setEmailVerified(false);
        UserEntity savedUser = userRepository.save(user);

        VerificationTokenEntity token = VerificationTokenEntity.builder().verified(false).token(UUID.randomUUID().toString()).expiresAt(LocalDateTime.now().plusDays(1)).userEntity(user).build();
        savedUser.getVerificationTokenEntities().add(token);
        userRepository.save(savedUser);

        String text = String.format("Please click the link below to verify your email. \n \"http://localhost:8080/auth/verify/%s\"", token.getToken());
        EmailDTO email_verification = EmailDTO.builder().subject("Email Verification")
                .to(user.getEmail())
                .text(text)
                .receiverId(savedUser.getId())
                .build();
        emailService.sendEmailToQueue(email_verification);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user/me")
                .buildAndExpand(savedUser.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponseDTO(true, "User registered successfully@"));
    }

}
