/*
 * Copyright (C) BRIGUET Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Briguet, December 2020
 */
package com.jasonpercus.magnetar;



/**
 * Cette exception apparait si une méthode n'es pas implémenté
 * @author BRIGUET
 * @version 1.0
 */
public class ErrorNoImplementation extends TCPException {
    
    
    
    // <editor-fold defaultstate="collapsed" desc="SERIAL_VERSION_UID">
    /**
     * Correspond au numéro de série qui identifie le type de dé/sérialization utilisé pour l'objet
     */
    private static final long serialVersionUID = 1L;
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="CONSTRUCTOR">
    /**
     * Ce constructeur crée une exception avec un message
     * @param message Correspond au message de l'exception
     */
    public ErrorNoImplementation(String message) {
        super(message);
    }
    // </editor-fold>
    
    
    
}