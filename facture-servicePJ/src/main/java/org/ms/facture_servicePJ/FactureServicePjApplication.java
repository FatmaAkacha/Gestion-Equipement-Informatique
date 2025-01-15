package org.ms.facture_servicePJ;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableFeignClients
public class FactureServicePjApplication {


    public static void main(String[] args) {
        SpringApplication.run(FactureServicePjApplication.class, args);
    }
/*

	@Bean
	CommandLineRunner start(FactureRepository factureRepository, FactureLigneRepository factureLigneRepository,
			ClientServiceClient clientServiceClient, ProduitServiceClient produitServiceClient)

	{
		return args -> {
//Récupérer un client à distance
			Client client = clientServiceClient.findClientById(1L);
//Insérer une facture
			Facture facture = factureRepository.save(new Facture(null, new Date(0), false , null, client, client.getId()));
//Récupérer les produits à distance
			List<Produit> listeProduits = produitServiceClient.findAllProduits();
//Parcourir la liste des produits
			listeProduits.forEach(p -> {
//pour chaque produit, insérer une factureligne
				FactureLigne factureLigne = new FactureLigne();
				factureLigne.setProduitID(p.getId());
				factureLigne.setPrice(p.getStock());
				factureLigne.setQuantity(1 + new Random().nextInt(100));
				factureLigne.setFacture(facture);
				factureLigneRepository.save(factureLigne);
			});
		};
	}*/
}