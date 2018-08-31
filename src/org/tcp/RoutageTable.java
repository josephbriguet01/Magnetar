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
 * Cette classe permet de créer une liste qui stocke des services. Cette liste est indispensable pour le serveur
 * @author BRIGUET
 * @version 1.0
 */
public class RoutageTable extends ListSynchronized<Service>{

    
    
    // <editor-fold defaultstate="collapsed" desc="CONSTRUCTOR">
    /**
     * Crée une liste permettant de stocker des services
     */
    protected RoutageTable() {
        super();
    }
    // </editor-fold>

    
    
    // <editor-fold defaultstate="collapsed" desc="METHODE PUBLIC">
    @Override
    public String toString() {
        String chain = "";
        for(int i=0;i<size();i++){
            Service s = get(i);
            if(i>0) chain +=", ";
            chain+=s.identity_other;
        }
        return "RoutageTable{" +chain+ '}';
    }
    // </editor-fold>
 
    
    
}