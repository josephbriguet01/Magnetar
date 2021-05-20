/*
 * Copyright (C) BRIGUET Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Briguet, August 2018
 */
package com.jasonpercus.magnetar;



/**
 * Cette interface permet de communiquer entre une application cliente ou serveur et le client ou le serveur
 * @author BRIGUET
 * @version 1.0
 */
public interface IStatutClient {
    
    
    
    // <editor-fold defaultstate="collapsed" desc="METHODES PUBLICS">
    /**
     * Cette méthode est appelé lorsqu'un client vient de se connecter
     * @param identity Correspond à l'identité du client
     */
    public void hasConnected(Identity identity);
    
    /**
     * Cette méthode est appelé lorsqu'un client vient de se déconnecter
     * @param identity Correspond à l'identité du client
     * @param reason Correspond à la raison de la déconnexion
     */
    public void hasDisconnected(Identity identity, EDisconnectReason reason);
    // </editor-fold>
    
    
    
}