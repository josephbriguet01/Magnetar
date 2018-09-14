/*
 * Copyright (C) BRIGUET Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Briguet, September 2018
 */
package org.tcp;



/**
 * Cette classe permet de déterminer si oui ou non un client à le droit de se connecter. Il s'agit d'une classe abstraite qui est étendue par la whitelist et la blacklist
 * @author Briguet
 * @version 1.0
 */
public abstract class BAWList implements java.io.Serializable, IConditionATBC {

    
    
    // <editor-fold defaultstate="collapsed" desc="CONSTRUCTOR">
    /**
     * Crée un objet objet BAWList
     */
    public BAWList() {
        this.list = new java.util.ArrayList<>();
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
     * Ajoute un élément BAW dans la liste
     * @param bawe Correspond à l'élément à ajouter
     */
    @SuppressWarnings({"NestedSynchronizedStatement", "SynchronizeOnNonFinalField"})
    public synchronized void addBAWElement(BAWElement bawe){
        synchronized(list){
            this.list.add(bawe);
        }
    }
    
    /**
     * Supprime un élément BAW dans la liste
     * @param bawe Correspond à l'élément à supprimer
     */
    @SuppressWarnings({"NestedSynchronizedStatement", "SynchronizeOnNonFinalField"})
    public synchronized void remove(BAWElement bawe){
        synchronized(list){
            this.list.remove(bawe);
        }
    }
    
    /**
     * Supprime un élément BAW dans la liste
     * @param index Correspond à l'index de l'élément à supprimer
     */
    @SuppressWarnings({"NestedSynchronizedStatement", "SynchronizeOnNonFinalField"})
    public synchronized void remove(int index){
        synchronized(list){
            this.list.remove(index);
        }
    }
    
    /**
     * Renvoie l'élément BAW à l'index de la liste
     * @param index Correspond à l'index de l'élément à récupérer
     * @return Retourne l'élément BAW
     */
    @SuppressWarnings({"NestedSynchronizedStatement", "SynchronizeOnNonFinalField"})
    public synchronized BAWElement get(int index){
        synchronized(list){
            return this.list.get(index);
        }
    }
    
    /**
     * Renvoie la taille de la liste
     * @return Retoune la taille de la liste
     */
    @SuppressWarnings({"NestedSynchronizedStatement", "SynchronizeOnNonFinalField"})
    public synchronized int size(){
        synchronized(list){
            return this.list.size();
        }
    }
    
    /**
     * Renvoie true si l'élément BAW est contenu dans la liste
     * @param bawe Correspond à l'élément dont on veut connaitre l'existance
     * @return Retourne true si l'élément BAW est contenu dans la liste, sinon false
     */
    @SuppressWarnings({"NestedSynchronizedStatement", "SynchronizeOnNonFinalField"})
    public synchronized boolean contains(BAWElement bawe){
        synchronized(list){
            return this.list.contains(bawe);
        }
    }
    
    /**
     * Renvoie l'index de l'élément BAW contenu dans la liste, sinon -1 s'il n'existe pas
     * @param bawe Correspond à l'élément dont on veut connaitre la position
     * @return Retourne l'index de l'élément BAW contenu dans la liste, sinon -1 s'il n'existe pas
     */
    @SuppressWarnings({"NestedSynchronizedStatement", "SynchronizeOnNonFinalField"})
    public synchronized int indexof(BAWElement bawe){
        synchronized(list){
            return this.list.indexOf(bawe);
        }
    }
    
    /**
     * Renvoie le dernier élément BAW contenu dans la liste, sinon -1 s'il n'existe pas
     * @param bawe Correspond à l'élément dont on veut connaitre la dernière position
     * @return Retourne le dernier élément BAW contenu dans la liste, sinon -1 s'il n'existe pas
     */
    @SuppressWarnings({"NestedSynchronizedStatement", "SynchronizeOnNonFinalField"})
    public synchronized int lastIndexof(BAWElement bawe){
        synchronized(list){
            return this.list.lastIndexOf(bawe);
        }
    }
    
    /**
     * Cette méthode est appelé pour vérifier qu'un utilisateur à le droit de se connecter
     * @param identity Correspond à l'identity qu'il faut vérifier
     * @return Retourne 0, si l'identity est autorisé à se connecter
     */
    @Override
    public int authorizedToBeConnected(Identity identity){
        int result = authorizeOrNot(identity);
        if(result == 0 && this.icatbc != null){
            return this.icatbc.authorizedToBeConnected(identity);
        }
        return result;
    }
    
    /**
     * Renvoie si oui ou non l'identité du client a le droit de se connecter au serveur
     * @param identity Correspond à l'identité dont on veut vérifier si oui ou non elle a le droit de se connecter au serveur
     * @return Retourne si oui ou non l'identité du client a le droit de se connecter au serveur
     */
    public abstract int authorizeOrNot(Identity identity);
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="ATTRIBUT">
    /**
     * Correspond à la liste des éléments BAW
     */
    final java.util.List<BAWElement> list;
    /**
     * Correspond à la classe qui vérifiera après la BAWList que le client à le droit de se connecter
     */
    transient IConditionATBC icatbc;
    // </editor-fold>
    
    
    
}