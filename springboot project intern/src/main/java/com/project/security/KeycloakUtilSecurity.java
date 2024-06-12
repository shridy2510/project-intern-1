package com.project.security;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class KeycloakUtilSecurity {
    Keycloak keycloak;
    @Value( "${realm}")
    private String realm;
    @Value("${server-url}")
    private String server;
    @Value("${client-id}")
    private String clientId;
    @Value("${grant-type}")
    private String grantType;
    @Value("${name}")
    private String username;
    @Value("${password}")
    private String password;
    @Value("${clientSecret}")
    private String secret;


    public Keycloak KeycloakInstance(){
        if (this.keycloak == null){
            keycloak= KeycloakBuilder.builder()
                    .realm(realm)
                    .serverUrl(server)
                    .clientId(clientId)
                    .grantType(grantType)
                    .username(username)
                    .password(password)
                    .clientSecret(secret)
                    .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                    .build();
        }
        return keycloak;
    }




}



