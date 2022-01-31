package com.example.ekawa;

/**
 * @file Programmation.java
 * @brief Déclaration de la classe Programmation
 * @author LECOMTE Jean-Luc
 * $LastChangedRevision: 101 $
 * $LastChangedDate: 2021-05-28 11:43:56 +0200 (ven. 28 mai 2021) $
 */

/**
 * @class Programmation
 * @brief Définit les caractéristiques des programmations EKAWA
 */
public class Programmation
{
    /**
     * Constantes
     */
    private static final String TAG = "Programmation";              //!< TAG pour les logs

    public final static int MIN_PROGRAMMATION = 0;                  //!< Le nombre minimum de programmation
    public final static int MAX_PROGRAMMATION = 4;                  //!< Le nombre maximum de programmation

    public final static String PROGRAMMATION = "programmation";     //!< L'identifiant qui permet l'enregistrement du nom de la programmation
    public final static String CAPSULE = "capsule";                 //!< L'identifiant qui permet l'enregistrement du nom de la capsule de la programmation
    public final static String BOISSON = "boisson";                 //!< L'identifiant qui permet l'enregistrement du nom de la boisson de la programmation
    public final static String JOUR = "jour";                       //!< L'identifiant qui permet l'enregistrement du jour de la programmation
    public final static String HEURE = "heure";                     //!< L'identifiant qui permet l'enregistrement de l'heure de la programmation
    public final static String FREQUENCE = "frequence";             //!< L'identifiant qui permet l'enregistrement de la fréquence de la programmation
    public final static String IDENTIFIANT = "identifiant";         //!< L'identifiant qui permet l'enregistrement de l'identifiant de la programmation

    public final static boolean MODE_CREATION = false;              //!< Le mode création de la programmation
    public final static boolean MODE_MODIFICATION = true;           //!< Le mode modification de la programmation

    /**
     * @class Jours
     * @brief Définit les caractéristiques des jours
     */
    public static class Jours
    {
        public final static int LUNDI = 0;      //!< La valeur associée au jour : Lundi
        public final static int MARDI = 1;      //!< La valeur associée au jour : Mardi
        public final static int MERCREDI = 2;   //!< La valeur associée au jour : Mercredi
        public final static int JEUDI = 3;      //!< La valeur associée au jour : Jeudi
        public final static int VENDREDI = 4;   //!< La valeur associée au jour : Vendredi
        public final static int SAMEDI = 5;     //!< La valeur associée au jour : Samedi
        public final static int DIMANCHE = 6;   //!< La valeur associée au jour : Dimanche

        public final static String[] JOURS =
        {
            "Lundi",
            "Mardi",
            "Mercredi",
            "Jeudi",
            "Vendredi",
            "Samedi",
            "Dimanche"
        };  //!< Le nom de chaques jour
    }

    /**
     * @class Frequences
     * @brief Définit les caractéristiques des fréquences
     */
    public static class Frequences
    {
        public final static int UNE_SEULE_FOIS = 0;         //!< La valeur associée à la fréquence : Une seule fois
        public final static int TOUS_LES_JOURS = 1;         //!< La valeur associée à la fréquence : Tous les jours
        public final static int TOUTES_LES_SEMAINES = 2;    //!< La valeur associée à la fréquence : Tous les semaines
        public final static int TOUS_LES_MOIS = 3;          //!< La valeur associée à la fréquence : Tous les mois

        public final static String[] FREQUENCES =
        {
            "Une seule fois",
            "Tous les jours",
            "Toutes les semaines",
            "Tous les mois"
        };  //!< Le nom de chaques fréquences
    }

    private int capsule;        //!< Le nom de la capsule de la programmation
    private int boisson;        //!< Le nom de la boisson de la programmation
    private int jour;           //!< Le jour de la programmation
    private String heure;       //!< L'heure de la programmation
    private int frequence;      //!< La fréquence de la programmation
    private int identifiant;    //!< L'identifiant de la programmation

    /**
     * @brief Constructeur de la classe Programmation
     * @fn Programmation::Programmation(int capsule, int boisson, int jour, String heure, int frequence)
     * @param capsule la capsule demandée
     * @param boisson la boisson demandée
     * @param jour le jour demandé
     * @param heure l'heure demandée
     * @param frequence la fréquence demandée
     */
    public Programmation(int capsule, int boisson, int jour, String heure, int frequence)
    {
        this.capsule = capsule;
        this.boisson = boisson;
        this.jour = jour;
        this.heure = heure;
        this.frequence = frequence;
        this.identifiant = 0;
    }

    /**
     * @brief Constructeur de la classe Programmation
     * @fn Programmation::Programmation(int capsule, int boisson, int jour, String heure, int frequence, int identifiant)
     * @param capsule la capsule demandée
     * @param boisson la boisson demandée
     * @param jour le jour demandé
     * @param heure l'heure demandée
     * @param frequence la fréquence demandée
     * @param identifiant l'identifiant de la programmation
     */
    public Programmation(int capsule, int boisson, int jour, String heure, int frequence, int identifiant)
    {
        this.capsule = capsule;
        this.boisson = boisson;
        this.jour = jour;
        this.heure = heure;
        this.frequence = frequence;
        this.identifiant = identifiant;
    }

    /**
     * @brief Méthode qui renvoie la capsule de la programmation
     * @fn Programmation::obtenirCapsule()
     * @return int la capsule de la programmation
     */
    public int obtenirCapsule()
    {
        return capsule;
    }

    /**
     * @brief Méthode qui renvoie la boisson de la programmation
     * @fn Programmation::obtenirBoisson()
     * @return int la boisson de la programmation
     */
    public int obtenirBoisson()
    {
        return boisson;
    }

    /**
     * @brief Méthode qui renvoie le jour de la programmation
     * @fn Programmation::obtenirJour()
     * @return int le jour de la programmation
     */
    public int obtenirJour()
    {
        return jour;
    }

    /**
     * @brief Méthode qui renvoie l'heure de la programmation
     * @fn Programmation::obtenirHeure()
     * @return int l'heure de la programmation
     */
    public String obtenirHeure()
    {
        return heure;
    }

    /**
     * @brief Méthode qui renvoie la fréquence de la programmation
     * @fn Programmation::obtenirFrequence()
     * @return int la fréquence de la programmation
     */
    public int obtenirFrequence()
    {
        return frequence;
    }

    /**
     * @brief Méthode qui renvoie l'identifiant de la programmation
     * @fn Programmation::obtenirIdentifiant()
     * @return int l'identifiant de la programmation
     */
    public int obtenirIdentifiant()
    {
        return identifiant;
    }

    /**
     * @brief Méthode qui permet de changer l'identifiant de la programmation
     * @fn Programmation::changerIdentifiant(int identifiant)
     * @param identifiant l'identifiant de la programmation
     */
    public void changerIdentifiant(int identifiant)
    {
        this.identifiant = identifiant;
    }
}
