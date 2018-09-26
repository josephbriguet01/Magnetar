/*
 * Copyright (C) BRIGUET Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Briguet, August 2018
 */
package org.tcpudp;

import java.util.Arrays;



/**
 * Cette classe permet d'identifier un client ou un serveur. Il s'agit d'une carte d'identité. Il faut en donner une à chaque client et serveur
 * @author BRIGUET
 * @version 1.0
 */
public class Identity implements java.io.Serializable{

    
    
    // <editor-fold defaultstate="collapsed" desc="CONSTRUCTORS">
    /**
     * Ce constructeur crée une identité unique pour un client ou un serveur
     */
    public Identity() {
        this.identity = generateChain(15);
        this.options = new java.util.ArrayList<>();
    }
    
    /**
     * Ce constructeur crée une identité unique pour un le client générique de fermeture du serveur
     */
    private Identity(int generic){
        this.identity = GENERIC_IDENTITY;
        this.options = new java.util.ArrayList<>();
    }
    // </editor-fold>

    
    
    // <editor-fold defaultstate="collapsed" desc="GETTER">
    /**
     * Renvoie la chaine unique de cette identité parmis toutes les identités
     * @return Retourne la chaine
     */
    public String               getIdentity() {
        return identity;
    }
    // </editor-fold>

    
    
    // <editor-fold defaultstate="collapsed" desc="METHODES PUBLICS & PROTECTED">
    /**
     * Ajoute à la liste les options en paramètre
     * @param options Correspond à la liste des options
     */
    public void addOptions(String... options){
        this.options.addAll(Arrays.asList(options));
    }
    
    /**
     * Supprime de la liste les options en paramètre
     * @param options Correspond à la liste des options
     */
    public void removeOptions(String... options){
        for (String option : options) {
            this.options.remove(option);
        }
    }
    
    /**
     * Renvoie la liste des options de l'identité
     * @return retourne la liste des options de l'identité
     */
    public java.util.List<String> getOptions(){
        return this.options;
    }
    
    /**
     * Redéfinit la méthode toString()
     * @return Retourne le résultat de la méthode
     */
    @Override
    public String               toString() {
        return "Identity{" + "identity=" + identity + '}';
    }

    /**
     * Redéfinit la méthode hashCode()
     * @return Retourne le résultat de la méthode
     */
    @Override
    public int                  hashCode() {
        int hash = 5;
        hash = 97 * hash + java.util.Objects.hashCode(this.identity);
        return hash;
    }

    /**
     * Redéfinit la méthode equals()
     * @param obj Correspond à l'objet à tester
     * @return Retourne le résultat de la méthode
     */
    @Override
    public boolean              equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Identity other = (Identity) obj;
        return java.util.Objects.equals(this.identity, other.identity);
    }
    
    /**
     * Renvoie une instance de l'identité d'un client généric de déconnexion
     * @return Retourne l'identité
     */
    protected static Identity   getInstanceGeneric(){
        return new Identity(0);
    }
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="METHODE PRIVATE">
    /**
     * Cette méthode génère une chaine aléatoire de length caractère
     * @param length Correspond à la taille de la chaine voulue
     * @return Retourne la chaine de caractère
     */
    private String              generateChain(int length) {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"; // Tu supprimes les lettres dont tu ne veux pas
        String pass = "";
        for (int x = 0; x < length; x++) {
            int i = (int) Math.floor(Math.random() * chars.length());
            pass += chars.charAt(i);
        }
        return pass;
    }
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="ATTRIBUTS">
    /**
     * Correspond à l'identité générique de déconnexion
     */
    protected   static  String  GENERIC_IDENTITY    = "GENERIC";
    /**
     * Correspond à l'identité sous forme de chaîne de caractères
     */
    private     final   String  identity;
    /**
     * Correspond aux éventuelles options de l'identité
     */
    private java.util.List<String> options;
    // </editor-fold>
    
    
    
}