package org.ms.facture_servicePJ.service;

import java.time.ZoneId;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.ms.facture_servicePJ.entities.Facture;
import org.ms.facture_servicePJ.entities.FactureLigne;
import org.ms.facture_servicePJ.feign.ClientServiceClient;
import org.ms.facture_servicePJ.feign.CurrencyServiceClient;
import org.ms.facture_servicePJ.feign.ProduitServiceClient;
import org.ms.facture_servicePJ.model.Client;
import org.ms.facture_servicePJ.model.Produit;
import org.ms.facture_servicePJ.repository.FactureLigneRepository;
import org.ms.facture_servicePJ.repository.FactureRepository;
import org.springframework.stereotype.Service;

@Service
public class FactureService {
	private final FactureRepository factureRepository;
    private final FactureLigneRepository factureLigneRepository;
    private final ClientServiceClient clientServiceClient;
    private final ProduitServiceClient produitServiceClient;
    private final CurrencyServiceClient currencyServiceClient;

    public FactureService(FactureRepository factureRepository, FactureLigneRepository factureLigneRepository,
                          ClientServiceClient clientServiceClient, ProduitServiceClient produitServiceClient,CurrencyServiceClient currencyServiceClient) {
        this.factureRepository = factureRepository;
        this.currencyServiceClient = currencyServiceClient;
        this.factureLigneRepository = factureLigneRepository;
        this.clientServiceClient = clientServiceClient;
        this.produitServiceClient = produitServiceClient;
    }

    public Facture getFullFacture(long id) {
        Facture facture = factureRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Facture non trouvée !"));

        // Récupération du client
        Client client = clientServiceClient.findClientById(facture.getClientID());
        if (client == null) {
            throw new RuntimeException("Client non trouvé pour l'ID : " + facture.getClientID());
        }
        facture.setClient(client);

        // Récupération des produits pour chaque ligne
        facture.getFacturelignes().forEach(fl -> {
            Produit product = produitServiceClient.findProduitById(fl.getProduitID());
            if (product == null) {
                throw new RuntimeException("Produit non trouvé pour l'ID : " + fl.getProduitID());
            }
            fl.setProduit(product);
        });

        return facture;
    }

    public Facture updateFactureStatus(Long id, String status) {
        Facture facture = factureRepository.findById(id).orElseThrow(() -> new RuntimeException("Facture non trouvée !"));
        facture.setStatut(status);
        return factureRepository.save(facture);
    }

    public double calculateFactureTotal(Long id) {
        Facture facture = factureRepository.findById(id).orElseThrow(() -> new RuntimeException("Facture non trouvée !"));
        return facture.getFacturelignes().stream()
                .mapToDouble(ligne -> ligne.getPrice().doubleValue() * ligne.getQuantity())
                .sum();
    }

    public List<Map<String, Object>> getMostLoyalClients() {
        // Récupérer toutes les factures
        List<Facture> factures = factureRepository.findAll();

        // Calculer le nombre de factures par client
        Map<Long, Long> clientFactureCounts = factures.stream()
                .collect(Collectors.groupingBy(Facture::getClientID, Collectors.counting()));

        // Trier les clients par nombre de factures décroissant
        return clientFactureCounts.entrySet()
                .stream()
                .sorted((entry1, entry2) -> Long.compare(entry2.getValue(), entry1.getValue()))
                .map(entry -> {
                    // Récupérer les détails du client via le service client
                    Client client = clientServiceClient.findClientById(entry.getKey());
                    Map<String, Object> clientDetails = new HashMap<>();
                    clientDetails.put("clientID", entry.getKey());
                    clientDetails.put("nom", client != null ? client.getName() : "Client inconnu");
                    clientDetails.put("email", client != null ? client.getEmail() : "Email inconnu");
                    clientDetails.put("nombreFactures", entry.getValue());
                    return clientDetails;
                })
                .collect(Collectors.toList());
    }
    
    public List<Facture> getAllFactures() {
        List<Facture> factures = factureRepository.findAll();

        // Compléter chaque facture avec les informations des clients et produits
        factures.forEach(facture -> {
            // Compléter les informations du client
            Client client = clientServiceClient.findClientById(facture.getClientID());
            if (client != null) {
                facture.setClient(client);
            } else {
                throw new RuntimeException("Client non trouvé pour l'ID : " + facture.getClientID());
            }

            // Compléter les informations des produits pour chaque ligne
            facture.getFacturelignes().forEach(ligne -> {
                Produit produit = produitServiceClient.findProduitById(ligne.getProduitID());
                if (produit != null) {
                    ligne.setProduit(produit);
                    ligne.updatePrice(produit);
                } else {
                    throw new RuntimeException("Produit non trouvé pour l'ID : " + ligne.getProduitID());
                }
            });
        });

        return factures;
    }

