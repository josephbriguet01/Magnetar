/*
 * Copyright (C) BRIGUET Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Briguet, August 2018
 */
package org.tcp;



import network.IPv4;



/**
 * Cette classe permet de créer un client qui pourra se connecter en TCP à un serveur
 * @author BRIGUET
 * @version 1.0
 */
public class Client extends TCP{



    // <editor-fold defaultstate="collapsed" desc="CONSTRUCTOR">
    /**
     * Crée un client. Ce client pourra se connecter à un serveur
     * @param identity Correspond à l'identité de ce client
     * @param host Correspond à l'adresse ip du serveur
     * @param port Correspond au port du serveur
     */
    public Client(Identity identity, IPv4 host, int port) {
        super(identity, port);
        this.host       = host.getIpv4();
        this.connected  = false;
    }
    // </editor-fold>



    // <editor-fold defaultstate="collapsed" desc="GETTER">
    /**
     * Revoie l'identité du serveur
     * @return Retourne l'identité du serveur ou null si le client n'est pas connecté
     */
    public Identity     getIdentity_server() {
        return identity_server;
    }
    // </editor-fold>

    
    
    // <editor-fold defaultstate="collapsed" desc="METHODES PUBLICS">
    /**
     * Ajoute un IResultATBC au client. De cette manière celui-ci est au courant du type de déconnexion 
     * @param listener Correspond à la classe qui implémente IResultATBC
     */
    public void addIResultATBC(IResultATBC listener) {
        this.iratbc = listener;
    }
    
    /**
     * Supprime l'objet IResultATBC du client
     */
    public void removeIResultATBC(){
        this.iratbc = null;
    }
    
    /**
     * Crée une connexion entre le client et le serveur
     */
    public void connect_to(){
        try {
            //On se connecte au serveur
            java.net.Socket socket = new java.net.Socket(host, port);


            //On crée les flux d'entrée sortie
            this.flux = new Flux(socket);


            //Vérifie si les flux doivent être chiffré si oui flux chiffrera les objet
            INIT_ENCRYPT();


            //Les présentation sont faites
            this.identity_server = PRESENTATION();
            
            
            if(!identity.getIdentity().equals(Identity.GENERIC_IDENTITY)){
                //Passe à la douane
                DOUANE(socket);
            }else{
                DESTROY_GENERIC_IDENTITY(socket);
            }
            
            
        } catch (java.io.IOException ex) {
            throw new org.tcp.SocketException("Error initializing Socket");
        }
    }

    /**
     * Déconnecte le client du serveur
     */
    public void disconnect(){
        if(isConnected()){
            send(new MetaTCP(EMetaType.META_REQUEST_DISCONNECT));
        }
    }
    
    /**
     * Récupère ou pas les logs du serveur
     * @param enable Correspond à true si l'on veut récupérer les log, false (par défaut) sinon
     */
    public void redirectLog(boolean enable){
        send(new MetaTCP(EMetaType.META_REDIRECTION_LOG, enable));
    }

    /**
     * Envoie un objet au serveur si l'utilisateur est connecté
     * @param obj Correspond à l'objet à envoyer (Il doit être serialisable)
     */
    public void send(Object obj){
        if(this.connected){
            try{
                service.getFlux().send(obj);
            }catch(java.lang.NullPointerException ex){
                throw new ErrorConnectionException("No Connection");
            }
        }else{
            throw new ErrorConnectionException("You are not connected");
        }
    }
    // </editor-fold>



    // <editor-fold defaultstate="collapsed" desc="METHODES PRIVATES">
    /**
     * Les présentations entre le serveur et le client se font dans cette méthode. Ils échangent leur identité pour pouvoir se connaitre
     * @return Retourne l'identité du serveur (car on se situe dans cette classe au niveau du client)
     */
    private Identity    PRESENTATION(){
        Identity identity_other = (Identity) this.flux.receive();
        this.flux.send(getIdentity());
        return identity_other;
    }

    /**
     * Vérifie si les flux doivent être chiffré si oui flux chiffrera les objet
     */
    private void        INIT_ENCRYPT(){
        boolean enc = (boolean) this.flux.receive();
        if (enc) {
            RSA rsa = new RSA();
            rsa.generateKeys();
            String keyPublic = rsa.getKeyPublic_toString();
            this.flux.send(keyPublic);
            String keyEncrypted = (String) this.flux.receive();
            rsa.setPlainText(keyEncrypted);
            this.flux.setKey(rsa.decrypt());
        } else {
            this.flux.send(0);
        }
        this.flux.setEncrypt(enc);
    }
    
    /**
     * Le serveur vérifie que l'on a le droit de se connecter
     * @param socket Correspond à la socket de communication
     */
    private void        DOUANE(java.net.Socket socket){
        MetaTCP meta = (MetaTCP) this.flux.receive();
        if(meta.getMeta().equals(EMetaType.META_CONNECTION_AUTHORIZED)){
        
            //On renvoie la confirmation
            this.flux.send(meta);
            
            //Crée et démarre un service server
            this.service = new Service(this, null, this.identity_server, socket, this.flux, list_i_received, list_i_statut, list_i_log);
            service.start();
            
            //Le client est connecté
            this.connected = true;
            
        }else{
            
            //On récupère la cause du refus
            int result = (int) meta.getValue();
            
            //On renvoie la confirmation
            this.flux.send(meta);
            
            try {
                this.flux.closeFlux();
                socket.close();
            } catch (java.io.IOException ex) {
                throw new SocketException("Error close socket");
            }
            //Affiche le refus
            if(iratbc != null) iratbc.refusedToConnected(result);
            
        }
    }
    
    /**
     * Détruit la connexion d'un client générique du côté client
     * @param socket Correspond à l'extrémité de la connexion côté client
     */
    private void        DESTROY_GENERIC_IDENTITY(java.net.Socket socket){
        MetaTCP meta = (MetaTCP) this.flux.receive();
        if(meta.getMeta().equals(EMetaType.META_REQUEST_DISCONNECT)){
            try {
                this.flux.closeFlux();
                socket.close();
            } catch (java.io.IOException ex) {
                throw new SocketException("Error close socket");
            }
        }
    }
    // </editor-fold>

    
    
    // <editor-fold defaultstate="collapsed" desc="ATTRIBUTS">
    private final       String host;
    private Service     service;
    private Identity    identity_server;
    private Flux        flux;
    IResultATBC         iratbc;
    // </editor-fold>
    
    
    
}