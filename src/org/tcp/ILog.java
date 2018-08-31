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
 * Cette interface permet à un client ou serveur de récupérer les logs (niveau serveur)
 * @author BRIGUET
 * @version 1.0
 */
public interface ILog {
    
    
    
    // <editor-fold defaultstate="collapsed" desc="METHODE PUBLIC">
    /**
     * Récupère un log
     * @param log Correspond à un log qui vient d'arriver
     */
    public void readLog(Log log);
    // </editor-fold>
    
    
    
}