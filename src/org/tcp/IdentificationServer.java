/*
 * Copyright (C) BRIGUET Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Briguet, September 2018
 */
package org.tcp;



import network.IPv4;



/**
 * Cette classe permet d'identifier un serveur lorsqu'un client fait un scan du réseau
 * @author Briguet
 * @version 1.0
 */
public class IdentificationServer implements java.io.Serializable, Comparable {

    
    
    // <editor-fold defaultstate="collapsed" desc="CONSTRUCTOR">
    /**
     * Crée un objet IdentificationServer
     * @param nameServer Correspond au nom du serveur réel affiché dans la découverte réseau
     * @param portServer Correspond au port du serveur réel affiché dans la découverte réseau
     */
    protected IdentificationServer(String nameServer, int portServer) {
        this.nameServer = nameServer;
        this.portServer = portServer;
    }
    // </editor-fold>

    

    // <editor-fold defaultstate="collapsed" desc="METHODES PUBLICS">
    /**
     * Renvoie le nom du serveur réel affiché dans la découverte réseau
     * @return Retourne le nom du serveur réel affiché dans la découverte réseau
     */
    public String getNameServer() {
        return nameServer;
    }

    /**
     * Modifie le nom du serveur réel affiché dans la découverte réseau
     * @param nameServer Correspond au nouveau nom du serveur réel affiché dans la découverte réseau
     */
    public void setNameServer(String nameServer) {
        this.nameServer = nameServer;
    }

    /**
     * Renvoie le port du serveur réel affiché dans la découverte réseau
     * @return Retourne le port du serveur réel affiché dans la découverte réseau
     */
    public int getPortServer() {
        return portServer;
    }

    /**
     * Modifie le port du serveur réel affiché dans la découverte réseau
     * @param portServer Correspond au nouveau port du serveur réel affiché dans la découverte réseau
     */
    public void setPortServer(int portServer) {
        this.portServer = portServer;
    }

    /**
     * Renvoie l'adresse IP du serveur réel
     * @return Retourne l'adresse IP du serveur réel
     */
    public IPv4 getIpServer() {
        return ipServer;
    }

    /**
     * Modifie l'adresse IP du serveur réel
     * @param ipServer Correpsond à la nouvelle adresse IP du serveur réel
     */
    protected void setIpServer(IPv4 ipServer) {
        this.ipServer = ipServer;
    }

    /**
     * Renvoie le résultat de la méthode hashCode()
     * @return Retourne le résultat de la méthode hashCode()
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + java.util.Objects.hashCode(this.nameServer);
        hash = 59 * hash + this.portServer;
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
        final IdentificationServer other = (IdentificationServer) obj;
        if (this.portServer != other.portServer) {
            return false;
        }
        return java.util.Objects.equals(this.nameServer, other.nameServer);
    }

    /**
     * Renvoie un objet IdentificationServer sous la forme d'une chaîne de caractères
     * @return Retourne un objet IdentificationServer sous la forme d'une chaîne de caractères
     */
    @Override
    public String toString() {
        return "IdentificationServer{" + "ipServer=" + ipServer + ", nameServer=" + nameServer + ", portServer=" + portServer + '}';
    }

    /**
     * Renvoie le résultat de la méthode compareTo()
     * @param o Correspond à l'objet à comparer
     * @return Retourne le résultat de la méthode compareTo()
     */
    @Override
    public int compareTo(Object o) {
        IdentificationServer is = (IdentificationServer) o;
        String sis = is.nameServer;
        return this.nameServer.compareTo(sis);
    }
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="ATTRIBUTS">
    /**
     * Correspond à l'IP du serveur réel
     */
    private IPv4 ipServer;
    /**
     * Correspond au nom du serveur réel affiché dans la découverte réseau
     */
    private String nameServer;
    /**
     * Correspond au port du serveur réel affiché dans la découverte réseau
     */
    private int portServer;
    // </editor-fold>
    
    
    
}