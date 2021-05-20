/*
 * Copyright (C) BRIGUET Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Briguet, September 2018
 */
package com.jasonpercus.magnetar;



/**
 * Cette classe permet de créer une Blacklist
 * @author Briguet
 * @version 1.0
 */
public class BlackList extends BAWList {
    
    
    
    // <editor-fold defaultstate="collapsed" desc="SERIAL_VERSION_UID">
    /**
     * Correspond au numéro de série qui identifie le type de dé/sérialization utilisé pour l'objet
     */
    private static final long serialVersionUID = 1L;
    // </editor-fold>

    
    
    // <editor-fold defaultstate="collapsed" desc="ATTRIBUT STATIC">
    /**
     * Cette variable détermine que le client a été refusé de connexion car il fait partie de la Blacklist
     */
    public static int REFUSED = 102;
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="METHODE PUBLIC">
    /**
     * Renvoie 0 si le client ne fait pas partie de la Blacklist, sinon 101
     * @param identity Correspond à l'identité dont on veut vérifier si oui ou non elle a le droit de se connecter au serveur
     * @return Retourne 0 si le client ne fait pas partie de la Blacklist, sinon 101
     */
    @Override
    public int authorizeOrNot(Identity identity) {
        if(identity instanceof Identity2){
            Identity2 i = (Identity2) identity;
            if(!contains(new BAWElement(i.getMac()))){
                return 0;
            }else{
                return REFUSED;
            }
        }else{
            return 0;
        }
    }
    // </editor-fold>
    
    
    
}