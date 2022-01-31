package com.example.ekawa;

import android.content.SharedPreferences;

import android.util.Log;

import java.text.DateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @file Preference.java
 * @brief Déclaration de la classe Preference
 * @author LECOMTE Jean-Luc
 * $LastChangedRevision: 101 $
 * $LastChangedDate: 2021-05-28 11:43:56 +0200 (ven. 28 mai 2021) $
 */

/**
 * @class Preference
 * @brief Définit les caractéristiques des préfences de l'utilisateur d'EKAWA
 */
public class Preference
{
    /**
     * Constantes
     */
    private static final String TAG = "Preference";                     //!< TAG pour les logs

    // Noms des préférences de l'utilisateur
    public final static String CAPSULE = "capsulePreferee";             //!< La capsule dans les préférences
    public final static String BOISSON = "boissonPreferee";             //!< La boisson dans les préférences
    public final static String DERNIERE_DATE = "derniereDate";          //!< La dernière date dans les préférences
    public final static String NB_CAFE_DU_JOUR = "nombreCafeDuJour";    //!< Le nombre de café du jour dans les préférences

    private SharedPreferences donneesUtilisateurs;                      //!< Les préférences utilisateur

    /**
     * @brief Constructeur de la classe Preference
     * @fn Preference::Preference(Ihm ihm)
     */
    public Preference(Ihm ihm)
    {
        Log.d(TAG, "Preference()");
        donneesUtilisateurs = ihm.getPreferences(ihm.MODE_PRIVATE);
    }

    /**
     * @brief Méthode qui permet d'obtenir une préférence
     * @fn Preference::obtenir(String nomPreference)
     * @param nomPreference le nom de la préférence
     * @return Object la valeur de la préférence
     */
    public Object obtenir(String nomPreference)
    {
        Log.d(TAG, "obtenir() nomPreference = " + nomPreference);
        Map<String,?> donnees = donneesUtilisateurs.getAll();
        Log.d(TAG, "obtenir() donnees = " + donnees);
        if(contient(nomPreference))
            return donnees.get(nomPreference);
        else
            return "";
    }

    /**
     * @brief Méthode qui retourne le nombre de café du jour
     * @fn Preference::obtenirNombreCafeDuJour(int nombreCafeDuJour)
     * @param nombreCafeDuJour le nombre de café du jour
     * @return int le nombre de café du jour
     */
    public int obtenirNombreCafeDuJour(int nombreCafeDuJour)
    {
        String date = DateFormat.getDateInstance().format(new Date());
        if(!contient(Preference.DERNIERE_DATE))
            editer(Preference.DERNIERE_DATE, date);
        if(!contient(Preference.NB_CAFE_DU_JOUR))
            editer(Preference.NB_CAFE_DU_JOUR, nombreCafeDuJour);

        if(date.equals((String) obtenir(Preference.DERNIERE_DATE)))
            nombreCafeDuJour = (int) obtenir(Preference.NB_CAFE_DU_JOUR);
        else
        {
            editer(Preference.NB_CAFE_DU_JOUR, nombreCafeDuJour);
            editer(Preference.DERNIERE_DATE, date);
            nombreCafeDuJour = 0;
        }
        return nombreCafeDuJour;
    }

    /**
     * @brief Méthode qui permet de vérifier la présence d'une préférence
     * @fn Preference::contient(String nomPreference)
     * @param nomPreference le nom de la préférence
     * @return boolean la présance de la préférence
     */
    public boolean contient(String nomPreference)
    {
        Log.d(TAG, "contient()");
        if(donneesUtilisateurs.contains(nomPreference))
            return true;
        return false;
    }

    /**
     * @brief Méthode qui permet de créer ou modifier une préférence
     * @fn Preference::editer(String nomPreference, String valeur)
     * @param nomPreference le nom de la préférence
     * @param valeur la valeur de la préférence
     */
    public void editer(String nomPreference, String valeur)
    {
        Log.d(TAG, "editer(String)");
        donneesUtilisateurs.edit().putString(nomPreference, valeur).apply();
    }

