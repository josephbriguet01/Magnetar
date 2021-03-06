/*
 * Copyright (C) JasonPercus Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by JasonPercus, August 2018
 */
package com.jasonpercus.magnetar;



/**
 * Cette interface permet de communiquer entre une application cliente ou serveur et le client ou le serveur
 * @author JasonPercus
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