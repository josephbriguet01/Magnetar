/*
 * Copyright (C) BRIGUET Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Briguet, September 2018
 */
package com.jasonpercus.magnetar;



/**
 * Cette exception apparait lorsqu'il y a une erreur de découverte réseau
 * @author BRIGUET
 * @version 1.0
 */
public class ErrorDiscoverException extends TCPException{

    
    
    // <editor-fold defaultstate="collapsed" desc="CONSTRUCTOR">
    /**
     * Ce constructeur crée une exception avec un message
     * @param message Correspond au message de l'exception
     */
    public ErrorDiscoverException(String message) {
        super(message);
    }
    // </editor-fold>
    
    
    
}