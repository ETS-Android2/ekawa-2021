package com.example.ekawa;

import android.util.Log;

/**
 * @file Protocole.java
 * @brief Déclaration de la classe Protocole
 * @author LECOMTE Jean-Luc
 * $LastChangedRevision: 101 $
 * $LastChangedDate: 2021-05-28 11:43:56 +0200 (ven. 28 mai 2021) $
 */

/**
 * @class Protocole
 * @brief Définit les caractéristiques du protocole EKAWA
 */
public class Protocole
{
    /**
     * Constantes
     */
    private static final String TAG = "Protocole";                      //!< TAG pour les logs

    // Délimiteurs
    public final static String DEBUT_TRAME = "$ekawa;";                 //!< Le début de la trame
    public final static String FIN_TRAME = "\r\n";                      //!< La fin de la trame

    // Les types de trame
    public final static String TEST_ALIVE = "A";                        //!< La trame de test de communication
    public final static String INDICATEUR_PREPARATION_CAFE = "C";       //!< La trame de préparation d'un café
    public final static String ERREUR_TRAME = "E";                      //!< La trame d'erreur
    public final static String REINITIALISER = "R";                     //!< La trame de réinitialisation
    public final static String CREER_PROGRAMMATION = "P";               //!< La trame de création d'un programmation
    public final static String MODIFIER_PROGRAMMATION = "M";            //!< La trame de modification d'un programmation
    public final static String SUPPRIMER_PROGRAMMATION = "S";           //!< La trame de suppression d'un programmation
    public final static String ACTUALISATION_CAFETIERE = "S1";          //!< La trame d'actualisation de la cafetière
    public final static String ACTUALISATION_MAGASIN = "S2";            //!< La trame d'actualisation du magasin
    public final static String ACTUALISATION_COMPLEMENTAIRE = "S3";     //!< La trame d'actualisation des données complémentaires
    public final static String ACTUALISATION_PROGRAMMATION = "S4";      //!< La trame d'actualisation des programmations
    public final static String CONFIGURATION = "?";                     //!< La trame des informations sur la configuration

    // Partie Cafetière
    public final static int EMPLACEMENT_CAFETIERE = 0;                  //!< L'emplacement de la cafetière dans la trame
    public final static String CAFETIERE_OCUPPEE = "0";                 //!< La cafetière est occupée
    public final static String CAFETIERE_REPOS = "1";                   //!< La cafetière est au repos

    // Partie Tasse
    public final static int EMPLACEMENT_TASSE = 1;                      //!< L'emplacement de la tasse dans la trame
    public final static String TASSE_ABSENTE = "0";                     //!< La tasse est absente
    public final static String TASSE_PRESENTE = "1";                    //!< La tasse est présente

    // Partie Bac
    public final static int EMPLACEMENT_BAC = 2;                        //!< L'emplacement du bac dans la trame
    public final static String BAC_PLEIN = "0";                         //!< Le bac est plein
    public final static String BAC_VIDE = "1";                          //!< Le bac est vide

    // Partie Eau
    public final static int EMPLACEMENT_EAU = 3;                        //!< L'emplacement du réservoir d'eau dans la trame
    public static final Integer NIVEAU_EAU_MIN = 0;                     //!< Le niveau d'eau min
    public static final Integer NIVEAU_EAU_MAX = 100;                   //!< Le niveau d'eau max

    // Partie Capsules
    public static final String CAPSULE_ABSENTE = "0";                   //!< La capsule est absente
    public static final String CAPSULE_PRESENTE = "1";                  //!< La capsule est présente

    // Partie Infomations complémentaires
    public final static int EMPLACEMENT_NB_CAFE = 0;                    //!< L'emplacement du nombre de café éffectuée au total dans la trame
    public final static int EMPLACEMENT_NB_BAC_VIDE = 1;                //!< L'emplacement du nombre de bac vidé au total dans la trame
    public final static int EMPLACEMENT_NB_EAU_REMPLIE = 2;             //!< L'emplacement du nombre de reservoir d'eau remplie au total dans la trame
    public final static int EMPLACEMENT_DURETEE_EAU = 3;                //!< L'emplacement de la dureté de l'eau dans la trame
    public final static int EMPLACEMENT_QUALITE_EAU = 4;                //!< L'emplacement de la qualité de l'eau dans la trame

