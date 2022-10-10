/*
 * Copyright (C) JasonPercus Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by JasonPercus, 10/2022
 */
package com.jasonpercus.magnetar;



import com.jasonpercus.magnetar.protocol.Protocol;



/**
 * Cette interface permet à une classe de définir le protocol utilisé pour son fonctionnement interne
 * @author JasonPercus
 * @version 1.0
 */
public interface Protocollable {
    
    
    
//METHODE PUBLIC
    /**
     * Renvoie le protocol utilisé
     * @return Retourne le protocol utilisé
     */
    public Protocol getProtocolName();
    
    
    
}