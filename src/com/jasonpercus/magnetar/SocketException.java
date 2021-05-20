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
 * Cette exception apparait lorsqu'une erreur de socket survient entre le serveur et le client
 * @author BRIGUET
 * @version 1.0
 */
public class SocketException extends TCPException{
    
    

    // <editor-fold defaultstate="collapsed" desc="CONSTRUCTOR">
    /**
     * Ce constructeur crée une exception avec un message
     * @param message Correspond au message de l'exception
     */
    public SocketException(String message) {
        super(message);
    }
    
    /**
     * Crée une exception avec un message et une cause
     * @param message Correspond au message de l'exception
     * @param cause Correspond à la cause de l'exception
     */
    public SocketException(String message, Throwable cause) {
        super(message, cause);
    }
    // </editor-fold>
    
    
    
}