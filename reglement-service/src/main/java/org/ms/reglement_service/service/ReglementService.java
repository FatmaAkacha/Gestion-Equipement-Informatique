package org.ms.reglement_service.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.ms.reglement_service.entites.Reglement;
import org.ms.reglement_service.feign.FactureServiceClient;
import org.ms.reglement_service.modal.Facture;
import org.ms.reglement_service.repository.ReglementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReglementService {
    private final ReglementRepository reglementRepository;
    private final FactureServiceClient factureServiceClient;

    @Autowired
    public ReglementService(ReglementRepository reglementRepository, FactureServiceClient factureServiceClient) {
        this.reglementRepository = reglementRepository;
        this.factureServiceClient = factureServiceClient;
    }

    public List<Reglement> getAllReglements() {
        return reglementRepository.findAll();
    }

    public List<Reglement> getReglementsByFacture(Long factureId) {
        return reglementRepository.findByFactureId(factureId);
    }

    public double calculateResteGlobal() {
        List<Facture> factures = factureServiceClient.getAllFactures();

        return factures.stream()
                .mapToDouble(facture -> {
                    List<Reglement> reglements = reglementRepository.findByFactureId(facture.getId());
                    double totalReglements = reglements.stream()
                            .mapToDouble(r -> r.getMontant().doubleValue())
                            .sum();
                    return Math.max(facture.getTotal() - totalReglements, 0);
                })
                .sum();
    }

    public Reglement createReglement(Reglement reglement) {
        // Remplir la date du règlement
        reglement.setDateReglement(LocalDateTime.now());

        // Sauvegarder le règlement
        Reglement savedReglement = reglementRepository.save(reglement);

        // Calculer le total des règlements pour la facture
        List<Reglement> reglements = reglementRepository.findByFactureId(reglement.getFactureId());
        double totalReglements = reglements.stream()
                                           .mapToDouble(r -> r.getMontant().doubleValue())
                                           .sum();

        // Récupérer le total de la facture
        double totalFacture = factureServiceClient.getFactureTotal(reglement.getFactureId());

        // Calculer et mettre à jour le statut
        String statut = calculateFactureStatus(reglement.getFactureId(), totalFacture, totalReglements);
        savedReglement.setStatut(statut);

        // Calculer le reste à payer
        savedReglement.setResteAPayer(Math.max(totalFacture - totalReglements, 0));

        return reglementRepository.save(savedReglement);
    }

    public Facture getFactureDetails(Long factureId) {
        return factureServiceClient.getFactureById(factureId);
    }

    private String calculateFactureStatus(Long factureId, double totalFacture, double totalReglements) {
        String statut;
        double reste = totalFacture - totalReglements;

        if (reste <= 0) {
            statut = "Payée";
        } else if (totalReglements > 0) {
            statut = "Partiellement Payée";
        } else {
            statut = "Non Payée";
        }

        // Mettre à jour le statut dans facture-service
        factureServiceClient.updateFactureStatus(factureId, statut);

        return statut;
    }

    // --- Ajout de la méthode Update ---
    public Reglement updateReglement(Long id, Reglement reglementDetails) {
        Optional<Reglement> existingReglementOpt = reglementRepository.findById(id);
        if (!existingReglementOpt.isPresent()) {
            throw new IllegalArgumentException("Règlement avec l'ID " + id + " non trouvé");
        }
        Reglement existingReglement = existingReglementOpt.get();

        // Mettre à jour les propriétés du règlement
        existingReglement.setFactureId(reglementDetails.getFactureId());
        existingReglement.setMontant(reglementDetails.getMontant());
        existingReglement.setModePaiement(reglementDetails.getModePaiement());
        existingReglement.setStatut(reglementDetails.getStatut());
        existingReglement.setDateReglement(LocalDateTime.now()); // Mettre à jour la date
        existingReglement.setResteAPayer(reglementDetails.getResteAPayer());

        // Recalculer le statut et le reste à payer
        List<Reglement> reglements = reglementRepository.findByFactureId(existingReglement.getFactureId());
        double totalReglements = reglements.stream()
                                           .mapToDouble(r -> r.getMontant().doubleValue())
                                           .sum();
        double totalFacture = factureServiceClient.getFactureTotal(existingReglement.getFactureId());
        String statut = calculateFactureStatus(existingReglement.getFactureId(), totalFacture, totalReglements);
        existingReglement.setStatut(statut);
        existingReglement.setResteAPayer(Math.max(totalFacture - totalReglements, 0));

        return reglementRepository.save(existingReglement);
    }

    // --- Ajout de la méthode Delete ---
    public void deleteReglement(Long id) {
        Optional<Reglement> existingReglementOpt = reglementRepository.findById(id);
        if (!existingReglementOpt.isPresent()) {
            throw new IllegalArgumentException("Règlement avec l'ID " + id + " non trouvé");
        }
        
        // Suppression du règlement
        reglementRepository.deleteById(id);
    }
}
