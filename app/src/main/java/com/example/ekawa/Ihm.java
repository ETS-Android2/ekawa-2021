package com.example.ekawa;

import android.Manifest;

import android.app.Activity;
import android.app.AlertDialog;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.pm.PackageManager;

import android.os.Build;
import android.os.Bundle;

import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

/**
 * @file Ihm.java
 * @brief Déclaration de la classe Ihm
 * @author LECOMTE Jean-Luc
 * $LastChangedRevision: 111 $
 * $LastChangedDate: 2021-06-10 08:32:18 +0200 (jeu. 10 juin 2021) $
 */

/**
 * @class Ihm
 * @brief Déclaration de l'activité principale de l'application Ekawa
 * @version 1.0
 */
public class Ihm extends AppCompatActivity
{
    /**
     * Constantes
     */
    private static final String TAG = "_Ihm";                                               //!< TAG pour les logs

    // Requêtes pour les permissions
    final static int CODE_REQUETE_PERMISSION = 1;                                           //!< Code de requêtes
    final static String PERMISSION_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;     //!< Requêtes de localisation
    final static String PERMISSION_BLUETOOTH = Manifest.permission.BLUETOOTH;               //!< Requêtes de bluetooth
    final static String PERMISSION_BLUETOOTH_ADMIN = Manifest.permission.BLUETOOTH_ADMIN;   //!< Requêtes de l'administration de bluetooth

    /**
     * Attributs
     */
    Cafetiere cafetiere;                                        //!< Relation avec l'objet principal Cafetiere

    /**
     * Les widgets
     */
    // Indicateurs
    private ImageButton boutonBluetooth;                        //!< Bouton indicateur du Bluetooth
    private ImageButton boutonTasse;                            //!< Bouton indicateur de la tasse
    private ImageButton boutonBac;                              //!< Bouton indicateur du bac
    private ProgressBar barNiveauEauFond;                       //!< Indicateur du niveau d'eau en fond
    private ProgressBar barNiveauEau;                           //!< Indicateur du niveau d'eau (bar de progression)
    private TextView texteNiveauEau;                            //!< Indicateur du niveau d'eau (texte)

    // Sélection capsule
    private FrameLayout boutonSelectionCapsule;                 //!< Bouton pour ouvrir/fermer la sélection des capsules
    private ImageView imageCapsuleActuelle;                     //!< Indicateur de la capsule actuellement sélectionnée
    private TextView texteCapsuleActuelle;                      //!< Le nom de la capsule actuellement sélectionnée
    private ListView listeSelectionCapsule;                     //!< La liste des capsules sélectionnables
    private boolean visibiliteListeSelectionCapsule = false;    //!< La visibilité de la liste de capsule
    private String[] nomsCapsules;                              //!< Les noms des capsules
    private Integer[] identifiantsImagesCapsules;               //!< Les images des capsules

    // Sélection boisson
    private FrameLayout boutonSelectionBoisson;                 //!< Bouton pour ouvrir/fermer la sélection des boissons
    private ImageView imageBoissonActuelle;                     //!< Indicateur de la boisson actuellement sélectionnée
    private TextView texteBoissonActuelle;                      //!< Le nom de la boisson actuellement sélectionnée
    private ListView listeSelectionBoisson;                     //!< La liste des boissons sélectionnables
    private boolean visibiliteListeSelectionBoisson = false;    //!< La visibilité de la liste de boisson
    private String[] nomsBoisson;                               //!< Les noms des boissons
    private Integer[] identifiantsImagesBoisson;                //!< Les images des boissons

    // Bouton Lancer Cafe
    private FrameLayout boutonLancerCafe;                       //!< Bouton central : lancer un café

    // Menu
    private LinearLayout boutonPageInformations;                //!< Bouton pour ouvrir/fermer la page Informations
    private LinearLayout boutonPageProgrammer;                  //!< Bouton pour ouvrir/fermer la page Programmer
    private LinearLayout boutonPageParametres;                  //!< Bouton pour ouvrir/fermer la page Parametres
    private LinearLayout pageInformations;                      //!< La page Informations
    private boolean visibilitePageInformations = false;         //!< La visibilité de la page Informations
    private LinearLayout pageProgrammer;                        //!< La page Programmer
    private boolean visibilitePageProgrammer = false;           //!< La visibilité de la page Programmer
    private LinearLayout pageParametres;                        //!< La page Parametres
    private boolean visibilitePageParametres = false;           //!< La visibilité de la page Parametres

    // Page Informations
    private TextView texteNbCafeJour;                           //!< Le texte affichant le nombre de cafés effectués par l'utilisateur du jour
    private TextView texteNomCafetiere;                         //!< Le texte affichant le nom de la cafetière connectée
    private TextView texteNbCafe;                               //!< Le texte affichant le nombre de cafés éffectués au total par la machine
    private TextView texteNbBac;                                //!< Le texte affichant le nombre de bac vidé au total
    private TextView texteNbEau;                                //!< Le texte affichant le nombre de réservoir d'eau remplie
    private TextView texteDureteEau;                            //!< Le texte affichant la dureté de l'eau
    private TextView texteQualiteEau;                           //!< Le texte affichant la qualité de l'eau
    private ListView listeCapsuleInformations;                  //!< La liste des capsules de la page Informations

    private AlertDialog.Builder parametresFenetreInformations;  //!< Les paramètres de la fenêtre Informations
    private AlertDialog fenetreInformations;                    //!< La fenêtre Informations

    // Page Programmer
    private boolean modeProgrammer;                             //!< Le mode de la fenêtre Programmer
    private int positionProgrammer;                             //!< la position de la programmation

    private int capsuleProgrammation = 0;                       //!< La capsule de la programmation
    private int boissonProgrammation = 0;                       //!< La boisson de la programmation

    private ListView listeProgrammer;                           //!< La liste des programmations

    private AlertDialog.Builder parametresFenetreProgrammer;    //!< Les paramètres de la fenêtre Programmer
    private AlertDialog fenetreProgrammer;                      //!< La fenêtre Programmer
    private View apparenceFenetreProgrammer;                    //!< L'apparence de la fenêtre Programmer

