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
 * Cette Interface permet à une classe d'autoriser ou pas les clients qui se connectent au serveur
 * @author JasonPercus
 * @version 1.0
 */
public interface IConditionATBC {
    
    
    
    // <editor-fold defaultstate="collapsed" desc="METHODE PUBLIC">
    /**
     * Cette méthode est appelé pour vérifier qu'un utilisateur à le droit de se connecter
     * @param identity Correspond à l'identity qu'il faut vérifier
     * @return Retourne 0, si l'identity est autorisé à se connecter
     */
    public int authorizedToBeConnected(Identity identity);
    // </editor-fold>
    
    
    
}