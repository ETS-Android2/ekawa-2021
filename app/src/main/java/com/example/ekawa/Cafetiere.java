package com.example.ekawa;

import android.util.Log;

import java.util.ArrayList;

/**
 * @file Cafetiere.java
 * @brief Déclaration de la classe Cafetiere
 * @author LECOMTE Jean-Luc
 * $LastChangedRevision: 111 $
 * $LastChangedDate: 2021-06-10 08:32:18 +0200 (jeu. 10 juin 2021) $
 */

/**
 * @class Cafetiere
 * @brief Déclaration de la classe principale de l'application
 */
public class Cafetiere
{
    /**
     * Constantes
     */
    private static final String TAG = "Cafetiere";          //!< TAG pour les logs
    public static final int AUCUNE_CAPSULE = -1;            //!< L'indicateur qu'il n'y a aucune capsule sélectionnée
    public static final Integer NOMBRE_CAPSULE_MAX = 8;     //!< Le nombre maximal de capsules
    public static final Integer NOMBRE_BOISSON_MAX = 2;     //!< Le nombre maximal de types de boisson (court/long)
    public static final int NB_MAX_CAFE_CONSEILLEE = 4;     //!< Le nombre de café conseillé par jour
    private static final int BOISSON_COURT = 0;             //!< L'emplacement de la boisson courte
    private static final int BOISSON_LONG = 1;              //!< L'emplacement de la boisson longue
    private static final int TEMPS_BOISSON_COURT = 6000;    //!< Le temps de la préparation de la boisson courte
    private static final int TEMPS_BOISSON_LONG = 12000;    //!< Le temps de la préparation de la boisson longue


    /**
     * Attributs
     */
    private String[] nomsCapusles = {
            "Colombia",
            "Indonesia",
            "Ethiopia",
            "Volluto",
            "Capriccio",
            "Cosi",
            "Scuro",
            "Vanilla Eclair"
    }; //!< Exemples de noms de capsule

    private Integer[] idImagesCapsules =
            {
                    R.mipmap.ic_capsule_colombia,
                    R.mipmap.ic_capsule_indonesia,
                    R.mipmap.ic_capsule_ethiopia,
                    R.mipmap.ic_capsule_volluto,
                    R.mipmap.ic_capsule_capriccio,
                    R.mipmap.ic_capsule_cosi,
                    R.mipmap.ic_capsule_scuro,
                    R.mipmap.ic_capsule_vanilla_eclair
            }; //!< Images des exemples de capsule

    private boolean[] presencesCapsules = { true, true, true, true, true, true, true, true };   //!< Les présences de base des capsules

    private String[] nomsBoissons = { "Court", "Long" };                                        //!< Les noms de base des boissons

    private Integer[] idImagesBoissons = { R.drawable.ic_cafe_court, R.drawable.ic_cafe_long }; //!< Les images de base des boissons

    private Integer[] descriptionCapsules = {
            R.string.colombia,
            R.string.indonesia,
            R.string.ethiopia,
            R.string.volluto,
            R.string.capriccio,
            R.string.cosi,
            R.string.scuro,
            R.string.vanilla_eclair
    }; //!< Les descriptions de base des boissons

    private Ihm ihm;                                    //!< La partie UI
    private Communication communication = null;         //!< La partie communication
    private Preference preference = null;               //!< Les préférences utilisateur


    private ArrayList<Capsule> capsules;                //!< La liste des capsules
    private ArrayList<Boisson> boissons;                //!< La liste des boissons
    private int capsuleActuelle = AUCUNE_CAPSULE;       //!< La capsule sélectionnée
    private int boissonActuelle = 0;                    //!< La boisson sélectionnée

    private boolean etatCafetiere = false;              //!< L'état de la cafetière
    private boolean etatTasse = false;                  //!< L'état de la tasse
    private boolean etatBac = false;                    //!< L'état du bac
    private int niveauEau = 0;                          //!< L'état du niveau d'eau


    private int nombreCafeDuJour = 0;                   //!< Le nombre de café effectué par l'utilisateur sur le même jour
    private int nombreCafeTotal = 0;                    //!< Le nombre de café effectué au total par la machine
    private int nombreBacVide = 0;                      //!< Le nombre de bac vidé au total
    private int nombreEauRemplie = 0;                   //!< Le nombre de eau remplie au total
    private int dureteeEau = 0;                         //!< La duretée de l'eau
    private int qualiteEau = 0;                        //!< La qualitée de l'eau

    private ArrayList<Programmation> programmations;    //!< La liste des programmations
    private Programmation derniereProgrammation;        //!< La dernière programmation (tampon)
    private int dernierePositionProgrammation;          //!< La position de la dernière programmation (tampon)

    private boolean ntp = false;                        //!< L'état du NTP
    private String versionCafetiere = "Inconnue";       //!< La version de la cafetière

    /**
     * @brief Constructeur de la classe Cafetière
     * @fn Cafetiere::Cafetiere(Ihm ihm)
     * @param ihm l'ihm
     */
    public Cafetiere(Ihm ihm)
    {
        Log.d(TAG, "Cafetiere()");
        this.ihm = ihm;
        initialiserPreference();
        initialiserCapsules();
        initialiserBoissons();
        initialiserCommunication();
        initaliserProgrammation();
    }

