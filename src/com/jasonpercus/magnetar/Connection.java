/*
 * Copyright (C) JasonPercus Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by JasonPercus, 10/2022
 */
package com.jasonpercus.magnetar;



import static com.jasonpercus.magnetar.ReasonDisconnection.CLIENT_DISCONNECTED_FROM_SERVER;
import static com.jasonpercus.magnetar.ReasonDisconnection.SERVER_DISCONNECTED_CLIENT;
import com.jasonpercus.magnetar.TCPServer.SocketProcessor;



/**
 * Cette classe abstraite représente une connexion TCP (entre un client et un serveur)
 * @author JasonPercus
 * @version 1.0
 */
public abstract class Connection {
    
    
    
//CONSTANT
    /**
     * Correspond à la taille du buffer lu ou écrit
     */
    private final static int SIZE_BUFFER = 1024;
    
    
    
//ATTRIBUT STATIC
    /**
     * Correspond au compteur d'id
     */
    private static int cptId = 0;
    
    
    
//ATTRIBUTS
    /**
     * Correspond au service du serveur qui permet de gérer les clients connectés sur le serveur
     */
    SocketProcessor processor;
    
    /**
     * Correspond au client
     */
    TCPClient client;
    
    /**
     * Correspond à la socket utilisée entre le client et le serveur
     */
    java.nio.channels.SocketChannel socket;
    
    /**
     * Détermine si la connexion est active ou pas
     */
    boolean connected;
    
    /**
     * Correspond à la liste des buffers qui sont prêts à être envoyés
     */
    private java.util.LinkedList<java.nio.ByteBuffer> outputBuffers;
    
    /**
     * Correspond à l'id unique de la connexion
     */
    private int id;
    
    /**
     * Correspond au buffer utilisé pour la lecture de la socket
     */
    private java.nio.ByteBuffer inputBuffer;
    
    /**
     * Correspond au buffer utilisé pour l'écriture de la socket
     */
    private java.nio.ByteBuffer outputBuffer;
    
    /**
     * Correspond à la taille des données écrites dans le buffer de la socket
     */
    private int sizeOutputBuffer = 0;

    
    
//CONSTRUCTOR
    /**
     * Crée une connexion
     */
    public Connection() {
        
    }

    
    
//METHODES PUBLICS
    /**
     * Détermine si la connexion est active ou pas
     * @return Retourne true si la connexion est active ou pas
     */
    public final boolean isConnected() {
        return connected;
    }
    
    /**
     * Envoie du texte à destination du serveur
     * @param str Correspond au texte à envoyer à destination du serveur
     */
    public final synchronized void write(String str){
        write(str.getBytes());
    }
    
    /**
     * Envoie un byte à destination du serveur
     * @param value Correspond au byte [0-255] à envoyer à destination du serveur
     */
    public final synchronized void write(int value) {
        if(this.outputBuffer == null){
            this.outputBuffer = java.nio.ByteBuffer.allocate(SIZE_BUFFER);
            this.sizeOutputBuffer = 0;
        }
        this.outputBuffer.put((byte)value);
        this.sizeOutputBuffer++;
        if(this.sizeOutputBuffer >= SIZE_BUFFER)
            flush();
    }
    
    /**
     * Envoie un tableau de bytes à destination du serveur
     * @param b Correspond au tableau de bytes à envoyer à destination du serveur
     */
    public final synchronized void write(byte b[]) {
        write(b, 0, b.length);
    }
    
    /**
     * Envoie une portion d'un tableau de bytes à destination du serveur
     * @param b Correspond au tableau de bytes à envoyer à destination du serveur
     * @param off Correspond au décalage de début dans le tableau b auquel les données sont écrites
     * @param len Correspond au nombre maximum d'octets à lire
     */
    public final synchronized void write(byte b[], int off, int len){
        if (b == null) {
            throw new NullPointerException();
        } else if ((off < 0) || (off > b.length) || (len < 0) ||
                   ((off + len) > b.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return;
        }
        for (int i = 0 ; i < len ; i++) {
            write(b[off + i]);
        }
    }
    
    /**
     * Envoie les données d'un flux à destination du serveur
     * @param is Correspond au flux d'entré dont on récupère les données et que l'on envoie
     * @throws java.io.IOException Si le flux ne peut être lu
     */
    public final synchronized void write(java.io.InputStream is) throws java.io.IOException {
        try (java.io.BufferedInputStream bis = new java.io.BufferedInputStream(is)) {
            int value;
            while((value = bis.read()) > -1){
                write(value);
            }
            flush();
        }
    }
    
    /**
     * Vide ce flux de sortie et force tous les octets de sortie mis en mémoire tampon à être écrits
     */
    public final synchronized void flush() {
        if(outputBuffer != null && sizeOutputBuffer > 0){
            this.outputBuffer.rewind();
            this.outputBuffers.addFirst(outputBuffer);
            this.outputBuffer     = java.nio.ByteBuffer.allocate(SIZE_BUFFER);
            this.sizeOutputBuffer = 0;
        }
    }
    
    /**
     * Déconnecte le client du serveur
     * @throws java.io.IOException Si le client n'est pas connecté
     */
    public final void disconnect() throws java.io.IOException {
        if(isConnected()){
            if(this.processor != null)
                this.processor.disconnection(socket, this, SERVER_DISCONNECTED_CLIENT);
            else
                this.client.disconnection(socket, this, CLIENT_DISCONNECTED_FROM_SERVER);
        }else
            throw new java.io.IOException("Client is already disconnected !");
    }

    /**
     * Renvoie le hashCode de la connexion
     * @return Retourne le hashCode de la connexion
     */
    @Override
    public final int hashCode() {
        int hash = 3;
        hash = 73 * hash + this.id;
        return hash;
    }

    /**
     * Détermine si deux connexions sont identiques ou pas
     * @param obj Correspond à la scondes connexions à comparer à la courante
     * @return Retourne true si elles sont identiques, sinon false
     */
    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Connection other = (Connection) obj;
        return this.id == other.id;
    }

