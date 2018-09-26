/*
 * Copyright (C) BRIGUET Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Briguet, September 2018
 */
package org.tcpudp;



/**
 * Cette classe permet de créer une Blacklist
 * @author Briguet
 * @version 1.0
 */
public class BlackList extends BAWList {

    
    
    /**
     * Cette variable détermine que le client a été refusé de connexion car il fait partie de la Blacklist
     */
    public static int REFUSED = 102;
    
    
    
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
    
    
    
}