    /**
     * @brief Méthode qui permet d'initialiser les préférences de l'utilisateur
     * @fn Cafetiere::initialiserPreference()
     */
    private void initialiserPreference()
    {
        preference = new Preference(ihm);
        String donnee = preference.obtenir(Preference.CAPSULE).toString();
        if(!donnee.isEmpty())
            capsuleActuelle = Integer.valueOf(donnee);
        else
            capsuleActuelle = 0; // première capsule
        donnee = preference.obtenir(Preference.BOISSON).toString();
        if(!donnee.isEmpty()) // café court
            boissonActuelle = Integer.valueOf(donnee);
        else
            boissonActuelle = BOISSON_COURT;
        nombreCafeDuJour = preference.obtenirNombreCafeDuJour(nombreCafeDuJour);
        Log.d(TAG, "initialiserPreference() capsuleActuelle = " + capsuleActuelle);
        Log.d(TAG, "initialiserPreference() boissonActuelle = " + boissonActuelle);
        Log.d(TAG, "initialiserPreference() nombreCafeDuJour = " + nombreCafeDuJour);
    }

    /**
     * @brief Méthode qui permet d'initialiser les programmations de l'utilisateur
     * @fn Cafetiere::initaliserProgrammation()
     */
    private void initaliserProgrammation()
    {
        Log.d(TAG, "initaliserProgrammation()");
        programmations = new ArrayList<Programmation>();
        initialiserProgrammations();
    }

    /**
     * @brief Méthode qui permet d'initialiser la communication
     * @fn Cafetiere::initialiserCommunication()
     */
    private void initialiserCommunication()
    {
        Log.d(TAG, "initialiserCommunication()");
        communication = new Communication(ihm, this);
    }

    /**
     * @brief Méthode qui permet d'initialiser la liste des capsules
     * @fn Cafetiere::initialiserCapsules()
     */
    private void initialiserCapsules()
    {
        Log.d(TAG, "initialiserCapsules()");
        capsules = new ArrayList<>();
        for(int i = 0; i < NOMBRE_CAPSULE_MAX; ++i)
            capsules.add(new Capsule(nomsCapusles[i], idImagesCapsules[i], presencesCapsules[i]));
    }

    /**
     * @brief Méthode qui permet d'initialiser la liste des boissons
     * @fn Cafetiere::initialiserBoissons()
     */
    private void initialiserBoissons()
    {
        Log.d(TAG, "initialiserBoissons()");
        boissons = new ArrayList<>();
        for(int i = 0; i < NOMBRE_BOISSON_MAX; ++i)
            boissons.add(new Boisson(nomsBoissons[i], idImagesBoissons[i]));
    }

    /**
     * @brief Méthode qui renvoie la liste des capsules
     * @fn Cafetiere::listerCapsules()
     * @return ArrayList<Capsule> la liste des capsules
     */
    public ArrayList<Capsule> listerCapsules()
    {
        Log.d(TAG, "listerCapsules()");
        return capsules;
    }

    /**
     * @brief Méthode qui renvoie la liste des boissons
     * @fn Cafetiere::listerBoissons()
     * @return ArrayList<Boisson> la liste des boissons
     */
    public ArrayList<Boisson> listerBoissons()
    {
        Log.d(TAG, "listerBoissons()");
        return boissons;
    }

    /**
     * @brief Méthode qui renvoie la description d'une capsule
     * @fn Cafetiere::obtenirDescriptionCapsule(int position)
     * @param position position de la capsule
     * @return Integer la description de la capsule
     */
    public Integer obtenirDescriptionCapsule(int position)
    {
        Log.d(TAG, "obtenirDescriptionCapsule()");
        return descriptionCapsules[position];
    }

    /**
     * @brief Méthode qui modifie la capsule actuelle
     * @fn Cafetiere::changerCapsuleActuelle(int capsule)
     * @param capsule Le numéro de capsule
     */
    public void changerCapsuleActuelle(int capsule)
    {
        Log.d(TAG, "changerCapsuleActuelle()");
        this.capsuleActuelle = capsule;
        preference.editer(Preference.CAPSULE, capsuleActuelle);
        ihm.actualiserIndicateurs();
    }

    /**
     * @brief Méthode qui modifie la boisson actuelle
     * @fn Cafetiere::changerBoissonActuelle(int boisson)
     * @param boisson Le numéro de boisson
     */
    public void changerBoissonActuelle(int boisson)
    {
        Log.d(TAG, "changerBoissonActuelle()");
        this.boissonActuelle = boisson;
        preference.editer(Preference.BOISSON, boissonActuelle);
    }

    /**
     * @brief Méthode qui renvoie la capsule actuelle
     * @fn Cafetiere::informerCapsuleActuelle()
     * @return int capsule actuelle
     */
    public int informerCapsuleActuelle()
    {
        Log.d(TAG, "informerCapsuleActuelle()");
        return capsuleActuelle;
    }

    /**
     * @brief Méthode qui renvoie la boisson actuelle
     * @fn Cafetiere::informerBoissonActuelle()
     * @return int boisson actuelle
     */
    public int informerBoissonActuelle()
    {
        Log.d(TAG, "informerBoissonActuelle()");
        return boissonActuelle;
    }

