package com.example.ekawa;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @file Reception.java
 * @brief Déclaration de la classe Reception
 * @author LECOMTE Jean-Luc
 * $LastChangedRevision: 101 $
 * $LastChangedDate: 2021-05-28 11:43:56 +0200 (ven. 28 mai 2021) $s
 */

/**
 * @class Reception
 * @brief Permet la réception des trames du périphérique Bluetooth de la cafetière
 */
public class Reception extends Thread
{
    private static final String TAG = "Reception";  //!< TAG pour les logs
    private Peripherique peripherique;              //!< Le nom du périphérique
    private Handler handler;                        //!< La gestionnaire des messages
    private boolean fini = false;                   //!< Etat de la réception

    /**
     * @brief Constructeur de la classe Peripherique
     * @fn Reception::Reception(Peripherique peripherique, Handler handler)
     */
    public Reception(Peripherique peripherique, Handler handler)
    {
        this.peripherique = peripherique;
        this.handler = handler;
    }

    /**
     * @brief Méthode qui permet de recevoir des trames de la cafetière (thread)
     * @fn Reception::run()
     */
    @Override
    public void run()
    {
        Log.d(TAG, "Thread réception démarré");
        BufferedReader reception = new BufferedReader(new InputStreamReader(peripherique.obtenirFluxReception()));
        while(!fini)
        {
            try
            {
                String trame = "";
                if(reception.ready())
                {
                    trame = reception.readLine(); // Récupère la trame reçue sans les délimiteurs de fin (\r\n)
                }
                if(trame.length() > 0)
                {
                    Log.d(TAG, "run() trame : " + trame);
                    if(handler != null)
                    {
                        Message msg = Message.obtain();
                        msg.what = Peripherique.CODE_RECEPTION;
                        msg.obj = trame;
                        handler.sendMessage(msg);
                    }
                }
            }
            catch (IOException e)
            {
                Log.d(TAG, "Erreur lecture socket !");
                e.printStackTrace();
            }
            try
            {
                Thread.sleep(250);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "Thread réception arrêté");
    }

    /**
     * @brief Méthode qui permet d'arrêter la réception de trame
     * @fn Reception::arreter()
     */
    public void arreter()
    {
        if (fini == false)
        {
            fini = true;
        }
        try
        {
            Thread.sleep(250);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}