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
 * Cette classe permet de gérer certains comportements basiques de communication entre le serveur et le client
 * @author BRIGUET
 * @version 1.0
 */
public final class MetaTCP implements java.io.Serializable{

    
    
    // <editor-fold defaultstate="collapsed" desc="CONSTRUCTORS">
    /**
     * Crée un objet MetaTCP ayant une valeur null
     * @param meta Correspond à la valeur d'un type de communication
     */
    protected MetaTCP(EMetaType meta) {
        this.meta = meta;
        this.value = null;
    }

    /**
     * Crée un objet MetaTCP
     * @param meta Correspond à la valeur d'un type de communication
     * @param value Correspond à une valeur indispensable utilisé pour la communication
     */
    protected MetaTCP(EMetaType meta, Object value) {
        this.meta = meta;
        this.value = value;
    }
    // </editor-fold>

    
    
    // <editor-fold defaultstate="collapsed" desc="METHODES PUBLICS">
    /**
     * Renvoie la valeur du type de communication
     * @return Retourne la valeur
     */
    public EMetaType getMeta() {
        return meta;
    }

    /**
     * Renvoie la valeur de la communication
     * @return Retourne la valeur
     */
    public Object   getValue() {
        return value;
    }
    
    /**
     * Redéfinit la méthode toString()
     * @return Retourne le résultat de la méthode
     */
    @Override
    public String   toString() {
        return "MetaTCP{" + "meta=" + meta + ", value=" + value + '}';
    }

    /**
     * Redéfinit la méthode hashCode()
     * @return Retourne le résultat de la méthode hashCode()
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + java.util.Objects.hashCode(this.meta);
        hash = 37 * hash + java.util.Objects.hashCode(this.value);
        return hash;
    }

    /**
     * Redéfinit la méthode equals()
     * @param obj Correspond à l'objet à comparer
     * @return Retourne le résultat de la méthode equals()
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MetaTCP other = (MetaTCP) obj;
        if (this.meta != other.meta) {
            return false;
        }
        return java.util.Objects.equals(this.value, other.value);
    }
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="ATTRIBUTS">
    /**
     * Correspond au type de trame de controle envoyé
     */
    private final EMetaType meta;
    /**
     * Correspond à une éventuelle valeur
     */
    private final Object   value;
    // </editor-fold>
    
    
    
}