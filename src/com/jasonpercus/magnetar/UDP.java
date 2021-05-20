/*
 * Copyright (C) BRIGUET Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Briguet, September 2018
 */
package com.jasonpercus.magnetar;



import com.jasonpercus.encryption.Cipher;
import com.jasonpercus.encryption.Key;
import com.jasonpercus.network.IPv4;
import com.jasonpercus.util.Serializer;



/**
 * Cette classe permet d'ouvrir une connexion UDP entre deux objets UDP et envoyer/recevoir des paquets
 * @author Briguet
 * @version 1.0
 */
public class UDP {
    
    
    
    // <editor-fold defaultstate="collapsed" desc="ATTRIBUTS STATICS PUBLICS">
    /**
     * Cette variable définit l'état d'un paquet OK
     */
    public static int STATUT_PACKET_OK = 0;
    /**
     * Cette variable définit l'état d'un paquet malformé
     */
    public static int STATUT_PACKET_MALFORMED = 1;
    /**
     * Cette variable définit l'état d'un paquet ayant une erreur de décryptage
     */
    public static int STATUT_ERROR_DECRYPT = 2;
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="ATTRIBUTS">
    /**
     * Correspond à la variable qui définit la taille des paquets sur le réseau
     */
    private final int lengthPacket;
    /**
     * Correspond au port d'entrée/sortie de l'objet
     */
    private final int myPort;
    /**
     * Correspond au port d'entrée/sortie du destinataire
     */
    private final int receiverPort;
    /**
     * Correspond à l'adresse IP de destination
     */
    private IPv4 receiverIPv4;
    /**
     * Correspond à un objet DatagramSocket
     */
    private java.net.DatagramSocket ds;
    /**
     * Correspond au thread qui écoute les trames entrante sur le réseau
     */
    private UDPClientServerThread ust;
    /**
     * Si cette variable est à true, on continue d'écouter les trame entrante
     */
    private boolean run;
    /**
     * Correspond à la classe de dé/chiffrement
     */
    private Cipher cipher;
    /**
     * Correspond à la clef de dé/chiffrement
     */
    private Key key;
    /**
     * Correspond à la liste des listeners
     */
    private final ListSynchronized<IReceivedUDP> list_i_received;
    // </editor-fold>
    

    
    // <editor-fold defaultstate="collapsed" desc="CONSTRUCTOR">
    /**
     * Crée un objet UDPClientServer
     * @param lengthPacket Correspond à la taille max des paquets UDP sur le réseau
     * @param myPort Correspond au port d'entrée/sortie de l'objet
     * @param receiverPort Correspond au port d'entrée/sortie du destinataire
     * @param receiverIPv4 Correspond à l'adresse IP de destination
     */
    public UDP(int lengthPacket, int myPort, int receiverPort, IPv4 receiverIPv4) {
        this.lengthPacket = lengthPacket;
        this.myPort = myPort;
        this.receiverPort = receiverPort;
        this.receiverIPv4 = receiverIPv4;
        this.list_i_received = new ListSynchronized<>();
    }
    // </editor-fold>

    
    
    // <editor-fold defaultstate="collapsed" desc="METHODES PUBLICS">
    /**
     * Modifie le destinataire des prochains datagrammes
     * @param newReceiver Correspond au nouveau destinataire des prochains datagrammes
     */
    public void setReceiver(IPv4 newReceiver){
        this.receiverIPv4 = newReceiver;
    }
    
    /**
     * Définit une méthode de dé/chiffrement
     * @param cipher Correspond à l'objet qui se chargera de dé/chiffrer
     * @param key Correspond à la clef de dé/chiffrement
     */
    public void setEncryptor(Cipher cipher, Key key) {
        this.cipher = cipher;
        this.key    = key;
    }
    
    /**
     * Ajoute un listener IReceivedUDP
     * @param listener Correspond au listener
     */
    @SuppressWarnings("NestedSynchronizedStatement")
    public synchronized void addIReceivedListener(IReceivedUDP listener){
        synchronized(list_i_received){
            list_i_received.add(listener);
        }
    }
    
    /**
     * Enlève un listener IReceivedUDP
     * @param listener Correspond au listener
     */
    @SuppressWarnings("NestedSynchronizedStatement")
    public synchronized void removeIReceivedListener(IReceivedUDP listener){
        synchronized(list_i_received){
            list_i_received.remove(listener);
        }
    }
    
