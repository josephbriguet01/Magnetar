/*
 * Copyright (C) BRIGUET Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Briguet, August 2018
 */
package org.tcpudp;



/**
 * Cette classe permet de créer un Log daté permettant à l'utilisateur de suivre le déroulement des actions du serveur
 * @author BRIGUET
 * @version 1.0
 */
public class Log implements java.io.Serializable {

    
    
    // <editor-fold defaultstate="collapsed" desc="CONSTRUCTORS">
    /**
     * Crée un log
     * @param value Correspond au texte du Log
     */
    public Log(String value) {
        this.date = new java.util.Date();
        this.value = value;
        this.tags = new java.util.ArrayList<>();
    }
    
    /**
     * Crée un log
     * @param value Correspond au texte du Log
     * @param tags Correspond aux différents tags liés au Log
     */
    public Log(String value, ETAG...tags) {
        this.date = new java.util.Date();
        this.value = value;
        this.tags = new java.util.ArrayList<>();
        this.tags.addAll(java.util.Arrays.asList(tags));
    }
    
    /**
     * Crée un log
     * @param date Correspond à la date du Log
     * @param value Correspond au texte du Log
     * @param tags Correspond aux différents tags liés au Log
     */
    public Log(java.util.Date date, String value, ETAG...tags) {
        this.date = date;
        this.value = value;
        this.tags = new java.util.ArrayList<>();
        this.tags.addAll(java.util.Arrays.asList(tags));
    }
    // </editor-fold>

    
    
    // <editor-fold defaultstate="collapsed" desc="METHODES PUBLICS">
    /**
     * Ajoute un Tag au Log
     * @param tag Correspond au Tag à ajouter
     */
    public void addTag(ETAG tag){
        this.tags.add(tag);
    }
    
    /**
     * Ajoute des Tags au Log
     * @param tags Correspond à un tableau de Tags
     */
    public void addTag(ETAG...tags){
        this.tags.addAll(java.util.Arrays.asList(tags));
    }

    /**
     * Renvoie la date du Log
     * @return Retourne la date du Log
     */
    public java.util.Date getDate() {
        return date;
    }

    /**
     * Modifie la date du Log
     * @param date Correspond à la nouvelle date du Log
     */
    public void setDate(java.util.Date date) {
        this.date = date;
    }

    /**
     * Renvoie la valeur du Log
     * @return Retourne la valeur du Log
     */
    public String getValue() {
        return value;
    }

    /**
     * Modifie la valeur du Log
     * @param value Correspond à la nouvelle valeur du Log
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Renvoie les Tags du Log
     * @return Retourne les Tags du Log
     */
    public java.util.List<ETAG> getTags() {
        return tags;
    }

    /**
     * Modifie les Tags du Log
     * @param tags Correspond au tableau de tags du Log
     */
    public void setTags(java.util.List<ETAG> tags) {
        this.tags = tags;
    }

    /**
     * Renvoie le Log sous la forme d'une chaîne de caractères
     * @return Retourne le Log sous la forme d'une chaîne de caractères
     */
    @Override
    public String toString() {
        return date + ": "+value+" -> TAGS"+tags;
    }
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="ATTRIBUTS">
    /**
     * Correspond à la date de Log
     */
    private java.util.Date date;
    /**
     * Correspond à la valeur du Log
     */
    private String value;
    /**
     * Correspond au tag WARNING, SEVERE... du log permettant de créer des filtres de recherche
     */
    private java.util.List<ETAG> tags;
    // </editor-fold>
    
    
    
}