package org.ms.reglement_service;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients

public class ReglementServiceApplication {


	public static void main(String[] args) {
		SpringApplication.run(ReglementServiceApplication.class, args);
	}
	/*
	@Bean
	CommandLineRunner start(ReglementRepository reglementRepository, ReglementLigneRepository reglementLigneRepository,
			ClientServiceClient clientServiceClient, ProduitServiceClient produitServiceClient, FactureServiceClient factureServiceClient)

	{
		return args -> {
//Récupérer un client à distance
			Client client = clientServiceClient.findClientById(1L);
			System.out.println(client);
			Facture facture = factureServiceClient.findFactureById(1L);
			System.out.println(facture);
//Insérer une facture
			Reglement reglement = reglementRepository.save(new Reglement());
			List<Produit> listeProduits = produitServiceClient.findAllProduits();
			listeProduits.forEach(p -> {
//pour chaque produit, insérer une factureligne
				ReglementLigne reglementLigne = new ReglementLigne();
				reglementLigne.setProduitID(p.getId());
				reglementLigne.setProduitID(p.getId());
				reglementLigne.setPrice(p.getStock());
				reglementLigne.setQuantity(1+ new Random().nextInt(100));
				reglementLigne.setFacture(facture);
				reglementLigne.setReglement(reglement);

				reglementLigneRepository.save(reglementLigne);
			});
		};
	}
*/
}
