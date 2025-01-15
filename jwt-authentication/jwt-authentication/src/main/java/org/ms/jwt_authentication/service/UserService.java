package org.ms.jwt_authentication.service;

import java.util.Collections;

import org.ms.jwt_authentication.model.User;
import org.ms.jwt_authentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService{

	 private final UserRepository userRepository;
	    private final BCryptPasswordEncoder passwordEncoder;

	    @Autowired
	    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
	        this.userRepository = userRepository;
	        this.passwordEncoder = passwordEncoder;
	    }

	    public User saveUser(User user) {
	        user.setPassword(passwordEncoder.encode(user.getPassword()));
	        return userRepository.save(user);
	    }

	    public User findByEmail(String email) {
	        return userRepository.findByEmail(email);
	    }

	    @Override
	    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	        User user = userRepository.findByEmail(username);
	        if (user == null) {
	            throw new UsernameNotFoundException("User not found");
	        }
	        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), Collections.emptyList());
	    }
	}    