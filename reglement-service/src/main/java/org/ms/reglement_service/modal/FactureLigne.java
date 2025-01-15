package org.ms.reglement_service.modal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FactureLigne {
    private Long id;
    private Long produitID;
    private BigDecimal price;
    private int quantity;
}