    // Réinitialiser
    public static final boolean REINITIALISER_NB_CAFE = true;           //!< La valeur de la réinitialisation du nombre de café éffectuée au total
    public static final boolean REINITIALISER_NB_BAC_VIDE = true;       //!< La valeur de la réinitialisation du nombre de bac vidé au total
    public static final boolean REINITIALISER_NB_EAU_REMPLIE = true;    //!< La valeur de la réinitialisation du nombre de reservoir d'eau remplie au total
    public static final int REINITIALISER_DURETEE_EAU = 0;              //!< La valeur de la réinitialisation de la dureté de l'eau
    public static final int REINITIALISER_QUALITE_EAU = 0;              //!< La valeur de la réinitialisation de la qualité de l'eau
    public static final boolean REINITIALISER_PROGRAMMATIONS = true;    //!< La valeur de la réinitialisation des programmations
    public static final boolean REINITIALISER_SIMULATEUR = true;        //!< La valeur de la réinitialisation du simulateur

    // Vérification
    public static final int EMPLACEMENT_VERIFICATION = 0;                   //!< L'emplacement de la vérification dans la trame
    public static final int EMPLACEMENT_VERIFICATION_PROGRAMMATION = 1;     //!< L'emplacement de la vérification d'un programmation dans la trame
    public static final int VERIFICATION_INVALIDE = 0;                      //!< La valeur de la vérification invalide
    public static final int VERIFICATION_VALIDE = 1;                        //!< La valeur de la vérification valide

    // Retours
    public static final int EMPLACEMENT_RETOUR_PREPARATION_CAFE = 0;        //!< L'emplacement du retour de la préparation d'un café dans la trame
    public static final char RETOUR_INVALIDE = '0';                         //!< La valeur du retour invalide de la préparation d'un café
    public static final char RETOUR_VALIDE = '1';                           //!< La valeur du retour valide de la préparation d'un café

    // Programmation
    public static final int EMPLACEMENT_IDENTIFIANT = 0;                    //!< L'emplacement de l'identifiant d'une programmation dans la trame
    public static final int ERREUR_IDENTIFIANT = 0;                         //!< La valeur de l'identifiant d'une programmation

    // Erreur trame
    public static final int EMPLACEMENT_ERREUR = 0;                         //!< L'emplacement de l'erreur dans la trame
    public static final int AUCUNE_ERREUR = 0;                              //!< La valeur d'aucune erreur
    public static final int ERREUR_ENTETE = 1;                              //!< La valeur de l'erreur de l'entête
    public static final int ERREUR_NB_PARAMETRES = 2;                       //!< La valeur de l'erreur du nombre de paramètres
    public static final int ERREUR_TRAME_INCONNUE = 3;                      //!< La valeur de l'erreur d'une trame d'inconnue
    public static final int ERREUR_TYPE_CAFE = 4;                           //!< La valeur de l'erreur du type de café
    public static final int ERREUR_LONGUEUR_CAFE = 5;                       //!< La valeur de l'erreur de la longueur du café

    // Informations sur la configuration
    public static final int EMPLACEMENT_NTP = 0;                            //!< L'emplacement du NTP
    public static final int NTP_INDISPONIBLE = 0;                           //!< La valeur du NTP indisponible
    public static final int NTP_DISPONIBLE = 1;                             //!< La valeur du NTP disponible
    public static final int EMPLACEMENT_VERSION = 6;                        //!< L'emplacement de la version du simulateur

