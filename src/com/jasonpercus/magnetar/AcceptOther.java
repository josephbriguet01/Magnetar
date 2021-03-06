/*
 * Copyright (C) JasonPercus Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by JasonPercus, August 2018
 */
package com.jasonpercus.magnetar;



import com.jasonpercus.encryption.Cipher;
import com.jasonpercus.encryption.Key;
import com.jasonpercus.encryption.rsa.RSA;
import com.jasonpercus.network.IPv4;



/**
 * Cette classe permet d'écouter l'arrivée des nouveaux clients et de leur créer un processus à chacun
 * @author JasonPercus
 * @version 1.0
 */
public class AcceptOther extends Thread {
    
    
    
    // <editor-fold defaultstate="collapsed" desc="ATTRIBUTS">
    /**
     * Correspond à l'objet TCP utilisé pour communiquer
     */
    TCP                             tcp;
    
    /**
     * Correspond au port TCP utilisé
     */
    int                             port;
    
    /**
     * Détermine si le serveur doit écouter les nouveaux clients qui viennent d'initialiser une socket
     */
    boolean                         listen_client;
    
    /**
     * Détermine si le serveur écoute les nouveaux clients qui viennent d'initialiser une socket
     */
    boolean                         listen;
    
    /**
     * Détermine si le serveur et les clients doivent chiffrer leur message
     */
    boolean                         encrypt_flux;
    
    /**
     * Correspond à la socket serveur. Celle qui écoute les connexions entrantes
     */
    java.net.ServerSocket           serverSocket;
    
    /**
     * Correspond à la table de routage. C'est cette table qui contient toute la liste des clients connectés
     */
    RoutageTable                    routage_table;
    
    /**
     * Correspond à la liste des listeners concernant les dé/connexions des clients/serveur
     */
    ListSynchronized<IStatutClient> list_i_statut;
    
    /**
     * Correspond à la liste des listeners concernant les réceptions de messages des clients/serveur
     */
    ListSynchronized<IReceived>     list_i_received;
    
    /**
     * Correspond à la liste des listeners concernant la redirection des logs du serveur vers le(s) client(s)
     */
    ListSynchronized<ILog>          list_i_log;
    
    /**
     * Correspond aux flux entrants/sortants d'une connexion tcp entre un serveur et un client
     */
    Flux                            flux;
    
    /**
     * Détermine si à la prochaine connexion d'un client, le serveur doit stopper l'écoute des connexions entrantes
     */
    boolean                         afterCloseServer;
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="CONSTRUCTOR">
    /**
     * Crée un processus qui va écouter l'arrivée des nouveaux clients connectés
     * @param tcp Correspond à la classe manipulable du serveur
     * @param port Correspond au port de connexion du serveur
     * @param routage_table Correspond à la table de routage du serveur. Elle contient tous les processus clients de chaque connexion
     * @param list_i_statut Correspond à la liste des classes qui implémentent IStatut
     * @param list_i_received Correspond à la liste des classes qui implémentent IReceived
     * @param list_i_log Correspond à la liste des classes qui implémentent ILog
     * @param encrypt_flux Se paramètre dit si oui ou non les flux tcp doivent être crypté
     */
    protected AcceptOther(TCP tcp, int port, RoutageTable routage_table, ListSynchronized<IStatutClient> list_i_statut, ListSynchronized<IReceived> list_i_received, ListSynchronized<ILog> list_i_log, boolean encrypt_flux) {
        try {
            this.listen_client      = true;
            this.encrypt_flux       = encrypt_flux;
            this.tcp                = tcp;
            serverSocket            = new java.net.ServerSocket(port);
            this.routage_table      = routage_table;
            this.list_i_statut      = list_i_statut;
            this.list_i_received    = list_i_received;
            this.list_i_log         = list_i_log;
            this.port               = port;
        } catch (java.io.IOException ex) {
            WRITE_ERROR_LOG(ex);
            throw new com.jasonpercus.magnetar.ServerSocketException(ex.getMessage(), ex.getCause());
        }
    }
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="METHODES PUBLICS & PROTECTED">
    /**
     * Renvoie si oui ou non le serveur écoute les nouveaux clients
     * @return Retourne true, s'il écoute, sinon false
     */
    public boolean isListen() {
        return listen;
    }

