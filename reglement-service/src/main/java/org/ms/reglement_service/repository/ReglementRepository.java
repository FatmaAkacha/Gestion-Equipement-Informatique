package org.ms.reglement_service.repository;

import java.util.List;

import org.ms.reglement_service.entites.Reglement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReglementRepository extends JpaRepository<Reglement, Long> {
	List<Reglement> findByFactureId(Long factureId);
}