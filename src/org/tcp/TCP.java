/*
 * Copyright (C) BRIGUET Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Briguet, Août 2018
 */
package org.tcp;



/**
 * Cette classe permet la création d'un serveur ou d'un client
 * @author BRIGUET
 * @version 1.0
 */
public abstract class TCP {

    
    
    // <editor-fold defaultstate="collapsed" desc="CONSTRUCTOR">
    /**
     * Crée un objet TCP
     * @param identity Correspond à l'identité du serveur ou du client
     * @param port Correspond au port de connexion du serveur
     */
    public TCP(Identity identity, int port) {
        this.identity           = identity;
        this.port               = port;
        this.list_i_received    = new java.util.ArrayList<>();
        this.list_i_statut      = new java.util.ArrayList<>();
        this.list_i_log         = new java.util.ArrayList<>();
        this.routage_table      = new RoutageTable();
    }
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="METHODES PUBLICS">
    /**
     * Ajoute un listener ILog
     * @param listener Correspond au listener
     */
    public void addILogListener(ILog listener){
        list_i_log.add(listener);
    }
    
    /**
     * Enlève un listener ILog
     * @param listener Correspond au listener
     */
    public void removeILogListener(ILog listener){
        list_i_log.remove(listener);
    }
    
    /**
     * Enlève un listener ILog
     * @param index Correspond à l'index du listener
     */
    public void removeILogListener(int index){
        list_i_log.remove(index);
    }
    
    /**
     * Ajoute un listener IStatut
     * @param listener Correspond au listener
     */
    public void addIStatutListener(IStatutClient listener){
        list_i_statut.add(listener);
    }
    
    /**
     * Enlève un listener IStatut
     * @param listener Correspond au listener
     */
    public void removeIStatutListener(IStatutClient listener){
        list_i_statut.remove(listener);
    }
    
    /**
     * Enlève un listener IStatut
     * @param index Correspond à l'index du listener
     */
    public void removeIStatutListener(int index){
        list_i_statut.remove(index);
    }
    
    /**
     * Ajoute un listener IReceived
     * @param listener Correspond au listener
     */
    public void addIReceivedListener(IReceived listener){
        list_i_received.add(listener);
    }
    
    /**
     * Enlève un listener IReceived
     * @param listener Correspond au listener
     */
    public void removeIReceivedListener(IReceived listener){
        list_i_received.remove(listener);
    }
    
    /**
     * Enlève un listener IReceived
     * @param index Correspond à l'index du listener
     */
    public void removeIReceivedListener(int index){
        list_i_received.remove(index);
    }

    /**
     * Renvoie l'identité du client ou du serveur
     * @return Retourne l'identité
     */
    public Identity getIdentity() {
        return identity;
    }

    /**
     * Renvoie si oui ou non le client est connecté
     * @return Retourne true si le client est connecté, sinon false
     */
    public boolean isConnected(){
        return this.connected;
    }

    /**
     * Renvoie la liste des listeners IReceived
     * @return Retourne la liste des listeners
     */
    public java.util.List<IReceived> getList_i_received() {
        return list_i_received;
    }

    /**
     * Renvoie la liste des listeners IStatut
     * @return Retourne la liste des listeners
     */
    public java.util.List<IStatutClient> getList_i_statut() {
        return list_i_statut;
    }

    /**
     * Renvoie la table de routage ou table des services
     * @return Retourne la table de routage
     */
    public RoutageTable getRoutage_table() {
        return routage_table;
    }
    
    /**
     * Redéfinit la méthode hashCode()
     * @return Retourne le résultat de la méthode
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + java.util.Objects.hashCode(this.identity);
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
        final TCP other = (TCP) obj;
        return java.util.Objects.equals(this.identity, other.identity);
    }

    /**
     * Redéfinit la méthode toString()
     * @return Retourne le résultat de la méthode
     */
    @Override
    public String toString() {
        return "TCP{" + "identity=" + identity + '}';
    }
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="ATTRIBUTS">
    /**
     * Correspond à l'identité du client ou du serveur
     */
    Identity                        identity;
    /**
     * Correspond au port de connexion
     */
    int                             port;
    /**
     * Correspond à la liste d'interface IReceived
     */
    java.util.List<IReceived>       list_i_received;
    /**
     * Correspond à la liste d'interface IStatut
     */
    java.util.List<IStatutClient>   list_i_statut;
    /**
     * Correspond à la liste d'interface ILog
     */
    java.util.List<ILog>            list_i_log;
    /**
     * Correspond à la table comportant les clients connectés
     */
    RoutageTable                    routage_table;
    /**
     * Cette variable dit s'il y a une connexion entre le client et le serveur
     */
    boolean                         connected;
    // </editor-fold>
    
    
    
}