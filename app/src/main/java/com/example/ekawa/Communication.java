package com.example.ekawa;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.Set;

/**
 * @file Communication.java
 * @brief Déclaration de la classe Communication
 * @author LECOMTE Jean-Luc
 * $LastChangedRevision: 113 $
 * $LastChangedDate: 2021-06-11 16:17:29 +0200 (ven. 11 juin 2021) $
 */

/**
 * @class Communication
 * @brief Permet la communication Bluetooth avec la cafetière
 */
public class Communication
{
    /**
     * Constantes
     */
    private static final String TAG = "Communication";                  //!< TAG pour les logs
    private final static String EKAWA = "ekawa-";                       //!< Le nom du périphérique bluetooth
    public final static String NOM_CAFETIERE_NON_CONNECTEE = "Aucune";  //!< Le nom de la cafetière si non-connectée

    /**
     * Attributs
     */
    private boolean activee = false;            //!< Indique si le bluetooth est activée
    private boolean connectee = false;          //!< Indique l'état de la connexion avec la cafetière
    private BluetoothAdapter bluetooth = null;  //!< L'adaptateur Bluetooth de la tablette
    private Peripherique peripherique = null;   //!< Le périphérique bluetooth distant
    private Ihm ihm;                            //!< L'ihm de l'application
    private Cafetiere cafetiere;                //!< Relation avec l'objet principal Cafetiere
    private ArrayList<Peripherique> listePeripheriques;
    private int peripheriqueActuel = 0;
    private boolean recherche = false;
    private boolean changementPeripherique = false;
    private Peripherique nouveauPeripherique;

