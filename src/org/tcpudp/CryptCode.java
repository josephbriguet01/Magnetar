/*
 * Copyright (C) BRIGUET Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Briguet, August 2018
 */
package org.tcpudp;



/**
 * Cette classe abstraite permet de définir la structure d'une classe de chiffrement/déchiffrement (comme AES).
 * @author BRIGUET
 * @version 1.0
 */
public abstract class CryptCode {
    
    
    
    // <editor-fold defaultstate="collapsed" desc="METHODES PUBLICS">
    /**
     * Génère une clé
     * @param length Correspond à la taille de la clé
     * @return Retourne une clé de taille length caractères
     */
    public static String generateKey(int length){
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"; // Tu supprimes les lettres dont tu ne veux pas
        String pass = "";
        for (int x = 0; x < length; x++) {
            int i = (int) Math.floor(Math.random() * chars.length());
            pass += chars.charAt(i);
        }
        return pass;
    }
    
    /**
     * Chiffre data
     * @param data Correspond aux données à crypter
     * @return Retourne les données cryptées
     * @throws Exception Correspond à une éventuelle exception levée lors du cryptage
     */
    public abstract byte[] encrypt(byte[] data) throws Exception;
    
    /**
     * Déchiffre encryptedData
     * @param encryptedData Correspond aux données cryptées
     * @return Retourne les données décryptées
     * @throws Exception Correspond à une éventuelle exception levée lors du décryptage
     */
    public abstract byte[] decrypt(byte[] encryptedData) throws Exception;
    // </editor-fold>
    
    
    
}