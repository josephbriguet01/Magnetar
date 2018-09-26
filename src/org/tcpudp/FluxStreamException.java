/*
 * Copyright (C) BRIGUET Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Briguet, August 2018
 */
package org.tcpudp;



/**
 * Cette exception apparait lorsqu'il y a une erreur de flux
 * @author BRIGUET
 * @version 1.0
 */
public class FluxStreamException extends TCPException{

    

    // <editor-fold defaultstate="collapsed" desc="CONSTRUCTOR">
    /**
     * Ce constructeur crée une exception avec un message
     * @param message Correspond au message de l'exception
     */
    public FluxStreamException(String message) {
        super(message);
    }
    // </editor-fold>
    
    
    
}