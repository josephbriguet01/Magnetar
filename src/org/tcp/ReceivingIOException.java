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
 * Cette exception apparait lorsqu'une erreur d'entrée/sortie de lecture survient
 * @author BRIGUET
 * @version 1.0
 */
public class ReceivingIOException extends ReceivingException{

    
    
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