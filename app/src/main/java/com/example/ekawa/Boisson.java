package com.example.ekawa;

/**
 * @file Boisson.java
 * @brief Déclaration de la classe Boisson
 * @author LECOMTE Jean-Luc
 * $LastChangedRevision: 101 $
 * $LastChangedDate: 2021-05-28 11:43:56 +0200 (ven. 28 mai 2021) $
 */

/**
 * @class Boisson
 * @brief Définit les caractéristiques des boissons d'EKAWA
 */
public class Boisson
{
    private String nom;                 //!< Le nom de la boisson
    private Integer identifiantImage;   //!< L'identifiant de l'image de la boisson

    /**
     * @brief Constructeur de la classe Boisson
     * @fn Boisson::Boisson(String nom, Integer identifiantImage)
     * @param nom le nom de la boisson
     * @param identifiantImage l'identifiant de l'image de la boisson
     */
    public Boisson(String nom, Integer identifiantImage)
    {
        this.nom = nom;
        this.identifiantImage = identifiantImage;
    }

    /**
     * @brief Méthode qui permet d'obtenir le nom de la boisson
     * @fn Boisson::obtenirNom()
     * @return String le nom de la boisson
     */
    public String obtenirNom()
    {
        return this.nom;
    }

    /**
     * @brief Méthode qui permet d'obtenir l'identifiant de l'image de la boisson
     * @fn Boisson::obtenirImage()
     * @return Integer l'identifiant de l'image de la boisson
     */
    public Integer obtenirImage()
    {
        return this.identifiantImage;
    }
}
