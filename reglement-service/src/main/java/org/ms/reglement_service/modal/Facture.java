package org.ms.reglement_service.modal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Facture {
    private Long id;
    private java.util.Date dateFacture;
    private Long clientID;
    private List<FactureLigne> facturelignes; // Détails des lignes de la facture

    public double getTotal() {
        return facturelignes.stream()
                .mapToDouble(ligne -> ligne.getPrice().doubleValue() * ligne.getQuantity())
                .sum();
    }
}