    /**
     * Ecoute la connexion des nouveaux clients et crée un processus à chacun
     */
    @Override
    public void run() {
        //On prévient le serveur que celui-ci a bien démarré
        SERVER_IS_STARTED();
        
        
        //On écoute les nouveaux clients
        while(listen_client){
            listen = true;
            try {
                
                
                //On accepte un client
                java.net.Socket socket = serverSocket.accept();
                
                try{
                    //On crée les flux d'entrée sortie
                    this.flux = new Flux(socket, encrypt_flux);


                    //Vérifie si les flux doivent être chiffré, si oui flux chiffrera les objets
                    INIT_ENCRYPT();


                    //Les présentation sont faites
                    Identity identity_other = PRESENTATION();


                    //On vérifie qu'il ne s'agit pas de l'identité générique de fermeture d'écoute
                    if(!identity_other.getIdentity().equals(Identity.GENERIC_IDENTITY)){
                        //Passe la douane
                        DOUANE(socket, identity_other);
                    }else{
                        DESTROY_GENERIC_IDENTITY(socket);
                    }
                }catch(com.jasonpercus.magnetar.FluxStreamException e){
                    
                }
                
                
            } catch (java.io.IOException ex) {
                WRITE_ERROR_LOG(ex);
                throw new com.jasonpercus.magnetar.SocketException(ex.getMessage(), ex.getCause());
            }
        }
        
        //Ferme l'écoute
        CLOSE_LISTENING(this.afterCloseServer);
    }
    
    /**
     * Provoque l'arrêt de l'écoute des nouveaux clients du serveur en créant la connexion d'un client généric
     */
    protected void stop_listening(){
        listen_client = false;
        this.afterCloseServer = true;
        new Client(Identity.getInstanceGeneric(), new IPv4("127.0.0.1"), port).connect_to();
    }
    
    /**
     * Ferme les services des clients
     */
    protected void stop_services(){
        CLOSE_SERVICES();
    }
    
