/*
 * Copyright (C) BRIGUET Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Briguet, August 2018
 */
package org.tcp;



/**
 * Cette Interface permet à une classe d'autoriser ou pas les clients qui se connectent au serveur
 * @author BRIGUET
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