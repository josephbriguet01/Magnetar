/*
 * Copyright (C) JasonPercus Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by JasonPercus, 10/2022
 */
package com.jasonpercus.magnetar;



import com.jasonpercus.magnetar.protocol.Protocol;
import com.jasonpercus.magnetar.protocol.ProtocolUDP;



/**
 * Cette classe permet d'envoyer/recevoir des données à partir d'un datagramme UDP
 * @author JasonPercus
 * @version 1.0
 */
public class UDP implements AutoCloseable, Protocollable {
    
    
    
//CONSTANT
    /**
     * Correspond à la taille de base des données lu ou écrite
     */
    private final static int SIZE_BUFFER = 1024;
    
    
    
//ATTRIBUTS
    /**
     * Correspond au type de connecteur UDP. S'agit-il d'un récepteur ou d'un émetteur ?
     */
    private final Type type;
    
    /**
     * Correspond à la socket qui gère les envoies ou les réceptions UDP
     */
    private final java.net.DatagramSocket socket;
    
    /**
     * Correspond au buffer de donnée qui sera envoyé
     */
    private java.util.LinkedList<Byte> outputBuffer;
    
    /**
     * Correspond à l'adresse et port du destinataire des packets udp
     */
    private java.net.InetSocketAddress addressOther;
    
    /**
     * Correspond à la taille des données lu ou écrite
     */
    private int packetSize;

    
    
//CONSTRUCTORS
    /**
     * Crée un objet UDP qui sera en mode écoute {@link Type#RECEIVER}
     * @param port Correspond au port d'écoute
     * @throws java.net.SocketException Si le socket n'a pas pu être ouverte, ou si le socket n'a pas pu se lier au port local spécifié
     */
    public UDP(int port) throws java.net.SocketException {
        this.socket = new java.net.DatagramSocket(port);
        this.type = Type.RECEIVER;
        this.packetSize = SIZE_BUFFER;
    }

    /**
     * Crée un objet UDP qui sera en mode envoi {@link Type#SENDER}
     * @param addressOther Correspond à l'adresse ip et port udp du destinataire
     * @throws java.net.SocketException Si le socket n'a pas pu être ouverte
     */
    public UDP(java.net.InetSocketAddress addressOther) throws java.net.SocketException {
        this.socket = new java.net.DatagramSocket();
        this.type = Type.SENDER;
        this.outputBuffer = new java.util.LinkedList<>();
        this.addressOther = addressOther;
        this.packetSize = SIZE_BUFFER;
    }

    
    
//METHODES PUBLICS
    /**
     * Ferme la socket UDP
     */
    @Override
    public void close() {
        this.socket.close();
    }
    
    /**
     * Renvoie la taille de base des packets lu ou écrit
     * @return Retourne la taille de base des packets lu ou écrit
     */
    public final int getPacketSize() {
        return packetSize;
    }

    /**
     * Modifie la taille de base des packets lu ou écrit
     * @param packetSize Correspond à la nouvelle taille de base des packets lu ou écrit
     */
    public final void setPacketSize(int packetSize) {
        this.packetSize = this.packetSize <= 0 ? SIZE_BUFFER : packetSize;
    }
    
    /**
     * Renvoie le type d'objet UDP. S'agit-il d'un récepteur ou d'un émetteur ?
     * @return Retourne le type d'objet UDP
     */
    public final Type getType() {
        return type;
    }
    
    /**
     * Ecoute le prochain packet UDP entrant. Cette méthode est bloquante
     * @return Retourne les données du packet réceptionné
     * @throws java.io.IOException Si une erreur d'E/S se produit ou si l'objet udp est un émetteur
     */
    public final synchronized byte[] read() throws java.io.IOException {
        if(type == Type.RECEIVER){
            byte[] array = new byte[packetSize];
            java.net.DatagramPacket packet = new java.net.DatagramPacket(array, packetSize);
            this.socket.receive(packet);
            int lengthP = packet.getLength();
            byte[] data = packet.getData();
            if (lengthP < data.length) {
                byte[] dps = new byte[lengthP];
                System.arraycopy(data, 0, dps, 0, dps.length);
                data = dps;
                return data;
            } else if (lengthP == data.length) {
                return data;
            } else
                throw new java.io.IOException("data truncated !");
        } else 
            throw new java.io.IOException("this objet is not a receiver but a sender !");
    }
    
