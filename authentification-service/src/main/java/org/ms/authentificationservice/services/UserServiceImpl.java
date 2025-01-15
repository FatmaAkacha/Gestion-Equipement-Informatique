package org.ms.authentificationservice.services;

import org.ms.authentificationservice.entities.AppRole;
import org.ms.authentificationservice.entities.AppUser;
import org.ms.authentificationservice.repositories.AppRoleRepository;
import org.ms.authentificationservice.repositories.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private AppRoleRepository appRoleRepository;

  //déclaration d’un attribut
    private PasswordEncoder passwordEncoder=null;
    //Injection de dépendance par constructeur
    public UserServiceImpl(PasswordEncoder passwordEncoder)
    {
    this.passwordEncoder=passwordEncoder;
    }


    @Override
    public AppUser addUser(AppUser appUser) {
    appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
    return appUserRepository.save(appUser);
    }

    @Override
    public AppRole addRole(AppRole appRole) {
        return appRoleRepository.save(appRole);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        AppUser user = appUserRepository.findByUsername(username);
        AppRole role = appRoleRepository.findByRoleName(roleName);
        
        if (user != null && role != null) {
            user.getAppRoles().add(role);
        }
   
    }

    @Override
    public AppUser getUserByName(String username) {
        return appUserRepository.findByUsername(username);
    }

    @Override
    public List<AppUser> getAllUsers() {
        return appUserRepository.findAll();}
}