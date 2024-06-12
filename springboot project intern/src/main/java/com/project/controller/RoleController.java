package com.project.controller;

import com.project.dto.UserDto;
import com.project.service.RoleService;
import com.project.util.CheckRole;
import jakarta.servlet.http.HttpServletRequest;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RoleController {
    @Autowired
    RoleService roleService;

    private CheckRole checkRole=new CheckRole();


//xoá role của người dùng với userId
    @PostMapping("/keycloak/remove-role")
    //@PreAuthorize("hasRole('User')or hasRole('Admin')")
    public ResponseEntity<String> removeRole(@RequestBody UserDto userDto, HttpServletRequest request) {
        if(checkRole.checkRoleUser(request)) {
        roleService.deleteRoleToUser(userDto);
        return ResponseEntity.ok("Role removed successfully");
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }
//trả về danh sách roles với userId
    @GetMapping("/keycloak/roles")
    //@PreAuthorize("hasRole('User')or hasRole('Admin')")
    public ResponseEntity<List<RoleRepresentation>> getUserRoles(@RequestBody UserDto userDto, HttpServletRequest request) {
        if(checkRole.checkRoleUser(request)) {
        List<RoleRepresentation> roles = roleService.getRoleList(userDto);
        return ResponseEntity.ok(roles);
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }
    // assign role cho người dùng với UserId
    @PostMapping("/keycloak/assign-role")
    //@PreAuthorize("hasRole('User')or hasRole('Admin')")
    public ResponseEntity<String> assignRole(@RequestBody UserDto userDto, HttpServletRequest request) {

        roleService.assignRoleToUser(userDto);
        return ResponseEntity.ok("Role assign successfully");


    }


}
