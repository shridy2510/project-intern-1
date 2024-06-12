package com.project.util;

import com.project.security.KeycloakUtilSecurity;
import jakarta.servlet.http.HttpServletRequest;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;



@Component
public class CheckRole {
    @Autowired
    private KeycloakUtilSecurity keycloakUtilSecurity;
    @Value("${realm}")
    private String realm;
    public boolean checkRoleUser(HttpServletRequest request) {

        Keycloak keycloak = keycloakUtilSecurity.KeycloakInstance();
        Token token = new Token();
        String userId=token.getUserIdFromToken(request);
        // Lấy RealmResource từ Keycloak
        RealmResource realmResource = keycloak.realm(realm);
        UserResource userResource = realmResource.users().get(userId);
        List<RoleRepresentation> roles = userResource.roles().realmLevel().listAll();
        List<String> roleNames = roles.stream()
                .map(RoleRepresentation::getName)
                .collect(Collectors.toList());
        if(roleNames.contains("Admin") || roleNames.contains("User")){
            return true;
        }
        else{
            return false;
        }






    }

}

