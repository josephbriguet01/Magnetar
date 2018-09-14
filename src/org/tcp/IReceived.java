/*
 * Copyright (C) BRIGUET Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Briguet, August 2018
 */
package org.tcp;



/**
 * Cette interface permet de communiquer entre une application cliente ou serveur et le client ou le serveur
 * @author BRIGUET
 * @version 1.0
 */
public interface IReceived {
    
    
    
    // <editor-fold defaultstate="collapsed" desc="METHODE PUBLIC">
    /**
     * Cette méthode est appelé lorsque l'on reçoit un objet d'un expediteur
     * @param expeditor Correspond à l'expéditeur de l'envoi
     * @param obj Correspond à l'objet reçu
     */
    public void received(Identity expeditor, Object obj);
    // </editor-fold>
    
    
    
}