    public Facture createFacture(Facture newFacture) {
        Client client = clientServiceClient.findClientById(newFacture.getClientID());
        if (client == null) {
            throw new RuntimeException("Client non trouvé pour l'ID : " + newFacture.getClientID());
        }
        newFacture.setClient(client);
       

        for (FactureLigne ligne : newFacture.getFacturelignes()) {
            Produit produit = produitServiceClient.findProduitById(ligne.getProduitID());
            ligne.setProduit(produit);
            ligne.updatePrice(produit); // Récupérer automatiquement le prix du produit
            ligne.setFacture(newFacture);
        }

        return factureRepository.save(newFacture);
    }

    public double calculateGlobalChiffreAffaires() {
        List<Facture> factures = factureRepository.findAll();
        return factures.stream()
                .mapToDouble(this::calculateTotalForFacture)
                .sum();
    }

    public double calculateChiffreAffairesByYear(int year) {
        List<Facture> factures = factureRepository.findAll(); // Add filtering logic for year
        return factures.stream()
                .mapToDouble(this::calculateTotalForFacture)
                .sum();
    }

    private double calculateTotalForFacture(Facture facture) {
        Collection<FactureLigne> lignes = factureLigneRepository.findByFactureId(facture.getId());
        return lignes.stream()
                .mapToDouble(ligne -> ligne.getPrice().doubleValue() * ligne.getQuantity())
                .sum();
    }

