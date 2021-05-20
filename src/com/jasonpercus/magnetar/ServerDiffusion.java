/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jasonpercus.magnetar;



import com.jasonpercus.network.IPCard;
import com.jasonpercus.network.Network;
import com.jasonpercus.network.NetworkCard;



/**
 * Cette classe permet de diffuser les informations d'un serveur
 * @author JasonPercus
 * @version 1.0
 */
public class ServerDiffusion extends Thread {

    
    
    // <editor-fold defaultstate="collapsed" desc="ATTRIBUTS">
    /**
     * Correspond au serveur à diffuser
     */
    private final Diffusion diffusion;
    
    /**
     * Détermine si la diffusion est en cours
     */
    private boolean run;
    
    /**
     * Détermine si les trames seront broadcastées
     */
    private boolean broadcast;
    
    /**
     * Correspond au port d'envoi UDP
     */
    private final int portSend;
    
    /**
     * Correspond au port de réception UDP
     */
    private final int portReceive;
    
    /**
     * Correspond à la taille des paquets UDP
     */
    private final int sizePacket;
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="CONSTRUCTORS">
    /**
     * Crée un serveur de diffusion
     * @param diffusion Correspond au serveur à diffuser
     * @param portSend Correspond au port d'envoi UDP
     * @param portReceive Correspond au port de réception UDP
     */
    public ServerDiffusion(Diffusion diffusion, int portSend, int portReceive) {
        super.setPriority(MIN_PRIORITY);
        super.setName("ServerDiffusion");
        this.diffusion = diffusion;
        this.run = false;
        this.portSend = portSend;
        this.portReceive = portReceive;
        this.sizePacket = 2048;
    }
    
    /**
     * Crée un serveur de diffusion
     * @param diffusion Correspond au serveur à diffuser
     * @param portSend Correspond au port d'envoi UDP
     * @param portReceive Correspond au port de réception UDP
     * @param sizePacket Correspond à la taille des paquets UDP
     */
    public ServerDiffusion(Diffusion diffusion, int portSend, int portReceive, int sizePacket) {
        super.setPriority(MIN_PRIORITY);
        super.setName("ServerDiffusion");
        this.diffusion = diffusion;
        this.run = false;
        this.portSend = portSend;
        this.portReceive = portReceive;
        this.sizePacket = sizePacket;
    }
    // </editor-fold>

    

    // <editor-fold defaultstate="collapsed" desc="METHODES PUBLICS">
    /**
     * Renvoie le serveur à diffuser
     * @return Retourne le serveur à diffuser
     */
    public Diffusion getDiffusion(){
        return diffusion;
    }

    /**
     * Démarre la diffusion
     */
    public void startDiffusion() {
        startDiffusion(true);
    }

    /**
     * Démarre la diffusion
     * @param broadcast Détermine si les trames seront broadcastées
     */
    public void startDiffusion(boolean broadcast) {
        this.run = true;
        this.broadcast = broadcast;
        this.start();
    }
    
    /**
     * Stoppe la diffusion
     */
    public void stopDiffusion(){
        this.run = false;
    }
    
    /**
     * @deprecated NE PAS UTILISER
     */
    @Override
    public void run(){
        java.util.List<IPCard> cards = getIP();
        if(!cards.isEmpty()){
            this.diffusion.setIp(cards.get(0).getIp().getIpv4());

            UDP udp = new UDP(sizePacket, portSend, portReceive, cards.get(0).getBroadcast());
            udp.start(this.broadcast);
            while(this.run){
                
                for(IPCard card : cards){
                    udp.setReceiver(card.getBroadcast());
                    diffusion.setIp(card.getIp().getIpv4());
                    udp.send(diffusion);
                }
                waitTime(1);
            }
            udp.stop();
        }
    }
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="METHODES PRIVATES">
    /**
     * Attend time seconde
     * @param time Correpsond au temps d'attente du thread
     */
    private void waitTime(int time){
        try {
            Thread.sleep(time*1000);
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(ServerDiffusion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Renvoie la liste des cartes ip du système
     * @return Retourne la liste des cartes ip du système
     */
    private java.util.List<IPCard> getIP(){
        java.util.List<IPCard> ipCards = new java.util.ArrayList<>();
        java.util.List<NetworkCard> cards = new Network().getHardwareList();
        
        for(NetworkCard card : cards){
            if(card.getMacAddress() != null && card.isUp() && !card.getIpCards().isEmpty()){
                ipCards.addAll(card.getIpCards());
            }
        }
        if(!cards.isEmpty()) 
            return ipCards;
        return null;
    }
    // </editor-fold>
    
    
    
}