    private ListView listeCapsuleProgrammer;                    //!< La liste des capsules de la fenêtre Programmer
    private ListView listeBoissonProgrammer;                    //!< La liste des boissons de la fenêtre Programmer
    private Spinner spinnerJourProgrammer;                      //!< La sélection du jour de la semaine
    private TimePicker heureProgrammer;                         //!< La sélection de l'heure
    private Spinner spinnerFrequenceProgrammer;                 //!< La sélection de la fréquence

    // Page Parametre
    private ListView listePeripheriques;
    private TextView aucunPeripherique;
    private Button boutonReinitialiserInformations;             //!< Bouton de réinitialisation des données de la cafetière
    private Button boutonAPropos;                               //!< Bouton A propos

    private AlertDialog.Builder parametresFenetreReinitialiserParametres;//!< Les paramètres de la fenêtre réinitialiser informations des paramètres
    private AlertDialog fenetreReinitialiserParametres;         //!< La fenêtre réinitialiser informations des paramètres
    private View apparenceFenetreReinitialiserParametres;       //!< L'apparence de la fenêtre réinitialiser informations des paramètres

    private AlertDialog.Builder parametresFenetreAProposParametres;//!< Les paramètres de la fenêtre a propos des paramètres
    private AlertDialog fenetreAProposParametres;               //!< La fenêtre a propos des parametres

    private Switch nombreCafe;                                  //!< Bouton réinitialiser le nombre de cafés effectués au total par la machine de la fenêtre Parametres
    private Switch nombreBacVide;                               //!< Bouton réinitialiser le nombre de bac vidé au total
    private Switch nombreEauRemplie;                            //!< Bouton réinitialiser le nombre de réservoir d'eau remplie
    private EditText dureteEau;                                 //!< Bouton réinitialiser la dureté de l'eau
    private EditText qualiteEau;                                //!< Bouton réinitialiser la qualité de l'eau
    private Switch programmations;                              //!< Bouton réinitialiser les programmations
    private Switch simulateur;                                  //!< Bouton réinitialiser le réservoir d'eau, le bac et le magasin

    private Notification.Builder parametresNotification;  //!< Le créateur de notification
    private NotificationManager gestionnaireNotification;       //!< Le gestionnaire de notification

    /**
     * @class AdaptateurSelection
     * @brief Déclaration de l'adaptateur de liste de capsules et boissons
     */
    public class AdaptateurSelection extends ArrayAdapter
    {
        private Activity context;               //!< L'activitée
        private String[] noms;                  //!< Les noms des capsules ou des boissons
        private Integer[] identifiantsImages;   //!< Les identifiants des images des capsules ou des boissons

        /**
         * @brief Constructeur de la classe AdaptateurSelection
         * @fn Ihm::AdaptateurSelection::AdaptateurSelection(Activity context, String[] noms, Integer[] identifiantsImages)
         * @param context le contexte de l'application
         * @param noms les noms des capsules ou des boissons
         * @param identifiantsImages les identifiants des images des capsules ou des boissons
         */
        public AdaptateurSelection(Activity context, String[] noms, Integer[] identifiantsImages)
        {
            super(context, R.layout.item_selection, noms);
            this.context = context;
            this.noms = noms;
            this.identifiantsImages = identifiantsImages;
        }

        /**
         * @brief Réécriture de la méthode ArrayAdapter::getView()
         * @fn Ihm::AdaptateurSelection::getView(int position, View itemConverti, ViewGroup parent)
         * @param position la position de la ligne
         * @param itemConverti la ligne
         * @param parent la liste
         */
        @Override
        public View getView(int position, View itemConverti, ViewGroup parent)
        {
            View item = itemConverti;
            LayoutInflater inflater = context.getLayoutInflater();
            if (itemConverti == null)
                item = inflater.inflate(R.layout.item_selection, null, true);
            ImageView imageItem = (ImageView) item.findViewById(R.id.item_image);
            TextView texteItem = (TextView) item.findViewById(R.id.item_texte);

            imageItem.setImageResource(identifiantsImages[position]);
            texteItem.setText(String.valueOf(position) + " - " + noms[position]);
            return item;
        }
    }

    /**
     * @class AdaptateurProgrammer
     * @brief Déclaration de l'adapteur des programmations
     */
    public class AdaptateurProgrammer extends ArrayAdapter
    {
        private Activity context;                                           //!< L'activite
        private ArrayList<String> nomsCapsulesProgrammer;                   //!< Les noms des capsules des programmations
        private ArrayList<String> nomsBoissonsProgrammer;                   //!< Les noms des boissons des programmations
        private ArrayList<Integer> identifiantsImagesCapsulesProgrammer;    //!< Les identifiants des images des capsules des programmations
        private ArrayList<String> joursProgrammer;                          //!< Les jours des programmations
        private ArrayList<String> heuresProgrammer;                         //!< Les heures des programmations
        private ArrayList<String> frequencesProgrammer;                     //!< Les fréquences des programmations

        /**
         * @brief Constructeur de la classe AdaptateurProgrammer
         * @fn Ihm::AdaptateurProgrammer::AdaptateurProgrammer(Activity context, ArrayList<Programmation> programmations)
         * @param context le contexte de l'application
         * @param programmations les programmations
         */
        public AdaptateurProgrammer(Activity context, ArrayList<Programmation> programmations)
        {
            super(context, R.layout.item_selection, programmations);
            this.context = context;
            nomsCapsulesProgrammer = new ArrayList<String>();
            nomsBoissonsProgrammer = new ArrayList<String>();
            identifiantsImagesCapsulesProgrammer = new ArrayList<Integer>();
            joursProgrammer = new ArrayList<String>();
            heuresProgrammer = new ArrayList<String>();
            frequencesProgrammer = new ArrayList<String>();
            for (Programmation programmation : programmations)
            {
                this.nomsCapsulesProgrammer.add(nomsCapsules[programmation.obtenirCapsule()]);
                this.nomsBoissonsProgrammer.add(nomsBoisson[programmation.obtenirBoisson()]);
                this.identifiantsImagesCapsulesProgrammer.add(identifiantsImagesCapsules[programmation.obtenirCapsule()]);
                this.joursProgrammer.add(Programmation.Jours.JOURS[programmation.obtenirJour()]);
                this.heuresProgrammer.add(programmation.obtenirHeure());
                this.frequencesProgrammer.add(Programmation.Frequences.FREQUENCES[programmation.obtenirFrequence()]);
            }
        }

