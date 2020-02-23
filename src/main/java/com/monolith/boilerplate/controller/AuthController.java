package com.monolith.boilerplate.controller;

import com.monolith.boilerplate.exception.BadRequestException;
import com.monolith.boilerplate.model.*;
import com.monolith.boilerplate.dto.ApiResponseDTO;
import com.monolith.boilerplate.dto.AuthorizationDTO;
import com.monolith.boilerplate.dto.LoginDTO;
import com.monolith.boilerplate.dto.SignUpDTO;
import com.monolith.boilerplate.repository.RoleRepository;
import com.monolith.boilerplate.repository.UserRepository;
import com.monolith.boilerplate.repository.VerificationTokenRepository;
import com.monolith.boilerplate.security.TokenProvider;
import com.monolith.boilerplate.security.UserPrincipal;
import com.monolith.boilerplate.service.UserService;
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
import java.util.Arrays;
import java.util.HashSet;

@RestController
@RequestMapping("/auth")
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

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpDTO signUpDTO) {
        if(userRepository.existsByEmail(signUpDTO.getEmail())) {
            throw new BadRequestException("Email address already in use.");
        }

        Permission permission = Permission.builder().name("ROLE_USER").build();
        Role role = Role.builder()
                .name("DEFAULT_ROLE")
                .permissions(new HashSet<>(Arrays.asList(permission)))
                .build();
        permission.setRoles(new HashSet<>(Arrays.asList(role)));
        roleRepository.save(role);
        Role default_role = roleRepository.findByName("DEFAULT_ROLE");

        // Creating user's account
        User user = new User();
        user.setName(signUpDTO.getName());
        user.setEmail(signUpDTO.getEmail());
        user.setPassword(signUpDTO.getPassword());
        user.setProvider(AuthProvider.app);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(new HashSet<>(Arrays.asList(default_role)));
        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user/me")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponseDTO(true, "User registered successfully@"));
    }

}