    /**
     * @brief Méthode qui renvoie si la cafetière est utilisable ou non
     * @fn Cafetiere::informerEtatCafetiere()
     * @return boolean l'état de la cafetière
     */
    public boolean informerEtatCafetiere()
    {
        Log.d(TAG, "informerEtatCafetiere()");
        return etatCafetiere;
    }

    /**
     * @brief Méthode qui renvoie si le bluetooth est activé ou non
     * @fn Cafetiere::informerEtatBluetooth()
     * @return boolean l'état du bluetooth
     */
    public boolean informerEtatBluetooth()
    {
        Log.d(TAG, "informerEtatBluetooth()");
        return communication.estActivee();
    }

    /**
     * @brief Méthode qui renvoie si le bluetooth est connecté ou non
     * @fn Cafetiere::informerConnexionBluetooth()
     * @return boolean l'état de la connexion bluetooth
     */
    public boolean informerConnexionBluetooth()
    {
        Log.d(TAG, "informerConnexionBluetooth()");
        return communication.estConnectee();
    }

    /**
     * @brief Méthode qui renvoie si la tasse est bien placée ou non
     * @fn Cafetiere::informerEtatTasse()
     * @return boolean l'état de la tasse
     */
    public boolean informerEtatTasse()
    {
        Log.d(TAG, "informerEtatTasse()");
        return etatTasse;
    }

    /**
     * @brief Méthode qui renvoie si le bac est plein ou non
     * @fn Cafetiere::informerEtatBac()
     * @return boolean l'état du bac
     */
    public boolean informerEtatBac()
    {
        Log.d(TAG, "informerEtatBac()");
        return etatBac;
    }

    /**
     * @brief Méthode qui renvoie le niveau d'eau
     * @fn Cafetiere::informerNiveauEau()
     * @return int niveau d'eau
     */
    public int informerNiveauEau()
    {
        Log.d(TAG, "informerNiveauEau()");
        return niveauEau;
    }

    /**
     * @brief Méthode qui renvoie le niveau d'eau
     * @fn Cafetiere::informerPresenceCapsule(int position)
     * @param position position de la capsule
     * @return boolean la présence capsule en fonction du paramètre position
     */
    public boolean informerPresenceCapsule(int position)
    {
        Log.d(TAG, "informerPresenceCapsule()");
        return capsules.get(position).obtenirPresence();
    }

    /**
     * @brief Méthode qui renvoie le nombre de cafe du jour
     * @fn Cafetiere::informerNombreCafeDuJour()
     * @return int le nombre de cafe du jour
     */
    public int informerNombreCafeDuJour()
    {
        Log.d(TAG, "informerNombreCafeDuJour()");
        return nombreCafeDuJour;
    }

    /**
     * @brief Méthode qui renvoie le nom de la cafetière connectée
     * @fn Cafetiere::informerNomCafetiere()
     * @return String le nom de la cafetière connectée
     */
    public String informerNomCafetiere()
    {
        Log.d(TAG, "informerNomCafetiere()");
        if(communication.estConnectee())
            return communication.obtenirNomPeripherique();
        return Communication.NOM_CAFETIERE_NON_CONNECTEE;
    }

    /**
     * @brief Méthode qui renvoie le nombre de café total
     * @fn Cafetiere::informerNombreCafeTotal()
     * @return int le nombre de café total
     */
    public int informerNombreCafeTotal()
    {
        Log.d(TAG, "informerNombreCafeTotal()");
        return nombreCafeTotal;
    }

    /**
     * @brief Méthode qui renvoie le nombre de bac vidée
     * @fn Cafetiere::informerNombreBacVide()
     * @return int le nombre de bac vidée
     */
    public int informerNombreBacVide()
    {
        Log.d(TAG, "informerNombreBacVide()");
        return nombreBacVide;
    }

    /**
     * @brief Méthode qui renvoie le nombre d'eau remplie
     * @fn Cafetiere::informerNombreEauRemplie()
     * @return int le nombre d'eau remplie
     */
    public int informerNombreEauRemplie()
    {
        Log.d(TAG, "informerNombreEauRemplie()");
        return nombreEauRemplie;
    }

    /**
     * @brief Méthode qui renvoie la duretee de l'eau
     * @fn Cafetiere::informerDureteeEau()
     * @return int la duretee de l'eau
     */
    public int informerDureteeEau()
    {
        Log.d(TAG, "informerDureteeEau()");
        return dureteeEau;
    }

    /**
     * @brief Méthode qui renvoie la qualité de l'eau
     * @fn Cafetiere::informerQualiteEau()
     * @return int la qualité de l'eau
     */
    public int informerQualiteEau()
    {
        Log.d(TAG, "informerQualiteEau()");
        return qualiteEau;
    }

    /**
     * @brief Méthode qui renvoie l'état du NTP
     * @fn Cafetiere::informerNTP()
     * @return boolean l'état du NTP
     */
    public boolean informerNTP()
    {
        Log.d(TAG, "informerNTP()");
        return ntp;
    }

    /**
     * @brief Méthode qui renvoie la version de la cafetière
     * @fn Cafetiere::informerVersionCafetiere()
     * @return String l'état du NTP
     */
    public String informerVersionCafetiere()
    {
        Log.d(TAG, "informerVersionCafetiere()");
        return versionCafetiere;
    }

