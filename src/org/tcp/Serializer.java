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
 * Cette classe permet de transformer on objet (Serializable) en tableau de bytes et inversement
 * @author Briguet
 * @version 1.0
 */
public class Serializer {
    
    
    
    // <editor-fold defaultstate="collapsed" desc="CONSTRUCTOR">
    /**
     * Crée un objet Serializer par défaut
     */
    private Serializer(){ }
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="METHODES PUBLICS STATICS">
    /**
     * Renvoie un tableau de byte
     * @param s Correspond à l'objet à sérialiser
     * @return Retourne l'objet à sérialiser sous la forme d'un tableau de bytes
     */
    public static byte[] getData(java.io.Serializable s){
        return new SerializerStatic().getData(s);
    }
    
    /**
     * Renvoie un objet
     * @param data Correspond à l'objet à déserialiser
     * @return Retourne le tableau de bytes à déserialiser sous la forme d'un objet
     */
    public static java.io.Serializable getObject(byte[] data){
        return new SerializerStatic().getObject(data);
    }
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="CLASS">
    /**
     * Cette classe gère les transformations d'objet à tableau de bytes et inversement.
     */
    private static class SerializerStatic{
    
        
        
        // <editor-fold defaultstate="collapsed" desc="ATTRIBUT">
        /**
         * Correspond à l'index de déplacement lors de la lecture du tableau de bytes
         */
        private final int index = 0;
        // </editor-fold>

        
        
        // <editor-fold defaultstate="collapsed" desc="METHODES PUBLICS">
        /**
         * Renvoie un tableau de byte
         * @param s Correspond à l'objet à sérialiser
         * @return Retourne l'objet à sérialiser sous la forme d'un tableau de bytes
         */
        public byte[] getData(java.io.Serializable s){
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            try {
                byte[] yourBytes;
                try (java.io.ObjectOutput out = new java.io.ObjectOutputStream(baos)) {
                    out.flush();
                    out.writeObject(s);
                    out.flush();
                    yourBytes = baos.toByteArray();
                }
                baos.close();
                return yourBytes;
            } catch (java.io.IOException ex) {
                java.util.logging.Logger.getLogger(Serializer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            return null;
        }

        /**
         * Renvoie un objet
         * @param data Correspond à l'objet à déserialiser
         * @return Retourne le tableau de bytes à déserialiser sous la forme d'un objet
         */
        public java.io.Serializable getObject(byte[] data){
            try {
                Object o;
                try (java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(data); java.io.ObjectInput in = new java.io.ObjectInputStream(bis)) {
                    o = in.readObject();
                }
                return (java.io.Serializable) o;
            } catch (java.io.IOException | ClassNotFoundException ex) {
                java.util.logging.Logger.getLogger(Serializer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            return null;
        }
        // </editor-fold>
        
        
        
    }
    // </editor-fold>
    
    
    
}