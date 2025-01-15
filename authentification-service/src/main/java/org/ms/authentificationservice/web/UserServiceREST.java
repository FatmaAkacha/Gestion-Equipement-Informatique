package org.ms.authentificationservice.web;

import org.ms.authentificationservice.dto.UserRoleData;
import org.ms.authentificationservice.entities.AppRole;
import org.ms.authentificationservice.entities.AppUser;
import org.ms.authentificationservice.services.UserService;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserServiceREST {

    private final UserService userService;

    public UserServiceREST(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/register")  // Assurez-vous que la route est correcte ici
    public AppUser addUser(@RequestBody AppUser user) {
        return userService.addUser(user);
    }

    @PostMapping("/roles")
    @PostAuthorize("hasAuthority('ADMIN')")
    public AppRole addRole(@RequestBody AppRole role) {
        return userService.addRole(role);
    }

    @PostMapping("/addRoleToUser")
    @PostAuthorize("hasAuthority('ADMIN')")
    public void addRoleToUser(@RequestBody UserRoleData userRoleData) {
        userService.addRoleToUser(userRoleData.getUsername(), userRoleData.getRoleName());
    }

    @GetMapping
    public List<AppUser> getAllUsers() {
        return userService.getAllUsers();}
}