/*
 * Copyright (C) BRIGUET Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Briguet, August 2018
 */
package org.tcp;



/**
 * Cette classe permet de chiffrer et déchiffrer un tableau de byte selon l'algorithme AES
 * @author jbriguet
 */
public class AES extends CryptCode {
    
    
    
    // <editor-fold defaultstate="collapsed" desc="CONSTRUCTOR">
    /**
     * Crée un objet AES
     * @param key Correspond à la clé de chiffrement et de déchiffrement utilisée
     */
    public AES(String key) {
        keyValue = key.getBytes();
    }
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="METHODES PUBLICS">
    @Override
    public byte[] encrypt(byte[] data) throws Exception {
        java.security.Key key = gen();
        javax.crypto.Cipher c = javax.crypto.Cipher.getInstance(ALGO);
        c.init(javax.crypto.Cipher.ENCRYPT_MODE, key);
        return c.doFinal(data);
    }

    
    @Override
    public byte[] decrypt(byte[] encryptedData) throws Exception {
        java.security.Key key = gen();
        javax.crypto.Cipher c = javax.crypto.Cipher.getInstance(ALGO);
        c.init(javax.crypto.Cipher.DECRYPT_MODE, key);
        return c.doFinal(encryptedData);
    }
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="METHODE PRIVATE">
    /**
     * Génère une clé en fonction de la chaîne
     * @return Retourne une Clé
     * @throws Exception Correspond à l'erreur levée si une erreur survient
     */
    private java.security.Key gen() throws Exception {
        return new javax.crypto.spec.SecretKeySpec(keyValue, ALGO);
    }
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="ATTRIBUTS">
    /**
     * Correspond au type d'algorithme utilisé par l'instance Cipher
     */
    private static final String ALGO = "AES";
    
    /**
     * Correspond à la clé par défaut de l'algorithme AES
     */
    private byte[] keyValue = new byte[]{'T', 'h', 'e', 'B', 'e', 's', 't', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y'};
    // </editor-fold>
    
    
    
}