/*
 * Copyright (C) BRIGUET Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Briguet, August 2018
 */
package com.jasonpercus.magnetar;

import com.jasonpercus.util.Strings;



/**
 * Cette classe est un service (client ou serveur) utilisé pour communiquer avec un destinataire
 * @author BRIGUET
 * @version 1.0
 */
public class Service extends Thread{

    
    
    // <editor-fold defaultstate="collapsed" desc="CONSTRUCTOR">
    /**
     * Crée un service
     * @param tcp Correspond au serveur ou au client qui traite ce service
     * @param routage_table Correspond à la table de routage du serveur
     * @param identity_other Correspond à l'identité du destinataire des messages
     * @param socket Correspond à la connexion en tre le destinataire et l'expediteur possédant ce service
     * @param flux Correspond aux flux d'entrées/sorties de la connexion
     * @param list_i_received Correspond à la liste des classes implémentant IReceived
     * @param list_i_statut Correspond à la liste des classes implémentant IStatut
     * @param list_i_log Correspond à la liste des classes implémentant ILog
     */
    public Service(TCP tcp, RoutageTable routage_table, Identity identity_other, java.net.Socket socket, Flux flux, ListSynchronized<IReceived> list_i_received, ListSynchronized<IStatutClient> list_i_statut, ListSynchronized<ILog> list_i_log) {
        this.tcp                = tcp;
        this.routage_table      = routage_table;
        this.identity_other     = identity_other;
        this.socket             = socket;
        this.flux               = flux;
        this.list_i_received    = list_i_received;
        this.list_i_statut      = list_i_statut;
        this.list_i_log         = list_i_log;
        this.id                 = Strings.generate(50);
        this.run_process        = true;
        this.redirect_log       = false;
        this.reason             = EDisconnectReason.UNKNOWN_DISCONNECTION;
    }
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="GETTERS">
    /**
     * Renvoie l'identité du client client ou du serveur à qui le serveur ou le client actuel est connecté
     * @return Retourne une identité
     */
    protected Identity getIdentity_other() {
        return identity_other;
    }

    /**
     * Renvoie les flux de la connexion
     * @return Retourne les flux
     */
    protected Flux getFlux() {
        return flux;
    }
    
    /**
     * Renvoie si oui ou non le service doit envoyer les logs du serveur
     * @return Retourne oui ou non
     */
    protected boolean isLogRedirection(){
        return this.redirect_log;
    }
    // </editor-fold>

    
    
    // <editor-fold defaultstate="collapsed" desc="METHODES PUBLICS & PROTECTED">
    /**
     * Ecoute les messages du client ou du serveur
     */
    @Override
    public void run() {
        //Le nouveau client s'est connecté (Affiche la nouvelle connection)
        NEW_CLIENT_HAS_CONNECTED(identity_other);
        
        //La connexion est active (pratique pour l'utilisateur final qui souhaite faire un isConnected)
        tcp.connected = true;
        
        //Ecoute l'arrivée des trames (pour un serveur ou pour un client)
        LISTEN();
        
        //La connexion est inactive (pratique pour l'utilisateur final qui souhaite faire un isConnected)
        tcp.connected = false;
        
        //Le client s'est déconnecté (Affiche la déconnection)
        NEW_CLIENT_HAS_DISCONNECTED(identity_other, reason);
    }
    
    /**
     * Traite la déconnexion d'un client
     * @param type_disconnect Correspond au type de communication
     */
    protected void disconnect(EMetaType type_disconnect){
        if(type_disconnect.equals(EMetaType.META_REQUEST_DISCONNECT)){
            
            //Crée la trame Meta
            MetaTCP meta = new MetaTCP(EMetaType.META_CONFIRM_DISCONNECT);
            
            //Envoie la trame
            this.getFlux().send(meta);
            
            //Ecrit le trame meta reçue dans les logs
            WRITE_LOG(new Log("Send META: "+meta.getMeta().toString()+((meta.getValue() != null) ? " VALUE "+meta.getValue().toString() : "")+" ("+identity_other+")", ETAG.NORMAL, ETAG.META, ETAG.SEND));
            
            //Détermine la raison de la déconnexion
            if(tcp instanceof Client) 
                reason = EDisconnectReason.SERVER_DISCONNECTED_CLIENT;
            else 
                reason = EDisconnectReason.CLIENT_DISCONNECTED;
        }else{
            //Détermine la raison de la déconnexion
            if(tcp instanceof Client) 
                reason = EDisconnectReason.CLIENT_DISCONNECTED;
        }
        
        //Arrête le processus d'écoute des trame entrante bouclée
        this.run_process = false;
        
        //Ferme les flux et la connexion
        CLOSE_FLUX_AND_CONNECTION();
    }
    
