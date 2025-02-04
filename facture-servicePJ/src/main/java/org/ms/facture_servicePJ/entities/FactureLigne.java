package org.ms.facture_servicePJ.entities;

import java.math.BigDecimal;

import org.ms.facture_servicePJ.model.Produit;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @ToString
public class FactureLigne {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private long id;
	    private long produitID;
	    private long quantity;
	    private BigDecimal price; // Prix sera automatiquement défini

	    @Transient
	    private Produit produit;

	    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	    @ManyToOne
	    @JoinColumn(name = "facture_id")
	    private Facture facture;

	    // Méthode pour définir le prix à partir du produit
	    public void updatePrice(Produit produit) {
	        if (produit != null) {
	            this.price = produit.getPrice();
	        }
	    }
}

