/*
 * Copyright (C) BRIGUET Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Briguet, Août 2018
 */
package org.tcp;



import org.apache.commons.lang.SerializationUtils;



/**
 * Cette classe permet la création des flux d'entrée/sortie d'une connexion 
 * @author BRIGUET
 * @version 1.0
 */
public class Flux {

    
    
    // <editor-fold defaultstate="collapsed" desc="CONSTRUCTORS">
    /**
     * Crée un objet Flux à partir d'une socket. Ce constructeur va extraire les Streams de celle-ci
     * @param socket Correspond à la socket (la connexion) d'un client ou d'un serveur
     */
    protected Flux(java.net.Socket socket) {
        try {
            //On crée le flux de sortie
            this.sortie = new java.io.ObjectOutputStream(socket.getOutputStream());
            //On vide le flux
            this.sortie.flush();
        } catch (java.io.IOException ex) {
            throw new org.tcp.FluxStreamException("Error initializing OutputStream");
        }
        
        try {
            //On crée le flux d'entrée et on renvoie le flux
            this.entree = new java.io.ObjectInputStream(socket.getInputStream());
        } catch (java.io.IOException ex) {
            throw new org.tcp.FluxStreamException("Error initializing InputStream");
        }
    }
    
    /**
     * Crée un objet Flux à partir d'une socket. Ce constructeur va extraire les Streams de celle-ci
     * @param socket Correspond à la socket (la connexion) d'un client ou d'un serveur
     * @param secured Correspond a si oui ou non les flux seront chiffrée ou pas
     */
    protected Flux(java.net.Socket socket, boolean secured) {
        this.secured = secured;
        try {
            //On crée le flux de sortie
            this.sortie = new java.io.ObjectOutputStream(socket.getOutputStream());
            //On vide le flux
            this.sortie.flush();
        } catch (java.io.IOException ex) {
            throw new org.tcp.FluxStreamException("Error initializing OutputStream");
        }
        
        try {
            //On crée le flux d'entrée et on renvoie le flux
            this.entree = new java.io.ObjectInputStream(socket.getInputStream());
        } catch (java.io.IOException ex) {
            throw new org.tcp.FluxStreamException("Error initializing InputStream");
        }
    }
    // </editor-fold>

    
    
    // <editor-fold defaultstate="collapsed" desc="GETTERS">
    /**
     * Renvoie le flux de sortie de la connexion
     * @return Retourne le flux de sortie
     */
    protected java.io.ObjectOutputStream    getSortie() {
        return sortie;
    }

    /**
     * Renvoie le flux d'entrée de la connexion
     * @return Retourne le flux d'entrée
     */
    protected java.io.ObjectInputStream     getEntree() {
        return entree;
    }
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="METHODES PROTECTED">
    /**
     * Renvoie si oui ou non il y a une volonté de sécuriser le flux
     * @return Retourne true s'il y en a une, sinon false
     */
    protected boolean isSecured() {
        return secured;
    }

    /**
     * Renvoie si oui ou non le flux est chiffré ou non
     * @return Retourne true si le flux est chiffré, sinon false
     */
    protected boolean isEncrypt() {
        return encrypt;
    }

    /**
     * Modifie si oui ou non le flux doit être chiffré ou pas
     * @param encrypt Correspond à true si l'on veut chiffrer le flux, sinon false
     */
    protected void setEncrypt(boolean encrypt) {
        this.encrypt = encrypt;
    }

    /**
     * Modifie la clé de chiffrement
     * @param key Correspond à la nouvelle clé de chiffrement
     */
    protected void setKey(String key) {
        this.cryptCode = new AES(key);
    }
    
    /**
     * Envoie un objet au destinataire à l'autre extrémité du flux
     * @param obj Correspond à l'objet à envoyer
     */
    protected void      send(Object obj) {
        if(encrypt){
            try {
                sortie.writeObject(this.cryptCode.encrypt(SerializationUtils.serialize((java.io.Serializable) obj)));
                sortie.flush();
            } catch (java.io.IOException ex) {
                throw new org.tcp.SendingException("Error sending trame");
            } catch (Exception ex) {
                throw new org.tcp.SendingException("Encrypt trame is impossible");
            }
        }else{
            try {
                sortie.writeObject(obj);
                sortie.flush();
            } catch (java.io.IOException ex) {
                throw new org.tcp.SendingException("Error sending trame");
            }
        }
    }

    /**
     * Reçoit un objet de l'expéditeur à l'autre extrémité du flux
     * @return Retourne l'objet reçu
     */
    protected Object    receive() {
        if(encrypt){
            try {
                return SerializationUtils.deserialize(this.cryptCode.decrypt((byte[])entree.readObject()));
            } catch (java.io.IOException ex) {
                throw new org.tcp.ReceivingIOException(ex.getMessage(), ex.getCause());
            } catch (ClassNotFoundException ex) {
                throw new org.tcp.ReceivingClassNotFoundException("Error reading to Trame");
            } catch(java.lang.ClassCastException ex){
                throw new org.tcp.ReceivingClassNotFoundException("Error ClassCastException");
            } catch (Exception ex) {
                throw new org.tcp.SendingException("Decrypt trame is impossible");
            }
        }else{
            try {
                return entree.readObject();
            } catch (java.io.IOException ex) {
                throw new org.tcp.ReceivingIOException(ex.getMessage(), ex.getCause());
            } catch (ClassNotFoundException ex) {
                throw new org.tcp.ReceivingClassNotFoundException("Error reading to Trame");
            } catch(java.lang.ClassCastException ex){
                throw new org.tcp.ReceivingClassNotFoundException("Error ClassCastException");
            }
        }
    }
    
    /**
     * Ferme le flux
     * @throws java.io.IOException Correspond à l'exception levée si le flux à un problème de fermeture
     */
    protected void      closeFlux() throws java.io.IOException{
        entree.close();
        sortie.flush();
        sortie.close();
    }
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="ATTRIBUTS">
    /**
     * Correspond à l'extrémité émettrice
     */
    private final java.io.ObjectOutputStream    sortie;
    /**
     * Correspond à l'extrémité réceptrice
     */
    private final java.io.ObjectInputStream     entree;
    /**
     * Correspond à la variable de volonté de chiffrer le flux
     */
    private boolean secured;
    /**
     * Correspond à la variable qui dit si le flux est chiffré
     */
    private boolean encrypt;
    /**
     * Correspond à l'instance d'une classe de chiffrage (comme AES)
     */
    private CryptCode cryptCode;
    // </editor-fold>
    
    
    
}