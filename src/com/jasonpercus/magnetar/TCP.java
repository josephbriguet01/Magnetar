/*
 * Copyright (C) BRIGUET Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Briguet, August 2018
 */
package com.jasonpercus.magnetar;



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
        this.list_i_received    = new ListSynchronized<>();
        this.list_i_statut      = new ListSynchronized<>();
        this.list_i_log         = new ListSynchronized<>();
        this.routage_table      = new RoutageTable();
    }
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="METHODES PUBLICS">
    /**
     * Ajoute un listener ILog
     * @param listener Correspond au listener
     */
    @SuppressWarnings({"SynchronizeOnNonFinalField", "NestedSynchronizedStatement"})
    public synchronized void addILogListener(ILog listener){
        synchronized(list_i_log){
            list_i_log.add(listener);
        }
    }
    
    /**
     * Enlève un listener ILog
     * @param listener Correspond au listener
     */
    @SuppressWarnings({"NestedSynchronizedStatement", "SynchronizeOnNonFinalField"})
    public synchronized void removeILogListener(ILog listener){
        synchronized(list_i_log){
            list_i_log.remove(listener);
        }
    }
    
    /**
     * Enlève un listener ILog
     * @param index Correspond à l'index du listener
     */
    @SuppressWarnings({"NestedSynchronizedStatement", "SynchronizeOnNonFinalField"})
    public synchronized void removeILogListener(int index){
        synchronized(list_i_log){
            list_i_log.remove(index);
        }
    }
    
    /**
     * Ajoute un listener IStatut
     * @param listener Correspond au listener
     */
    @SuppressWarnings({"NestedSynchronizedStatement", "SynchronizeOnNonFinalField"})
    public synchronized void addIStatutListener(IStatutClient listener){
        synchronized(list_i_statut){
            list_i_statut.add(listener);
        }
    }
    
    /**
     * Enlève un listener IStatut
     * @param listener Correspond au listener
     */
    @SuppressWarnings({"NestedSynchronizedStatement", "SynchronizeOnNonFinalField"})
    public synchronized void removeIStatutListener(IStatutClient listener){
        synchronized(list_i_statut){
            list_i_statut.remove(listener);
        }
    }
    
    /**
     * Enlève un listener IStatut
     * @param index Correspond à l'index du listener
     */
    @SuppressWarnings({"SynchronizeOnNonFinalField", "NestedSynchronizedStatement"})
    public synchronized void removeIStatutListener(int index){
        synchronized(list_i_statut){
            list_i_statut.remove(index);
        }
    }
    
    /**
     * Ajoute un listener IReceived
     * @param listener Correspond au listener
     */
    @SuppressWarnings({"NestedSynchronizedStatement", "SynchronizeOnNonFinalField"})
    public synchronized void addIReceivedListener(IReceived listener){
        synchronized(list_i_received){
            list_i_received.add(listener);
        }
    }
    
    /**
     * Enlève un listener IReceived
     * @param listener Correspond au listener
     */
    @SuppressWarnings({"NestedSynchronizedStatement", "SynchronizeOnNonFinalField"})
    public synchronized void removeIReceivedListener(IReceived listener){
        synchronized(list_i_received){
            list_i_received.remove(listener);
        }
    }
    
    /**
     * Enlève un listener IReceived
     * @param index Correspond à l'index du listener
     */
    @SuppressWarnings({"NestedSynchronizedStatement", "SynchronizeOnNonFinalField"})
    public synchronized void removeIReceivedListener(int index){
        synchronized(list_i_received){
            list_i_received.remove(index);
        }
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
    @SuppressWarnings("SynchronizeOnNonFinalField")
    public ListSynchronized<IReceived> getList_i_received() {
        synchronized(list_i_received){
            return list_i_received;
        }
    }

    /**
     * Renvoie la liste des listeners IStatut
     * @return Retourne la liste des listeners
     */
    @SuppressWarnings("SynchronizeOnNonFinalField")
    public ListSynchronized<IStatutClient> getList_i_statut() {
        synchronized(list_i_statut){
            return list_i_statut;
        }
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
    
    
    
    // <editor-fold defaultstate="collapsed" desc="METHODE PROTECTED">
    /**
     * Modifie l'identité du client ou du serveur
     * @param identity Correspond à la nouvelle identité
     */
    protected void setIdentity(Identity identity){
        this.identity = identity;
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
    ListSynchronized<IReceived>       list_i_received;
    /**
     * Correspond à la liste d'interface IStatut
     */
    ListSynchronized<IStatutClient>   list_i_statut;
    /**
     * Correspond à la liste d'interface ILog
     */
    ListSynchronized<ILog>            list_i_log;
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