    public List<Map.Entry<Long, Long>> getMostRequestedProducts(){
        // Récupérer toutes les lignes de factures
        List<FactureLigne> allFactureLignes = factureLigneRepository.findAll();

        // Calculer les quantités totales pour chaque produit
        Map<Long, Long> productQuantities = new HashMap<>();
        for (FactureLigne ligne : allFactureLignes) {
            productQuantities.merge(ligne.getProduitID(), ligne.getQuantity(), Long::sum);
        }

        // Trier les produits par quantité décroissante
        return productQuantities.entrySet()
                .stream()
                .sorted((entry1, entry2) -> Long.compare(entry2.getValue(), entry1.getValue()))
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getMostRequestedProductsWithDetails() {
        List<FactureLigne> allFactureLignes = factureLigneRepository.findAll();

        // Calculer les quantités totales pour chaque produit
        Map<Long, Long> productQuantities = new HashMap<>();
        for (FactureLigne ligne : allFactureLignes) {
            productQuantities.merge(ligne.getProduitID(), ligne.getQuantity(), Long::sum);
        }

        // Récupérer les détails des produits via ProduitServiceClient
        return productQuantities.entrySet()
                .stream()
                .sorted((entry1, entry2) -> Long.compare(entry2.getValue(), entry1.getValue()))
                .map(entry -> {
                    Produit produit = produitServiceClient.findProduitById(entry.getKey().intValue());
                    Map<String, Object> productDetails = new HashMap<>();
                    productDetails.put("produitID", entry.getKey());
                    productDetails.put("nom", produit != null ? produit.getName() : "Produit inconnu");
                    productDetails.put("quantite", entry.getValue());
                    return productDetails;
                })
                .collect(Collectors.toList());
    }
    
    public List<Map<String, Object>> getMostSoldProductsGlobal() {
        return calculateMostSoldProducts(null);
    }

    public List<Map<String, Object>> getMostSoldProductsByYear(int year) {
        return calculateMostSoldProducts(year);
    }

    private List<Map<String, Object>> calculateMostSoldProducts(Integer year) {
        List<FactureLigne> allFactureLignes;

        // Si une année est fournie, filtrez les lignes par année
        if (year != null) {
            allFactureLignes = factureLigneRepository.findAll().stream()
                    .filter(ligne -> ligne.getFacture() != null && // Vérifiez que la facture est non nulle
                            ligne.getFacture().getDateFacture() != null && // Vérifiez que la date n'est pas nulle
                            ligne.getFacture().getDateFacture().toInstant()
                                .atZone(ZoneId.systemDefault()).getYear() == year)
                    .collect(Collectors.toList());
        } else {
            allFactureLignes = factureLigneRepository.findAll();
        }

        // Calculer les quantités totales pour chaque produit
        Map<Long, Long> productQuantities = new HashMap<>();
        for (FactureLigne ligne : allFactureLignes) {
            productQuantities.merge(ligne.getProduitID(), ligne.getQuantity(), Long::sum);
        }

        // Récupérer les détails des produits via ProduitServiceClient
        return productQuantities.entrySet()
                .stream()
                .sorted((entry1, entry2) -> Long.compare(entry2.getValue(), entry1.getValue()))
                .map(entry -> {
                    Produit produit = produitServiceClient.findProduitById(entry.getKey().intValue());
                    Map<String, Object> productDetails = new HashMap<>();
                    productDetails.put("produitID", entry.getKey());
                    productDetails.put("nom", produit != null ? produit.getName() : "Produit inconnu");
                    productDetails.put("quantite", entry.getValue());
                    return productDetails;
                })
                .collect(Collectors.toList());
    }

    public void deleteFacture(Long factureId) {
        // Vérifiez si la facture existe
        Facture facture = factureRepository.findById(factureId)
                .orElseThrow(() -> new RuntimeException("Facture non trouvée avec l'ID : " + factureId));

        // Supprimez les lignes de facture associées
        factureLigneRepository.deleteAll(facture.getFacturelignes());

        // Supprimez la facture
        factureRepository.delete(facture);
    }

   
    
    
    public Map<String, List<Facture>> getFacturesByStatus() {
        List<Facture> factures = factureRepository.findAll();

        // Séparer les factures réglées et non réglées
        List<Facture> facturesReglees = factures.stream()
                .filter(facture -> "Payée".equalsIgnoreCase(facture.getStatut()))
                .collect(Collectors.toList());

        List<Facture> facturesNonReglees = factures.stream()
                .filter(facture -> !"Payée".equalsIgnoreCase(facture.getStatut()))
                .collect(Collectors.toList());

        Map<String, List<Facture>> result = new HashMap<>();
        result.put("facturesReglees", facturesReglees);
        result.put("facturesNonReglees", facturesNonReglees);

        return result;
    }
    
    
    
    public List<Map<String, Object>> getDettesParClient() {
        // Récupérer toutes les factures
        List<Facture> factures = factureRepository.findAll();

        // Filtrer les factures non payées
        List<Facture> facturesNonPayees = factures.stream()
                .filter(facture -> !facture.getStatut().equalsIgnoreCase("Payée"))
                .collect(Collectors.toList());

        // Calculer la dette totale pour chaque client
        Map<Long, Double> dettesParClient = new HashMap<>();
        for (Facture facture : facturesNonPayees) {
            double totalFacture = facture.getFacturelignes().stream()
                    .mapToDouble(ligne -> ligne.getPrice().doubleValue() * ligne.getQuantity())
                    .sum();
            dettesParClient.merge(facture.getClientID(), totalFacture, Double::sum);
        }

        // Créer une liste détaillée pour chaque client
        return dettesParClient.entrySet()
                .stream()
                .map(entry -> {
                    Client client = clientServiceClient.findClientById(entry.getKey());
                    Map<String, Object> detailsClient = new HashMap<>();
                    detailsClient.put("clientID", entry.getKey());
                    detailsClient.put("nom", client != null ? client.getName() : "Client inconnu");
                    detailsClient.put("email", client != null ? client.getEmail() : "Email inconnu");
                    detailsClient.put("dette", entry.getValue());
                    return detailsClient;
                })
                .collect(Collectors.toList());
    }

    public Facture updateFacture(Long factureId, Facture updatedFacture) {
        // Vérifiez si la facture existe
        Facture existingFacture = factureRepository.findById(factureId)
                .orElseThrow(() -> new RuntimeException("Facture non trouvée avec l'ID : " + factureId));

        // Mettre à jour les champs de la facture
        existingFacture.setDateFacture(updatedFacture.getDateFacture());
        existingFacture.setStatut(updatedFacture.getStatut());

        // Mettre à jour le client si nécessaire
        if (existingFacture.getClientID() != updatedFacture.getClientID()) {
            Client client = clientServiceClient.findClientById(updatedFacture.getClientID());
            if (client == null) {
                throw new RuntimeException("Client non trouvé pour l'ID : " + updatedFacture.getClientID());
            }
            existingFacture.setClientID(client.getId());
            existingFacture.setClient(client);
        }

        // Mettre à jour les lignes de facture
        // Supprimer les anciennes lignes
        factureLigneRepository.deleteAll(existingFacture.getFacturelignes());
        existingFacture.getFacturelignes().clear();

        // Ajouter les nouvelles lignes
        for (FactureLigne ligne : updatedFacture.getFacturelignes()) {
            Produit produit = produitServiceClient.findProduitById(ligne.getProduitID());
            if (produit == null) {
                throw new RuntimeException("Produit non trouvé pour l'ID : " + ligne.getProduitID());
            }
            ligne.setProduit(produit);
            ligne.updatePrice(produit); // Mettre à jour le prix depuis le produit
            ligne.setFacture(existingFacture);
            existingFacture.getFacturelignes().add(ligne);
        }

        // Enregistrer la facture mise à jour
        return factureRepository.save(existingFacture);
    }
}
