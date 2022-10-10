/*
 * Copyright (C) JasonPercus Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by JasonPercus, 10/2022
 */
package com.jasonpercus.magnetar;



/**
 * Cette énumération représente les différentes déconnexions d'un client par rapport à un serveur
 * @author JasonPercus
 * @version 1.0
 */
public enum ReasonDisconnection {
    
    
    
    /**
     * Le serveur a déconnecté le client
     */
    SERVER_DISCONNECTED_CLIENT,
    
    /**
     * Le client s'est déconnecté du serveur
     */
    CLIENT_DISCONNECTED_FROM_SERVER;
    
    
    
}
