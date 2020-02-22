package com.monolith.boilerplate.service;

import com.monolith.boilerplate.model.Role;
import com.monolith.boilerplate.repository.RoleRepository;
import com.monolith.boilerplate.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Transactional
    public void deleteRoleByName(String name){
        Role role = roleRepository.findByName(name);
        roleRepository.deleteAllRelationshipInRolesPermissionsByRoleId(role.getId());
        userRepository.deleteAllRelationshipInUsersRolesByRoleId(role.getId());
        roleRepository.deleteByName(name);
    }

}
