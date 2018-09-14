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
 * Cette exception apparait lorsq'il y a une erreur de socket d'écoute de nouveaux client
 * @author BRIGUET
 * @version 1.0
 */
public class ServerSocketException extends TCPException{

    
    
    // <editor-fold defaultstate="collapsed" desc="CONSTRUCTOR">
    /**
     * Ce constructeur crée une exception avec un message
     * @param message Correspond au message de l'exception
     */
    public ServerSocketException(String message) {
        super(message);
    }
    
    /**
     * Crée une exception avec un message et une cause
     * @param message Correspond au message de l'exception
     * @param cause Correspond à la cause de l'exception
     */
    public ServerSocketException(String message, Throwable cause) {
        super(message, cause);
    }
    // </editor-fold>
    
    
    
}