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
 * Cette classe permet de créer des listes synchronisées
 * @author BRIGUET
 * @param <E> Correspond à un paramètre de généricité
 */
public class ListSynchronized<E> implements java.io.Serializable{

    
    
    // <editor-fold defaultstate="collapsed" desc="CONSTRUCTOR">
    /**
     * Crée une liste synchronisé
     */
    public ListSynchronized() {
        this.list = new java.util.ArrayList<>();
    }
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="METHODES PUBLICS">
    /**
     * Ajoute un élément dans la liste
     * @param element Correspond à l'élément à ajouter
     */
    @SuppressWarnings({"SynchronizeOnNonFinalField", "NestedSynchronizedStatement"})
    public synchronized void add(E element) {
        synchronized (list) {
            list.add(element);
        }
    }

    /**
     * Supprime un élément de la liste
     * @param element Correspond à l'élément à supprimer
     */
    @SuppressWarnings({"SynchronizeOnNonFinalField", "NestedSynchronizedStatement"})
    public synchronized void remove(E element) {
        synchronized (list) {
            list.remove(element);
        }
    }

    /**
     * Supprime un élément de la liste
     * @param index Correspond à l'index de l'élément à supprimer
     */
    @SuppressWarnings({"SynchronizeOnNonFinalField", "NestedSynchronizedStatement"})
    public synchronized void remove(int index) {
        synchronized (list) {
            list.remove(index);
        }
    }

    /**
     * Renvoie un élément
     * @param index Correspond à l'index de l'élément à récupérer
     * @return Retourne un élément
     */
    @SuppressWarnings({"SynchronizeOnNonFinalField", "NestedSynchronizedStatement"})
    public synchronized E get(int index) {
        synchronized (list) {
            return list.get(index);
        }
    }

    /**
     * Renvoie la taille de la liste
     * @return Retourne la taille de la liste
     */
    @SuppressWarnings({"SynchronizeOnNonFinalField", "NestedSynchronizedStatement"})
    public synchronized int size() {
        synchronized (list) {
            return list.size();
        }
    }
    
    /**
     * Renvoie la liste des éléments de ListSynchronized
     * @return Retourne la liste des éléments de ListSynchronized
     */
    protected java.util.List<E> getList(){
        synchronized (list) {
            return this.list;
        }
    }

    /**
     * Redéfinit la méthode toString()
     * @return Retourne le résultat de la méthode
     */
    @Override
    public String toString() {
        return "ListSynchronized{" + "list=" + list + '}';
    }
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="ATTRIBUT">
    private final java.util.List<E> list;
    // </editor-fold>
    
    
    
}