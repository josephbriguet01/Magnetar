/*
 * Copyright (C) BRIGUET Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Briguet, August 2018
 */
package org.tcpudp;



import static org.tcpudp.Identity.GENERIC_IDENTITY;



/**
 * Cette classe permet de créer un serveur
 * @author BRIGUET
 * @version 1.0
 */
public class Server extends TCP{
    
    
    
    // <editor-fold defaultstate="collapsed" desc="CONSTRUCTOR">
    /**
     * Crée un serveur
     * @param identity Correspond à l'identité du serveur
     * @param port Correspond au port du serveur
     */
    public Server(Identity identity, int port) {
        super(identity, port);
    }
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="METHODES PUBLICS">
    /**
     * Ajoute un listener IConditionATBC
     * @param listener Correspond au listener
     */
    public void addIConditionATBC(IConditionATBC listener) {
        this.icatbc = listener;
    }
    
    /**
     * Enlève un listener IConditionATBC
     */
    public void removeIConditionATBC(){
        this.icatbc = null;
    }
    
    /**
     * Dévoile la présence du serveur
     * @param nameServer Correspond au nom du serveur qui sera dévoilé sur le réseau
     * @param portPropagation Correspond au port de propagation du nom du serveur sur le réseau
     */
    public void startPropagation(String nameServer, int portPropagation){
        if(aus == null){
            aus = new AuthServer(port, nameServer);
            aus.startAuthServer(portPropagation);
        }
    }
    
    /**
     * Le serveur n'est plus dévoilé sur le réseau
     */
    public void stopPropagation(){
        if(aus != null){
            aus.stopAuthServer();
            aus = null;
        }
    }
    
    /**
     * Lance l'écoute des nouveaux clients et crée des services associés aux nouveaux clients
     * @param encrypt_flux Correspond à la variable qui dit si oui ou non les flux doivent être chiffrée
     */
    public void start_listening(boolean encrypt_flux){
        ao = new AcceptOther(this, port, routage_table, list_i_statut, list_i_received, list_i_log, encrypt_flux);
        ao.start();
    }
    
    /**
     * Stoppe l'écoute des nouveaux clients
     */
    public void stop_listening(){
        if(this.ao != null){
            this.ao.stop_listening();
            stopPropagation();
        }
    }
    
    /**
     * Stoppe les services des clients connectés au serveur
     */
    public void stop_services(){
        if(this.ao != null){
            this.ao.stop_services();
        }
    }
    
    /**
     * Stoppe l'écoute des nouveaux clients et Stoppe les services des clients connectés au serveur
     */
    public void stop_server(){
        if(this.ao != null){
            this.ao.stop_server();
            stopPropagation();
        }
    }
    
    /**
     * Déconnecte un client du serveur
     * @param identity_client Correspond au client à déconnecter
     */
    public void disconnect_client(Identity identity_client){
        if(identity_client != null){
            try{
                for(int i=0;i<routage_table.size();i++){
                    if(routage_table.get(i).getIdentity_other().equals(identity_client)){
                        routage_table.get(i).send_disconnection();
                        break;
                    }
                }
            }catch(java.lang.NullPointerException | org.tcpudp.SendingException e){

            }
            
        }else{
            throw new org.tcpudp.ErrorDisconnectionException("Client = null");
        }
    }
    
    /**
     * Envoie un objet à un client connecté
     * @param receiver Correspond à l'identité de ce client
     * @param obj Correspond à l'objet à envoyer
     */
    public void send(Identity receiver, Object obj){
        try{
            for(int i=0;i<routage_table.size();i++){
                if(routage_table.get(i).getIdentity_other().equals(receiver)){
                    routage_table.get(i).getFlux().send(obj);
                    break;
                }
            }
        }catch(java.lang.NullPointerException | org.tcpudp.SendingException e){
                
        }
    }
    
    /**
     * Renvoie la liste des identités des clients connectés
     * @return Retourne la liste des identités des clients connectés
     */
    public synchronized java.util.List<Identity> getConnectedClient(){
        java.util.List<Service> services = routage_table.getList();
        synchronized(services){
            java.util.List<Identity> tempo = new java.util.ArrayList<>();
            synchronized(tempo){
                for(int i=0;i<services.size();i++){
                    Service s = services.get(i);
                    Identity id = s.getIdentity_other();
                    if(!id.getIdentity().equals(GENERIC_IDENTITY)){
                        tempo.add(id);
                    }
                }
                return tempo;
            }
        }
    }
    // </editor-fold>

    
    
    // <editor-fold defaultstate="collapsed" desc="METHODES PROTECTED">
    /**
     * Renvoie le processus d'écoute des nouveaux clients connectés
     * @return Retourne le processus
     */
    protected AcceptOther getAo() {
        return ao;
    }
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="ATTRIBUTS">
    /**
     * Correspond à la classe qui va écouter l'arrivée des nouveaux clients
     */
    private AcceptOther ao;
    /**
     * Correspond à la classe qui vérifie qi un client à le droit de se connecter
     */
    IConditionATBC      icatbc;
    /**
     * Correspond au serveur de propagation de l'identité du serveur
     */
    private AuthServer aus;
    // </editor-fold>
    
    
    
}