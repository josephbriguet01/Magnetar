/*
 * Copyright (C) JasonPercus Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by JasonPercus, 10/2022
 */
package com.jasonpercus.magnetar;



/**
 * Cette classe abstraite représente un client TCP
 * @author JasonPercus
 * @version 1.0
 */
public abstract class TCPClient extends TCP {

    
    
//ATTRIBUTS
    /**
     * Correspond à la socket utilisé pour communiquer avec le serveur
     */
    private java.nio.channels.SocketChannel socket;
    
    /**
     * Correspond à l'objet {@link Connection} qui représente une connexion vers le serveur
     */
    private Connection connection;

    
    
//CONSTRUCTOR
    /**
     * Crée un client TCP
     * @param address Correspond à l'adresse ip et port d'écoute du serveur
     */
    public TCPClient(java.net.InetSocketAddress address) {
        super(address);
    }

    
    
//GETTERS
    /**
     * Renvoie la connexion entre le client et le serveur
     * @return Retourne la connexion entre le client et le serveur
     */
    public final Connection getConnection() {
        return connection;
    }
    
    /**
     * Détermine si le client est connecté au serveur ou pas
     * @return Retourne true si c'est le cas, sinon false
     */
    public final boolean isConnected() {
        if(connection != null)
            return connection.isConnected();
        else
            return false;
    }

    
    
//METHODES PUBLICS
    /**
     * Connecte le client à un serveur
     * @throws java.io.IOException Si le client est déjà connecté, ou si le serveur n'est pas atteignable ou si une erreur inconnue survient
     */
    @SuppressWarnings("SleepWhileInLoop")
    public final void connect() throws java.io.IOException {
        if(!isConnected()) {
            try {
                this.socket = java.nio.channels.SocketChannel.open(this.address);
                this.socket.configureBlocking(false);
                this.accepted(socket);
                this.connection = newConnection();
                this.connection.initClient(this, socket);
                this.clientConnected(connection);
                this.connection.clientConnected();
                while (isConnected()) {
                    boolean used;
                    try{
                        used = executeCycle();
                    }catch(java.io.IOException ex){
                        disconnection(socket, connection, ReasonDisconnection.SERVER_DISCONNECTED_CLIENT);
                        used = true;
                    }
                    if (!used)
                        Thread.sleep(10);
                }
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }else{
            throw new java.io.IOException("Client is already connected !");
        }
    }

    /**
     * Déconnecte le client du serveur
     * @throws java.io.IOException Si le client n'est pas connecté
     */
    public final void disconnect() throws java.io.IOException {
        if(isConnected())
            this.connection.disconnect();
        else
            throw new java.io.IOException("Client is already disconnected !");
    }
    
    /**
     * Envoie du texte à destination du serveur
     * @param str Correspond au texte à envoyer à destination du serveur
     * @throws java.io.IOException Si le client n'est pas connecté
     */
    public final synchronized void write(String str) throws java.io.IOException {
        if(isConnected())
            this.connection.write(str);
        else
            throw new java.io.IOException("Client is not connected !");
    }
    
    /**
     * Envoie un byte à destination du serveur
     * @param value Correspond au byte [0-255] à envoyer à destination du serveur
     * @throws java.io.IOException Si le client n'est pas connecté
     */
    public final synchronized void write(int value) throws java.io.IOException {
        if(isConnected())
            this.connection.write(value);
        else
            throw new java.io.IOException("Client is not connected !");
    }
    
    /**
     * Envoie un tableau de bytes à destination du serveur
     * @param b Correspond au tableau de bytes à envoyer à destination du serveur
     * @throws java.io.IOException Si le client n'est pas connecté
     */
    public final synchronized void write(byte b[]) throws java.io.IOException {
        if(isConnected())
            this.connection.write(b);
        else
            throw new java.io.IOException("Client is not connected !");
    }
    
    /**
     * Envoie une portion d'un tableau de bytes à destination du serveur
     * @param b Correspond au tableau de bytes à envoyer à destination du serveur
     * @param off Correspond au décalage de début dans le tableau b auquel les données sont écrites
     * @param len Correspond au nombre maximum d'octets à lire
     * @throws java.io.IOException Si le client n'est pas connecté
     */
    public final synchronized void write(byte b[], int off, int len) throws java.io.IOException{
        if(isConnected())
            this.connection.write(b, off, len);
        else
            throw new java.io.IOException("Client is not connected !");
    }
    
    /**
     * Envoie les données d'un flux à destination du serveur
     * @param is Correspond au flux d'entré dont on récupère les données et que l'on envoie
     * @throws java.io.IOException Si le client n'est pas connecté ou si le flux ne peut pas être lu
     */
    public final synchronized void write(java.io.InputStream is) throws java.io.IOException {
        if(isConnected())
            this.connection.write(is);
        else
            throw new java.io.IOException("Client is not connected !");
    }
    
    /**
     * Vide ce flux de sortie et force tous les octets de sortie mis en mémoire tampon à être écrits
     * @throws java.io.IOException Si le client n'est pas connecté
     */
    public final synchronized void flush() throws java.io.IOException {
        if(isConnected())
            this.connection.flush();
        else
            throw new java.io.IOException("Client is not connected !");
    }
    
    
    
//METHODE
    /**
     * Déclare ce client comme déconnecté
     * @param socket Correspond à la socket du client déconnecté
     * @param connection Correspond à la connexion du client déconnecté
     * @param reason Correspond à la raison de la déconnexion du client
     */
    @SuppressWarnings("UnusedAssignment")
    void disconnection(java.nio.channels.SocketChannel socket, Connection connection, ReasonDisconnection reason) {
        if (isConnected()) {
            try {
                if(receiveEOS)
                    connection.read(connection, -1);
                connection.connected = false;
                socket.close();
                connection.clientDisconnected(reason);
                clientDisconnected(connection, reason);
                removed(socket);
                connection = null;
            } catch (java.io.IOException ex) {
                connection.connected = true;
                java.util.logging.Logger.getLogger(TCPServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        }
    }

    
    
//METHODE PRIVATE
    /**
     * Execute un cycle d'analyse (lit les données reçues pour chaque client et écrit les données à envoyer pour chaque client)
     * @return Retourne true si au minimum une opération a été effectuée, sinon false
     */
    private boolean executeCycle() throws java.io.IOException {
        boolean used = false;
        used = used | connection.read();
        used = used | connection.write();
        return used;
    }

    
    
}