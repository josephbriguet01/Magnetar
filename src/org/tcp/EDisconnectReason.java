/*
 * Copyright (C) BRIGUET Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Briguet, Août 2018
 */
package org.tcp;



/**
 * Cette classe permet de lister les raisons de déconnexion d'un client ou d'un serveur. Un objet EDisconnectReason est envoyé au client et au serveur dans la méthode hasDisconnected().
 * @author BRIGUET
 * @version 1.0
 */
public enum EDisconnectReason {
    
    
    
    // <editor-fold defaultstate="collapsed" desc="ATTRIBUTS STATICS">
    /**
     * Le serveur s'est fermé violemment
     */
    VIOLENTLY_SERVER_CLOSED("Server closed violently"),
    /**
     * Le client a perdu la connexion
     */
    VIOLENTLY_CLIENT_CLOSED("Client lost the connection"),
    /**
     * Le serveur a déconnecté le client
     */
    SERVER_DISCONNECTED_CLIENT("The server disconnected the client"),
    /**
     * Le client s'est déconnecté
     */
    CLIENT_DISCONNECTED("The client has disconnected"),
    /**
     * Déconnexion de raison inconnue
     */
    UNKNOWN_DISCONNECTION("Unknown disconnection");
    // </editor-fold>
   
    
    
    // <editor-fold defaultstate="collapsed" desc="ATTRIBUT">
    /**
     * Correspond à la raison de la déconnexion
     */
    private String reason = "";
    // </editor-fold>

    
    
    // <editor-fold defaultstate="collapsed" desc="CONSTRUCTOR">
    /**
     * Constructeur par défaut
     * @param reason Correspond à la raison de la déconnexion
     */
    EDisconnectReason(String reason) {
        this.reason = reason;
    }
    // </editor-fold>

    
    
    // <editor-fold defaultstate="collapsed" desc="METHODE PUBLIC">
    /**
     * Redéfinit la méthode toString()
     * @return Retourne le résultat de la méthode toString()
     */
    @Override
    public String toString() {
        return reason;
    }
    // </editor-fold>
    
    
    
}