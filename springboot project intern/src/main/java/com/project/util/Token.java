package com.project.util;

import jakarta.servlet.http.HttpServletRequest;
import org.keycloak.RSATokenVerifier;
import org.keycloak.representations.AccessToken;

public class Token {
    public String getUserIdFromToken(HttpServletRequest request) {
        try {
            String tokenStr = request.getHeader("Authorization").split(" ")[1];
            AccessToken token = RSATokenVerifier.create(tokenStr).getToken();
            return token.getSubject();
        } catch (Exception e) {
            return null;
        }


    }
}
