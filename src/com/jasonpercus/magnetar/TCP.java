/*
 * Copyright (C) JasonPercus Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by JasonPercus, 10/2022
 */
package com.jasonpercus.magnetar;



import com.jasonpercus.magnetar.protocol.Protocol;
import com.jasonpercus.magnetar.protocol.ProtocolTCP;



/**
 * Cette classe abstraite représente une entité TCP (ex: client, serveur, ...)
 * @author JasonPercus
 * @version 1.0
 */
public abstract class TCP implements Protocollable {
    
    
    
//CONSTANT
    /**
     * Correspond à la valeur  -1 (EOS utilisé généralement pour marqué la fin d'un flux, genre {@link java.io.InputStream})
     */
    public final static int EOS = -1;
    
    
    
//ATTRIBUTS
    /**
     * Correspond à l'adresse ip et port de l'entité TCP
     */
    protected java.net.InetSocketAddress address;
    
    /**
     * Détermine si à la déconnexion d'un client la valeur -1 (EOS) doit être envoyée (comme à l'identique des {@link java.io.InputStream})
     */
    protected boolean receiveEOS;

    
    
//CONSTRUCTOR
    /**
     * Crée une entité TCP
     * @param address Correspond à l'adresse ip et port de l'entité TCP
     */
    public TCP(java.net.InetSocketAddress address) {
        this.address = address;
    }

    
    
//GETTER & SETTER
    /**
     * Détermine si à la déconnexion d'un client la valeur -1 (EOS) doit être envoyée (comme à l'identique des {@link java.io.InputStream})
     * @return Retourne true s'il faut envoyer la valeur -1 (EOS)
     */
    public boolean isReceiveEOS() {
        return receiveEOS;
    }

    /**
     * Modifie si oui ou non à la déconnexion d'un client la valeur -1 (EOS) doit être envoyée (comme à l'identique des {@link java.io.InputStream})
     * @param receiveEOS True s'il faut envoyer la valeur -1 (EOS), sinon false
     */
    public void setReceiveEOS(boolean receiveEOS) {
        this.receiveEOS = receiveEOS;
    }
    
    
    
//EVTS
    /**
     * Crée une nouvelle connexion (à définir)
     * @return Retourne la connexion créée
     */
    protected abstract Connection newConnection();
    
    /**
     * Lorsque le client est connecté
     * @param connection Correspond à la connexion du client
     */
    protected void clientConnected(Connection connection){
        
    }
    
    /**
     * Lorsque le client est déconnecté
     * @param connection Correspond à la connexion du client
     * @param reason Correspond à la raison de la déconnexion
     */
    protected void clientDisconnected(Connection connection, ReasonDisconnection reason){
        
    }
    
    /**
     * Lorsque la socket cliente est acceptée
     * @param socket Correspond à la socket qui a été acceptée
     */
    protected void accepted(java.nio.channels.SocketChannel socket){
        
    }
    
    /**
     * Lorsque la socket cliente a été enlevé du circuit du serveur
     * @param socket Correspond à la socket qui a été enlevée
     */
    protected void removed(java.nio.channels.SocketChannel socket) {
        
    }

    
    
//METHODE PUBLIC
    /**
     * Renvoie le protocol utilisé
     * @return Retourne le protocol utilisé
     */
    @Override
    public Protocol getProtocolName() {
        return new ProtocolTCP();
    }
    
    
    
    
}