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
public interface IReceivedUDP {
    
    
    
    // <editor-fold defaultstate="collapsed" desc="METHODES PUBLICS">
    /**
     * Cette méthode est appelé lorsque l'on reçoit un tableau de byte d'un expediteur
     * @param data Correspond au tableau de byte reçu
     */
    public void received(byte[] data);
    
    /**
     * Cette méthode est appelé lorsque l'on reçoit un objet d'un expediteur
     * @param obj Correspond à l'objet reçu
     * @param statut Correspond à l'état de la trame reçu (OK, MALFORMED, ...)
     */
    public void received(Object obj, int statut);
    // </editor-fold>
    
    
    
}