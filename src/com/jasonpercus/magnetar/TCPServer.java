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
 * Cette classe abstraite représente un serveur TCP
 * @author JasonPercus
 * @version 1.0
 */
public abstract class TCPServer extends TCP implements AutoCloseable {
    
    
    
//ATTRIBUTS
    /**
     * Correspond à la liste des connexions en cours d'ouverture
     */
    private final java.util.Queue<java.nio.channels.SocketChannel> waitingRoomInputs;
    
    /**
     * Correspond à la liste des connexions en cours de fermeture
     */
    private final java.util.Queue<java.nio.channels.SocketChannel> waitingRoomOutputs;
    
    /**
     * Correspond à la liste des connexions en cours sur le serveur
     */
    private final java.util.HashMap<java.nio.channels.SocketChannel, Connection> table;
    
    /**
     * Détermine si le serveur bloque ou non la fermeture du programme (false = bloque la fermeture)
     */
    private final boolean daemon;
    
    /**
     * Correspond au serveur d'écoute des connexions entrantes
     */
    private java.nio.channels.ServerSocketChannel serverSocket;
    
    /**
     * Correspond au service qui accepte ou non les connexions entrantes
     * @see #waitingRoomInputs Lorsque les connexions sont acceptées, elles sont ajoutées dans la liste des connexions en cours d'ouverture
     */
    private SocketAccepter  accepter;
    
    /**
     * Correspond au service qui gère toutes les connexions sur le serveur
     */
    private SocketProcessor processor;
    
    /**
     * Détermine si le serveur est en activité ou pas
     */
    private boolean opened;

    
    
//CONSTRUCTORS
    /**
     * Crée un serveur TCP
     * @param address Correspond à l'adresse ip et port d'écoute du serveur
     */
    public TCPServer(java.net.InetSocketAddress address) {
        this(address, false);
    }
    
    /**
     * Crée un serveur TCP
     * @param address Correspond à l'adresse ip et port d'écoute du serveur
     * @param daemon Détermine si le serveur bloque ou non la fermeture du programme (false = bloque la fermeture)
     */
    public TCPServer(java.net.InetSocketAddress address, boolean daemon) {
        super(address);
        this.daemon             = daemon;
        this.waitingRoomInputs  = new java.util.concurrent.ArrayBlockingQueue<>(1024);
        this.waitingRoomOutputs = new java.util.concurrent.ArrayBlockingQueue<>(1024);
        this.table              = new java.util.HashMap<>();
    }
    
    
    
//EVTS
    /**
     * Lorsque le service d'écoute des nouveaux clients est en train de démarrer
     */
    protected void accepterStarting(){
        
    }
    
    /**
     * Lorsque le service d'écoute des nouveaux clients est démarré
     */
    protected void accepterStarted(){
        
    }
    
    /**
     * Lorsque le service qui gère les flux des clients est en train de démarrer
     */
    protected void processorStarting(){
        
    }
    
    /**
     * Lorsque le service qui gère les flux des clients est démarré
     */
    protected void processorStarted(){
        
    }
    
    /**
     * Controle les clients entrants
     * @param socket Correspond à la connexion entrante d'un nouveau client
     * @return Retourne true si le client a le droit de se connecter, sinon false
     */
    protected boolean customs(java.nio.channels.SocketChannel socket){
        return true;
    }
    
    
    
//METHODES PUBLICS
    /**
     * Lance le serveur
     * @throws java.io.IOException Si le serveur est déjà lancé ou si le port est déjà utilisé ou si une erreur inconnue survient
     */
    public void open() throws java.io.IOException {
        if(!opened){
            if (serverSocket == null) {
                this.serverSocket = java.nio.channels.ServerSocketChannel.open();
                this.serverSocket.configureBlocking(false);
                this.serverSocket.bind(address);
                this.opened = true;
            }
            
            if(this.processor == null){
                processorStarting();
                this.processor = new SocketProcessor();
                this.processor.start();
            }
            
            if(this.accepter == null){
                accepterStarting();
                this.accepter  = new SocketAccepter();
                this.accepter.start();
            }
        } else {
            throw new java.io.IOException("Server is already opened !");
        }
    }
    
    /**
     * Stoppe le serveur
     * @throws java.io.IOException Si le serveur n'est pas lancé
     */
    @Override
    public void close() throws java.io.IOException {
        if(this.opened)
            this.opened = false;
        else
            throw new java.io.IOException("Server is already closed !");
    }

    /**
     * Détermine si le serveur est démarré ou pas
     * @return Retourne true si c'est le cas, sinon false
     */
    public boolean isOpened() {
        return opened;
    }
    
    
    
//CLASS
    /**
     * Cette classe représente un service qui s'exécute sur un thread
     * @author JasonPercus
     * @version 1.0
     */
    private class Service extends Thread {

        
        
    //CONSTRUCTOR
        /**
         * Crée un service
         */
        public Service() {
            this.setDaemon(daemon);
        }
        
        
        
    }
    
    /**
     * Cette classe représente un service qui permet d'accepter les nouveaux clients qui se connectent
     * @author JasonPercus
     * @version 1.0
     */
    private class SocketAccepter extends Service {
        
        
        