    /**
     * @brief Méthode qui permet de créer une trame pour lancer la préparation d'un café
     * @fn Protocole::fabriquerTramePreparationCafe(int boissonActuelle, int capsuleActuelle)
     * @param boissonActuelle la boissons actuelle
     * @param capsuleActuelle la capsule actuelle
     * @return String la trame fabriquée
     */
    public static String fabriquerTramePreparationCafe(int boissonActuelle, int capsuleActuelle)
    {
        String trame = Protocole.DEBUT_TRAME;
        trame += Protocole.INDICATEUR_PREPARATION_CAFE + ";";
        trame += boissonActuelle + ";";
        trame += capsuleActuelle;
        trame += Protocole.FIN_TRAME;

        Log.d(TAG, "Fabriquer trame = " + trame);

        return trame;
    }

    /**
     * @brief Méthode qui permet de créer une trame pour tester la connection avec la machine
     * @fn Protocole::fabriquerTrameTestAlive()
     * @return String la trame fabriquée
     */
    public static String fabriquerTrameTestAlive()
    {
        Log.d(TAG, "fabriquerTrameTestAlive()");
        return Protocole.DEBUT_TRAME + Protocole.TEST_ALIVE + Protocole.FIN_TRAME;
    }

    /**
     * @brief Méthode qui permet de créer une trame pour réinitialiser les données complémentaires
     * @fn Protocole::fabriquerTrameReinitialiser(boolean nbCafe, boolean nbBacVide, boolean nbEauRemplie, int dureteeEau, int qualiteEau, boolean programmations, boolean simulateur)
     * @param nbCafe le nombre de café
     * @param nbBacVide le nombre de bac vidé
     * @param nbEauRemplie le nombre de réservoir d'eau remplie
     * @param dureteeEau la dureté de l'eau
     * @param qualiteEau la qualité de l'eau
     * @param programmations les programmations
     * @param simulateur le simulateur
     * @return String la trame fabriquée
     */
    public static String fabriquerTrameReinitialiser(boolean nbCafe, boolean nbBacVide, boolean nbEauRemplie, int dureteeEau, int qualiteEau, boolean programmations, boolean simulateur)
    {
        Log.d(TAG, "fabriquerTrameReinitialiser()");
        String trame = Protocole.DEBUT_TRAME;
        trame += Protocole.REINITIALISER + ";";
        trame += nbCafe ? 1 + ";" : 0 + ";";
        trame += nbBacVide ? 1 + ";" : 0 + ";";
        trame += nbEauRemplie ? 1 + ";" : 0 + ";";
        trame += dureteeEau + ";";
        trame += qualiteEau + ";";
        trame += programmations ? 1 + ";" : 0 + ";";
        trame += simulateur ? 1 : 0;
        trame += Protocole.FIN_TRAME;
        return trame;
    }

    /**
     * @brief Méthode qui permet de créer une trame pour actualiser les données de la cafetière
     * @fn Protocole::fabriquerTrameActualisationCafetiere()
     * @return String la trame fabriquée
     */
    public static String fabriquerTrameActualisationCafetiere()
    {
        Log.d(TAG, "fabriquerTrameActualisationCafetiere()");
        return Protocole.DEBUT_TRAME + Protocole.ACTUALISATION_CAFETIERE + Protocole.FIN_TRAME;
    }

    /**
     * @brief Méthode qui permet de créer une trame pour actualiser les données du magasin
     * @fn Protocole::fabriquerTrameActualisationMagasin()
     * @return String la trame fabriquée
     */
    public static String fabriquerTrameActualisationMagasin()
    {
        Log.d(TAG, "fabriquerTrameActualisationMagasin()");
        return Protocole.DEBUT_TRAME + Protocole.ACTUALISATION_MAGASIN + Protocole.FIN_TRAME;
    }

    /**
     * @brief Méthode qui permet de créer une trame pour actualiser les données complémentaires
     * @fn Protocole::fabriquerTrameActualisationComplementaires()
     * @return String la trame fabriquée
     */
    public static String fabriquerTrameActualisationComplementaires()
    {
        Log.d(TAG, "fabriquerTrameActualisationComplementaires()");
        return Protocole.DEBUT_TRAME + Protocole.ACTUALISATION_COMPLEMENTAIRE + Protocole.FIN_TRAME;
    }

