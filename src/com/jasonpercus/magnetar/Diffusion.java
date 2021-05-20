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
 * Cette classe représente un serveur détecté ou devant être détecté sur le réseau
 * @author JasonPercus
 * @version 1.0
 */
public class Diffusion implements java.io.Serializable {
    
    
    
    // <editor-fold defaultstate="collapsed" desc="SERIAL_VERSION_UID">
    /**
     * Correspond au numéro de série qui identifie le type de dé/sérialization utilisé pour l'objet
     */
    private static final long serialVersionUID = 1L;
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="ATTRIBUTS">
    /**
     * Correspond à l'adresse ip d'un serveur
     */
    private String ip;
    
    /**
     * Correspond aux données associées à un serveur
     */
    private Object[] datas;
    // </editor-fold>

    
    
    // <editor-fold defaultstate="collapsed" desc="CONSTRUCTORS">
    /**
     * Crée un objet détaillant un serveur dont la présence est ou doit être diffusée sur le réseau
     */
    public Diffusion() {
        super();
    }

    /**
     * Crée un objet détaillant un serveur dont la présence est ou doit être diffusée sur le réseau
     * @param datas Correspond aux données associées à un serveur
     */
    public Diffusion(Object... datas){
        super();
        this.datas = datas;
    }
    
    /**
     * Crée un objet détaillant un serveur dont la présence est ou doit être diffusée sur le réseau
     * @param ip Correspond à l'adresse ip d'un serveur
     * @param datas Correspond aux données associées à un serveur
     */
    public Diffusion(String ip, Object... datas) {
        super();
        this.ip = ip;
        this.datas = datas;
    }
    // </editor-fold>

    
    
    // <editor-fold defaultstate="collapsed" desc="METHODES PUBLICS">
    /**
     * Renvoie les données associées à un serveur
     * @return Retourne les données associées à un serveur
     */
    public Object[] getDatas() {
        return datas;
    }
    
    /**
     * Modifie les données associées à un serveur
     * @param datas Correspond aux nouvelles données associées à un serveur
     */
    public void setDatas(Object[] datas) {
        this.datas = datas;
    }

    /**
     * Renvoie l'adresse ip du joueur
     * @return Retourne l'adresse ip du joueur
     */
    public String getIp() {
        return ip;
    }

    /**
     * Modifie l'adresse ip du serveur HOST
     * @param ip Correspond à la nouvelle adresse ip du serveur HOST
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * Renvoie le résultat de la méthode hashCode()
     * @return Retourne le résultat de la méthode hashCode()
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + java.util.Objects.hashCode(this.ip);
        return hash;
    }

    /**
     * Renvoie le résultat de la méthode equals
     * @param obj Correspond à l'objet à comparer avec le courant
     * @return Retourne true si les deux objets sont identiques, sinon false
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
        final Diffusion other = (Diffusion) obj;
        return java.util.Objects.equals(this.ip, other.ip);
    }

    /**
     * Renvoie un serveur détecté ou devant être détecté sous la forme d'une chaîne de caractères
     * @return Retourne un serveur détecté ou devant être détecté sous la forme d'une chaîne de caractères
     */
    @Override
    public String toString() {
        return ip;
    }
    // </editor-fold>
    
    
    
}