/*
 * Copyright (C) BRIGUET Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Briguet, August 2018
 */
package org.tcpudp;



/**
 * Cette interface permet de communiquer entre une application cliente ou serveur et le client ou le serveur
 * @author BRIGUET
 * @version 1.0
 */
public interface IStatutServer extends IStatutClient{
    
    
    
    // <editor-fold defaultstate="collapsed" desc="METHODES PUBLICS">
    /**
     * Cette méthode est appelé pour prévenir l'application serveur que celui-ci à démarré
     */
    public void serverIsStart();
    
    /**
     * Cette méthode est appelé pour prévenir l'application serveur que celui-ci à stoppé
     */
    public void serverIsStop();
    // </editor-fold>
    
    
    
}