    /**
     * @brief Méthode qui permet de créer ou modifier une préférence
     * @fn Preference::editer(String nomPreference, int valeur)
     * @param nomPreference le nom de la préférence
     * @param valeur la valeur de la préférence
     */
    public void editer(String nomPreference, int valeur)
    {
        Log.d(TAG, "editer(int)");
        donneesUtilisateurs.edit().putInt(nomPreference, valeur).apply();
    }

    /**
     * @brief Méthode qui permet de créer ou modifier une préférence
     * @fn Preference::editer(String nomPreference, boolean valeur)
     * @param nomPreference le nom de la préférence
     * @param valeur la valeur de la préférence
     */
    public void editer(String nomPreference, boolean valeur)
    {
        Log.d(TAG, "editer(boolean)");
        donneesUtilisateurs.edit().putBoolean(nomPreference, valeur).apply();
    }

    /**
     * @brief Méthode qui permet de supprimer une préférence
     * @fn Preference::supprimer(String nomPreference)
     * @param nomPreference le nom de la préférence
     */
    public void supprimer(String nomPreference)
    {
        Log.d(TAG, "supprimer()");
        donneesUtilisateurs.edit().remove(nomPreference).apply();
    }

    /**
     * @brief Méthode qui permet d’obtenir le nombre de programmation
     * @fn Preference::obtenirNbProgrammations()
     * @return int le nombre de programmations
     */
    public int obtenirNbProgrammations()
    {
        int i = 0;
        while(contient(Programmation.PROGRAMMATION + i))
        {
            ++i;
        }
        return i;
    }

    /**
     * @brief Méthode qui permet de supprimer toutes les programmations
     * @fn Preference::supprimerLesProgrammations()
     */
    public void supprimerLesProgrammations()
    {
        Log.d(TAG, "supprimerLesProgrammations()");
        int id = 0;
        while(contient(Programmation.PROGRAMMATION + id))
        {
            String idProgrammation = Programmation.PROGRAMMATION + id;
            supprimer(idProgrammation);
            ++id;
        }
    }

    /**
     * @brief Méthode qui permet de réorganiser les programmations
     * @fn Preference::reorganiserLesProgrammations(int position)
     * @param position la position de la programmation
     */
    public void reorganiserLesProgrammations(int position)
    {
        Log.d(TAG, "reorganiserLesProgrammations()");
        if(position >= Programmation.MIN_PROGRAMMATION && position < Programmation.MAX_PROGRAMMATION)
        {
            int anciennePosition = position + 1;
            String idAncienneProgrammation = Programmation.PROGRAMMATION + anciennePosition;
            String idNouvelleProgrammation = Programmation.PROGRAMMATION + position;
            if(contient(idAncienneProgrammation))
            {
                editer(idNouvelleProgrammation, (int) obtenir(idAncienneProgrammation));
                editer(idNouvelleProgrammation + "_" + Programmation.CAPSULE, (int) obtenir(idAncienneProgrammation + "_" + Programmation.CAPSULE));
                editer(idNouvelleProgrammation + "_" + Programmation.BOISSON, (int) obtenir(idAncienneProgrammation + "_" + Programmation.BOISSON));
                editer(idNouvelleProgrammation + "_" + Programmation.JOUR, (int) obtenir(idAncienneProgrammation + "_" + Programmation.JOUR));
                editer(idNouvelleProgrammation + "_" + Programmation.HEURE, (String) obtenir(idAncienneProgrammation + "_" + Programmation.HEURE));
                editer(idNouvelleProgrammation + "_" + Programmation.FREQUENCE, (int) obtenir(idAncienneProgrammation + "_" + Programmation.FREQUENCE));
                editer(idNouvelleProgrammation + "_" + Programmation.IDENTIFIANT, (int) obtenir(idAncienneProgrammation + "_" + Programmation.IDENTIFIANT));

                supprimer(idAncienneProgrammation);
            }
            reorganiserLesProgrammations(anciennePosition);
        }
    }
}