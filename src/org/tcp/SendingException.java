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
 * Cette exception apparait lorsqu'une erreur d'envoi est détectée
 * @author BRIGUET
 * @version 1.0
 */
public class SendingException extends TCPException{

    
    
    // <editor-fold defaultstate="collapsed" desc="CONSTRUCTOR">
    /**
     * Ce constructeur crée une exception avec un message
     * @param message Correspond au message de l'exception
     */
    public SendingException(String message) {
        super(message);
    }
    // </editor-fold>
    
    
    
}