        /**
         * Execute le service
         */
        @Override
        public void run() {
            accepterStarted();
            while(opened){
                try {
                    java.nio.channels.SocketChannel socket = serverSocket.accept();
                    if(socket != null){
                        if(customs(socket)) {
                            accepted(socket);
                            waitingRoomInputs.add(socket);
                        } else {
                            socket.close();
                        }
                    }
                } catch (java.io.IOException ex) {
                    java.util.logging.Logger.getLogger(TCPServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
            }
        }
        
        
        
    }
    
    /**
     * Cette classe représente un service qui permet de gérer les clients connectés sur le serveur
     * @author JasonPercus
     * @version 1.0
     */
    class SocketProcessor extends Service {

        
        
    //METHODE PUBLIC
        /**
         * Execute le service
         */
        @Override
        @SuppressWarnings("SleepWhileInLoop")
        public void run() {
            processorStarted();
            try {
                while (opened) {
                    boolean used = executeCycle();
                    if(!used)
                        Thread.sleep(10);
                }
                for(java.util.Map.Entry<java.nio.channels.SocketChannel, Connection> entry : table.entrySet()){
                    Connection connection = entry.getValue();
                    try {
                        connection.disconnect();
                    } catch (java.io.IOException ex) {
                        java.util.logging.Logger.getLogger(TCPServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    }
                }
                table.clear();
                waitingRoomInputs.clear();
                waitingRoomOutputs.clear();
            } catch (InterruptedException ex) {
                java.util.logging.Logger.getLogger(TCPServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        }
        
        
        
    //METHODES PRIVATES
        /**
         * Execute un cycle d'analyse (vérifie s'il existe un nouveau client de connecté, lit les données reçues pour chaque client, écrit les données à envoyer pour chaque client et vérifie s'il existe des clients à éjecter)
         * @return Retourne true si au minimum une opération a été effectuée, sinon false
         */
        private boolean executeCycle() {
            boolean used = false;
            used = used | takeNewSockets();
            used = used | readSockets();
            used = used | writeSockets();
            used = used | removeDisconnectedSockets();
            return used;
        }
        
        /**
         * Vérifie s'il existe des nouveaux clients de connectés. Si c'est le cas alors le service les ajoutes dans le cycle de lecture/écriture
         * @return Retourne true si au minimum une opération a été effectuée, sinon false
         */
        private boolean takeNewSockets() {
            boolean used = false;
            java.nio.channels.SocketChannel socket = waitingRoomInputs.poll();
            while (socket != null) {
                try {
                    Connection connection = newConnection();
                    connection.initServer(this, socket);
                    table.put(socket, connection);
                    clientConnected(connection);
                    connection.clientConnected();
                } catch (java.io.IOException ex) {
                    java.util.logging.Logger.getLogger(TCPServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
                socket = waitingRoomInputs.poll();
                used = true;
            }
            return used;
        }
        
        /**
         * Supprime du cycle lecture/écriture tous les clients qui sont déconnectés du serveur
         * @return Retourne true si au minimum une opération a été effectuée, sinon false
         */
        private boolean removeDisconnectedSockets() {
            boolean used = false;
            java.nio.channels.SocketChannel socket = waitingRoomOutputs.poll();
            while (socket != null) {
                table.remove(socket);
                removed(socket);
                socket = waitingRoomInputs.poll();
                used = true;
            }
            return used;
        }
        
        /**
         * Exécute un cycle de lecture pour tous les clients connectés
         * @return Retourne true si au minimum une opération a été effectuée, sinon false
         */
        private boolean readSockets(){
            boolean used = false;
            for(java.util.Map.Entry<java.nio.channels.SocketChannel, Connection> entry : table.entrySet()){
                java.nio.channels.SocketChannel socket  = entry.getKey();
                Connection connection = entry.getValue();
                try {
                    used = used | connection.read();
                } catch (java.io.IOException ex) {
                    disconnection(socket, connection, ReasonDisconnection.CLIENT_DISCONNECTED_FROM_SERVER);
                    used = true;
                }
            }
            return used;
        }
        
        /**
         * Exécute un cycle d'écriture pour tous les clients connectés
         * @return Retourne true si au minimum une opération a été effectuée, sinon false
         */
        private boolean writeSockets(){
            boolean used = false;
            for(java.util.Map.Entry<java.nio.channels.SocketChannel, Connection> entry : table.entrySet()){
                java.nio.channels.SocketChannel socket  = entry.getKey();
                Connection connection = entry.getValue();
                try {
                    used = used | connection.write();
                } catch (java.io.IOException ex) {
                    disconnection(socket, connection, ReasonDisconnection.CLIENT_DISCONNECTED_FROM_SERVER);
                    used = true;
                }
            }
            return used;
        }
        
        
        
    //METHODE
        /**
         * Lorsqu'un client est considéré comme déconnecté, alors on ferme proprement sa socket et on le déclare inutilisable puis on l'ajoute au cycle de purge
         * @param socket Correspond à la socket du client déconnecté
         * @param connection Correspond à la connexion du client déconnecté
         * @param reason Correspond à la raison de la déconnexion du client
         */
        void disconnection(java.nio.channels.SocketChannel socket, Connection connection, ReasonDisconnection reason){
            if(connection.isConnected()){
                try {
                    if(receiveEOS)
                        connection.read(connection, -1);
                    connection.connected = false;
                    socket.close();
                    waitingRoomOutputs.add(socket);
                    connection.clientDisconnected(reason);
                    clientDisconnected(connection, reason);
                } catch (java.io.IOException ex) {
                    connection.connected = true;
                    java.util.logging.Logger.getLogger(TCPServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
            }
        }
        
        
        
    }
    
    
    
}