    /**
     * Envoye la demande de déconnexion de ce client à partir du serveur
     */
    protected void send_disconnection(){
        //Détermine la raison de la déconnexion
        reason = EDisconnectReason.SERVER_DISCONNECTED_CLIENT;
        
        //Crée la trame Meta
        MetaTCP meta = new MetaTCP(EMetaType.META_REQUEST_DISCONNECT);
        
        //Envoie la trame
        this.getFlux().send(meta);
        
        //Ecrit le trame meta reçue dans les logs
        WRITE_LOG(new Log("Send META: "+meta.getMeta().toString()+((meta.getValue() != null) ? " VALUE "+meta.getValue().toString() : "")+" ("+identity_other+")", ETAG.NORMAL, ETAG.META, ETAG.SEND));
    }
    
    /**
     * Stoppe le service
     */
    protected void close(){
        this.run_process = false;
    }

    /**
     * Redéfinit la méthode hashCode()
     * @return Retourne le résultat de la méthode
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + java.util.Objects.hashCode(this.id);
        return hash;
    }

    /**
     * Redéfinit la méthode equals()
     * @param obj Correspond à l'objet à tester
     * @return Retourne le résultat de la méthode
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Service other = (Service) obj;
        return java.util.Objects.equals(this.id, other.id);
    }
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="METHODES PRIVATES">
    /**
     * Cette méthode execute certaines actions en fonction de certaines communications privées entre le serveur et le client
     * @param meta Correspond au type de communication
     */
    private void PROCESS_META_TCP(MetaTCP meta){
        //S'il s'agit de trame MetaTCP de déconnexion ou de confirmation
        if(meta.getMeta().equals(EMetaType.META_REQUEST_DISCONNECT) || meta.getMeta().equals(EMetaType.META_CONFIRM_DISCONNECT))
            disconnect(meta.getMeta());
        
        //S'il s'agit d'une trame Log, on active ou pas la redirection
        if(meta.getMeta().equals(EMetaType.META_REDIRECTION_LOG))
            redirect_log = (boolean) meta.getValue();
        
        //Ecrit dans les logs la réception des trames metatcp
        WRITE_LOG(new Log("Received META: "+meta.getMeta().toString()+((meta.getValue() != null) ? " VALUE "+meta.getValue().toString() : "")+" ("+identity_other+")", ETAG.NORMAL, ETAG.META, ETAG.RECEIVED));
        
    }
    
    /**
     * Prévient le serveur qu'un nouveau client vient de se connecter
     * @param new_client Correspond au nom du nouveau client
     */
    @SuppressWarnings({"SynchronizeOnNonFinalField", "NestedSynchronizedStatement"})
    private synchronized void NEW_CLIENT_HAS_CONNECTED(Identity new_client){
        synchronized(list_i_statut){
            //Affiche qu'un client s'est connecté
            int size = list_i_statut.size();
            for(int i=0;i<size;i++){
                IStatutClient list_i_statut1 = list_i_statut.get(i);
                if(list_i_statut1 != null)
                    list_i_statut1.hasConnected(new_client);
            }
        }
        
        //Ecrit dans les logs la connexion de new_client
        WRITE_LOG(new Log("New client connected: "+new_client, ETAG.INFO, ETAG.CONNEXION));
    }
    
    /**
     * Prévient le serveur qu'un nouveau client vient de se connecter
     * @param new_client Correspond au nom du nouveau client
     * @param reason Correspond à la raison de la déconnexion
     */
    @SuppressWarnings({"SynchronizeOnNonFinalField", "NestedSynchronizedStatement"})
    private synchronized void NEW_CLIENT_HAS_DISCONNECTED(Identity new_client, EDisconnectReason reason){
        //Supprime le client de la table de routage
        if(routage_table != null){
            synchronized(routage_table){
                routage_table.remove(this);
            }
        }
        
        synchronized(list_i_statut){
            //Affiche qu'un client s'est déconnecté
            int size = list_i_statut.size();
            for(int i=0;i<size;i++){
                IStatutClient list_i_statut1 = list_i_statut.get(i);
                if(list_i_statut1 != null)
                    list_i_statut1.hasDisconnected(new_client, reason);
            }
        }
        
        //Ecrit dans les logs la déconnexion de new_client
        WRITE_LOG(new Log("Client disconnected: "+new_client+"("+reason+")", ETAG.INFO, ETAG.DECONNEXION));
    }
    
    /**
     * Ecoute l'arrivé des trames les analyse avec ANALYSE_TRAME
     */
    private void LISTEN(){
        try{
            while(run_process){

                ANALYSE_TRAME(this.flux.receive());
                
            }
        }catch(com.jasonpercus.magnetar.ReceivingIOException e){
            if(tcp instanceof Client) reason = EDisconnectReason.VIOLENTLY_SERVER_CLOSED;
            if(tcp instanceof Server) reason = EDisconnectReason.VIOLENTLY_CLIENT_CLOSED;
            
            WRITE_ERROR_LOG(e);
        }
    }
    
