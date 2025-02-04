package org.ms.auth_service.service;

import org.ms.auth_service.model.Utilisateur;
import org.ms.auth_service.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	private UtilisateurRepository userRepository;

	public Utilisateur authenticate(String username, String password) {
		Utilisateur user = userRepository.findByLogin(username);
		if (user != null && user.getPassword().equals(password)) {
			return user;
		}
		return null;
	}
}