    /**
     * Stoppe l'écoute des nouveaux clients ainsi que les services
     */
    protected void stop_server(){
        listen_client = false;
        new Client(Identity.getInstanceGeneric(), new IPv4("127.0.0.1"), port).connect_to();
        afterCloseServer = false;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            WRITE_ERROR_LOG(ex);
            java.util.logging.Logger.getLogger(AcceptOther.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        CLOSE_SERVICES();
    }
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="METHODES PRIVATES">
    /**
     * Les présentations entre le serveur et le client se font dans cette méthode. Ils échangent leur identité pour pouvoir se connaitre
     * @return Retourne l'identité du client (car on se situe dans cette classe au niveau du serveur)
     */
    private Identity PRESENTATION(){
        this.flux.send(tcp.getIdentity());
        return (Identity) this.flux.receive();
    }
    
    /**
     * Vérifie si les flux doivent être chiffré, si oui flux chiffrera les objets
     */
    private void INIT_ENCRYPT(){
        this.flux.send(this.flux.isSecured());
        boolean enc = this.flux.isSecured();
        if (enc) {
            Cipher cipherEncryptor  = new RSA();
            Cipher cipherUsable     = NEW_CIPHER();
            Key keyEncryptor        = (Key) this.flux.receive();
            Key keyUsable           = cipherUsable.generateKey();
            this.flux.send(cipherEncryptor.encrypt(keyEncryptor, keyUsable.toString()));
            this.flux.setKey(keyUsable, cipherUsable);
        } else {
            int val = (int) this.flux.receive();
        }
        this.flux.setEncrypt(enc);
    }
    
    /**
     * Vérifie que le client à le droit de se connecter
     * @param socket Correspond à la socket de communication
     * @param identity_other Correspond à l'identité du client
     */
    private void DOUANE(java.net.Socket socket, Identity identity_other){
        
        //Envoie la réponse d'autorisation ou non de connexion
        SEND_RESPONSE_CONNECTION(identity_other);
        
        //Récupère la réponse qui doit être identique à la SEND_RESPONSE_CONNECTION
        MetaTCP meta = (MetaTCP) this.flux.receive();

        //Analyse la réponse. En fonction de la réponse on crée soit un service soit on ferme la connection
        ANALYSE_RESPONSE(meta, socket, identity_other);
        
    }
    
    /**
     * Ferme le listening
     */
    private void CLOSE_LISTENING(boolean executeServerStopped){
        if(listen){
            listen = false;
            try {
                serverSocket.close();
            } catch (java.io.IOException ex) {
                WRITE_ERROR_LOG(ex);
                throw new com.jasonpercus.magnetar.SocketException(ex.getMessage(), ex.getCause());
            }
            
            WRITE_LOG(new Log("Server not listen", ETAG.INFO, ETAG.STOP_LISTENING));
        }
        
        SERVER_IS_STOPPED(executeServerStopped);
    }
    
    /**
     * Déconnecte tous les clients du serveur
     */
    @SuppressWarnings("SynchronizeOnNonFinalField")
    private void CLOSE_SERVICES(){
        int size_table_routage = routage_table.size();
        
        synchronized(routage_table){
            int size = routage_table.size();
            for(int i=0;i<size;i++){
                try{
                    routage_table.get(i).send_disconnection();
                }catch(com.jasonpercus.magnetar.SendingException ex){
                    WRITE_ERROR_LOG(ex);
                }
            }
        }
        
        if(size_table_routage > 0)
            WRITE_LOG(new Log("Server close clients services", ETAG.INFO, ETAG.STOP_SERVICES));
        
        SERVER_IS_STOPPED(true);
    }
    
    /**
     * Prévient le serveur que celui-ci est stoppé
     */
    @SuppressWarnings({"SleepWhileInLoop", "NestedSynchronizedStatement", "SynchronizeOnNonFinalField"})
    private synchronized void SERVER_IS_STOPPED(boolean execute){
        if(!listen && routage_table.size() > 0 && execute){
            while(routage_table.size() > 0) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    WRITE_ERROR_LOG(ex);
                    java.util.logging.Logger.getLogger(AcceptOther.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
            }
        }
        
        if(!listen && routage_table.size() == 0 && execute){
            synchronized(list_i_statut){
                int size = list_i_statut.size();
                for(int i=0;i<size;i++){
                    IStatutClient list_i_statut1 = list_i_statut.get(i);
                    if (list_i_statut1 instanceof IStatutServer)
                        ((IStatutServer) list_i_statut1).serverIsStop();
                }
            }
        }
    }
    
    /**
     * Prévient le serveur que celui-ci est démarré
     */
    @SuppressWarnings({"SynchronizeOnNonFinalField", "NestedSynchronizedStatement", "SleepWhileHoldingLock"})
    private synchronized void SERVER_IS_STARTED(){
        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                java.util.logging.Logger.getLogger(AcceptOther.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            if(listen && routage_table.size() == 0){
                synchronized(list_i_statut){
                    int size = list_i_statut.size();
                    for(int i=0;i<size;i++){
                        IStatutClient list_i_statut1 = list_i_statut.get(i);
                        if (list_i_statut1 instanceof IStatutServer){
                            ((IStatutServer) list_i_statut1).serverIsStart();
                        }
                    }
                }
            }
        }).start();
        
        WRITE_LOG(new Log("Server listen new clients", ETAG.INFO, ETAG.START_LISTENING));
    }
    
    /**
     * Envoie l'autorisation de connexion
     */
    private void SEND_CONNECTION_AUTHORIZED(){
        this.flux.send(new MetaTCP(EMetaType.META_CONNECTION_AUTHORIZED));
    }
    
    /**
     * Envoie la trame de rejet
     * @param code Correspond au code de rejet
     */
    private void SEND_CONNECTION_REJECTED(int code){
        this.flux.send(new MetaTCP(EMetaType.META_CONNECTION_REJECTED, code));
    }
    
    /**
     * Analyse l'identité de l'utilisateur entrant et détermine si celui-ci à le droit se connecter ou pas
     * @param icatbc Correspond à l'éventuelle variable qui détermine si un utilisteur à le droit de se connecter
     * @param identity_other Correspond à l'utilisateur qui cherche à se connecter
     */
    private void SYSTEM_ICONDITION_ACTIVE(IConditionATBC icatbc, Identity identity_other){
        int result = icatbc.authorizedToBeConnected(identity_other);
        if (result == 0) SEND_CONNECTION_AUTHORIZED();
        else             SEND_CONNECTION_REJECTED(result);
    }
    