    /**
     * @brief Traitement des messages en provenance des Threads
     */
    private final Handler handler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case Peripherique.CODE_CONNEXION:
                    connectee = true;
                    cafetiere.actualiserIHM();
                    peripherique.envoyer(Protocole.fabriquerTrameTestAlive());
                    break;
                case Peripherique.CODE_RECEPTION:
                    cafetiere.changerEtats(recevoirTrame((String) msg.obj));
                    break;
                case Peripherique.CODE_DECONNEXION:
                    connectee = false;
                    cafetiere.actualiserIHM();
                    break;
                case Peripherique.CODE_ERREUR_CONNEXION:
                    connectee = false;
                    cafetiere.actualiserIHM();
                    if(recherche)
                        ihm.afficherMessage(peripherique.obtenirNom() + " est indisponible. Veuillez sélectionner un autre périphérique.");
                    else
                    {
                        changementPeripherique = false;
                        ihm.afficherMessage("Erreur de connexion. Veuillez patienter !");
                        chercherCafetiere();
                    }
                    break;
            }
        }
    };

    /**
     * @brief Détecteur de changement d'état du bluetooth
     * @fn Communication::detectionChangementEtatBluetooth()
     * @return BroadcastReceiver le receveur de Bluetooth
     */
    private final BroadcastReceiver detectionChangementEtatBluetooth()
    {
        BroadcastReceiver receiverEtatBluetooth = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                final String action = intent.getAction();
                Log.d(TAG,"[detectionChangementEtatBluetooth] action : " + action);
                if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED))
                {
                    final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                    switch (state)
                    {
                        case BluetoothAdapter.STATE_OFF:
                            Log.d(TAG,"[detectionChangementEtatBluetooth] bluetooth désactivé !");
                            activee = false;
                            cafetiere.actualiserIHM();
                            break;
                        case BluetoothAdapter.STATE_TURNING_OFF:
                            Log.d(TAG,"[detectionChangementEtatBluetooth] bluetooth en cours de désactivation !");
                            break;
                        case BluetoothAdapter.STATE_ON:
                            Log.d(TAG,"[detectionChangementEtatBluetooth] bluetooth activé !");
                            activee = true;
                            cafetiere.actualiserIHM();
                            if(!recherche)
                                chercherCafetiere();
                            break;
                        case BluetoothAdapter.STATE_TURNING_ON:
                            Log.d(TAG,"[detectionChangementEtatBluetooth] bluetooth en cours d'activation !");
                            break;
                        default:
                            Log.d(TAG,"[detectionChangementEtatBluetooth] etat : " + state);
                            break;
                    }
                }
                else if (action.equals(BluetoothDevice.ACTION_ACL_DISCONNECTED))
                {
                    Log.d(TAG,"[detectionChangementEtatBluetooth] bluetooth déconnecté !");
                    connectee = false;
                    cafetiere.remettreAZero();
                    if(changementPeripherique)
                        connecterPeripherique();
                }
                else if(action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED))
                {
                    final int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
                    switch (state)
                    {
                        case BluetoothDevice.BOND_NONE:
                            Log.d(TAG,"[detectionChangementEtatBluetooth] cafetière non appairée !");
                            break;
                        case BluetoothDevice.BOND_BONDING:
                            break;
                        case BluetoothDevice.BOND_BONDED:
                            Log.d(TAG,"[detectionChangementEtatBluetooth] cafetière appairée !");
                            peripherique.connecter();
                            connectee = true;
                            break;
                    }
                }
            }
        };

        return receiverEtatBluetooth;
    }

    /**
     * @brief Détecteur de périphériques
     * @fn Communication::detectionPeripherique()
     * @return BroadcastReceiver le receveur de Bluetooth
     */
    private final BroadcastReceiver detectionPeripherique()
    {
        BroadcastReceiver receiverDetectionBluetooth = new BroadcastReceiver()
        {
            public void onReceive(Context context, Intent intent)
            {
                boolean appaire = false;

                String action = intent.getAction();
                Log.d(TAG, "[receiverDetectionBluetooth] action : " + action);

                if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action))
                {
                    Log.d(TAG, "[receiverDetectionBluetooth] découverte démarrée");
                    listePeripheriques = new ArrayList<>();
                }
                else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
                {
                    cafetiere.actualiserListePeripheriques();
                    if(recherche)
                    {
                        Log.d(TAG, "[receiverDetectionBluetooth] découverte terminée");
                        if (peripherique == null && !listePeripheriques.isEmpty())
                        {
                            changerPeripherique(peripheriqueActuel);
                            peripheriqueActuel++;
                        }
                        if (peripherique != null && !peripherique.estConnecte())
                        {

                        }
                        recherche = false;
                    }
                }
                else if (BluetoothDevice.ACTION_FOUND.equals(action))
                {
                    if(recherche)
                    {
                        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        Log.d(TAG, "[receiverDetectionBluetooth] device détecté : " + device.getName() + " [" + device.getAddress() + "]");
                        if (device.getName() != null && device.getName().startsWith(EKAWA))
                        {
                            if(listePeripheriques.isEmpty())
                                listePeripheriques.add(new Peripherique(device, handler));
                            if(!device.getAddress().equals(listePeripheriques.get(listePeripheriques.size()-1).obtenirAdresse()))
                                listePeripheriques.add(new Peripherique(device, handler));
                        }
                    }
                }
            }
        };

        return receiverDetectionBluetooth;
    };

    /**
     * @brief Constructeur de la classe Communication
     * @fn Communication::Communication(AppCompatActivity activity, Cafetiere cafetiere)
     * @param ihm l'ihm de l'activité
     * @param cafetiere la cafetière actuelle
     */
    public Communication(Ihm ihm, Cafetiere cafetiere)
    {
        Log.d(TAG,"Communication()");
        this.ihm = ihm;
        this.cafetiere = cafetiere;
        bluetooth = BluetoothAdapter.getDefaultAdapter();
        if(bluetooth != null)
        {
            installerDetectionEtatBluetooth();
            installerRecherchePeripheriques();
            activer();
        }
        else
        {
            Log.d(TAG,"Pas de bluetooth ?");
        }
    }

    /**
     * @brief Méthode qui permet d'allumer le bluetooth
     * @fn Communication::activer()
     */
    public void activer()
    {
        Log.d(TAG, "activer()");
        if (!bluetooth.isEnabled())
            bluetooth.enable();
        activee = true;
        connecter();
    }

    /**
     * @brief Méthode qui permet de connecter le bluetooth à la cafetière
     * @fn Communication::connecter()
     */
    public void connecter()
    {
        Log.d(TAG, "connecter()");
        if(bluetooth.isEnabled() && !recherche)
        {
            chercherCafetiere();
        }
    }

    /**
     * @brief Méthode qui permet d'éteindre le bluetooth
     * @fn Communication::desactiver()
     */
    public void desactiver()
    {
        if(bluetooth.isEnabled())
        {
            deconnecter();
            bluetooth.disable();
            activee = false;
        }
    }

    /**
     * @brief Méthode qui permet de déconnecter le bluetooth de la cafetière
     * @fn Communication::deconnecter()
     */
    public void deconnecter()
    {
        if(bluetooth.isEnabled())
            peripherique.deconnecter();
        connectee = false;
    }

    /**
     * @brief Méthode qui retourne l'état de la connection avec la cafetière
     * @fn Communication::estConnectee()
     * @return boolean la vérification
     */
    public boolean estConnectee()
    {
        return connectee;
    }

    /**
     * @brief Méthode qui retourne si le bluetooth est activé ou non
     * @fn Communication::estActivee()
     * @return boolean la vérification
     */
    public boolean estActivee()
    {
        return activee;
    }

    /**
     * @brief Méthode qui permet de chercher da cafetière dans les périphérique bluetooth apparié
     * @fn Communication::chercherCafetiere()
     */
    private void chercherCafetiere()
    {
        Set<BluetoothDevice> devices;
        recherche = true;
        listePeripheriques = new ArrayList<>();

        if(peripherique != null)
            peripherique = null;

        devices = bluetooth.getBondedDevices();
        for(BluetoothDevice blueDevice : devices)
        {
            Log.d(TAG,"[chercherCafetiere] device : " + blueDevice.getName() + " [" + blueDevice.getAddress() + "]");
            if(blueDevice.getName().startsWith(EKAWA))
            {
                Log.d(TAG,"[chercherCafetiere] cafetière : " + blueDevice.getName() + " [" + blueDevice.getAddress() + "]");
                creerPeripherique(blueDevice);
                peripherique.connecter();
                connectee = true;
                ihm.afficherMessage("Cafetière : " + peripherique.obtenirNom());
                break;
            }
        }

        Log.d(TAG,"[chercherCafetiere] cafetière ekawa non trouvée !");
        demarrerRecherche();
    }

    /**
     * @brief Méthode qui permet d'envoyer des trames à la cafetière
     * @fn Communication::envoyerTrame(String trame)
     * @param trame Trame à envoyer
     */
    public void envoyerTrame(String trame)
    {
        Log.v(TAG, "envoyerTrame()");
        if(peripherique != null)
            peripherique.envoyer(trame);
    }

    /**
     * @brief Méthode qui permet de recevoir des trames de la cafetière
     * @fn Communication::recevoirTrame(String trame)
     * @param trame Trame reçue
     * @return String la trame reçue
     */
    public String recevoirTrame(String trame)
    {
        Log.d(TAG," Recu  : " + " " + peripherique.obtenirNom() + " " + trame);
        if(verifierTrame(trame))
            return trame;
        return null;
    }

    /**
     * @brief Méthode qui permet de vérifier la trame reçue
     * @fn Communication::verifierTrame(String trame)
     * @param trame Trame reçue
     * @return boolean la vérification
     */
    private boolean verifierTrame(String trame)
    {
        if(trame.startsWith(Protocole.DEBUT_TRAME))
            return true;
        return false;
    }

    /**
     * @brief Méthode qui installe la détection des changements d'états bluetooth
     * @fn Communication::installerDetectionEtatBluetooth()
     */
    private void installerDetectionEtatBluetooth()
    {
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        ihm.getApplicationContext().registerReceiver(detectionChangementEtatBluetooth(), filter);
    }

    private void installerRecherchePeripheriques()
    {
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        ihm.getApplicationContext().registerReceiver(detectionPeripherique(), filter);
    }

    /**
     * @brief Méthode qui permet de lancer la recherche de périphérique non apparié
     * @fn Communication::demarrerRecherche()
     */
    private void demarrerRecherche()
    {
        if(bluetooth.isDiscovering())
            bluetooth.cancelDiscovery();
        boolean etatDemarrageDecouverte = bluetooth.startDiscovery();
        Log.d(TAG,"[chercherCafetiere] démarrage découverte bluetooth " + etatDemarrageDecouverte);
    }

    /**
     * @brief Méthode qui permet de créer le périphérique
     * @fn Communication::creerPeripherique(BluetoothDevice peripherique)
     */
    private void creerPeripherique(BluetoothDevice peripherique)
    {
        Log.d(TAG,"[definirPeripherique] nom : " + peripherique.getName());
        if(this.peripherique != null)
        {
            this.peripherique.deconnecter();
            connectee = false;
            this.peripherique = null;
        }
        this.peripherique = new Peripherique(peripherique, handler);
    }

    /**
     * @brief Méthode qui retourne le nom du périphérique
     * @fn Communication::obtenirNomPeripherique()
     * @return String le nom du périphérique
     */
    public String obtenirNomPeripherique()
    {
        if(peripherique != null)
            return peripherique.obtenirNom();
        return null;
    }

    public ArrayList<Peripherique> listerPeripheriques()
    {
        if(listePeripheriques != null)
            return listePeripheriques;
        return new ArrayList<Peripherique>();
    }

    public void changerPeripherique(int position)
    {
        changementPeripherique = true;
        nouveauPeripherique = listePeripheriques.get(position);
        if (peripherique != null)
        {
            if (peripherique.estConnecte())
            {
                peripherique.deconnecter();
                connectee = false;
            }
            else
                connecterPeripherique();
        }
        else
            connecterPeripherique();
    }

    public void connecterPeripherique()
    {
        cafetiere.actualiserListePeripheriques();
        peripherique = nouveauPeripherique;
        if (peripherique.obtenirBluetoothDevice().getBondState() == BluetoothDevice.BOND_BONDED)
        {
            peripherique.connecter();
            connectee = true;
            return;
        }
        else if (peripherique.obtenirBluetoothDevice().getBondState() == BluetoothDevice.BOND_NONE)
            if (Build.VERSION.SDK_INT >= 19)
                peripherique.obtenirBluetoothDevice().createBond();
        changementPeripherique = false;
    }
}