    /**
     * @brief Méthode qui permet de créer une trame pour créer une programmation
     * @fn Protocole::fabriquerTrameCreerProgrammation(int capsule, int boisson, int jour, String heure, int frequence)
     * @param capsule la capsule de la programmation
     * @param boisson la boisson de la programmation
     * @param jour le jour de la semaine de la programmation
     * @param heure l'heure de la programmation
     * @param frequence la fréquence de la programmation
     * @return String la trame fabriquée de la programmation
     */
    public static String fabriquerTrameCreerProgrammation(int capsule, int boisson, int jour, String heure, int frequence)
    {
        Log.d(TAG, "fabriquerTrameCreerProgrammation()");
        String trame = Protocole.DEBUT_TRAME;
        trame += Protocole.CREER_PROGRAMMATION + ";";
        trame += capsule + ";";
        trame += boisson + ";";
        trame += jour + ";";
        trame += heure + ";";
        trame += frequence;
        trame += Protocole.FIN_TRAME;
        return trame;
    }

    /**
     * @brief Méthode qui permet de créer une trame pour modifier une programmation
     * @fn Protocole::fabriquerTrameModifierProgrammation(int identifiant, int capsule, int boisson, int jour, String heure, int frequence)
     * @param identifiant l'identifiant de la programmation
     * @param capsule la capsule de la programmation
     * @param boisson la boisson de la programmation
     * @param jour le jour de la semaine de la programmation
     * @param heure l'heure de la programmation
     * @param frequence la fréquence de la programmation
     * @return String la trame fabriquée de la programmation
     */
    public static String fabriquerTrameModifierProgrammation(int identifiant, int capsule, int boisson, int jour, String heure, int frequence)
    {
        Log.d(TAG, "fabriquerTrameModifierProgrammation()");
        String trame = Protocole.DEBUT_TRAME;
        trame += Protocole.MODIFIER_PROGRAMMATION + ";";
        trame += identifiant + ";";
        trame += capsule + ";";
        trame += boisson + ";";
        trame += jour + ";";
        trame += heure + ";";
        trame += frequence;
        trame += Protocole.FIN_TRAME;
        return trame;
    }

    /**
     * @brief Méthode qui permet de créer une trame pour supprimer une programmation
     * @fn Protocole::fabriquerTrameSupprimerProgrammation(int identifiant)
     * @param identifiant l'identifiant l'identifiant de la programmation
     * @return String la trame fabriquée
     */
    public static String fabriquerTrameSupprimerProgrammation(int identifiant)
    {
        Log.d(TAG, "fabriquerTrameCreerProgrammation()");
        String trame = Protocole.DEBUT_TRAME;
        trame += Protocole.SUPPRIMER_PROGRAMMATION + ";";
        trame += identifiant;
        trame += Protocole.FIN_TRAME;
        return trame;
    }

    /**
     * @brief Méthode qui permet de créer une trame pour actualiser la configuration de la cafetière
     * @fn Protocole::fabriquerTrameActualiserConfiguration()
     * @return String la trame fabriquée
     */
    public static String fabriquerTrameActualiserConfiguration()
    {
        Log.d(TAG, "fabriquerTrameCreerProgrammation()");
        String trame = Protocole.DEBUT_TRAME;
        trame += Protocole.CONFIGURATION;
        trame += Protocole.FIN_TRAME;
        return trame;
    }

