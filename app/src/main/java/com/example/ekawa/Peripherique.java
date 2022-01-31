package com.example.ekawa;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import android.os.Handler;
import android.os.Message;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.UUID;

/**
 * @file Peripherique.java
 * @brief Déclaration de la classe Peripherique
 * @author LECOMTE Jean-Luc
 * $LastChangedRevision: 111 $
 * $LastChangedDate: 2021-06-10 08:32:18 +0200 (jeu. 10 juin 2021) $
 */

/**
 * @class Peripherique
 * @brief Permet le dialogue avec le périphérique Bluetooth de la cafetière
 */
public class Peripherique
{
    private static final String TAG = "Peripherique";   //!< TAG pour les logs

    public final static int CODE_CONNEXION = 0;         //!< Le code de connexion
    public final static int CODE_RECEPTION = 1;         //!< Le code de réception
    public final static int CODE_DECONNEXION = 2;       //!< Le code de déconnexion
    public final static int CODE_ERREUR_CONNEXION = 3;  //!< Le code d'érreur de connexion
    public final static int CODE_ERREUR_DECONNEXION = 4;//!< Le code d'érreur de déconnexion

    private String nom;                                 //!< Le nom du périphérique
    private String adresse;                             //!< L'adresse MAC du périphérique
    private BluetoothDevice peripherique = null;        //!< Le périphérique

    private BluetoothSocket socket = null;              //!!< Le socket de connection
    private InputStream receiveStream = null;           //!< Le flux de données entrant
    private OutputStream sendStream = null;             //!< Le flux de données sortant

    private Reception reception = null;                 //!< La réception des données

    private Handler handler;                            //!< La gestionnaire des messages

    /**
     * @brief Constructeur de la classe Peripherique
     * @fn Peripherique::Peripherique(BluetoothDevice peripherique, Handler handler)
     */
    public Peripherique(BluetoothDevice peripherique, Handler handler)
    {
        Log.d(TAG,"Peripherique()");
        if (peripherique != null)
        {
            this.peripherique = peripherique;
            this.nom = peripherique.getName();
            this.adresse = peripherique.getAddress();
            this.handler = handler;

            try
            {
                socket = peripherique.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                receiveStream = socket.getInputStream();
                sendStream = socket.getOutputStream();
            }
            catch(IOException e)
            {
                e.printStackTrace();
                socket = null;
                Log.d(TAG,"Erreur création socket !");
            }
        }
        else
        {
            this.peripherique = peripherique;
            this.nom = "Aucun";
            this.adresse = "";
            this.handler = null;
        }

        if(socket != null)
        {
            reception = new Reception(this, handler);
            Log.v(TAG, "Périphérique " + obtenirNom() + " prêt");
        }
    }

    /**
     * @brief Méthode qui permet de changer le gestionnaire des messages
     * @fn Peripherique::changerHandler(Handler handler)
     * @param handler le gestionnaire des messages
     */
    public void changerHandler(Handler handler)
    {
        this.handler = handler;
    }

    /**
     * @brief Méthode qui permet de connecter le bluetooth à la cafetière
     * @fn Peripherique::connecter()
     */
    public void connecter()
    {
        if(peripherique == null)
            return;

        new Thread()
        {
            @Override public void run()
            {
                try
                {
                    if(socket != null)
                    {
                        if(!socket.isConnected())
                        {
                            Log.d(TAG, "Socket connexion à " + obtenirNom());
                            socket.connect();
                        }

                        if(socket.isConnected())
                        {
                            Log.d(TAG, "Socket connecté à " + obtenirNom());
                            if(handler != null)
                            {
                                Message msg = Message.obtain();
                                msg.what = CODE_CONNEXION;
                                handler.sendMessage(msg);
                            }
                        }

                        if (reception != null)
                        {
                            reception.start();
                            Log.d(TAG, "Démarrage thread réception");
                        }
                    }
                }
                catch (IOException e)
                {
                    Log.d(TAG,"Erreur connexion socket !");
                    e.printStackTrace();

                    Message msg = Message.obtain();
                    msg.what = CODE_ERREUR_CONNEXION;
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    /**
     * @brief Méthode qui permet de déconnecter le bluetooth de la cafetière
     * @fn Peripherique::deconnecter()
     */
    public void deconnecter()
    {
        if(peripherique == null)
            return;

        new Thread()
        {
            @Override public void run()
            {
                try
                {
                    if (reception != null)
                    {
                        Log.d(TAG,"Arrêt thread réception");
                        reception.arreter();
                        receiveStream.close();
                        sendStream.close();
                        if (socket != null)
                        {
                            Log.d(TAG, "Socket déconnexion de " + obtenirNom());
                            socket.close();

                            if (!socket.isConnected())
                            {
                                Log.d(TAG, "Socket déconnecté de " + obtenirNom());
                                if (handler != null)
                                {
                                    Message msg = Message.obtain();
                                    msg.what = CODE_DECONNEXION;
                                    handler.sendMessage(msg);
                                }
                            }
                        }
                    }
                }
                catch (IOException e)
                {
                    Log.d(TAG,"Erreur fermeture socket !");
                    e.printStackTrace();

                    Message msg = Message.obtain();
                    msg.what = CODE_ERREUR_DECONNEXION;
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    /**
     * @brief Méthode qui permet d'envoyer des trames à la cafetière
     * @fn Peripherique::envoyer(String trame)
     * @param trame la trame à envoyer
     * @return boolean la vérification
     */
    public boolean envoyer(String trame)
    {
        final String trameEnvoyee = trame;

        if(socket == null)
            return false;

        if(!socket.isConnected())
            return false;

        new Thread()
        {
            @Override public void run()
            {
                try
                {
                    if(socket.isConnected())
                    {
                        sendStream.write(trameEnvoyee.getBytes());
                        sendStream.flush();
                        Log.d(TAG, "Envoyer : " + " " + nom + " " + trameEnvoyee);
                    }
                }
                catch (IOException e)
                {
                    Log.d(TAG,"Erreur écriture socket !");
                    e.printStackTrace();
                }
            }
        }.start();

        return true;
    }

    /**
     * @brief Méthode qui renvoie si le périphérique est connecté ou non
     * @fn Peripherique::estConnecte()
     * @return boolean la vérification
     */
    public boolean estConnecte()
    {
        if(socket != null)
            return socket.isConnected();
        return false;
    }

    /**
     * @brief Méthode qui renvoie le nom du périphérique
     * @fn Peripherique::obtenirNom()
     * @return String le nom du périphérique
     */
    public String obtenirNom()
    {
        return nom;
    }


    /**
     * @brief Méthode qui renvoie l'adresse du périphérique
     * @fn Peripherique::obtenirAdresse()
     * @return String l'adresse du périphérique
     */
    public String obtenirAdresse()
    {
        return adresse;
    }

    /**
     * @brief Méthode qui renvoie le flux de données entrant
     * @fn Peripherique::obtenirFluxReception()
     * @return InputStream le flux de données entrant
     */
    public InputStream obtenirFluxReception()
    {
        return receiveStream;
    }

    /**
     * @brief Méthode qui renvoie le BluetoothDevice du périphérique
     * @fn Peripherique::obtenirBluetoothDevice()
     * @return String le BluetoothDevice du périphérique
     */
    public BluetoothDevice obtenirBluetoothDevice()
    {
        return peripherique;
    }
}