        /**
         * @brief Réécriture de la méthode ArrayAdapter::getView()
         * @fn Ihm::AdaptateurProgrammer::getView(int position, View itemConverti, ViewGroup parent)
         * @param position la position de la ligne
         * @param itemConverti la ligne
         * @param parent la liste
         */
        @Override
        public View getView(int position, View itemConverti, ViewGroup parent)
        {
            View item = itemConverti;
            LayoutInflater inflater = context.getLayoutInflater();
            if (itemConverti == null)
                item = inflater.inflate(R.layout.item_programmer, null, true);
            ImageView imageItem = (ImageView) item.findViewById(R.id.item_image_programmer);
            TextView texteCapsuleItem = (TextView) item.findViewById(R.id.item_capsule);
            TextView texteBoissonItem = (TextView) item.findViewById(R.id.item_boisson);
            TextView texteJourHeureItem = (TextView) item.findViewById(R.id.item_jour_heure);
            TextView texteFrequenceItem = (TextView) item.findViewById(R.id.item_frequence);

            if(position == 0)
            {
                imageItem.setImageResource(R.drawable.ic_plus);
                texteCapsuleItem.setText("");
                texteBoissonItem.setText("");
                texteJourHeureItem.setText("");
                texteFrequenceItem.setText("");
            }
            else
            {
                imageItem.setImageResource(identifiantsImagesCapsulesProgrammer.get(position));
                texteCapsuleItem.setText(nomsCapsulesProgrammer.get(position));
                texteBoissonItem.setText(nomsBoissonsProgrammer.get(position));
                texteJourHeureItem.setText(joursProgrammer.get(position) + " - " + heuresProgrammer.get(position));
                texteFrequenceItem.setText(frequencesProgrammer.get(position));
            }
            return item;
        }
    }

    /**
     * @brief Méthode appelée à la création de l'activité
     * @fn Ihm::onCreate(Bundle savedInstanceState)
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ihm);
        Log.d(TAG, "Création de l'activité");

        // instancie l'objet principal de l'application
        cafetiere = new Cafetiere(this);

        // initialise l'application
        demanderPermissions();
        initialiserBoutonsIndicateurs();
        initialiserSelectionCapsule();
        initialiserSelectionBoisson();
        initialiserBoutonLancerCafe();
        initialiserMenu();
        initialiserPageInformations();
        initialiserPageProgrammer();
        initialiserPageParametres();
        actualiserIndicateurs();
        initialiserNotifications();
    }

    /**
     * @brief Méthode appelée au démarrage après le onCreate() ou un restart après un onStop()
     * @fn Ihm::onStart()
     */
    @Override
    protected void onStart()
    {
        super.onStart();
        Log.d(TAG, "onStart()");
    }

    /**
     * @brief Méthode appelée après onStart() ou après onPause()
     * @fn Ihm::onResume()
     */
    @Override
    protected void onResume()
    {
        super.onResume();
        Log.d(TAG, "onResume()");
        actualiserIndicateurs();
    }

    /**
     * @brief Méthode appelée après qu'une boîte de dialogue s'est affichée (on reprend sur un onResume()) ou avant onStop() (activité plus visible)
     * @fn Ihm::onPause()
     */
    @Override
    protected void onPause()
    {
        super.onPause();
        Log.d(TAG, "onPause()");
    }

    /**
     * @brief Méthode appelée lorsque l'activité n'est plus visible
     * @fn Ihm::onStop()
     */
    @Override
    protected void onStop()
    {
        super.onStop();
        Log.d(TAG, "onStop()");
    }

