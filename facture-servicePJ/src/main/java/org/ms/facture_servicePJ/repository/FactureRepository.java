package org.ms.facture_servicePJ.repository;

import java.util.List;

import org.ms.facture_servicePJ.entities.Facture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.webmvc.RepositoryRestController;
@RepositoryRestController
public interface FactureRepository extends
JpaRepository<Facture, Long> {
	  List<Facture> findByClientID(Long clientID);
	    @Query("SELECT f.clientID, COUNT(f) AS totalFactures " +
	    	       "FROM Facture f GROUP BY f.clientID ORDER BY totalFactures DESC")
	    	List<Object[]> findMostLoyalClients();
}