    /**
     * Ecrit un byte dans le prochain packet UDP à envoyer
     * @param value Correspond au byte [0;255] à envoyer
     * @throws java.io.IOException Si une erreur d'E/S se produit ou si l'objet udp est un récepteur
     */
    public final synchronized void write(int value) throws java.io.IOException {
        if(type == Type.SENDER){
            this.outputBuffer.add((byte) value);
            if(this.outputBuffer.size() >= packetSize)
                flush();
        } else
            throw new java.io.IOException("this objet is not a sender but a receiver !");
    }
    
    /**
     * Envoie un tableau de bytes dans le prochain packet UDP à envoyer
     * @param b Correspond au tableau de bytes
     * @throws java.io.IOException Si une erreur d'E/S se produit ou si l'objet udp est un récepteur
     */
    public final synchronized void write(byte b[]) throws java.io.IOException {
        if(type == Type.SENDER){
            for(byte value : b)
                write(value & 0xFF);
            flush();
        } else
            throw new java.io.IOException("this objet is not a sender but a receiver !");
    }
    
    /**
     * Ecrit une portion d'un tableau de bytes dans le prochain packet UDP à envoyer
     * @param b Correspond au tableau de bytes
     * @param off Correspond au décalage de début dans le tableau b auquel les données sont écrites
     * @param len Correspond au nombre maximum d'octets à lire
     * @throws java.io.IOException Si une erreur d'E/S se produit ou si l'objet udp est un récepteur
     */
    public final synchronized void write(byte b[], int off, int len) throws java.io.IOException {
        if(type == Type.SENDER){
            int cpt = 0;
            while(cpt < len){
                write(b[cpt + off] & 0xFF);
                cpt++;
            }
            flush();
        } else
            throw new java.io.IOException("this objet is not a sender but a receiver !");
    }
    
    /**
     * Envoie les données tamponnées. Ces données son empaqueté et envoyer vers le client à l'aide d'un datagramme udp
     * @throws java.io.IOException Si une erreur d'E/S se produit ou si l'objet udp est un récepteur
     */
    public final synchronized void flush() throws java.io.IOException {
        if(type == Type.SENDER){
            if(outputBuffer != null && !this.outputBuffer.isEmpty()){
                byte[] data = toArray();
                java.net.DatagramPacket packet = new java.net.DatagramPacket(data, data.length, addressOther);
                packet.setLength(data.length);
                this.socket.send(packet);
                this.outputBuffer.clear();
            } else 
                throw new java.io.IOException("nothing to send !");
        } else
            throw new java.io.IOException("this objet is not a sender but a receiver !");
    }
    
    /**
     * Renvoie le protocol utilisé
     * @return Retourne le protocol utilisé
     */
    @Override
    public Protocol getProtocolName() {
        return new ProtocolUDP();
    }
    
    
    
//METHODE PRIVATE
    /**
     * Transforme la liste chaînée en tableau de bytes
     * @return Retourne un tableau de bytes
     */
    private byte[] toArray(){
        byte[] array = new byte[outputBuffer.size()];
        int cpt = 0;
        while(!outputBuffer.isEmpty()){
            array[cpt++] = outputBuffer.removeFirst();
        }
        return array;
    }
    
    
    
    /**
     * Cette classe énumère les différents types d'objet UDP
     * @author JasonPercus
     * @version 1.0
     */
    public enum Type {
        
        
        
        /**
         * L'objet UDP est un émetteur
         */
        SENDER,
        
        /**
         * L'objet UDP est un récepteur
         */
        RECEIVER;
        
        
        
    }
    
    
    
}