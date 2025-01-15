package org.ms.auth_service.repository;

import org.ms.auth_service.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtilisateurRepository extends
JpaRepository<Utilisateur, Long> {
	/* Optional<Utilisateur> findByLogin(String username); */
    Utilisateur findByLoginAndPassword(String username, String motdepasse);
    Utilisateur findByLogin(String username);
}