    /**
     * @brief Méthode qui permet de découper une trame
     * @fn Protocole::decouperTrame(String trame)
     * @param trame la trame à découper
     * @return String[] les données de la trame
     */
    private static String[] decouperTrame(String trame)
    {
        Log.d(TAG, "decouperTrame()");
        if(trame.startsWith(Protocole.ACTUALISATION_CAFETIERE))
            trame = trame.replace(Protocole.ACTUALISATION_CAFETIERE + ';', "");
        else if(trame.startsWith(Protocole.ACTUALISATION_MAGASIN))
            trame = trame.replace(Protocole.ACTUALISATION_MAGASIN + ';', "");
        else if(trame.startsWith(Protocole.ACTUALISATION_COMPLEMENTAIRE))
            trame = trame.replace(Protocole.ACTUALISATION_COMPLEMENTAIRE + ";", "");
        else if(trame.startsWith(Protocole.ACTUALISATION_PROGRAMMATION))
            trame = trame.replace(Protocole.ACTUALISATION_PROGRAMMATION + ";", "");
        else if(trame.startsWith(Protocole.REINITIALISER))
            trame = trame.replace(Protocole.REINITIALISER + ";", "");
        else if(trame.startsWith(Protocole.CREER_PROGRAMMATION))
            trame = trame.replace(Protocole.CREER_PROGRAMMATION + ";", "");
        else if(trame.startsWith(Protocole.SUPPRIMER_PROGRAMMATION))
            trame = trame.replace(Protocole.SUPPRIMER_PROGRAMMATION + ";", "");
        else if(trame.startsWith(Protocole.MODIFIER_PROGRAMMATION))
            trame = trame.replace(Protocole.MODIFIER_PROGRAMMATION + ";", "");
        else if (trame.startsWith(Protocole.ERREUR_TRAME))
            trame = trame.replace(Protocole.ERREUR_TRAME + ";", "");
        else if (trame.startsWith(Protocole.CONFIGURATION))
            trame = trame.replace(Protocole.CONFIGURATION + ";", "");
        return trame.split(";");
    }

    /**
     * @brief Méthode qui permet d'extraire la valeur de la cafetière
     * @fn Protocole::extraireValeurCafetiere(String trame)
     * @param trame la trame reçue
     * @return boolean la valeur de la cafetière
     */
    public static boolean extraireValeurCafetiere(String trame)
    {
        String[] valeurs = decouperTrame(trame);

        boolean etatCafetiere = false;
        if(valeurs[Protocole.EMPLACEMENT_CAFETIERE].equals(Protocole.CAFETIERE_REPOS))
            etatCafetiere = true;
        else if(valeurs[Protocole.EMPLACEMENT_CAFETIERE].equals(Protocole.CAFETIERE_OCUPPEE))
            etatCafetiere = false;
        else
            Log.d(TAG, "Erreur extraire etatCafetiere ! " + valeurs[Protocole.EMPLACEMENT_CAFETIERE]);

        Log.d(TAG, "Extraire etatCafetiere = " + etatCafetiere);

        return etatCafetiere;
    }

    /**
     * @brief Méthode qui permet d'extraire la valeur de la tasse
     * @fn Protocole::extraireValeurTasse(String trame)
     * @param trame la trame reçue
     * @return boolean la valeur de la tasse
     */
    public static boolean extraireValeurTasse(String trame)
    {
        String[] valeurs = decouperTrame(trame);

        boolean etatTasse = false;
        if(valeurs[Protocole.EMPLACEMENT_TASSE].equals(Protocole.TASSE_PRESENTE))
            etatTasse = true;
        else if(valeurs[Protocole.EMPLACEMENT_TASSE].equals(Protocole.TASSE_ABSENTE))
            etatTasse = false;
        else
            Log.d(TAG, "Erreur extraire etatTasse ! " + valeurs[Protocole.EMPLACEMENT_TASSE]);

        Log.d(TAG, "Extraire etatTasse = " + etatTasse);

        return etatTasse;
    }

    /**
     * @brief Méthode qui permet d'extraire la valeur du bac
     * @fn Protocole::extraireValeurBac(String trame)
     * @param trame la trame reçue
     * @return boolean la valeur du bac
     */
    public static boolean extraireValeurBac(String trame)
    {
        String[] valeurs = decouperTrame(trame);

        boolean etatBac = false;
        if(valeurs[Protocole.EMPLACEMENT_BAC].equals(Protocole.BAC_VIDE))
            etatBac = true;
        else if(valeurs[Protocole.EMPLACEMENT_BAC].equals(Protocole.BAC_PLEIN))
            etatBac = false;
        else
            Log.d(TAG, "Erreur extraire etatBac ! " + valeurs[Protocole.EMPLACEMENT_BAC]);

        Log.d(TAG, "Extraire etatBac = " + etatBac);

        return etatBac;
    }

