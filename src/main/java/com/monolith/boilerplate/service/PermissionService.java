package com.monolith.boilerplate.service;

import com.monolith.boilerplate.model.PermissionEntity;
import com.monolith.boilerplate.repository.PermissionRepository;
import com.monolith.boilerplate.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PermissionService {
    @Autowired
    PermissionRepository permissionRepository;

    @Autowired
    RoleRepository roleRepository;

    @Transactional
    public void deletePermissionByName(String name){
        PermissionEntity permission = permissionRepository.findByName(name);
        roleRepository.deleteAllRelationshipInRolesPermissionsByPermissionId(permission.getId());
        permissionRepository.deleteByName(permission.getName());
    }
}