    /**
     * Envoie une réponse de connection à l'utilisateur après avoir vérifié si l'utilisateur à implémenté IConditionATBC. Dans le cas où l'utilisateur l'a bien implémenté, il faut analyser le nouveau venu
     * @param identity_other Correspond au nouveau venu
     */
    private void SEND_RESPONSE_CONNECTION(Identity identity_other){
        IConditionATBC icatbc = ((Server) tcp).icatbc;
        if (icatbc != null) 
            SYSTEM_ICONDITION_ACTIVE(icatbc, identity_other);
        else 
            SEND_CONNECTION_AUTHORIZED();
    }
    
    /**
     * Crée un service de communication
     * @param socket Correspond à la socket de communication
     * @param identity_other Correspond à l'utilisateur qui vient de se connecter
     */
    private void CREATE_SERVICE(java.net.Socket socket, Identity identity_other){
        Service service = new Service(tcp, routage_table, identity_other, socket, this.flux, list_i_received, list_i_statut, list_i_log);
        routage_table.add(service);
        service.start();
    }
    
    /**
     * Ferme le flux de communication
     * @param socket Correspond à la socket de communication
     */
    private void CLOSE_CONNECTION(java.net.Socket socket){
        try {
            this.flux.closeFlux();
            socket.close();
        } catch (java.io.IOException ex) {
            WRITE_ERROR_LOG(ex);
            throw new SocketException("Error close socket");
        }
    }
    
    /**
     * Analyse la réponse de l'utilisateur. Si en effet l'utilisateur reconnait devoir se déconnecter par rejet, le serveur ferme les flux, sinon il crée un service de communication
     * @param metaCorrespond à la réponse de l'utilisateur
     * @param socket Correspond à la socket de communication
     * @param identity_other Correspond à l'identité de l'utilisateur entrant
     */
    private void ANALYSE_RESPONSE(MetaTCP meta, java.net.Socket socket, Identity identity_other){
        if (meta.getMeta().equals(EMetaType.META_CONNECTION_AUTHORIZED))
            CREATE_SERVICE(socket, identity_other);
        else
            CLOSE_CONNECTION(socket);
    }
    
    /**
     * Détruit la connexion d'un client générique du côté serveur
     * @param socket Correspond à l'extrémité de la connexion côté serveur
     */
    private void DESTROY_GENERIC_IDENTITY(java.net.Socket socket){
        this.flux.send(new MetaTCP(EMetaType.META_REQUEST_DISCONNECT));
        try {
            this.flux.closeFlux();
            socket.close();
        } catch (java.io.IOException ex) {
            WRITE_ERROR_LOG(ex);
            throw new SocketException("Error close socket");
        }
    }
    
    /**
     * Ecrit une exception dans les logs
     * @param e Correpsond à l'exception à insérer dans les logs
     */
    private void WRITE_ERROR_LOG(Exception e){
        WRITE_LOG(new Log(e.getMessage(), ETAG.SEVERE, ETAG.ERROR));
    }
    
    /**
     * Ecrit un log
     * @param log Correspond au log à écrire
     */
    @SuppressWarnings({"NestedSynchronizedStatement", "SynchronizeOnNonFinalField"})
    private synchronized void WRITE_LOG(Log log){
        try{
            synchronized(list_i_log){
                int size = list_i_log.size();
                for(int i=0;i<size;i++){
                    ILog list_i_log1 = list_i_log.get(i);
                    if(list_i_log1 != null)
                        list_i_log1.readLog(log);
                }
            }
        }catch(java.lang.NullPointerException e){ }
    }
    
    /**
     * Crée un nouvel objet de dé/chiffrement
     * @return Retourne un nouvel objet de dé/chiffrement
     */
    private Cipher NEW_CIPHER(){
        try {
            return (Cipher) Server.ENCRYPTOR.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AcceptOther.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        return null;
    }
    // </editor-fold>



}