    /**
     * Renvoie la connexion sous la forme d'une chaîne de caractères
     * @return Retourne la connexion sous la forme d'une chaîne de caractères
     */
    @Override
    public String toString() {
        return socket.toString();
    }
    
    
    
//METHODES PROTECTEDS
    /**
     * Lorsqu'un byte est disponible sur la socket
     * @param connection Correspond à la connexion sur laquelle un byte est disponible
     * @param value Correspond au byte disponible [0-255]
     */
    protected abstract void read(Connection connection, int value);
    
    /**
     * Lorsque le client est connecté
     */
    protected void clientConnected(){
        
    }
    
    /**
     * Lorsque le client est déconnecté
     * @param reason Correspond à la raison de la déconnexion
     */
    protected void clientDisconnected(ReasonDisconnection reason){
        
    }
    
    
    
//METHODES
    /**
     * Initialise la connexion
     * @param processor Correspond au service du serveur qui permet de gérer les clients connectés sur le serveur
     * @param socket Correspond à la socket utilisée entre le client et le serveur
     * @throws java.io.IOException Si la socket ne peut être configurée comme non bloquante
     */
    final void initServer(SocketProcessor processor, java.nio.channels.SocketChannel socket) throws java.io.IOException {
        this.id               = cptId++;
        this.processor        = processor;
        this.client           = null;
        this.socket           = socket;
        this.sizeOutputBuffer = 0;
        this.inputBuffer      = java.nio.ByteBuffer.allocate(SIZE_BUFFER);
        this.outputBuffers    = new java.util.LinkedList<>();
        this.connected        = true;
        this.socket.configureBlocking(false);
    }
    
    /**
     * Initialise la connexion
     * @param client Correspond au client
     * @param socket Correspond à la socket utilisée entre le client et le serveur
     * @throws java.io.IOException Si la socket ne peut être configurée comme non bloquante
     */
    final void initClient(TCPClient client, java.nio.channels.SocketChannel socket) throws java.io.IOException {
        this.id               = cptId++;
        this.processor        = null;
        this.client           = client;
        this.socket           = socket;
        this.sizeOutputBuffer = 0;
        this.inputBuffer      = java.nio.ByteBuffer.allocate(SIZE_BUFFER);
        this.outputBuffers    = new java.util.LinkedList<>();
        this.connected        = true;
        this.socket.configureBlocking(false);
    }
    
    /**
     * Active un cycle de lecture du flux
     * @return Retourne true si au minimum une opération a été effectuée, sinon false
     * @throws java.io.IOException Si rien ne peut être lu
     */
    final boolean read() throws java.io.IOException {
        boolean used = false;
        int count = this.socket.read(inputBuffer);
        this.inputBuffer.flip();
        this.inputBuffer.mark();
        if (count > -1) {
            while (this.inputBuffer.hasRemaining()) {
                used = true;
                read(this, this.inputBuffer.get());
            }
        } else {
            throw new java.io.IOException("Socket closed");
        }
        this.inputBuffer.compact();
        return used;
    }
    
    /**
     * Active un cycle d'écriture du flux
     * @return Retourne true si au minimum une opération a été effectuée, sinon false
     * @throws java.io.IOException Si rien ne peut être écrit
     */
    final boolean write() throws java.io.IOException {
        boolean used = false;
        try {
            java.nio.ByteBuffer buffer = outputBuffers.getLast();
            int len = socket.write(buffer);
            if (len == -1)
                throw new java.io.IOException("Socket closed");
            else
                used = true;
            if (buffer.remaining() == 0)
                outputBuffers.removeLast();
        } catch(java.util.NoSuchElementException ex) {}
        return used;
    }
    
    
    
}