    /**
     * @brief Méthode qui renvoie si la cafetière est prête à l'emploie
     * @fn Cafetiere::estPrete()
     * @return boolean si la cafetière est prête
     */
    public boolean estPrete()
    {
        if(informerEtatBluetooth() && informerConnexionBluetooth() && informerCapsuleActuelle() != Cafetiere.AUCUNE_CAPSULE && informerEtatCafetiere() && informerEtatTasse() && informerEtatBac() && informerNiveauEau() != 0)
            return true;
        else
            return false;
    }

    /**
     * @brief Méthode qui permet d'allumer le bluetooth
     * @fn Cafetiere::allumer()
     */
    public void allumer()
    {
        Log.d(TAG,"allumer()");
        if(communication != null)
            communication.activer();
    }

    /**
     * @brief Méthode qui permet d'éteindre le bluetooth
     * @fn Cafetiere::eteindre()
     */
    public void eteindre()
    {
        Log.d(TAG,"eteindre()");
        if(communication != null)
            communication.desactiver();
        etatCafetiere = false;
        etatTasse = false;
        etatBac = false;
        niveauEau = 0;
    }

    /**
     * @brief Méthode qui permet de connecter le bluetooth à la cafetière
     * @fn Cafetiere::connecter()
     */
    public void connecter()
    {
        Log.d(TAG,"connecter()");
        if(communication != null)
            communication.connecter();
    }

    /**
     * @brief Méthode qui permet de déconnecter le bluetooth de la cafetière
     * @fn Cafetiere::deconnecter()
     */
    public void deconnecter()
    {
        Log.d(TAG,"deconnecter()");
        if(communication != null)
            communication.deconnecter();
        etatCafetiere = false;
        etatTasse = false;
        etatBac = false;
        niveauEau = 0;
    }

    /**
     * @brief Méthode qui permet d'actualiser les états de la cafetière, la tasse, le bac et le niveau d'eau + les présences des capsules
     * @fn Cafetiere::changerEtats(String trame)
     * @param trame Trame reçue
     */
    public void changerEtats(String trame)
    {
        Log.d(TAG, "changerEtats()");
        trame = trame.replace(Protocole.DEBUT_TRAME, "");

        if(trame != null)
        {
            if (trame.startsWith(Protocole.TEST_ALIVE))
                actualiserDonnees();
            else if (trame.startsWith(Protocole.INDICATEUR_PREPARATION_CAFE))
                verifierPreparationCafe(trame);
            else if (trame.startsWith(Protocole.ACTUALISATION_CAFETIERE))
                actualiserCafetiere(trame);
            else if (trame.startsWith(Protocole.ACTUALISATION_MAGASIN))
                actualiserMagasin(trame);
            else if (trame.startsWith(Protocole.ACTUALISATION_COMPLEMENTAIRE) || trame.startsWith(Protocole.REINITIALISER))
                actualiserInformationsComplementaires(trame);
            else if (trame.startsWith(Protocole.ACTUALISATION_PROGRAMMATION))
            {
                if (verifierProgrammation(trame))
                    lancerPreparationCafe();
            }
            else if (trame.startsWith(Protocole.CREER_PROGRAMMATION))
                creerUneProgrammation(trame);
            else if (trame.startsWith(Protocole.SUPPRIMER_PROGRAMMATION))
                verifierSuppressionDuneProgrammation(trame);
            else if (trame.startsWith(Protocole.MODIFIER_PROGRAMMATION))
                verifierModifierUneProgrammation(trame);
            else if (trame.startsWith(Protocole.ERREUR_TRAME))
                informerErreur(trame);
            else if (trame.startsWith(Protocole.CONFIGURATION))
                actualiserConfiguration(trame);
        }
    }

    /**
     * @brief Méthode qui permet d'envoyer les trames d'actualisations à la cafetière
     * @fn Cafetiere::actualiserDonnees()
     */
    public void actualiserDonnees()
    {
        communication.envoyerTrame(Protocole.fabriquerTrameActualisationCafetiere());
        communication.envoyerTrame(Protocole.fabriquerTrameActualisationMagasin());
        communication.envoyerTrame(Protocole.fabriquerTrameActualisationComplementaires());
        communication.envoyerTrame(Protocole.fabriquerTrameActualiserConfiguration());
    }

    /**
     * @brief Méthode qui permet de lancer la préparation d'un café
     * @fn Cafetiere::demanderPreparationCafe()
     */
    public void demanderPreparationCafe()
    {
        if(capsuleActuelle != AUCUNE_CAPSULE)
        {
            String trame = Protocole.fabriquerTramePreparationCafe(boissonActuelle, capsuleActuelle);
            communication.envoyerTrame(trame);
            Log.d(TAG, "demanderPreparationCafe() : trame " + trame);
        }
        else
        {
            ihm.afficherMessage("Veuillez sélectionner une capsule");
        }
        if(!capsules.get(capsuleActuelle).obtenirPresence())
        {
            ihm.afficherMessage("Il n'y a plus cette capsule dans le magasin");
        }
    }

