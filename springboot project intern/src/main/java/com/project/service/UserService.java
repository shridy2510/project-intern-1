package com.project.service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.dto.Role;
import com.project.dto.TokenDto;
import com.project.dto.UserDto;
import com.project.repository.entities.UserEntity;
import com.project.security.KeycloakUtilSecurity;
import com.project.repository.UserRepository;
import jakarta.ws.rs.core.Response;
import org.apache.catalina.User;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class UserService {
    @Value("${realm}")
    private String realm;
    @Value("${server-url}")
    private String serverUrl;
    @Value("${client-id}")
    private String clientId;
    @Value("${grant-type}")
    private String grantType;
    @Value("${clientSecret}")
    private String clientSecret;


    @Autowired
    private KeycloakUtilSecurity keycloakUtilSecurity;
    @Autowired
    private UserRepository userRepository;




    public void updateUser(UserDto userDto) {
        //update keycloak
        Keycloak keycloak = keycloakUtilSecurity.KeycloakInstance();
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setEmail(userDto.getEmail());
        userRepresentation.setFirstName(userDto.getFirstName());
        userRepresentation.setLastName(userDto.getLastName());
        userRepresentation.setUsername(userDto.getUserName());
        List<CredentialRepresentation> credentials = new ArrayList<>();
        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setTemporary(false);
        cred.setValue(userDto.getPassword());
        credentials.add(cred);
        userRepresentation.setCredentials(credentials);
        userRepresentation.setEnabled(true);
        keycloak.realm(realm).users().get(userDto.getUserId()).update(userRepresentation);

        //updateDb
        Optional<UserEntity> optionalUser = userRepository.findByUserId(userDto.getUserId());
        if (optionalUser.isPresent()) {
            UserEntity userEntity = optionalUser.get();
            // Cập nhật các trường của userEntity với dữ liệu từ userDto
            if(userDto.getUserName()!=null){
            userEntity.setUsername(userDto.getUserName());}
            if(userDto.getEmail()!=null){userEntity.setGmail(userDto.getEmail());}
            if(userDto.getLastName()!=null){userEntity.setLastName(userDto.getLastName());}
            if(userDto.getFirstName()!=null){userEntity.setFirstName(userDto.getFirstName());}
            // Lưu lại cập nhật vào cơ sở dữ liệu
            userRepository.save(userEntity);
        } else {
            // Xử lý khi không tìm thấy người dùng với userId cụ thể
            throw new RuntimeException("User not found with userId: " + userDto.getUserId());
        }


    }

    public void deleteUser(UserDto userDto) {
        //xoá keycloak
        Keycloak keycloak = keycloakUtilSecurity.KeycloakInstance();
        keycloak.realm(realm).users().delete(userDto.getUserId());
        //xoá db
        userRepository.deleteByUserId(userDto.getUserId());


    }

    public void createUser(UserDto userDto) {

        //lạp tk keycloak
        Keycloak keycloak = keycloakUtilSecurity.KeycloakInstance();
        UsersResource usersResource = keycloak.realm(realm).users();
        UserRepresentation newUser = new UserRepresentation();
        newUser.setEmail(userDto.getEmail());
        newUser.setFirstName(userDto.getFirstName());
        newUser.setLastName(userDto.getLastName());
        newUser.setUsername(userDto.getUserName());
        newUser.setEnabled(true);
        List<CredentialRepresentation> credentials = new ArrayList<>();
        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setTemporary(false);
        cred.setValue(userDto.getPassword());
        credentials.add(cred);
        newUser.setCredentials(credentials);
        usersResource.create(newUser);

        //lập trong db
        // lấy id vừa tạo
        UserRepresentation user=usersResource.search(userDto.getUserName()).get(0);

        UserEntity userEntity=new UserEntity();
        userEntity.setUsername(userDto.getUserName());
        userEntity.setGmail(userDto.getUserName());
        userEntity.setLastName(userDto.getLastName());
        userEntity.setFirstName(userDto.getFirstName());
        userEntity.setUserId(user.getId());
        userRepository.save(userEntity);


        System.out.println("UserEntity saved: " + userEntity);

        //assign role cho người dùng

        RoleRepresentation role = keycloak.realm(realm).roles().get(userDto.getRole()).toRepresentation();
        usersResource.get(user.getId()).roles().realmLevel().add(Collections.singletonList(role));

    }

    public List<UserDto> getUserListFromKeycloak() {
        Keycloak keycloak = keycloakUtilSecurity.KeycloakInstance();
        List<UserRepresentation> userRepresentation = keycloak.realm(realm).users().list();

        List<UserDto> userDtos = new ArrayList<>();
        if (userRepresentation != null) {
            for (UserRepresentation userRep : userRepresentation) {
                UserDto userDto = new UserDto();
                userDto.setUserId(userRep.getId());
                userDto.setUserName(userRep.getUsername());
                userDto.setEmail(userRep.getEmail());
                userDto.setFirstName(userRep.getFirstName());
                userDto.setLastName(userRep.getLastName());
                userDtos.add(userDto);
            }

        }
        return userDtos;
    }

    public List<UserEntity> getUserListFromDb() {
        return userRepository.findAll();

    }


    public ResponseEntity<?> login(UserDto userDto) {
        RestTemplate restTemplate = new RestTemplate();
        String url = serverUrl + "/realms/" + realm + "/protocol/openid-connect/token";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("grant_type", grantType);
        map.add("username", userDto.getUserName());
        map.add("password", userDto.getPassword());
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response.getBody());
            String accessToken = root.path("access_token").asText();
            String refreshToken = root.path("refresh_token").asText();
            Map<String, String> tokenResponse = new HashMap<>();
            tokenResponse.put("access_token", accessToken);
            tokenResponse.put("refresh_token", refreshToken);

            return ResponseEntity.ok(tokenResponse);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error extracting access token");
        }
    }
    public ResponseEntity<?> logout(TokenDto tokenDto) {

        RestTemplate restTemplate = new RestTemplate();
        String url = serverUrl + "/realms/" + realm + "/protocol/openid-connect/logout";;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("refresh_token",tokenDto.getRefresh_token());
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        restTemplate.postForEntity(url, request, Object.class);
        return ResponseEntity.ok().build();

    }


}



