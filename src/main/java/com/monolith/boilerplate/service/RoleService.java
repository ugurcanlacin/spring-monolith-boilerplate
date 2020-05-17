package com.monolith.boilerplate.service;

import com.monolith.boilerplate.model.RoleEntity;
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
        RoleEntity roleEntity = roleRepository.findByName(name);
        roleRepository.deleteAllRelationshipInRolesPermissionsByRoleId(roleEntity.getId());
        userRepository.deleteAllRelationshipInUsersRolesByRoleId(roleEntity.getId());
        roleRepository.deleteByName(name);
    }

}