    /**
     * @brief Méthode qui permet d'extraire le niveau d'eau
     * @fn Protocole::extraireValeurEau(String trame)
     * @param trame la trame reçue
     * @return boolean le niveau d'eau
     */
    public static int extraireValeurEau(String trame)
    {
        String[] valeurs = decouperTrame(trame);

        int niveauEau = 0;
        if(Integer.parseInt(valeurs[Protocole.EMPLACEMENT_EAU]) >= Protocole.NIVEAU_EAU_MIN && Integer.parseInt(valeurs[3]) <= Protocole.NIVEAU_EAU_MAX)
            niveauEau = Integer.parseInt(valeurs[Protocole.EMPLACEMENT_EAU]);
        else
            Log.d(TAG, "Erreur extraire niveauEau ! " + valeurs[Protocole.EMPLACEMENT_EAU]);

        Log.d(TAG, "Extraire niveauEau = " + niveauEau);

        return niveauEau;
    }

    /**
     * @brief Méthode qui permet les états des capsules
     * @fn Protocole::extraireValeursCapsules(String trame)
     * @param trame la trame reçue
     * @return boolean les états des capsules
     */
    public static boolean[] extraireValeursCapsules(String trame)
    {
        String[] valeurs = decouperTrame(trame);

        boolean[] etatsCapsules = new boolean[Cafetiere.NOMBRE_CAPSULE_MAX];
        for(int i = 0; i < Cafetiere.NOMBRE_CAPSULE_MAX; ++i)
        {
            if(valeurs[i].equals(CAPSULE_ABSENTE))
                etatsCapsules[i] = false;
            else if(valeurs[i].equals(CAPSULE_PRESENTE))
                etatsCapsules[i] = true;
            else
                Log.d(TAG, "Erreur extraire état capsule n°" + i + " ! " + valeurs[i]);
            Log.d(TAG, "Extraire état capsule n°" + i + " = " + etatsCapsules[i]);
        }

        return etatsCapsules;
    }

    /**
     * @brief Méthode qui permet d'extraire le nombre de cafe effectué au total
     * @fn Protocole::extraireNombreCafeTotal(String trame)
     * @param trame la trame reçue
     * @return int le nombre de cafe effectué au total
     */
    public static int extraireNombreCafeTotal(String trame)
    {
        String[] valeurs = decouperTrame(trame);

        int nombreCafeTotal = 0;
        nombreCafeTotal = Integer.parseInt(valeurs[Protocole.EMPLACEMENT_NB_CAFE]);

        Log.d(TAG, "Extraire nombreCafeTotal = " + nombreCafeTotal);

        return nombreCafeTotal;
    }

    /**
     * @brief Méthode qui permet d'extraire le nombre de bac vidé
     * @fn Protocole::extraireNombreBacVide(String trame)
     * @param trame la trame reçue
     * @return int le nombre de bac vidé
     */
    public static int extraireNombreBacVide(String trame)
    {
        String[] valeurs = decouperTrame(trame);

        int nombreBacVide = 0;
        nombreBacVide = Integer.parseInt(valeurs[Protocole.EMPLACEMENT_NB_BAC_VIDE]);

        Log.d(TAG, "Extraire nombreBacVide = " + nombreBacVide);

        return nombreBacVide;
    }

    /**
     * @brief Méthode qui permet d'extraire le nombre d'eau remplie
     * @fn Protocole::extraireNombreEauRemplie(String trame)
     * @param trame la trame reçue
     * @return int le nombre d'eau remplie
     */
    public static int extraireNombreEauRemplie(String trame)
    {
        String[] valeurs = decouperTrame(trame);

        int nombreEauRemplie = 0;
        nombreEauRemplie = Integer.parseInt(valeurs[Protocole.EMPLACEMENT_NB_EAU_REMPLIE]);

        Log.d(TAG, "Extraire nombreEauRemplie = " + nombreEauRemplie);

        return nombreEauRemplie;
    }