    /**
     * @brief Méthode qui permet de vérifier la trame de réponse d'une demande de préparation d'un café
     * @fn Cafetiere::verifierPreparationCafe(String trame)
     * @param trame Trame reçue
     */
    private void verifierPreparationCafe(String trame)
    {
        trame = trame.replace(Protocole.INDICATEUR_PREPARATION_CAFE + ';', "");

        if(trame.charAt(Protocole.EMPLACEMENT_RETOUR_PREPARATION_CAFE) == Protocole.RETOUR_INVALIDE)
        {
            Log.d(TAG, "Erreur préparataion café");
            ihm.afficherMessage("Impossible de préparer le café");
        }
        else
        {
            lancerPreparationCafe();
        }
    }

    /**
     * @brief Méthode qui permet de lancer la préparation d'un café
     * @fn Cafetiere::lancerPreparationCafe()
     */
    private void lancerPreparationCafe()
    {
        Log.d(TAG, "lancerPreparationCafe()");
        ihm.afficherMessage("Votre café est en cours de préparation");
        ++nombreCafeDuJour;
        ++nombreCafeTotal;
        etatCafetiere = false;
        if(boissonActuelle == BOISSON_COURT)
            ihm.afficherMessageAvecRetard("Votre café est prêt", TEMPS_BOISSON_COURT);
        if(boissonActuelle == BOISSON_LONG)
            ihm.afficherMessageAvecRetard("Votre café est prêt", TEMPS_BOISSON_LONG);
        preference.editer(Preference.NB_CAFE_DU_JOUR, nombreCafeDuJour);
        ihm.actualiserIndicateurs();
        ihm.actualiserPageInformations();
    }

    /**
     * @brief Méthode qui permet d'actualiser les états de la cafetière, la tasse, le bac et le niveau d'eau
     * @fn Cafetiere::actualiserCafetiere(String trame)
     * @param trame Trame reçue
     */
    private void actualiserCafetiere(String trame)
    {
        Log.d(TAG, "actualiserCafetiere()");

        boolean etatCafetiere = Protocole.extraireValeurCafetiere(trame);
        boolean etatTasse = Protocole.extraireValeurTasse(trame);
        boolean etatBac = Protocole.extraireValeurBac(trame);
        int niveauEau = Protocole.extraireValeurEau(trame);

        changerEtatsCafetiere(etatCafetiere, etatTasse, etatBac, niveauEau);

        ihm.actualiserIndicateurs();
    }

    /**
     * @brief Méthode qui permet d'actualiser les présences des capsules
     * @fn Cafetiere::actualiserMagasin(String trame)
     * @param trame Trame reçue
     */
    private void actualiserMagasin(String trame)
    {
        Log.d(TAG, "actualiserMagasin()");
        presencesCapsules = Protocole.extraireValeursCapsules(trame);
        for(int i = 0; i < NOMBRE_CAPSULE_MAX; ++i)
            capsules.get(i).changerPresence(presencesCapsules[i]);
        ihm.actualiserSelection();
    }

    /**
     * @brief Méthode qui permet d'actualiser et d'initialiser les informations complémentaires
     * @fn Cafetiere::actualiserInformationsComplementaires(String trame)
     * @param trame Trame reçue
     */
    private void actualiserInformationsComplementaires(String trame)
    {
        Log.d(TAG, "actualiserInformationsComplementaires()");

        if(trame.startsWith(Protocole.ACTUALISATION_COMPLEMENTAIRE))
        {
            nombreCafeTotal = Protocole.extraireNombreCafeTotal(trame);
            nombreBacVide = Protocole.extraireNombreBacVide(trame);
            nombreEauRemplie = Protocole.extraireNombreEauRemplie(trame);
            dureteeEau = Protocole.extraireDureteeEau(trame);
            qualiteEau = Protocole.extraireQualiteEau(trame);
        }
        else if(trame.startsWith(Protocole.REINITIALISER))
        {
            if(Protocole.extraireVerification(trame))
            {
                nombreCafeTotal = 0;
                nombreBacVide = 0;
                nombreEauRemplie = 0;
                dureteeEau = 0;
                qualiteEau = 0;
                supprimerLesProgrammations();
                ihm.afficherMessage("Données réinitialisées");
            }
        }
        ihm.actualiserPageInformations();
    }

    /**
     * @brief Méthode qui permet de vérifier si la programmation s'est bien lancer
     * @fn Cafetiere::verifierProgrammation(String trame)
     * @param trame Trame reçue
     * @return boolean la vérification
     */
    private boolean verifierProgrammation(String trame)
    {
        Log.d(TAG, "verifierProgrammation()");
        int identifiant = Protocole.extraireIdentifiant(trame);
        boolean valide = false;
        for(Programmation programmation:programmations)
        {
            if(programmation.obtenirIdentifiant() == identifiant)
            {
                valide = true;
                dernierePositionProgrammation = programmations.indexOf(programmation);
                break;
            }
        }
        if(valide && Protocole.extraireVerification(trame))
        {
            ihm.envoyerNotification(programmations.get(dernierePositionProgrammation));
            if(programmations.get(dernierePositionProgrammation).obtenirFrequence() == Programmation.Frequences.UNE_SEULE_FOIS)
                supprimerUneProgrammation();
            return true;
        }
        ihm.afficherMessage("Erreur lors du lancement de votre programmation");
        return false;
    }

