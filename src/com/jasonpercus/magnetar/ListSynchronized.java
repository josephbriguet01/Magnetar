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
 * Cette classe permet de créer des listes synchronisées
 * @author BRIGUET
 * @param <E> Correspond à un paramètre de généricité
 */
public class ListSynchronized<E> implements java.io.Serializable {
    
    
    
    // <editor-fold defaultstate="collapsed" desc="SERIAL_VERSION_UID">
    /**
     * Correspond au numéro de série qui identifie le type de dé/sérialization utilisé pour l'objet
     */
    private static final long serialVersionUID = 1L;
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="ATTRIBUT">
    /**
     * Correspond à la liste des éléments stockés
     */
    private final java.util.List<E> list;
    // </editor-fold>

    
    
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
     * Efface toute la liste
     */
    @SuppressWarnings("NestedSynchronizedStatement")
    public synchronized void clear() {
        synchronized (list) {
            list.clear();
        }
    }
    
    /**
     * Renvoie l'index d'un élément
     * @param element Correspond à l'élément à rechercher
     * @return Retourne l'index de l'élément
     */
    @SuppressWarnings({"SynchronizeOnNonFinalField", "NestedSynchronizedStatement"})
    public synchronized int indexOf(E element) {
        synchronized (list) {
            return list.indexOf(element);
        }
    }
    
    /**
     * Détermine s'il existe l'élément dans la liste
     * @param element Correspond à l'élément à rechercher
     * @return Retourne true s'il existe, sinon false
     */
    @SuppressWarnings({"SynchronizeOnNonFinalField", "NestedSynchronizedStatement"})
    public synchronized boolean contains(E element) {
        synchronized (list) {
            return list.contains(element);
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
    
    
    
}