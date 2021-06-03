/*
 * Copyright (C) JasonPercus Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by JasonPercus, August 2018
 */
package com.jasonpercus.magnetar;



import com.jasonpercus.network.MACAddress;



/**
 * Cette classe permet de créer des objets BAWElement qui détermine si un utilisateur à le droit de se connecter ou pas au serveur
 * @author JasonPercus
 * @version 1.0
 */
public class BAWElement implements java.io.Serializable, Cloneable {
    
    
    
    // <editor-fold defaultstate="collapsed" desc="SERIAL_VERSION_UID">
    /**
     * Correspond au numéro de série qui identifie le type de dé/sérialization utilisé pour l'objet
     */
    private static final long serialVersionUID = 1L;
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="ATTRIBUTS">
    /**
     * Correspond à la date de création de l'élément BAW
     */
    private java.util.Date date;
    
    /**
     * Correspond à l'adresse MAC de l'élément
     */
    private MACAddress mac;
    // </editor-fold>

    
    
    // <editor-fold defaultstate="collapsed" desc="CONSTRUCTORS">
    /**
     * Crée un objet BAWElement
     * @param date Correspond à la date de l'élément
     * @param mac Correspond à l'adresse MAC de l'élément
     */
    public BAWElement(java.util.Date date, MACAddress mac) {
        this.date = date;
        this.mac = mac;
    }

    /**
     * Crée un objet BAWElement
     * @param mac Correspond à l'adresse MAC de l'élément
     */
    public BAWElement(MACAddress mac) {
        this.date = new java.util.Date();
        this.mac = mac;
    }
    // </editor-fold>

    
    
    // <editor-fold defaultstate="collapsed" desc="METHODES PUBLICS">
    /**
     * Renvoie la date de l'élément BAW
     * @return Retourne la date de l'élément BAW
     */
    public java.util.Date getDate() {
        return date;
    }

    /**
     * Modifie la date de l'élément BAW
     * @param date Correpsond à la nouvelle date
     */
    public void setDate(java.util.Date date) {
        this.date = date;
    }

    /**
     * Renvoie l'adresse MAC de l'élément BAW
     * @return Retourne l'adresse MAC de l'élément BAW
     */
    public MACAddress getMac() {
        return mac;
    }

    /**
     * Modifie l'adresse MAC de l'élément BAW
     * @param mac Correpsond à la nouvelle adresse MAC
     */
    public void setMac(MACAddress mac) {
        this.mac = mac;
    }

    /**
     * Renvoie un clone de l'objet BAW en cours
     * @return Retourne un clone de l'objet BAW en cours
     * @throws CloneNotSupportedException Correspond à l'éventuelle erreur de clonage
     */
    @Override
    @SuppressWarnings("CloneDoesntCallSuperClone")
    public Object clone() throws CloneNotSupportedException {
        return new BAWElement(date, mac);
    }

    /**
     * Renvoie le résultat de la méthode hashCode()
     * @return Retourne le résultat de la méthode hashCode()
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + java.util.Objects.hashCode(this.mac);
        return hash;
    }

    /**
     * Renvoie le résultat de la méthode equals()
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
        final BAWElement other = (BAWElement) obj;
        return java.util.Objects.equals(this.mac, other.mac);
    }

    /**
     * Renvoie l'élément BAW sous la forme d'une chaîne de caractères
     * @return Retourne l'élément BAW sous la forme d'une chaîne de caractères
     */
    @Override
    public String toString() {
        return "BAWElement(" + date + ": " + mac + ')';
    }
    // </editor-fold>
    
    
    
}