    /**
     * @brief Méthode qui permet d'actualiser les états de la cafetière, la tasse, le bac et le niveau d'eau
     * @fn Cafetiere::changerEtatsCafetiere(boolean etatCafetiere, boolean etatTasse, boolean etatBac, int niveauEau)
     * @param etatCafetiere État de la cafetière
     * @param etatTasse État de la tasse
     * @param etatBac État du bac
     * @param niveauEau Niveau de l'eau
     */
    private void changerEtatsCafetiere(boolean etatCafetiere, boolean etatTasse, boolean etatBac, int niveauEau)
    {
        Log.d(TAG, "changerEtatsCafetiere()");
        if(communication.estActivee() && communication.estConnectee())
        {
            this.etatCafetiere = etatCafetiere;
            this.etatTasse = etatTasse;
            this.etatBac = etatBac;
            this.niveauEau = niveauEau;
        }
        else
        {
            this.etatCafetiere = false;
            this.etatTasse = false;
            this.etatBac = false;
            this.niveauEau = 0;
        }
    }

    /**
     * @brief Méthode qui permet de remettre les arguments à zéro
     * @fn Cafetiere::remettreAZero()
     */
    public void remettreAZero()
    {
        Log.d(TAG, "remettreAZero()");
        capsuleActuelle = AUCUNE_CAPSULE;
        boissonActuelle = 0;
        etatCafetiere = false;
        etatTasse = false;
        etatBac = false;
        niveauEau = 0;
        ihm.actualiserIndicateurs();
        ihm.actualiserSelection();
    }

    /**
     * @brief Méthode qui permet d'actualiser l'IHM
     * @fn Cafetiere::actualiserIHM()
     */
    public void actualiserIHM()
    {
        Log.d(TAG, "actualiserIHM()");
        ihm.actualiserIndicateurs();
    }

    /**
     * @brief Méthode qui permet de réinitialiser les informations complémentaires
     * @fn Cafetiere::modifierInformationsComplementaires(boolean nbCafe, boolean nbBacVide, boolean nbEauRemplie, int dureteeEau, int qualiteEau, boolean programmations, boolean simulateur)
     * @param nbCafe nombre de café
     * @param nbBacVide nombre de bac vidé
     * @param nbEauRemplie nombre de réservoir d'eau remplie
     * @param dureteeEau duretée de l'eau
     * @param qualiteEau qualité de l'eau
     * @param programmations états des programmations
     * @param simulateur états du simulateur
     */
    public void modifierInformationsComplementaires(boolean nbCafe, boolean nbBacVide, boolean nbEauRemplie, int dureteeEau, int qualiteEau, boolean programmations, boolean simulateur)
    {
        Log.d(TAG, "modifierInformationsComplementaires()");

        communication.envoyerTrame(Protocole.fabriquerTrameReinitialiser(
                nbCafe ? Protocole.REINITIALISER_NB_CAFE : !Protocole.REINITIALISER_NB_CAFE,
                nbBacVide ? Protocole.REINITIALISER_NB_BAC_VIDE : !Protocole.REINITIALISER_NB_BAC_VIDE,
                nbEauRemplie ? Protocole.REINITIALISER_NB_EAU_REMPLIE : !Protocole.REINITIALISER_NB_EAU_REMPLIE,
                dureteeEau,
                qualiteEau,
                programmations ? Protocole.REINITIALISER_PROGRAMMATIONS : !Protocole.REINITIALISER_PROGRAMMATIONS,
                simulateur ? Protocole.REINITIALISER_SIMULATEUR : !Protocole.REINITIALISER_SIMULATEUR
        ));
    }

    /**
     * @brief Méthode qui initialiser les programmations au lancement de l'application
     * @fn Cafetiere::initialiserProgrammations()
     */
    private void initialiserProgrammations()
    {
        Log.d(TAG, "initialiserProgrammations()");
        for (int i = 0; i < Programmation.MAX_PROGRAMMATION; ++i)
        {
            String idNouvelleProgrammation = Programmation.PROGRAMMATION + i;
            if(preference.contient(idNouvelleProgrammation))
            {
                programmations.add(new Programmation(
                        (int) preference.obtenir(idNouvelleProgrammation + "_" + Programmation.CAPSULE),
                        (int) preference.obtenir(idNouvelleProgrammation + "_" + Programmation.BOISSON),
                        (int) preference.obtenir(idNouvelleProgrammation + "_" + Programmation.JOUR),
                        (String) preference.obtenir(idNouvelleProgrammation + "_" + Programmation.HEURE),
                        (int) preference.obtenir(idNouvelleProgrammation + "_" + Programmation.FREQUENCE),
                        (int) preference.obtenir(idNouvelleProgrammation + "_" + Programmation.IDENTIFIANT)
                ));
            }
        }
    }

