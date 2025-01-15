package org.ms.facture_servicePJ.feign;
import java.util.List;

import org.ms.facture_servicePJ.model.Produit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "produit-service", url = "http://localhost:7268/api")
public interface ProduitServiceClient {

    @GetMapping(path = "/produits/{id}")
    Produit findProduitById(@PathVariable(name = "id") long id);
    
    @GetMapping(path="/produits")
    List<Produit> findAllProduits();
    
    @GetMapping("/Products/out-of-stock")
    List<Produit> getOutOfStockProducts();
}