    /**
     * Détermine si la trame reçue est une trame en interne ou externe
     * @param receivedTrame Correspond à l'objet reçu
     */
    @SuppressWarnings("SynchronizeOnNonFinalField")
    private void ANALYSE_TRAME(Object receivedTrame){
        if (receivedTrame != null) {

            //S'il s'agit d'une trame interne
            if (IS_INTERN_TRAME(receivedTrame)){
                if(receivedTrame instanceof MetaTCP){
                    PROCESS_META_TCP((MetaTCP) receivedTrame);
                }
                if(receivedTrame instanceof Log){
                    synchronized(list_i_log){
                        int size = list_i_log.size();
                        for(int i=0;i<size;i++){
                            list_i_log.get(i).readLog((Log) receivedTrame);
                        }
                    }
                }
            }
                

            //S'il s'agit d'une trame externe
            else
                REDIRECT_EXTERN_TRAME(receivedTrame);

        }
    }
    
    /**
     * Renvoie si oui ou non la trame est une trame interne
     * @param receivedTrame Correspond à la trame reçue
     * @return Retourne true si la trame est une trame interne
     */
    private boolean IS_INTERN_TRAME(Object receivedTrame){
        return (receivedTrame instanceof MetaTCP || receivedTrame instanceof Log);
    }
    
    /**
     * Redirige vers l'utilisateur le reste des requêtes
     * @param receivedTrame Correspond à la trame reçue
     */
    @SuppressWarnings("SynchronizeOnNonFinalField")
    private synchronized void REDIRECT_EXTERN_TRAME(Object receivedTrame){
        synchronized(list_i_received){
            
            boolean redirected = false;
            Boolean containsIReceivedSync = null;
            
            //Redirige les trame externe
            int size = list_i_received.size();
            for(int i=size-1; i>-1; i--) 
                if(list_i_received.get(i) != null){
                    if(receivedTrame instanceof SyncTrame){
                        SyncTrame question = (SyncTrame) receivedTrame;
                        if(question.isQuestion()){
                            if(!redirected){
                                if(containsIReceivedSync == null){
                                    containsIReceivedSync = CONTAINS_IRECEIVED_SYNC();
                                }
                                if(containsIReceivedSync){
                                    if(list_i_received.get(i) instanceof IReceivedSync){
                                        redirected = true;
                                        SyncTrame response = SyncTrame.generateResponse(question, ((IReceivedSync)list_i_received.get(i)).receivedSync(identity_other, question.getObject()));
                                        this.getFlux().send(response);
                                    }
                                }else{
                                    redirected = true;
                                    SyncTrame response = SyncTrame.generateResponse(question, new ErrorNoImplementation("The recipient does not implement at any time IReceivedSync !"));
                                    this.getFlux().send(response);
                                }
                            }
                        }else{
                            list_i_received.get(i).received(identity_other, receivedTrame);
                        }
                    }else{
                        list_i_received.get(i).received(identity_other, receivedTrame);
                    }
                }
        }
        
        //Ecrit dans les logs la réception des trames externes
        WRITE_LOG(new Log("Redirect: "+receivedTrame.toString()+"("+identity_other+")", ETAG.NORMAL, ETAG.REDIRECT));
    }
    
    /**
     * Ferme les flux et la connexion
     */
    private void CLOSE_FLUX_AND_CONNECTION(){
        try {
            this.flux.closeFlux();
            socket.close();
            tcp.connected = false;
        } catch (java.io.IOException ex) {
            if(!ex.getMessage().equals("Socket closed"))
                WRITE_ERROR_LOG(ex);
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
     * Ecrit un log et le redirige aux clients si besoin
     * @param log Correspond au log à écrire
     */
    @SuppressWarnings({"NestedSynchronizedStatement", "SynchronizeOnNonFinalField"})
    private synchronized void WRITE_LOG(Log log){
        synchronized(list_i_log){
            int size = list_i_log.size();
            for(int i=0;i<size;i++){
                ILog list_i_log1 = list_i_log.get(i);
                list_i_log1.readLog(log);
            }
        }
        
        if(tcp instanceof Server){
            synchronized(routage_table){
                int size = routage_table.size();
                for(int i=0;i<size;i++){
                    Service s = routage_table.get(i);
                    if(s.isLogRedirection()){
                        try{
                            routage_table.get(i).getFlux().send(log);
                        }catch(com.jasonpercus.magnetar.SendingException e){
                            
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Détermine si au moins un receveur d'objet implémente IReceivedSync
     * @return Retourne true s'il en existe un sinon false
     */
    @SuppressWarnings("SynchronizeOnNonFinalField")
    private synchronized boolean CONTAINS_IRECEIVED_SYNC(){
        synchronized(list_i_received){
            int size = list_i_received.size();
            for(int i=size-1; i>-1; i--) {
                IReceived receiver = list_i_received.get(i);
                if(receiver != null && receiver instanceof IReceivedSync)
                    return true;
            }
            return false;
        }
    }
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="ATTRIBUTS">
    TCP                             tcp;
    Identity                        identity_other;
    java.net.Socket                 socket;
    Flux                            flux;
    boolean                         run_process;
    boolean                         redirect_log;
    ListSynchronized<IReceived>     list_i_received;
    ListSynchronized<IStatutClient> list_i_statut;
    ListSynchronized<ILog>          list_i_log;
    private final String            id;
    private EDisconnectReason       reason;
    private final RoutageTable      routage_table;
    // </editor-fold>
    
    
    
}