    /**
     * @brief Méthode qui permet de demander la création d'une programmation
     * @fn Cafetiere::demanderCreationUneProgrammation(int capsule, int boisson, int jour, String heure, int frequence)
     * @param capsule la capsule demandée
     * @param boisson la boisson demandée
     * @param jour le jour demandé
     * @param heure l'heure demandée
     * @param frequence la fréquence demandée
     */
    public void demanderCreationUneProgrammation(int capsule, int boisson, int jour, String heure, int frequence)
    {
        Log.d(TAG, "demanderCreationUneProgrammation()");
        communication.envoyerTrame(Protocole.fabriquerTrameCreerProgrammation(capsule, boisson, jour, heure, frequence));
        derniereProgrammation = new Programmation(capsule, boisson, jour, heure, frequence);
    }

    /**
     * @brief Méthode qui permet de vérifier la création d'une programmation
     * @fn Cafetiere::verifierCreationUneProgrammation(String trame, int position)
     * @param trame la trame reçue
     * @param position la position de la programmation
     * @return boolean etat de la validation
     */
    private boolean verifierCreationUneProgrammation(String trame, int position)
    {
        if(position >= Programmation.MAX_PROGRAMMATION)
        {
            ihm.afficherMessage("Désolé, vous ne pouvez faire que 4 programmations");
            return false;
        }

        if(Protocole.extraireIdentifiant(trame) == Protocole.ERREUR_IDENTIFIANT)
        {
            ihm.afficherMessage("Erreur lors de la création de la programmation");
            return false;
        }
        return true;
    }

    /**
     * @brief Méthode qui permet de créer une programmation
     * @fn Cafetiere::creerUneProgrammation(String trame)
     * @param trame la trame reçue
     */
    private void creerUneProgrammation(String trame)
    {
        Log.d(TAG, "creerUneProgrammation()");
        int position = preference.obtenirNbProgrammations();

        if (!verifierCreationUneProgrammation(trame, position)) return;

        derniereProgrammation.changerIdentifiant(Protocole.extraireIdentifiant(trame));
        programmations.add(derniereProgrammation);

        String idNouvelleProgrammation = Programmation.PROGRAMMATION + position;
        preference.editer(idNouvelleProgrammation, position);
        preference.editer(idNouvelleProgrammation + "_" + Programmation.CAPSULE, derniereProgrammation.obtenirCapsule());
        preference.editer(idNouvelleProgrammation + "_" + Programmation.BOISSON, derniereProgrammation.obtenirBoisson());
        preference.editer(idNouvelleProgrammation + "_" + Programmation.JOUR, derniereProgrammation.obtenirJour());
        preference.editer(idNouvelleProgrammation + "_" + Programmation.HEURE, derniereProgrammation.obtenirHeure());
        preference.editer(idNouvelleProgrammation + "_" + Programmation.FREQUENCE, derniereProgrammation.obtenirFrequence());
        preference.editer(idNouvelleProgrammation + "_" + Programmation.IDENTIFIANT, derniereProgrammation.obtenirIdentifiant());

        ihm.actualiserPageProgrammer();
    }

    /**
     * @brief Méthode qui permet de demander la modification une programmation
     * @fn Cafetiere::demanderModificationUneProgrammation(int position, int capsule, int boisson, int jour, String heure, int frequence)
     * @param position la position de la programmation
     * @param capsule la capsule demandée
     * @param boisson la boisson demandée
     * @param jour le jour demandé
     * @param heure l'heure demandée
     * @param frequence la fréquence demandée
     */
    public void demanderModificationUneProgrammation(int position, int capsule, int boisson, int jour, String heure, int frequence)
    {
        Log.d(TAG, "demanderModificationUneProgrammation()");
        int identifiant = programmations.get(position).obtenirIdentifiant();
        communication.envoyerTrame(Protocole.fabriquerTrameModifierProgrammation(identifiant, capsule, boisson, jour, heure, frequence));
        dernierePositionProgrammation = position;
        derniereProgrammation = new Programmation(capsule, boisson, jour, heure, frequence, identifiant);
    }

    /**
     * @brief Méthode qui permet de modifier une programmation
     * @fn Cafetiere::verifierModifierUneProgrammation(String trame)
     * @param trame la trame reçue
     */
    public void verifierModifierUneProgrammation(String trame)
    {
        Log.d(TAG, "verifierModifierUneProgrammation()");
        if(Protocole.extraireIdentifiant(trame) != Protocole.ERREUR_IDENTIFIANT)
        {
            modifierUneProgrammation();
        }
        else
        {
            ihm.afficherMessage("Erreur lors de la modification de votre programmation");
        }
    }

    /**
     * @brief Méthode qui permet de modifier une programmation
     * @fn Cafetiere::modifierUneProgrammation()
     */
    public void modifierUneProgrammation()
    {
        Log.d(TAG, "modifierUneProgrammation()");
        programmations.set(dernierePositionProgrammation, derniereProgrammation);
        String idNouvelleProgrammation = Programmation.PROGRAMMATION + dernierePositionProgrammation;
        preference.editer(idNouvelleProgrammation, dernierePositionProgrammation);
        preference.editer(idNouvelleProgrammation + "_" + Programmation.CAPSULE, derniereProgrammation.obtenirCapsule());
        preference.editer(idNouvelleProgrammation + "_" + Programmation.BOISSON, derniereProgrammation.obtenirBoisson());
        preference.editer(idNouvelleProgrammation + "_" + Programmation.JOUR, derniereProgrammation.obtenirJour());
        preference.editer(idNouvelleProgrammation + "_" + Programmation.HEURE, derniereProgrammation.obtenirHeure());
        preference.editer(idNouvelleProgrammation + "_" + Programmation.FREQUENCE, derniereProgrammation.obtenirFrequence());
        preference.editer(idNouvelleProgrammation + "_" + Programmation.IDENTIFIANT, derniereProgrammation.obtenirIdentifiant());
        ihm.afficherMessage("Votre programmation a bien été mise à jour");
        ihm.actualiserPageProgrammer();
    }

