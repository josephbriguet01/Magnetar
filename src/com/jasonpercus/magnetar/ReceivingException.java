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
 * Cette classe est l'une des classes primaires des exceptions de réception des messages
 * @author BRIGUET
 * @version 1.0
 */
public abstract class ReceivingException extends TCPException {
    
    
    
    // <editor-fold defaultstate="collapsed" desc="SERIAL_VERSION_UID">
    /**
     * Correspond au numéro de série qui identifie le type de dé/sérialization utilisé pour l'objet
     */
    private static final long serialVersionUID = 1L;
    // </editor-fold>

    
    
    // <editor-fold defaultstate="collapsed" desc="CONSTRUCTORS">
    /**
     * Crée une exception avec un message
     * @param message Correspond au message de l'exception
     */
    public ReceivingException(String message) {
        super(message);
    }
    
    /**
     * Crée une exception avec un message et une cause
     * @param message Correspond au message de l'exception
     * @param cause Correspond à la cause de l'exception
     */
    public ReceivingException(String message, Throwable cause) {
        super(message, cause);
    }
    // </editor-fold>
    
    
    
}