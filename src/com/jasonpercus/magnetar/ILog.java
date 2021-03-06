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
 * Cette interface permet à un client ou serveur de récupérer les logs (niveau serveur)
 * @author JasonPercus
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