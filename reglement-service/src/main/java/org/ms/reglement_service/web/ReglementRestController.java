package org.ms.reglement_service.web;
import org.ms.reglement_service.service.ReglementService;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;


import org.ms.reglement_service.entites.Reglement;
import org.ms.reglement_service.modal.Facture;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class ReglementRestController {

    private final ReglementService reglementService;

	
	 @Value("${globalParam}")
	    private int globalParam;

	    @Value("${email}")
	    private String email;
	    public ReglementRestController(ReglementService reglementService) {
	        this.reglementService = reglementService;
	    }
@GetMapping("config")
public Map<String, Object> config() {
    Map<String, Object> params = new Hashtable<>();
    params.put("globalParam", globalParam);
    params.put("email", email);
    params.put("threadName", Thread.currentThread().toString());
    return params;
}

@GetMapping("/reglements")
public List<Reglement> getAllReglements() {
    return reglementService.getAllReglements();
}

@GetMapping("/facture/{id}")
public List<Reglement> getReglementsByFacture(@PathVariable Long id) {
    return reglementService.getReglementsByFacture(id);
}

@GetMapping("/reste-global")
public double getResteGlobal() {
    return reglementService.calculateResteGlobal();
}

@PostMapping("/reglement")
public Reglement createReglement(@RequestBody Reglement reglement) {
    return reglementService.createReglement(reglement);
}

@GetMapping("/facture-details/{id}")
public Facture getFactureDetails(@PathVariable Long id) {
    return reglementService.getFactureDetails(id);
}
@PutMapping("/reglement/{id}")
public Reglement updateReglement(@PathVariable Long id, @RequestBody Reglement reglementDetails) {
    return reglementService.updateReglement(id, reglementDetails);
}

@DeleteMapping("/reglement/{id}")
public void deleteReglement(@PathVariable Long id) {
    reglementService.deleteReglement(id);
}

}