    /**
     * @brief Méthode qui permet d'extraire la dureté de l'eau
     * @fn Protocole::extraireDureteeEau(String trame)
     * @param trame la trame reçue
     * @return int la dureté de l'eau
     */
    public static int extraireDureteeEau(String trame)
    {
        String[] valeurs = decouperTrame(trame);

        int dureteeEau = 0;
        dureteeEau = Integer.parseInt(valeurs[Protocole.EMPLACEMENT_DURETEE_EAU]);

        Log.d(TAG, "Extraire dureteeEau = " + dureteeEau);

        return dureteeEau;
    }

    /**
     * @brief Méthode qui permet d'extraire la qualité de l'eau
     * @fn Protocole::extraireQualiteEau(String trame)
     * @param trame la trame reçue
     * @return int la qualité de l'eau
     */
    public static int extraireQualiteEau(String trame)
    {
        String[] valeurs = decouperTrame(trame);

        int qualiteEau = 0;
        qualiteEau = Integer.parseInt(valeurs[Protocole.EMPLACEMENT_QUALITE_EAU]);

        Log.d(TAG, "Extraire qualiteEau = " + qualiteEau);

        return qualiteEau;
    }

    /**
     * @brief Méthode qui permet d'extraire l'identifiant
     * @fn Protocole::extraireIdentifiant(String trame)
     * @param trame la trame reçue
     * @return int l'identifiant
     */
    public static int extraireIdentifiant(String trame)
    {
        String[] valeurs = decouperTrame(trame);

        int identifiant = 0;
        identifiant = Integer.parseInt(valeurs[Protocole.EMPLACEMENT_IDENTIFIANT]);

        Log.d(TAG, "Extraire identifiant = " + identifiant);

        return identifiant;
    }

    /**
     * @brief Méthode qui permet d'extraire la vérification
     * @fn Protocole::extraireVerification(String trame)
     * @param trame la trame reçue
     * @return boolean la vérification
     */
    public static boolean extraireVerification(String trame)
    {
        int position = Protocole.EMPLACEMENT_VERIFICATION;
        if(trame.startsWith(Protocole.ACTUALISATION_PROGRAMMATION))
            position = Protocole.EMPLACEMENT_VERIFICATION_PROGRAMMATION;

        String[] valeurs = decouperTrame(trame);

        int verification = Integer.parseInt(valeurs[position]);

        Log.d(TAG, "Extraire vérification = " + verification);

        if(verification == Protocole.VERIFICATION_VALIDE)
            return true;
        return false;
    }

    /**
     * @brief Méthode qui permet d'extraire l'erreur
     * @fn Protocole::extraireErreur(String trame)
     * @param trame la trame reçue
     * @return int l'erreur
     */
    public static int extraireErreur(String trame)
    {
        String[] valeurs = decouperTrame(trame);

        int erreur = Integer.parseInt(valeurs[EMPLACEMENT_ERREUR]);

        Log.d(TAG, "Extraire erreur = " + erreur);

        return erreur;
    }

    /**
     * @brief Méthode qui permet d'extraire le NTP
     * @fn Protocole::extraireNTP(String trame)
     * @param trame la trame reçue
     * @return le NTP
     */
    public static boolean extraireNTP(String trame)
    {
        String[] valeurs = decouperTrame(trame);

        if(Integer.parseInt(valeurs[EMPLACEMENT_NTP]) == NTP_DISPONIBLE)
            return true;
        return false;
    }

    /**
     * @brief Méthode qui permet d'extraire la version de la cafetière
     * @fn Protocole::extraireVersionCafetire(String trame)
     * @param trame la trame reçue
     * @return la version de la cafetière
     */
    public static String extraireVersionCafetire(String trame)
    {
        String[] valeurs = decouperTrame(trame);

        return valeurs[EMPLACEMENT_VERSION];
    }
}