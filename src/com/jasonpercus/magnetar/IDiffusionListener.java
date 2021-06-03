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
 * Cette interface permet de recevoir les évènements liés à la détection d'une serveur sur le réseau
 * @author JasonPercus
 * @version 1.0
 */
public interface IDiffusionListener {
    
    
    
    // <editor-fold defaultstate="collapsed" desc="METHODES PUBLICS">
    /**
     * Lorsqu'une serveur vient d'être détecté sur le réseau ou au contraire vient de disparaitre
     * @param diffusion Correspond au serveur concerné
     * @param action Détermine si le serveur vient d'être trouvé (1) ou si au contraire il vient de disparaître du réseau (-1)
     */
    public void detectionServer(Diffusion diffusion, int action);
    
    /**
     * A chaque réception d'objet diffusion
     * @param diffusion Correspond à la partie récupéré
     */
    public void flux(Diffusion diffusion);
    // </editor-fold>
    
    
    
}