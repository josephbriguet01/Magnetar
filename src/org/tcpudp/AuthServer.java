/*
 * Copyright (C) BRIGUET Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Briguet, September 2018
 */
package org.tcpudp;



/**
 * Cette classe permet de renseigner aux détections des serveurs des client le nom, l'ip et le port d'écoute du serveur
 * @author Briguet
 * @version 1.0
 */
public class AuthServer {
    
    
    
    // <editor-fold defaultstate="collapsed" desc="CONSTRUCTOR">
    /**
     * Crée un serveur de découverte
     * @param portListening Correspond au port d'écoute du serveur réelle
     * @param nameServer Correspond au nom du serveur qui sera affiché dans la découverte réseau
     */
    protected AuthServer(int portListening, String nameServer) {
        this.portListening = portListening;
        this.nameServer = nameServer;
    }
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="METHODES PUBLICS">
    /**
     * Lance le serveur de propagation (le serveur réel deviendra donc visible sur le réseau par l'intermédiaire du serveur de propagation)
     * @param portPropagation Correspond au port d'écoute (!= portListening) du serveur de propagation
     */
    protected void startAuthServer(int portPropagation){
        if(s == null){
            if(portPropagation != portListening){
                this.s = new Server(new Identity(), portPropagation);
                this.s.addIStatutListener(new IStatutServer() {
                    @Override
                    public void serverIsStart() {

                    }

                    @Override
                    public void serverIsStop() {

                    }

                    @Override
                    public void hasConnected(Identity identity) {
                        AuthServer.this.s.send(identity, new IdentificationServer(nameServer, portListening));
                    }

                    @Override
                    public void hasDisconnected(Identity identity, EDisconnectReason reason) {

                    }
                });
                this.s.start_listening(true);
            }else{
                throw new ErrorDiscoverException("portListening = portPropagation");
            }
        }
    }
    
    /**
     * Stoppe le serveur de propagation. Le serveur réel peut continuer à tourner mais est invisible à la vue des clients
     */
    protected void stopAuthServer(){
        if(s!=null){
            this.s.stop_server();
            this.s = null;
        }
    }
    // </editor-fold>

    
    
    // <editor-fold defaultstate="collapsed" desc="ATTRIBUTS">
    /**
     * Correspond au port d'écoute du serveur != port de propagation
     */
    private final int portListening;
    /**
     * Correspond au nom du serveur qui l'identifiera dans la découverte d'équipements sur le réseau
     */
    private final String nameServer;
    /**
     * Correspond à un serveur de propagation
     */
    private Server s;
    // </editor-fold>
    
    
    
}