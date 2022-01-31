package com.example.ekawa;

/**
 * @file Capsule.java
 * @brief Déclaration de la classe Capsule
 * @author LECOMTE Jean-Luc
 * $LastChangedRevision: 101 $
 * $LastChangedDate: 2021-05-28 11:43:56 +0200 (ven. 28 mai 2021) $
 */

/**
 * @class Capsule
 * @brief Définit les caractéristiques des capsules d'EKAWA
 */

public class Capsule
{
    private String nom;
    private Integer identifiantImage;
    private boolean presence;

    /**
     * @brief Constructeur de la classe Capsule
     * @fn Capsule::Capsule(String nom, Integer identifiantImage, boolean presence)
     * @param nom le nom de la capsule
     * @param identifiantImage l'identifiant de l'image de la capsule
     * @param presence la présence de la capsule
     */
    public Capsule(String nom, Integer identifiantImage, boolean presence)
    {
        this.nom = nom;
        this.identifiantImage = identifiantImage;
        this.presence = presence;
    }

    /**
     * @brief Méthode qui permet d'obtenir le nom de la capsule
     * @fn Capsule::obtenirNom()
     * @return String le nom de la capsule
     */
    public String obtenirNom()
    {
        return this.nom;
    }

    /**
     * @brief Méthode qui permet d'obtenir l'identifiant de l'image de la capsule
     * @fn Capsule::obtenirImage()
     * @return Integer l'identifiant de l'image de la capsule
     */
    public Integer obtenirImage()
    {
        return this.identifiantImage;
    }

    /**
     * @brief Méthode qui permet d'obtenir la présence de la capsule
     * @fn Capsule::obtenirPresence()
     * @return boolean la présence de la capsule
     */
    public boolean obtenirPresence()
    {
        return this.presence;
    }

    /**
     * @brief Méthode qui permet de changer la présence de la capsule
     * @fn Capsule::changerPresence(boolean presence)
     * @param presence la présence de la capsule
     */
    public void changerPresence(boolean presence)
    {
        this.presence = presence;
    }
}
