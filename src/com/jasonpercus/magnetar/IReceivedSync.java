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
public interface IReceivedSync extends IReceived {
    
    
    
    // <editor-fold defaultstate="collapsed" desc="METHODE PUBLIC">
    /**
     * Cette méthode est appelé lorsque l'on reçoit un objet d'un expediteur et que celui-ci attend une réponse en mode synchrone
     * @param expeditor Correspond à l'expéditeur de l'envoi
     * @param obj Correspond à l'objet reçu
     * @return Retourne la réponse à l'expéditeur
     */
    public Object receivedSync(Identity expeditor, Object obj);
    // </editor-fold>
    
    
    
}