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
 * Cette classe est la classe primaire des exceptions du projet TCP
 * @author JasonPercus
 * @version 1.0
 */
public abstract class TCPException extends RuntimeException {
    
    
    
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
    public TCPException(String message) {
        super(message);
    }
    
    /**
     * Crée une exception avec un message et une cause
     * @param message Correspond au message de l'exception
     * @param cause Correspond à la cause de l'exception
     */
    public TCPException(String message, Throwable cause) {
        super(message, cause);
    }
    // </editor-fold>
    
    
    
}