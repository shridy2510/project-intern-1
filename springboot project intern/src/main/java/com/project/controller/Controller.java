package com.project.controller;
import com.project.dto.TokenDto;
import com.project.dto.UserDto;
import com.project.repository.UserRepository;
import com.project.repository.entities.UserEntity;
import com.project.service.UserService;
import com.project.util.CheckRole;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
public class Controller {


    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    //nhận  login, trả về thông tin user token
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
    private CheckRole checkRole;


//test kiểm tra
    @GetMapping("/employees")
    public Iterable<UserEntity> findAllEmployees(HttpServletRequest request) {
        return this.userRepository.findAll();

    }

    //login trả về accesstoken và refreshtoken
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto userDto,HttpServletRequest request) {
        return userService.login(userDto);
    }

//lấy list user từ keycloak

    @GetMapping("/keycloak/users")
//   @PreAuthorize("hasRole('User')or hasRole('Admin')")
    public ResponseEntity<List< UserDto>> getUser(HttpServletRequest request){
        if(checkRole.checkRoleUser(request)) {

        List<UserDto> userDto = userService.getUserListFromKeycloak();
        return ResponseEntity.ok(userDto);
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }
    //lấy list user từ db
    @GetMapping("/list/users")
    public ResponseEntity<List<UserEntity>> getUserList(HttpServletRequest request){
        if(checkRole.checkRoleUser(request)) {

        List<UserEntity> userEntity = userService.getUserListFromDb();
        return ResponseEntity.ok(userEntity);
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }


    }
//tạo user mới
    @PostMapping("/keycloak/user")

    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto,HttpServletRequest request) {
        userService.createUser(userDto);

        return new ResponseEntity<>(userDto, HttpStatus.CREATED);

    }
    //đổi thông tin user
    @PutMapping("/keycloak/users")

    public ResponseEntity<String> update(@RequestBody UserDto userDto, HttpServletRequest request) {
        if(checkRole.checkRoleUser(request)) {

        userService.updateUser(userDto);
        return ResponseEntity.ok("Info updated successfully");
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }
    //xoá user
    @DeleteMapping("/keycloak/users")
    //@PreAuthorize("hasRole('User')or hasRole('Admin')")
    public ResponseEntity<String> deleteUser(@RequestBody UserDto userDto,HttpServletRequest request) {
        if(checkRole.checkRoleUser(request)) {

        userService.deleteUser(userDto);
        return ResponseEntity.ok("User deleted successfully");
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }

    //logout keycloak session
    @PostMapping("/keycloak/logout")
    public ResponseEntity<?> logout(@RequestBody TokenDto tokenDto,HttpServletRequest request) {


        return userService.logout(tokenDto);


    }












}