    /**
     * @brief Méthode appelée à la destruction de l'application (après onStop() et détruite par le système Android)
     * @fn Ihm::onDestroy()
     */
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        cafetiere.eteindre();
        Log.d(TAG, "onDestroy()");
    }

    /**
     * @brief Méthode qui permet de demander le droit de permission location
     * @fn Ihm::demanderPermissions()
     */
    private void demanderPermissions()
    {
        Log.d(TAG, "demanderPermissions()");
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), PERMISSION_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{PERMISSION_LOCATION}, CODE_REQUETE_PERMISSION);
        }
    }

    /**
     * @brief Méthode qui permet de gérer la réponse à la demande de permission location
     * @fn Ihm::onRequestPermissionsResult()
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        Log.d(TAG, "onRequestPermissionsResult()");
        switch (requestCode)
        {
            case CODE_REQUETE_PERMISSION:
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    cafetiere.allumer();
                else
                    onDestroy();
                return;
            }
        }
    }

    /**
     * @brief Méthode qui permet d'initialiser les indicateurs (bluetooth, tasse, bac, eau)
     * @fn Ihm::initialiserBoutonsIndicateurs()
     */
    private void initialiserBoutonsIndicateurs()
    {
        Log.d(TAG, "initialiserBoutonsIndicateurs()");
        boutonBluetooth = (ImageButton) findViewById(R.id.bouton_bluetooth);
        boutonBluetooth.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "[boutonBluetooth] onClick : EtatBluetooth = " + cafetiere.informerEtatBluetooth() + " - Connexion = " + cafetiere.informerConnexionBluetooth());
                if(cafetiere.informerEtatBluetooth() && cafetiere.informerConnexionBluetooth())
                    cafetiere.eteindre();
                else if(cafetiere.informerEtatBluetooth() && !cafetiere.informerConnexionBluetooth())
                    cafetiere.connecter();
                else
                    cafetiere.allumer();
                cafetiere.actualiserDonnees();
            }
        });

        boutonTasse = (ImageButton) findViewById(R.id.bouton_tasse);
        boutonTasse.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                cafetiere.actualiserDonnees();
            }
        });

        boutonBac = (ImageButton) findViewById(R.id.bouton_bac);
        boutonBac.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                cafetiere.actualiserDonnees();
            }
        });

        barNiveauEauFond = (ProgressBar) findViewById(R.id.fond_niveau_eau);
        barNiveauEau = (ProgressBar) findViewById(R.id.bar_niveau_eau);
        texteNiveauEau = (TextView) findViewById(R.id.texte_niveau_eau);
        texteNiveauEau.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                cafetiere.actualiserDonnees();
            }
        });
    }

    /**
     * @brief Méthode qui permet d'initialiser la liste de sélection des capsules
     * @fn Ihm::initialiserSelectionCapsule()
     */
    private void initialiserSelectionCapsule()
    {
        Log.d(TAG, "initialiserSelectionCapsule()");
        boutonSelectionCapsule = (FrameLayout) findViewById(R.id.bouton_selection_capsule);
        listeSelectionCapsule = (ListView) findViewById(R.id.liste_selection_capsule);
        imageCapsuleActuelle = (ImageView) findViewById(R.id.image_selection_capsule);
        texteCapsuleActuelle = (TextView) findViewById(R.id.texte_capsule_actuelle);
        boutonSelectionCapsule.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (visibiliteListeSelectionCapsule)
                {
                    listeSelectionCapsule.setVisibility(View.INVISIBLE);
                    visibiliteListeSelectionCapsule = false;
                }
                else
                {
                    listeSelectionCapsule.setVisibility(View.VISIBLE);
                    visibiliteListeSelectionCapsule = true;
                    if (visibiliteListeSelectionBoisson)
                        boutonSelectionBoisson.callOnClick();
                }
            }
        });

        nomsCapsules = new String[Cafetiere.NOMBRE_CAPSULE_MAX];
        identifiantsImagesCapsules = new Integer[Cafetiere.NOMBRE_CAPSULE_MAX];

        for (int i = 0; i < Cafetiere.NOMBRE_CAPSULE_MAX; ++i) {
            nomsCapsules[i] = cafetiere.listerCapsules().get(i).obtenirNom();
            identifiantsImagesCapsules[i] = cafetiere.listerCapsules().get(i).obtenirImage();
        }

        listeSelectionCapsule.setAdapter(new AdaptateurSelection(this, nomsCapsules, identifiantsImagesCapsules));

        listeSelectionCapsule.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id)
            {
                if(cafetiere.informerPresenceCapsule(position))
                {
                    texteCapsuleActuelle.setText(nomsCapsules[position]);
                    imageCapsuleActuelle.setImageResource(identifiantsImagesCapsules[position]);
                    cafetiere.changerCapsuleActuelle(position);
                    boutonSelectionCapsule.callOnClick();
                }
            }
        });
    }

    /**
     * @brief Méthode qui permet d'initialiser la liste de sélection des boissons
     * @fn Ihm::initialiserSelectionBoisson()
     */
    private void initialiserSelectionBoisson()
    {
        Log.d(TAG, "initialiserSelectionBoisson()");
        boutonSelectionBoisson = (FrameLayout) findViewById(R.id.bouton_selection_boisson);
        listeSelectionBoisson = (ListView) findViewById(R.id.liste_selection_boisson);
        imageBoissonActuelle = (ImageView) findViewById(R.id.image_selection_boisson);
        texteBoissonActuelle = (TextView) findViewById(R.id.texte_boisson_actuelle);
        boutonSelectionBoisson.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (visibiliteListeSelectionBoisson)
                {
                    listeSelectionBoisson.setVisibility(View.INVISIBLE);
                    visibiliteListeSelectionBoisson = false;
                }
                else
                {
                    listeSelectionBoisson.setVisibility(View.VISIBLE);
                    visibiliteListeSelectionBoisson = true;
                    if (visibiliteListeSelectionCapsule)
                        boutonSelectionCapsule.callOnClick();
                }
            }
        });

        nomsBoisson = new String[Cafetiere.NOMBRE_BOISSON_MAX];
        identifiantsImagesBoisson = new Integer[Cafetiere.NOMBRE_BOISSON_MAX];

        for (int i = 0; i < Cafetiere.NOMBRE_BOISSON_MAX; ++i) {
            nomsBoisson[i] = cafetiere.listerBoissons().get(i).obtenirNom();
            identifiantsImagesBoisson[i] = cafetiere.listerBoissons().get(i).obtenirImage();
        }

        listeSelectionBoisson.setAdapter(new AdaptateurSelection(this, nomsBoisson, identifiantsImagesBoisson));

        listeSelectionBoisson.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id)
            {
                texteBoissonActuelle.setText(nomsBoisson[position]);
                imageBoissonActuelle.setImageResource(identifiantsImagesBoisson[position]);
                cafetiere.changerBoissonActuelle(position);
                boutonSelectionBoisson.callOnClick();
            }
        });
        texteBoissonActuelle.setText(nomsBoisson[0]);
        imageBoissonActuelle.setImageResource(identifiantsImagesBoisson[0]);
    }

    /**
     * @brief Méthode qui permet d'initialiser le bouton principal de l'application (pour lancer le café)
     * @fn Ihm::initialiserBoutonLancerCafe()
     */
    private void initialiserBoutonLancerCafe()
    {
        Log.d(TAG, "initialiserBoutonLancerCafe()");
        boutonLancerCafe = (FrameLayout) findViewById(R.id.bouton_lancer_cafe);
        boutonLancerCafe.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (cafetiere.estPrete())
                {
                    if(cafetiere.informerPresenceCapsule(cafetiere.informerCapsuleActuelle()))
                        cafetiere.demanderPreparationCafe();
                }
                else
                {
                    cafetiere.actualiserDonnees();
                }
            }
        });
    }

    /**
     * @brief Méthode qui permet d'initialiser le menu (page "informations", page "programmer", page "paramètres")
     * @fn Ihm::initialiserMenu()
     */
    private void initialiserMenu()
    {
        Log.d(TAG, "initialiserMenu()");
        boutonPageInformations = (LinearLayout) findViewById(R.id.bouton_page_informations);
        boutonPageProgrammer = (LinearLayout) findViewById(R.id.bouton_page_programmer);
        boutonPageParametres = (LinearLayout) findViewById(R.id.bouton_page_parametres);
        pageInformations = (LinearLayout) findViewById(R.id.page_informations);
        pageProgrammer = (LinearLayout) findViewById(R.id.page_programmer);
        pageParametres = (LinearLayout) findViewById(R.id.page_parametres);

        boutonPageInformations.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!visibilitePageInformations)
                {
                    pageInformations.setVisibility(View.VISIBLE);
                    pageProgrammer.setVisibility(View.INVISIBLE);
                    pageParametres.setVisibility(View.INVISIBLE);
                    visibilitePageInformations = true;
                    visibilitePageProgrammer = false;
                    visibilitePageParametres = false;
                    boutonLancerCafe.setEnabled(false);
                }
                else
                {
                    pageInformations.setVisibility(View.INVISIBLE);
                    visibilitePageInformations = false;
                    boutonLancerCafe.setEnabled(true);
                }
                actualiserPageInformations();
            }
        });

        boutonPageProgrammer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!visibilitePageProgrammer)
                {
                    pageInformations.setVisibility(View.INVISIBLE);
                    if(cafetiere.informerNTP())
                        pageProgrammer.setVisibility(View.VISIBLE);
                    else
                        afficherMessage("Votre cafetière n'est pas connectée au NTP");
                    pageParametres.setVisibility(View.INVISIBLE);
                    visibilitePageInformations = false;
                    visibilitePageProgrammer = true;
                    visibilitePageParametres = false;
                    boutonLancerCafe.setEnabled(false);
                }
                else
                {
                    pageProgrammer.setVisibility(View.INVISIBLE);
                    visibilitePageProgrammer = false;
                    boutonLancerCafe.setEnabled(true);
                }
            }
        });

        boutonPageParametres.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!visibilitePageParametres)
                {
                    pageInformations.setVisibility(View.INVISIBLE);
                    pageProgrammer.setVisibility(View.INVISIBLE);
                    pageParametres.setVisibility(View.VISIBLE);
                    visibilitePageInformations = false;
                    visibilitePageProgrammer = false;
                    visibilitePageParametres = true;
                    boutonLancerCafe.setEnabled(false);
                    actualiserListePeripheriques();
                }
                else
                {
                    pageParametres.setVisibility(View.INVISIBLE);
                    visibilitePageParametres = false;
                    boutonLancerCafe.setEnabled(true);
                }
            }
        });
    }

    public void actualiserListePeripheriques()
    {
        ArrayList<String> listeDesPeripherique = cafetiere.listerPeripheriques();
        if(listeDesPeripherique.isEmpty())
            aucunPeripherique.setVisibility(View.VISIBLE);
        else
            aucunPeripherique.setVisibility(View.INVISIBLE);
        listePeripheriques.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_expandable_list_item_1, cafetiere.listerPeripheriques()));
    }

    /**
     * @brief Méthode qui permet d'initialiser la page "Informations"
     * @fn Ihm::initialiserPageInformations()
     */
    private void initialiserPageInformations()
    {
        Log.d(TAG, "initialiserPageInformations()");
        texteNbCafeJour = (TextView) findViewById(R.id.nb_cafe_jour);
        texteNomCafetiere = (TextView) findViewById(R.id.nom_cafetiere);
        texteNbCafe = (TextView) findViewById(R.id.nb_cafe);
        texteNbBac = (TextView) findViewById(R.id.nb_bac);
        texteNbEau = (TextView) findViewById(R.id.nb_eau);
        texteDureteEau = (TextView) findViewById(R.id.duretee_eau);
        texteQualiteEau = (TextView) findViewById(R.id.qualite_eau);
        listeCapsuleInformations = (ListView) findViewById(R.id.liste_capsule_informations);
        listeCapsuleInformations.setAdapter(new AdaptateurSelection(this, nomsCapsules, identifiantsImagesCapsules));
        parametresFenetreInformations = new AlertDialog.Builder(this);
        listeCapsuleInformations.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                parametresFenetreInformations.setIcon(identifiantsImagesCapsules[position])
                        .setTitle(nomsCapsules[position])
                        .setMessage("Position : " + position + "\n" + getString(cafetiere.obtenirDescriptionCapsule(position)));
                fenetreInformations = parametresFenetreInformations.create();
                fenetreInformations.show();
            }
        });
    }

    /**
     * @brief Méthode qui permet d'initialiser la page "Programmer"
     * @fn Ihm::initialiserPageProgrammer()
     */
    private void initialiserPageProgrammer()
    {
        Log.d(TAG, "initialiserPageProgrammer()");
        listeProgrammer = (ListView) findViewById(R.id.liste_programmer);
        actualiserPageProgrammer();
        parametresFenetreProgrammer = new AlertDialog.Builder(this);
        apparenceFenetreProgrammer = getLayoutInflater().inflate(R.layout.page_programmer, null);
        parametresFenetreProgrammer.setView(apparenceFenetreProgrammer);

        initialiserFenetreProgrammer();

        listeProgrammer.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id)
            {
                Log.d(TAG, "initialiserPageProgrammer() onItemClick = " + position);

                positionProgrammer = position - 1;
                if(position == 0)
                {
                    modeProgrammer = Programmation.MODE_CREATION;
                    capsuleProgrammation = cafetiere.informerCapsuleActuelle();
                    boissonProgrammation = cafetiere.informerBoissonActuelle();
                }
                else
                {
                    modeProgrammer = Programmation.MODE_MODIFICATION;
                    Programmation programmation = cafetiere.obtenirProgrammation(positionProgrammer);
                    capsuleProgrammation = programmation.obtenirCapsule();
                    boissonProgrammation = programmation.obtenirBoisson();
                    spinnerJourProgrammer.setSelection(programmation.obtenirJour());
                    String heureMinute = programmation.obtenirHeure();
                    String[] splitHeureMinute = heureMinute.split("h");
                    heureProgrammer.setHour(Integer.parseInt(splitHeureMinute[0]));
                    heureProgrammer.setMinute(Integer.parseInt(splitHeureMinute[1]));
                    spinnerFrequenceProgrammer.setSelection(programmation.obtenirFrequence());
                }

                fenetreProgrammer.show();

                if(listeCapsuleProgrammer.getChildAt(capsuleProgrammation) != null)
                {
                    reinitialiserListe(listeCapsuleProgrammer);
                    listeCapsuleProgrammer.getChildAt(capsuleProgrammation).setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));
                }
                if(listeBoissonProgrammer.getChildAt(boissonProgrammation) != null)
                {
                    reinitialiserListe(listeBoissonProgrammer);
                    listeBoissonProgrammer.getChildAt(boissonProgrammation).setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));
                }
            }
        });
    }

    /**
     * @brief Méthode qui permet d'initialiser la fenêtre volante "Programmer"
     * @fn Ihm::initialiserFenetreProgrammer()
     */
    private void initialiserFenetreProgrammer()
    {
        Log.d(TAG, "initialiserFenetreProgrammer()");
        parametresFenetreProgrammer.setTitle("Créer ou modifier une programmation");

        capsuleProgrammation = cafetiere.informerCapsuleActuelle();
        boissonProgrammation = cafetiere.informerBoissonActuelle();
        Log.d(TAG, "initialiserFenetreProgrammer() capsuleProgrammation = " + capsuleProgrammation);
        Log.d(TAG, "initialiserFenetreProgrammer() boissonProgrammation = " + boissonProgrammation);

        listeCapsuleProgrammer = (ListView) apparenceFenetreProgrammer.findViewById(R.id.liste_capsule_programmer);
        listeBoissonProgrammer = (ListView) apparenceFenetreProgrammer.findViewById(R.id.liste_boisson_programmer);
        spinnerJourProgrammer = (Spinner) apparenceFenetreProgrammer.findViewById(R.id.liste_jour_programmer);
        heureProgrammer = (TimePicker) apparenceFenetreProgrammer.findViewById(R.id.temps_programmer);
        spinnerFrequenceProgrammer = (Spinner) apparenceFenetreProgrammer.findViewById(R.id.liste_frequence_programmer);

        listeCapsuleProgrammer.setAdapter(new AdaptateurSelection(this, nomsCapsules, identifiantsImagesCapsules));
        listeCapsuleProgrammer.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                reinitialiserListe(listeCapsuleProgrammer);
                listeCapsuleProgrammer.getChildAt(position).setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));
                capsuleProgrammation = position;
            }
        });

        listeBoissonProgrammer.setAdapter(new AdaptateurSelection(this, nomsBoisson, identifiantsImagesBoisson));
        listeBoissonProgrammer.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                reinitialiserListe(listeBoissonProgrammer);
                listeBoissonProgrammer.getChildAt(position).setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));
                boissonProgrammation = position;
            }
        });

        spinnerJourProgrammer.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Programmation.Jours.JOURS));
        heureProgrammer.setIs24HourView(true);
        spinnerFrequenceProgrammer.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Programmation.Frequences.FREQUENCES));

        parametresFenetreProgrammer.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if(modeProgrammer == Programmation.MODE_CREATION)
                {
                    cafetiere.demanderCreationUneProgrammation(
                            capsuleProgrammation,
                            boissonProgrammation,
                            spinnerJourProgrammer.getSelectedItemPosition(),
                            heureProgrammer.getHour() + "h" + heureProgrammer.getMinute(),
                            spinnerFrequenceProgrammer.getSelectedItemPosition()
                    );
                }
                if(modeProgrammer == Programmation.MODE_MODIFICATION)
                {
                    cafetiere.demanderModificationUneProgrammation(
                            positionProgrammer,
                            capsuleProgrammation,
                            boissonProgrammation,
                            spinnerJourProgrammer.getSelectedItemPosition(),
                            heureProgrammer.getHour() + "h" + heureProgrammer.getMinute(),
                            spinnerFrequenceProgrammer.getSelectedItemPosition()
                    );
                }
                actualiserPageProgrammer();
            }
        });

        parametresFenetreProgrammer.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });

        parametresFenetreProgrammer.setNeutralButton("Supprimer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if(modeProgrammer == Programmation.MODE_MODIFICATION)
                {
                    cafetiere.demanderSuppressionUneProgrammation(positionProgrammer);
                    actualiserPageProgrammer();
                }
            }
        });

        fenetreProgrammer = parametresFenetreProgrammer.create();
        fenetreProgrammer.setOnShowListener(new DialogInterface.OnShowListener(){
            @Override
            public void onShow(DialogInterface dialogInterface){
                Log.d(TAG, "fenetreProgrammer.onShow()");
                listeCapsuleProgrammer.performItemClick(listeCapsuleProgrammer, capsuleProgrammation, listeCapsuleProgrammer.getItemIdAtPosition(capsuleProgrammation));
                listeBoissonProgrammer.performItemClick(listeBoissonProgrammer, boissonProgrammation, listeBoissonProgrammer.getItemIdAtPosition(boissonProgrammation));
            }
        });
    }

    /**
     * @brief Méthode qui permet d'initialiser la page "Parametres"
     * @fn Ihm::initialiserPageParametres()
     */
    private void initialiserPageParametres()
    {
        Log.d(TAG, "initialiserPageParametres()");

        listePeripheriques = (ListView) findViewById(R.id.liste_peripheriques);
        aucunPeripherique = (TextView) findViewById(R.id.aucun_peripherique);
        listePeripheriques.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                cafetiere.changerPeripherique(position);
            }
        });

        initialiserFenetreReinitialiserParametres();
        boutonReinitialiserInformations = (Button) findViewById(R.id.bouton_reinitialiser_valeurs);
        boutonReinitialiserInformations.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                fenetreReinitialiserParametres.show();
            }
        });

        initialiserFenetreAProposParametres();
        boutonAPropos = (Button) findViewById(R.id.bouton_a_propos);
        boutonAPropos.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                actualiserPageParametres();
                fenetreAProposParametres.show();
            }
        });
    }

    /**
     * @brief Méthode qui permet d'initialiser la fenêtre "Réinitialiser paramètres"
     * @fn Ihm::initialiserFenetreReinitialiserParametres()
     */
    private void initialiserFenetreReinitialiserParametres()
    {
        Log.d(TAG, "initialiserFenetreReinitialiserParametres()");

        parametresFenetreReinitialiserParametres = new AlertDialog.Builder(this);
        apparenceFenetreReinitialiserParametres = getLayoutInflater().inflate(R.layout.page_reinisialiser_parametre, null);
        parametresFenetreReinitialiserParametres.setView(apparenceFenetreReinitialiserParametres);

        nombreCafe = (Switch) apparenceFenetreReinitialiserParametres.findViewById(R.id.reinitialiser_nombre_cafe);
        nombreBacVide = (Switch) apparenceFenetreReinitialiserParametres.findViewById(R.id.reinitialiser_nombre_bac_vide);
        nombreEauRemplie = (Switch) apparenceFenetreReinitialiserParametres.findViewById(R.id.reinitialiser_nombre_eau_remplie);
        dureteEau = (EditText) apparenceFenetreReinitialiserParametres.findViewById(R.id.reinitialiser_duretee_eau);
        qualiteEau = (EditText) apparenceFenetreReinitialiserParametres.findViewById(R.id.reinitialiser_qualite_eau);
        programmations = (Switch) apparenceFenetreReinitialiserParametres.findViewById(R.id.reinitialiser_programmations);
        simulateur = (Switch) apparenceFenetreReinitialiserParametres.findViewById(R.id.reinitialiser_simulateur);

        parametresFenetreReinitialiserParametres.setTitle("Réinitialiser valeurs informations complémentaires");
        parametresFenetreReinitialiserParametres.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                try
                {
                    if(dureteEau.getText().toString().equals(""))
                        dureteEau.setText("-1");
                    Integer.valueOf(dureteEau.getText().toString());
                }
                catch (Exception e)
                {
                    afficherMessage("Dureté de l'eau incorrecte");
                    return;
                }
                try
                {
                    if(qualiteEau.getText().toString().equals(""))
                        qualiteEau.setText("-1");
                    Integer.valueOf(qualiteEau.getText().toString());
                }
                catch (Exception e)
                {
                    afficherMessage("Qualité de l'eau incorrecte");
                    return;
                }
                cafetiere.modifierInformationsComplementaires(
                        nombreCafe.isChecked(),
                        nombreBacVide.isChecked(),
                        nombreEauRemplie.isChecked(),
                        Integer.valueOf(dureteEau.getText().toString()),
                        Integer.valueOf(qualiteEau.getText().toString()),
                        programmations.isChecked(),
                        simulateur.isChecked()
                );
            }
        });
        parametresFenetreReinitialiserParametres.setNegativeButton("Annuler", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });
        fenetreReinitialiserParametres = parametresFenetreReinitialiserParametres.create();
    }

    /**
     * @brief Méthode qui permet d'initialiser la fenêtre "A propos"
     * @fn Ihm::initialiserFenetreAProposParametres()
     */
    private void initialiserFenetreAProposParametres()
    {
        parametresFenetreAProposParametres = new AlertDialog.Builder(this);
        parametresFenetreAProposParametres.setTitle("À propos");
        parametresFenetreAProposParametres.setMessage("Nom de l'application : " + getString(R.string.nom_app) + "\n" +
                "Version de l'application : " + getString(R.string.version_app) + "\n" +
                "Version de la cafetière : " + cafetiere.informerVersionCafetiere() + "\n" +
                "Nom du développeur : " + getString(R.string.nom_developeur) + "\n" +
                "Contact : " + getString(R.string.mail));
        fenetreAProposParametres = parametresFenetreAProposParametres.create();
    }

    /**
     * @brief Méthode qui permet d'initialiser la fenetre "A propos"
     * @fn Ihm::initialiserNotifications()
     */
    private void initialiserNotifications()
    {
        parametresNotification = new Notification.Builder(this);
        parametresNotification.setSmallIcon(R.mipmap.ic_ekawa);
        gestionnaireNotification = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    /**
     * @brief Méthode qui permet de mettre à jour les indicateurs (bluetooth, tasse, bac, eau)
     * @fn Ihm::actualiserIndicateurs()
     */
    public void actualiserIndicateurs()
    {
        Log.d(TAG, "actualiserIndicateurs()");
        if (cafetiere.informerEtatBluetooth() && cafetiere.informerConnexionBluetooth())
            boutonBluetooth.setBackgroundResource(R.drawable.style_bouton_valide);
        else if (cafetiere.informerEtatBluetooth() && !cafetiere.informerConnexionBluetooth())
            boutonBluetooth.setBackgroundResource(R.drawable.style_bouton_semi_valide);
        else
            boutonBluetooth.setBackgroundResource(R.drawable.style_bouton_invalide);

        if (cafetiere.informerEtatTasse())
            boutonTasse.setBackgroundResource(R.drawable.style_bouton_valide);
        else
            boutonTasse.setBackgroundResource(R.drawable.style_bouton_invalide);

        if (cafetiere.informerEtatBac())
            boutonBac.setBackgroundResource(R.drawable.style_bouton_valide);
        else
            boutonBac.setBackgroundResource(R.drawable.style_bouton_invalide);

        if (cafetiere.informerEtatBluetooth() && cafetiere.informerConnexionBluetooth() && cafetiere.informerCapsuleActuelle() != Cafetiere.AUCUNE_CAPSULE && cafetiere.informerEtatCafetiere() && cafetiere.informerEtatTasse() && cafetiere.informerEtatBac() && cafetiere.informerNiveauEau() != 0)
        {
            boutonLancerCafe.setBackgroundResource(R.drawable.style_bouton_valide);
        }
        else
        {
            boutonLancerCafe.setBackgroundResource(R.drawable.style_bouton_invalide);
        }

        barNiveauEauFond.setProgress(cafetiere.informerNiveauEau());
        barNiveauEau.setProgress(cafetiere.informerNiveauEau());
        texteNiveauEau.setText(cafetiere.informerNiveauEau() + "%");
    }

    /**
     * @brief Méthode qui permet de mettre à jour la liste des sélections des capsules
     * @fn Ihm::actualiserSelection()
     */
    public void actualiserSelection()
    {
        Log.d(TAG, "actualiserSelection()");
        if(cafetiere.informerCapsuleActuelle() == Cafetiere.AUCUNE_CAPSULE)
        {
            texteCapsuleActuelle.setText("Aucune");
            imageCapsuleActuelle.setImageResource(R.drawable.ic_capsule);
        }
        else
        {
            texteCapsuleActuelle.setText(nomsCapsules[cafetiere.informerCapsuleActuelle()]);
            imageCapsuleActuelle.setImageResource(identifiantsImagesCapsules[cafetiere.informerCapsuleActuelle()]);
        }
        for (int i = 0; i < Cafetiere.NOMBRE_CAPSULE_MAX; ++i)
        {
            if(cafetiere.informerPresenceCapsule(i))
                listeSelectionCapsule.getChildAt(i).setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
            else
                listeSelectionCapsule.getChildAt(i).setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));
        }
        texteBoissonActuelle.setText(nomsBoisson[cafetiere.informerBoissonActuelle()]);
        imageBoissonActuelle.setImageResource(identifiantsImagesBoisson[cafetiere.informerBoissonActuelle()]);
    }

    /**
     * @brief Méthode qui permet d'actualiser la page "Informations"
     * @fn Ihm::actualiserPageInformations()
     */
    public void actualiserPageInformations()
    {
        Log.d(TAG, "actualiserPageInformations()");
        texteNbCafeJour.setText(String.valueOf(cafetiere.informerNombreCafeDuJour()));
        if(cafetiere.informerNombreCafeDuJour() >= Cafetiere.NB_MAX_CAFE_CONSEILLEE)
            texteNbCafeJour.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
        else
            texteNbCafeJour.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
        texteNomCafetiere.setText(cafetiere.informerNomCafetiere());
        texteNbCafe.setText(String.valueOf(cafetiere.informerNombreCafeTotal()));
        texteNbBac.setText(String.valueOf(cafetiere.informerNombreBacVide()));
        texteNbEau.setText(String.valueOf(cafetiere.informerNombreEauRemplie()));
        texteDureteEau.setText(String.valueOf(cafetiere.informerDureteeEau()));
        texteQualiteEau.setText(String.valueOf(cafetiere.informerQualiteEau()));
    }

    /**
     * @brief Méthode qui permet d'actualiser la page "Programmer"
     * @fn Ihm::actualiserPageProgrammer()
     */
    public void actualiserPageProgrammer()
    {
        Log.d(TAG, "actualiserPageProgrammer()");
        ArrayList<Programmation> programmations = new ArrayList<Programmation>();
        programmations.add(new Programmation(0,0, Programmation.Jours.LUNDI,"", Programmation.Frequences.UNE_SEULE_FOIS));
        if(cafetiere.listerProgrammations() != null)
            programmations.addAll(cafetiere.listerProgrammations());
        AdaptateurProgrammer adaptateurProgrammer = new AdaptateurProgrammer(this, programmations);
        listeProgrammer.setAdapter(adaptateurProgrammer);
    }

    /**
     * @brief Méthode qui permet d'actualiser la page "Parametres"
     * @fn Ihm::actualiserPageParametres()
     */
    public void actualiserPageParametres()
    {
        Log.d(TAG, "actualiserPageParametres()");
        parametresFenetreAProposParametres.setMessage("Nom de l'application : " + getString(R.string.nom_app) + "\n" +
                "Version de l'application : " + getString(R.string.version_app) + "\n" +
                "Version de la cafetière : " + cafetiere.informerVersionCafetiere() + "\n" +
                "Nom du dévelopeur : " + getString(R.string.nom_developeur) + "\n" +
                "Contact : " + getString(R.string.mail));
        fenetreAProposParametres = parametresFenetreAProposParametres.create();
    }

    /**
     * @brief Méthode qui permet d'afficher des messages
     * @fn Ihm::afficherMessage(String texte)
     * @param texte texte à afficher
     */
    public void afficherMessage(String texte)
    {
        Log.d(TAG, "afficherMessage()");
        Toast.makeText(getApplicationContext(), texte, Toast.LENGTH_LONG).show();
    }

    /**
     * @brief Méthode qui permet d'afficher des messages apres un retard
     * @fn Ihm::afficherMessageAvecRetard(String texte, int temps)
     * @param texte texte à afficher
     * @param temps temps du retard (en millisecondes)
     */
    public void afficherMessageAvecRetard(String texte, int temps)
    {
        Log.d(TAG, "afficherMessageAvecRetard()");
        new Thread()
        {
            @Override public void run()
            {
                try
                {
                    sleep(temps);
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toast.makeText(getApplicationContext(), texte, Toast.LENGTH_LONG).show();
                        }
                    });
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * @brief Méthode qui permet de réinitialiser une liste
     * @fn Ihm::reinitialiserListe(ListView liste)
     * @param liste la liste à réinitialiser
     */
    private void reinitialiserListe(ListView liste)
    {
        Log.d(TAG, "reinitialiserListe()");
        for(int i = 0; i < liste.getAdapter().getCount(); ++i)
        {
            liste.getChildAt(i).setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        }
    }

    /**
     * @brief Méthode qui permet d'envoyer une notification
     * @fn Ihm::envoyerNotification(Programmation programmation)
     * @param programmation la programmation
     */
    public void envoyerNotification(Programmation programmation)
    {
        Log.d(TAG, "envoyerNotification()");
        parametresNotification.setContentTitle("Votre programmation de " + programmation.obtenirHeure() + " est en cour de préparation.");
        parametresNotification.setContentText(nomsCapsules[programmation.obtenirCapsule()] + " - " + nomsBoisson[programmation.obtenirBoisson()]);
        gestionnaireNotification.notify(0, parametresNotification.build());
    }
}
