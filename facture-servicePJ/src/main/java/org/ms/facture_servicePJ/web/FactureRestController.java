package org.ms.facture_servicePJ.web;

import org.ms.facture_servicePJ.entities.Facture;
import org.ms.facture_servicePJ.service.FactureService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RefreshScope
public class FactureRestController {

    private final FactureService factureService;

    @Value("${globalParam}")
    private int globalParam;

    @Value("${email}")
    private String email;

    public FactureRestController(FactureService factureService) {
        this.factureService = factureService;
    }

    @GetMapping("config")
    public Map<String, Object> config() {
        Map<String, Object> params = new Hashtable<>();
        params.put("globalParam", globalParam);
        params.put("email", email);
        params.put("threadName", Thread.currentThread().toString());
        return params;
    }

    @GetMapping("/{id}")
    public Facture getFacture(@PathVariable long id) {
        return factureService.getFullFacture(id);
    }
    @PutMapping("/{id}/update")
    public Facture updateFacture(@PathVariable Long id, @RequestBody Facture updatedFacture) {
        return factureService.updateFacture(id, updatedFacture);
    }

    @PutMapping("/{id}/update-status")
    public Facture updateFactureStatus(@PathVariable Long id, @RequestParam String status) {
        return factureService.updateFactureStatus(id, status);
    }

    @GetMapping("/{id}/total")
    public double getFactureTotal(@PathVariable Long id) {
        return factureService.calculateFactureTotal(id);
    }

    @GetMapping("/most-loyal-clients")
    public List<Map<String, Object>> getMostLoyalClients() {
        return factureService.getMostLoyalClients();
    }

    @GetMapping("/most-requested-products")
    public List<Map<String, Object>> getMostRequestedProducts() {
        return factureService.getMostRequestedProductsWithDetails();
    }

    @GetMapping("/factures")
    public List<Facture> getAllFactures() {
        return factureService.getAllFactures();
    }

 
    @GetMapping("/factures/global-chiffre-affaires")
    public double getGlobalChiffreAffaires() {
        return factureService.calculateGlobalChiffreAffaires();
    }

    @GetMapping("/factures/chiffre-affaires")
    public double getChiffreAffairesByYear(@RequestParam int year) {
        return factureService.calculateChiffreAffairesByYear(year);
    }

    @DeleteMapping("/facture/{id}")
    public ResponseEntity<Void> deleteFacture(@PathVariable Long id) {
        factureService.deleteFacture(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PostMapping("/facture")
    public Facture createFacture(@RequestBody Facture newFacture) {
        if (newFacture.getCurrency() == null || newFacture.getCurrency().isEmpty()) {
            newFacture.setCurrency("TND");
        }
        return factureService.createFacture(newFacture);
    }
    @GetMapping("/factures/status")
    public Map<String, List<Facture>> getFacturesByStatus() {
        return factureService.getFacturesByStatus();
    }

    @GetMapping("/factures/dettes")
    public List<Map<String, Object>> getDettesParClient() {
        return factureService.getDettesParClient();
    }
     
    
}
