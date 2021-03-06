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
 * Cette exception apparait lorsqu'une erreur d'entrée/sortie de lecture survient
 * @author JasonPercus
 * @version 1.0
 */
public class ReceivingIOException extends ReceivingException {
    
    
    
    // <editor-fold defaultstate="collapsed" desc="SERIAL_VERSION_UID">
    /**
     * Correspond au numéro de série qui identifie le type de dé/sérialization utilisé pour l'objet
     */
    private static final long serialVersionUID = 1L;
    // </editor-fold>

    
    
    // <editor-fold defaultstate="collapsed" desc="CONSTRUCTORS">
    /**
     * Ce constructeur crée une exception avec un message
     * @param message Correspond au message de l'exception
     */
    public ReceivingIOException(String message) {
        super(message);
    }

    /**
     * Ce constructeur crée une exception avec un message et une cause
     * @param message Correspond au message de l'exception
     * @param cause Correspond à la cause de l'exception
     */
    public ReceivingIOException(String message, Throwable cause) {
        super(message, cause);
    }
    // </editor-fold>
    
    
    
}