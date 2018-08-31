/*
 * Copyright (C) BRIGUET Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Briguet, Août 2018
 */
package org.tcp;



/**
 * Cette classe est la classe primaire des exceptions du projet TCP
 * @author BRIGUET
 * @version 1.0
 */
public abstract class TCPException extends RuntimeException{
    
    
    
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