    /**
     * @brief Méthode qui permet de demander la suppression d'une programmation
     * @fn Cafetiere::demanderSuppressionUneProgrammation(int position)
     * @param position la position de la programmation
     */
    public void demanderSuppressionUneProgrammation(int position)
    {
        Log.d(TAG, "demanderSuppressionUneProgrammation()");
        communication.envoyerTrame(Protocole.fabriquerTrameSupprimerProgrammation(programmations.get(position).obtenirIdentifiant()));
        dernierePositionProgrammation = position;
    }

    /**
     * @brief Méthode qui permet de vérifier la suppression d'une programmation
     * @fn Cafetiere::verifierSuppressionDuneProgrammation(String trame)
     * @param trame la trame reçue
     */
    private void verifierSuppressionDuneProgrammation(String trame)
    {
        Log.d(TAG, "verifierSuppressionDuneProgrammation()");
        if(Protocole.extraireIdentifiant(trame) != Protocole.ERREUR_IDENTIFIANT)
        {
            supprimerUneProgrammation();
        }
        else
        {
            ihm.afficherMessage("Erreur lors de la suppression de votre programmation");
        }
    }

    /**
     * @brief Méthode qui permet de supprimer une programmation
     * @fn Cafetiere::supprimerUneProgrammation()
     */
    private void supprimerUneProgrammation()
    {
        Log.d(TAG, "supprimerUneProgrammation()");
        String idProgrammation = Programmation.PROGRAMMATION + dernierePositionProgrammation;
        programmations.remove(dernierePositionProgrammation);
        preference.supprimer(idProgrammation);
        preference.reorganiserLesProgrammations(dernierePositionProgrammation);
        ihm.afficherMessage("Vous avez supprimé une programmation");
        ihm.actualiserPageProgrammer();
    }

    /**
     * @brief Méthode qui permet de supprimer toutes les programmations
     * @fn Cafetiere::supprimerLesProgrammations()
     */
    private void supprimerLesProgrammations()
    {
        Log.d(TAG, "supprimerLesProgrammations()");
        programmations = new ArrayList<>();
        preference.supprimerLesProgrammations();
        ihm.actualiserPageProgrammer();
    }

    /**
     * @brief Méthode qui renvoie tous les programmations
     * @fn Cafetiere::listerProgrammations()
     * @return ArrayList<Programmation> les données des programmations
     */
    public ArrayList<Programmation> listerProgrammations()
    {
        return programmations;
    }

    /**
     * @brief Méthode qui renvoie une programmation
     * @fn Cafetiere::obtenirProgrammation(int position)
     * @param position la position de la programmation
     * @return Programmation les données de la programmation
     */
    public Programmation obtenirProgrammation(int position)
    {
        return programmations.get(position);
    }

    /**
     * @brief Méthode qui permet d'afficher les erreurs
     * @fn Cafetiere::informerErreur(String trame)
     * @param trame la trame reçue
     */
    private void informerErreur(String trame)
    {
        switch(Protocole.extraireErreur(trame))
        {
            case Protocole.AUCUNE_ERREUR :
                Log.d(TAG, "informerErreur() : Aucune erreur");
                break;
            case Protocole.ERREUR_ENTETE :
                Log.d(TAG, "informerErreur() : Erreur entete");
                break;
            case Protocole.ERREUR_NB_PARAMETRES :
                Log.d(TAG, "informerErreur() : Erreur nombre parametres");
                break;
            case Protocole.ERREUR_TRAME_INCONNUE :
                Log.d(TAG, "informerErreur() : Erreur trame inconnue");
                break;
            case Protocole.ERREUR_TYPE_CAFE :
                Log.d(TAG, "informerErreur() : Erreur type café");
                break;
            case Protocole.ERREUR_LONGUEUR_CAFE :
                Log.d(TAG, "informerErreur() : Erreur longueur café");
                break;
        }
    }

    /**
     * @brief Méthode qui permet d'actualiser la configuration de la cafetière
     * @fn Cafetiere::actualiserConfiguration(String trame)
     * @param trame la trame reçue
     */
    private void actualiserConfiguration(String trame)
    {
        ntp = Protocole.extraireNTP(trame);
        versionCafetiere = Protocole.extraireVersionCafetire(trame);
    }

    public ArrayList<String> listerPeripheriques()
    {
        ArrayList<String> noms = new ArrayList<>();
        ArrayList<Peripherique> listePeripheriques = communication.listerPeripheriques();
        for (Peripherique peripherique : listePeripheriques)
            noms.add(peripherique.obtenirNom());
        return noms;
    }

    public void actualiserListePeripheriques()
    {
        ihm.actualiserListePeripheriques();
    }

    public void changerPeripherique(int position)
    {
        communication.changerPeripherique(position);
    }
}