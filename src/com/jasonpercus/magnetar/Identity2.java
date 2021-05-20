/*
 * Copyright (C) BRIGUET Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Briguet, September 2018
 */
package com.jasonpercus.magnetar;



import com.jasonpercus.network.BroadcastIPv4;
import com.jasonpercus.network.IPCard;
import com.jasonpercus.network.IPv4;
import com.jasonpercus.network.MACAddress;
import com.jasonpercus.network.MaskIPv4;
import com.jasonpercus.network.Network;
import com.jasonpercus.network.NetworkCard;
import com.jasonpercus.util.thread.CodeTimeResult;
import com.jasonpercus.util.thread.Millisecond;
import com.jasonpercus.util.thread.TimedResult;



/**
 * Cette classe permet d'identifier un client ou un serveur. Il s'agit d'une carte d'identité. Il faut en donner une à chaque client et serveur. Cette identité contient l'adresse MAC du PC...
 * @author Briguet
 * @version 1.0
 */
public class Identity2 extends Identity {
    
    
    
    // <editor-fold defaultstate="collapsed" desc="SERIAL_VERSION_UID">
    /**
     * Correspond au numéro de série qui identifie le type de dé/sérialization utilisé pour l'objet
     */
    private static final long serialVersionUID = 1L;
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
    
    /**
     * Correspond à l'adresse IP principale de l'identité
     */
    private final IPv4 mainLocalIP;
    
    /**
     * Correspond au masque IP principale de l'identité
     */
    private final MaskIPv4 mainLocalMask;
    
    /**
     * Correspond au broadcast principale de l'identité
     */
    private final BroadcastIPv4 mainLocalBroadcast;
    // </editor-fold>

    
    
    // <editor-fold defaultstate="collapsed" desc="CONSTRUCTOR">
    /**
     * Crée un objet Identity2
     * @param name Correpsond au nom de l'identité
     * @param mac Correspond à l'adresse MAC de l'identité
     * @param publicIP Correspond à l'adresse IP publique de l'identité
     * @param mainLocalIP Correspond à l'adresse IP principale de l'équipement
     * @param mainLocalMask Correspond au masque IP principal de l'équipement
     * @param mainLocalBroadcast Correspond au broadcast principal de l'équipement
     * @param listIP Correspond à la liste des IP de l'identité
     */
    protected Identity2(String name, MACAddress mac, IPv4 publicIP, IPv4 mainLocalIP, MaskIPv4 mainLocalMask, BroadcastIPv4 mainLocalBroadcast, java.util.List<IPCard> listIP) {
        this.name = name;
        this.mac = mac;
        this.publicIP = publicIP;
        this.listIP = listIP;
        this.mainLocalIP = mainLocalIP;
        this.mainLocalMask = mainLocalMask;
        this.mainLocalBroadcast = mainLocalBroadcast;
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

    /**
     * Renvoie l'adresse IP principale locale de l'identité
     * @return Retourne l'adresse IP principale locale de l'identité
     */
    public IPv4 getMainLocalIP() {
        return mainLocalIP;
    }

    /**
     * Renvoie le masque principal local de l'identité
     * @return Retourne le masque principal local de l'identité
     */
    public MaskIPv4 getMainLocalMask() {
        return mainLocalMask;
    }

    /**
     * Renvoie l'adresse broadcast principal local de l'identité
     * @return Retourne l'adresse broadcast principal local de l'identité
     */
    public BroadcastIPv4 getMainLocalBroadcast() {
        return mainLocalBroadcast;
    }
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="METHODE PUBLIC STATIC">
    /**
     * Crée un objet Identity2
     * @param name Correspond au nom de l'identité
     * @param timeMaxToGetPublicIP Correspond au temps maximum (en ms) autorisé pour récupérer l'adresse IP publique, après quoi si le temps maximum est écoulé, la valeur null est utilisée comme IP publique (ce qui pourrait signifier dans votre application que la connexion internet est mauvaise)
     * @return Retourne un objet Identity2
     */
    public static Identity2 getInstance(String name, long timeMaxToGetPublicIP){
        NetworkCard nc = getNetworkCard();
        if(nc != null && name != null && name.length() > 0){
            CodeTimeResult<Void, IPv4> code = (Void in) -> {
                return IPv4.getPublicIP();
            };
            IPv4 publicIP = TimedResult.run(code, new Millisecond(timeMaxToGetPublicIP), null);
            return new Identity2(name, nc.getMacAddress(), publicIP, IPv4.getMainLocalIPv4(), MaskIPv4.getMainLocalMask(), BroadcastIPv4.getMainLocalBroadcast(), nc.getIpCards());
        }else{
            throw new ErrorIdentityException("NetworkCard cannot is null !");
        }
    }
    
    /**
     * Crée un objet Identity2. Attention, l'ip publique de l'identité sera récupérée en un maximum d'une seconde. Après ce délai écoulé, la valeur null est utilisée comme IP publique (ce qui pourrait signifier dans votre application que la connexion internet est mauvaise)
     * @param name Correspond au nom de l'identité
     * @see #getInstance(java.lang.String, long)
     * @return Retourne un objet Identity2
     */
    public static Identity2 getInstance(String name){
        return getInstance(name, 1000l);
    }
    
    /**
     * Crée un objet Identity2 (qui ne possèdera pas d'adresse IP publique)
     * @param name Correspond au nom de l'identité
     * @return Retourne un objet Identity2 (qui ne possèdera pas d'adresse IP publique)
     */
    public static Identity2 getInstanceWithoutPublicIP(String name){
        NetworkCard nc = getNetworkCard();
        if(nc != null && name != null && name.length() > 0){
            return new Identity2(name, nc.getMacAddress(), null, IPv4.getMainLocalIPv4(), MaskIPv4.getMainLocalMask(), BroadcastIPv4.getMainLocalBroadcast(), nc.getIpCards());
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
    
    
    
}