    /**
     * Enlève un listener IReceivedUDP
     * @param index Correspond à l'index du listener
     */
    @SuppressWarnings("NestedSynchronizedStatement")
    public synchronized void removeIReceivedListener(int index){
        synchronized(list_i_received){
            list_i_received.remove(index);
        }
    }
    
    /**
     * Ouvre la socket (par défaut les trames ne sont pas broadcastées)
     */
    public void start(){
        start(false);
    }
    
    /**
     * Ouvre la socket
     * @param broadcast Détermine si la socket enverra des broadcasts
     */
    public void start(boolean broadcast){
        if(this.ust == null && this.ds == null){
            try {
                this.ds = new java.net.DatagramSocket(myPort);
                if(broadcast)
                    this.ds.setBroadcast(true);
            } catch (java.net.SocketException ex) {
                java.util.logging.Logger.getLogger(UDP.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            this.ust = new UDPClientServerThread();
            this.run = true;
            this.ust.start();
        }
    }
    
    /**
     * Ferme la socket
     */
    public void stop(){
        if(this.ust != null || this.ds != null){
            this.run = false;
            ds.close();
            this.ust = null;
        }
    }
    
    /**
     * Envoie un tableau de byte
     * @param data Correspond à un tableau de byte à envoyer
     */
    public void send(byte[] data){
        if(cipher != null && key != null){
            try {
                data = cipher.encrypt(key, data);
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(UDP.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        }
        int lengthP = data.length;
        if(data.length < lengthPacket){
            byte[] bds = new byte[lengthPacket];
            System.arraycopy(data, 0, bds, 0, data.length);
            data = bds;
        }
        try {
            java.net.DatagramPacket dp = new java.net.DatagramPacket(data, data.length, java.net.InetAddress.getByName(receiverIPv4.getIpv4()), receiverPort);
            dp.setLength(lengthP);
            ds.send(dp);
        } catch (java.net.SocketException ex){
                
        } catch (java.io.IOException ex) {
            java.util.logging.Logger.getLogger(UDP.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Envoie un objet serializable
     * @param s Correspond à l'objet à envoyer
     */
    public void send(java.io.Serializable s){
        if(this.ust != null && this.ds != null){
            try {
                byte[] bs = Serializer.getData(s);
                send(bs);
            } catch (java.io.IOException ex) {
                java.util.logging.Logger.getLogger(UDP.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        }
    }
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="CLASS">
    /**
     * Correspond à la classe qui gère la réception des paquets
     */
    private class UDPClientServerThread extends Thread {

        @Override
        public void run() {
            while(run){
                try {
                    java.net.DatagramPacket dp = new java.net.DatagramPacket(new byte[lengthPacket], lengthPacket);
                    ds.receive(dp);
                    int lengthP = dp.getLength();
                    byte[] data = dp.getData();
                    
                    if(lengthP<data.length){
                        byte[] dps = new byte[lengthP];
                        System.arraycopy(data, 0, dps, 0, dps.length);
                        data = dps;
                    }
                    
                    if(cipher != null && key != null){
                        data = cipher.decrypt(key, data);
                    }
                    synchronized(list_i_received){
                        int size = list_i_received.size();
                        for (int i = 0; i < size; i++) {
                            IReceivedUDP iru = list_i_received.get(i);
                            if (iru != null) {
                                iru.received(data);
                                Object obj = Serializer.getObject(data);
                                iru.received(obj, STATUT_PACKET_OK);
                            }
                        }
                    }
                } catch (java.io.IOException | ClassNotFoundException ex){
                    synchronized(list_i_received){
                        int size = list_i_received.size();
                        for(int i=0;i<size;i++){
                            IReceivedUDP iru = list_i_received.get(i);
                            if(iru != null){
                                iru.received(new byte[lengthPacket]);
                                iru.received(null, STATUT_PACKET_MALFORMED);
                            }
                        }
                    }
                } catch (Exception ex) {
                    synchronized(list_i_received){
                        int size = list_i_received.size();
                        for (int i = 0; i < size; i++) {
                            IReceivedUDP iru = list_i_received.get(i);
                            if (iru != null) {
                                iru.received(new byte[lengthPacket]);
                                iru.received(null, STATUT_ERROR_DECRYPT);
                            }
                        }
                    }
                }
            }
        }
    }
    // </editor-fold>
    
    
    
}