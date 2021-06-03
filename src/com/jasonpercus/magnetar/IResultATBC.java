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
 * Cette interface permet d'expliquer au client pourquoi celui-ci a été refusé. Cette méthode n'est pas utilisé si le client est accepté.
 * @author JasonPercus
 * @version 1.0
 */
public interface IResultATBC {
    
    
    
    // <editor-fold defaultstate="collapsed" desc="METHODE PUBLIC">
    /**
     * Le client a été refusé
     * @param code Correspond au code de refus de connexion (libre au programmeur de déterminer sa valeur != 0)
     */
    public void refusedToConnected(int code);
    // </editor-fold>
    
    
    
}