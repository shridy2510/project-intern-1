package com.project.service;

import com.project.dto.UserDto;
import com.project.security.KeycloakUtilSecurity;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class RoleService {
    @Autowired
    private KeycloakUtilSecurity keycloakUtilSecurity;
    @Value("${realm}")
    private String realm;
    //lấy danh sách role của 1 người dùng với userId
    public List<RoleRepresentation> getRoleList(UserDto userDto) {
        Keycloak keycloak = keycloakUtilSecurity.KeycloakInstance();
        RealmResource realmResource = keycloak.realm(realm);
        UserResource userResource = realmResource.users().get(userDto.getUserId());
        return userResource.roles().realmLevel().listAll();

    }
    //thêm role cho người dùng với userId

    public void assignRoleToUser(UserDto userDto) {
        Keycloak keycloak = keycloakUtilSecurity.KeycloakInstance();
        UsersResource usersResource = keycloak.realm(realm).users();
        RoleRepresentation role = keycloak.realm(realm).roles().get(userDto.getRole()).toRepresentation();
        usersResource.get(userDto.getUserId()).roles().realmLevel().add(Collections.singletonList(role));

    }
    //xoá role với userId

    public void deleteRoleToUser(UserDto userDto) {
        Keycloak keycloak = keycloakUtilSecurity.KeycloakInstance();
        UsersResource usersResource = keycloak.realm(realm).users();
        RoleRepresentation role = keycloak.realm(realm).roles().get(userDto.getRole()).toRepresentation();
        usersResource.get(userDto.getUserId()).roles().realmLevel().remove(Collections.singletonList(role));

    }

}
