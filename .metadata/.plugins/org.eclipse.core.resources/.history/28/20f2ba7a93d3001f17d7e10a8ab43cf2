package org.ms.facture_servicePJ.entities;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;

import org.ms.facture_servicePJ.model.Client;
import org.ms.facture_servicePJ.model.Produit;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Facture {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Date dateFacture;

    @OneToMany(mappedBy = "facture", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<FactureLigne> facturelignes = new ArrayList<>();

    @Transient
    private Client client;
    private long clientID;
    private String statut = "Non Payée"; 
    private double total ;
    //private Currency currency;

    @PrePersist
    private void setDefaultDate() {
        if (dateFacture == null) {
            dateFacture = new Date(System.currentTimeMillis()); 
        }
    }
}