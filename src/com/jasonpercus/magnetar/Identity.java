/*
 * Copyright (C) JasonPercus Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by JasonPercus, August 2018
 */
package com.jasonpercus.magnetar;



import com.jasonpercus.util.Strings;



/**
 * Cette classe permet d'identifier un client ou un serveur. Il s'agit d'une carte d'identité. Il faut en donner une à chaque client et serveur
 * @author JasonPercus
 * @version 1.0
 */
public class Identity implements java.io.Serializable {
    
    
    
    // <editor-fold defaultstate="collapsed" desc="SERIAL_VERSION_UID">
    /**
     * Correspond au numéro de série qui identifie le type de dé/sérialization utilisé pour l'objet
     */
    private static final long serialVersionUID = 1L;
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="ATTRIBUTS">
    /**
     * Correspond à l'identité générique de déconnexion
     */
    protected   static  String  GENERIC_IDENTITY    = "GENERIC";
    
    /**
     * Correspond à l'identité générique de déconnexion
     */
    protected   static  String  BROADCAST_IDENTITY  = "BROADCAST";
    
    /**
     * Correspond à l'identité sous forme de chaîne de caractères
     */
    private     final   String  identity;
    
    /**
     * Correspond aux éventuelles options de l'identité
     */
    private     final   java.util.List<String> options;
    
    /**
     * Détermine s'il s'agit dun serveur ou d'un client qui utilise actuellement l'identity: 0 = serveur; 1 = client
     */
    private             int     clientOrServer;
    // </editor-fold>

    
    
    // <editor-fold defaultstate="collapsed" desc="CONSTRUCTORS">
    /**
     * Ce constructeur crée une identité unique pour un client ou un serveur
     */
    public Identity() {
        this.identity = Strings.generate(15);
        this.options = new java.util.ArrayList<>();
        this.clientOrServer = -1;
    }
    
    /**
     * Ce constructeur crée une identité unique pour un le client générique de fermeture du serveur
     */
    private Identity(int generic){
        if(generic == 1)
            this.identity = BROADCAST_IDENTITY;
        else
            this.identity = GENERIC_IDENTITY;
        this.options = new java.util.ArrayList<>();
        this.clientOrServer = -1;
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
    
    /**
     * Renvoie si oui ou non l'identité est utilisée actuellement par un client
     * @return Retourne si oui ou non l'identité est utilisée actuellement par un client
     */
    public boolean              isClientIdentity(){
        return (this.clientOrServer == 1);
    }
    
    /**
     * Renvoie si oui ou non l'identité est utilisée actuellement par un serveur
     * @return Retourne si oui ou non l'identité est utilisée actuellement par un client
     */
    public boolean              isServerIdentity(){
        return (this.clientOrServer == 2);
    }
    // </editor-fold>

    
    
    // <editor-fold defaultstate="collapsed" desc="METHODES PUBLICS & PROTECTED">    
    /**
     * Ajoute à la liste les options en paramètre
     * @param options Correspond à la liste des options
     */
    public void addOptions(String... options){
        this.options.addAll(java.util.Arrays.asList(options));
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
     * Renvoie une instance de l'identité broadcast permettant d'envoyer un message à tous les client en même temps
     * @return Retourne l'identité
     */
    public static Identity getBroadcastIdentity(){
        return new Identity(1);
    }
    
    /**
     * Renvoie une instance de l'identité d'un client généric de déconnexion
     * @return Retourne l'identité
     */
    protected static Identity   getInstanceGeneric(){
        return new Identity(0);
    }
    
    /**
     * Détermine si l'identité est celle d'un serveur ou celle d'un client
     * @param clientOrServer 0 = serveur, 1 = client
     */
    protected void setClientOrServer(int clientOrServer){
        this.clientOrServer = clientOrServer;
    }
    // </editor-fold>
    
    
    
}