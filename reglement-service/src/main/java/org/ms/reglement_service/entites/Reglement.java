package org.ms.reglement_service.entites;

import java.math.BigDecimal;
import java.time.LocalDateTime;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reglement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long factureId;
    private BigDecimal montant;
    private String modePaiement;
    private String statut;
    private LocalDateTime dateReglement;

    // Champ temporaire pour le reste à payer (non persistant)
    @Transient
    private double resteAPayer;
}
