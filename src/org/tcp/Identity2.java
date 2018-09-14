/*
 * Copyright (C) BRIGUET Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Briguet, September 2018
 */
package org.tcp;



import network.IPCard;
import network.IPv4;
import network.MACAddress;
import network.Network;
import network.NetworkCard;



/**
 * Cette classe permet d'identifier un client ou un serveur. Il s'agit d'une carte d'identité. Il faut en donner une à chaque client et serveur. Cette identité contient l'adresse MAC du PC...
 * @author Briguet
 * @version 1.0
 */
public class Identity2 extends Identity {

    
    
    // <editor-fold defaultstate="collapsed" desc="CONSTRUCTOR">
    /**
     * Crée un objet Identity2
     * @param name Correpsond au nom de l'identité
     * @param mac Correspond à l'adresse MAC de l'identité
     * @param publicIP Correspond à l'adresse IP publique de l'identité
     * @param listIP Correspond à la liste des IP de l'identité
     */
    protected Identity2(String name, MACAddress mac, IPv4 publicIP, java.util.List<IPCard> listIP) {
        this.name = name;
        this.mac = mac;
        this.publicIP = publicIP;
        this.listIP = listIP;
    }
    // </editor-fold>

    
    
    // <editor-fold defaultstate="collapsed" desc="GETTERS & SETTER">
    /**
     * Renvoie le nom de l'identité
     * @return Retourne le nom de l'identité
     */
    public String getName() {
        return name;
    }

    /**
     * Modifie le nom de l'identité
     * @param name Correspond au nom de l'identité
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Renvoie l'adresse MAC de l'identité
     * @return Retourne l'adresse MAC de l'identité
     */
    public MACAddress getMac() {
        return mac;
    }

    /**
     * Renvoie l'adresse IP publique de l'identité
     * @return Retourne l'adresse IP publique de l'identité
     */
    public IPv4 getPublicIP() {
        return publicIP;
    }

    /**
     * Renvoie la liste des IP de l'identité
     * @return Retourne la liste des IP de l'identité
     */
    public java.util.List<IPCard> getListIP() {
        return listIP;
    }
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="METHODE PUBLIC STATIC">
    /**
     * Crée un objet Identity2
     * @param name Correspond au nom de l'identité
     * @return Retourne un objet Identity2
     */
    public static Identity2 getInstance(String name){
        NetworkCard nc = getNetworkCard();
        if(nc != null && name != null && name.length() > 0){
            return new Identity2(name, nc.getMacAddress(), IPv4.getPublicIP(), nc.getIpCards());
        }else{
            throw new ErrorIdentityException("NetworkCard cannot is null !");
        }
    }
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="METHODE PRIVATE">
    /**
     * Renvoie la NetworkCard principale qui est en activité
     * @return Retourne la NetworkCard principale qui est en activité
     */
    private static NetworkCard getNetworkCard(){
        Network n = new Network();
        java.util.List<NetworkCard> ncs = n.getHardwareList();
        for(int i=0;i<ncs.size();i++){
            NetworkCard nc = ncs.get(i);
            if(nc != null && nc.getMacAddress() != null && nc.isUp()){
                return nc;
            }
        }
        return null;
    }
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="ATTRIBUTS">
    /**
     * Correspond au nom de l'identité
     */
    private String name;
    /**
     * Correspond à l'adresse MAC de l'identité
     */
    private final MACAddress mac;
    /**
     * Correspond à l'adresse IP publique de l'identité
     */
    private final IPv4 publicIP;
    /**
     * Correspond aux IP de l'identité
     */
    private final java.util.List<IPCard> listIP;
    // </editor-fold>
    
    
    
}