/*
 * Copyright (C) JasonPercus Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by JasonPercus, 10/2022
 */
package com.jasonpercus.magnetar.protocol;



/**
 * Cette classe représente le protocol TCP
 * @author JasonPercus
 * @version 1.0
 */
public class ProtocolTCP extends Protocol {

    
    
//CONSTRUCTOR
    /**
     * Crée le protocol TCP
     */
    public ProtocolTCP() {
    }

    
    
//METHODE PUBLIC
    /**
     * Renvoie le nom du protocol réseau
     * @return Retourne le nom du protocol réseau
     */
    @Override
    public String getProtocolName() {
        return "TCP";
    }
    
    
    
}