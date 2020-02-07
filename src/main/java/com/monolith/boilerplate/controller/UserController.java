package com.monolith.boilerplate.controller;

import com.monolith.boilerplate.exception.ResourceNotFoundException;
import com.monolith.boilerplate.model.User;
import com.monolith.boilerplate.repository.UserRepository;
import com.monolith.boilerplate.security.CurrentUser;
import com.monolith.boilerplate.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }
}
