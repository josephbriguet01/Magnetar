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
 * Cette classe permet d'énumérer les type de meta requête envoyé entre client serveur
 * @author jbriguet
 */
public enum EMetaType {
    
    
    
    // <editor-fold defaultstate="collapsed" desc="ATTRIBUTS STATICS">
    /**
     * Lorsqu'un client ou un serveur demande une déconnexion
     */
    META_REQUEST_DISCONNECT("Demande de déconnexion"),
    /**
     * Lorsqu'un client ou un serveur confirme la déconnexion
     */
    META_CONFIRM_DISCONNECT("Confirmation de demande de déconnexion"),
    /**
     * Lorsqu'un serveur autorise la connexion du client
     */
    META_CONNECTION_AUTHORIZED("La demande de connexion est autorisé"),
    /**
     * Lorsqu'un serveur refuse la connexion du client
     */
    META_CONNECTION_REJECTED("La demande de déconnexion est refusé"),
    /**
     * Lorsqu'un client demande une copie des Logs serveurs
     */
    META_REDIRECTION_LOG("Redirection de log");
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="ATTRIBUT">
    /**
     * Correspond au type d'une méta requête
     */
    private String meta = "";
    // </editor-fold>

    
    
    // <editor-fold defaultstate="collapsed" desc="CONSTRUCTOR">
    /**
     * Crée un type pour une méta requête
     * @param meta Correspond au type
     */
    EMetaType(String meta) {
        this.meta = meta;
    }
    // </editor-fold>

    
    
    // <editor-fold defaultstate="collapsed" desc="METHODE PUBLIC">
    /**
     * Redéfinit la méthode toString()
     * @return Retourne le résultat de la méthode toString()
     */
    @Override
    public String toString() {
        return meta;
    }
    // </editor-fold>
    
    
    
}