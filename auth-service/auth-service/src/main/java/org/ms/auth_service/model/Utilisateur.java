package org.ms.auth_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.Data;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE_UTILISATEUR")
@DiscriminatorValue("Utilisateur")
@Data
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String code;

    @Column(name = "TYPE_UTILISATEUR", insertable = false, updatable = false)
    private String typeUtilisateur;

    @Column(length = 50)
    private String nom;

    @Column(length = 50)
    private String prenom;

    @Column(unique = true, length = 50)
    private String login;

    @Column(length = 10)
    private String password;

}