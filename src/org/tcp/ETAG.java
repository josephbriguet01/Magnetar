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
 * Cette classe permet d'énumérer les différents type de Tag pour un Log
 * @author BRIGUET
 * @version 1.0
 */
public enum ETAG {
    
    
    
    /**
     * Un client s'est connecté
     */
    CONNEXION,
    /**
     * Un client s'est déconnecté
     */
    DECONNEXION,
    /**
     * Le serveur commence l'écoute des nouveaux clients
     */
    START_LISTENING,
    /**
     * Le serveur stoppe l'écoute des nouveaux clients
     */
    STOP_LISTENING,
    /**
     * Le serveur stoppe les services de chaque client connecté
     */
    STOP_SERVICES,
    /**
     * Le Log est de nature Sévère
     */
    SEVERE,
    /**
     * Le Log est de nature Attention
     */
    WARNING,
    /**
     * Le Log est de nature Normal
     */
    NORMAL,
    /**
     * Le Log est de nature Information
     */
    INFO,
    /**
     * Le Log est de nature Erreur
     */
    ERROR,
    /**
     * Le Log représente un envoie de métadonnées entre un serveur et un client
     */
    META,
    /**
     * Le serveur reçoit un objet d'un client
     */
    RECEIVED,
    /**
     * Le serveur reçoit la commande de copie des Log vers un client
     */
    REDIRECT,
    /**
     * Le serveur envoie un objet vers un client
     */
    SEND;
    
    
    
}