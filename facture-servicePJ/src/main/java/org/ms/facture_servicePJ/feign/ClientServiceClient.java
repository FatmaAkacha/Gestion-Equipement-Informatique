package org.ms.facture_servicePJ.feign;

import org.ms.facture_servicePJ.model.Client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "client-service", url = "http://localhost:7107/api")  // Nom du service Client dans Eureka
public interface ClientServiceClient {

    // Récupérer un client par son ID
    @GetMapping(path = "/clients/{id}")
    Client findClientById(@PathVariable(name="id") long id);

    // Récupérer tous les clients
    @GetMapping(path = "/api/clients")
    List<Client> findAllClients();
    

}
