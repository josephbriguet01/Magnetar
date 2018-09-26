/*
 * Copyright (C) BRIGUET Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by BRIGUET, October 2017
 */
package org.tcpudp;



/**
 * Cette classe permet de crypter une chaine à partir de l'algorithme RSA
 * @author BRIGUET
 * @version 1.0
 */
public class RSA {
    
    
    
    // <editor-fold defaultstate="collapsed" desc="ATTRIBUTS">
    private final static String ALGORITHM = "RSA";
    private String plainText;
    private java.security.PublicKey keyPublic;
    private java.security.PrivateKey keyPrivate;
    private int size;
    // </editor-fold>
    
    

    // <editor-fold defaultstate="collapsed" desc="CONSTRUCTOR">
    /**
     * Crée un objet RSA
     */
    protected RSA() {
        this.size = 512;
    }
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="GETTERS & SETTERS">
    /**
     * Renvoie la clef publique (Accesseur)
     * @return Retourne la clef publique
     */
    protected java.security.PublicKey getKeyPublic(){
        return this.keyPublic;
    }
    
    /**
     * Renvoie la clef publique en String (Accesseur)
     * @return Retourne la clef publique en String
     */
    @SuppressWarnings("static-access")
    protected String getKeyPublic_toString(){
        if(this.keyPublic!=null) return new Base64().encode(this.keyPublic.getEncoded());
        else return null;
    }

    /**
     * Renvoie la clef privée (Accesseur)
     * @return Retourne la clef privée
     */
    protected java.security.PrivateKey getKeyPrivate(){
        return this.keyPrivate;
    }
    
    /**
     * Renvoie la clef privée en String (Accesseur)
     * @return Retourne la clef privée en String
     */
    @SuppressWarnings("static-access")
    protected String getKeyPrivate_toString(){
        if(this.keyPrivate!=null) return new Base64().encode(this.keyPrivate.getEncoded());
        else return null;
    }

    /**
     * Modifie la clef publique (Mutateur)
     * @param keyPublic Correspond à la nouvelle clef publique
     */
    protected void setKeyPublic(java.security.PublicKey keyPublic) {
        if(keyPublic!=null) this.keyPublic = keyPublic;
    }
    
    /**
     * Modifie la clef publique (Mutateur)
     * @param keyPublic Correspond à la nouvelle clef publique
     */
    protected void setKeyPublic(String keyPublic) {
        if(keyPublic!=null) if(!keyPublic.isEmpty()) this.keyPublic = convert_String_TO_PublicKey(keyPublic);
    }

    /**
     * Modifie la clef privée (Mutateur)
     * @param keyPrivate Correspond à la nouvelle clef privée
     */
    protected void setKeyPrivate(java.security.PrivateKey keyPrivate) {
        if(keyPrivate!=null) this.keyPrivate = keyPrivate;
    }
    
    /**
     * Modifie la clef privée (Mutateur)
     * @param keyPrivate Correspond à la nouvelle clef privée
     */
    protected void setKeyPrivate(String keyPrivate) {
        if(keyPrivate!=null) if(!keyPrivate.isEmpty()) this.keyPrivate = convert_String_TO_PivateKey(keyPrivate);
    }
    
    /**
     * Renvoie la constante d'initialisation des clefs publiques et privées (Accesseur)
     * @return Retourne la constante d'initialisation des clefs publiques et privées
     */
    protected int getSize() {
        return size;
    }

    /**
     * Modifie la constante d'initialisation des clefs publiques et privées (Mutateur)
     * @param size Correspond à la constante d'initialisation des clefs publiques et privées&#46; Taille minimum: 512
     */
    protected void setSize(int size){
        if(size>=512)this.size = size;
        else try { throw new RSAPlainTextNullException("La taille doit être >= 512"); } catch (RSAPlainTextNullException ex) { java.util.logging.Logger.getLogger(RSA.class.getName()).log(java.util.logging.Level.SEVERE, null, ex); System.exit(1); }
    }

    /**
     * Renvoie le texte à crypter ou a décrypter (Accesseur)
     * @return Retourne le texte à crypter ou a décrypter
     */
    protected String getPlainText() {
        return plainText;
    }

    /**
     * Modifie le texte à crypter ou a décrypter (Mutateur)
     * @param plainText Correspond au texte à crypter ou a décrypter
     */
    protected void setPlainText(String plainText) {
        this.plainText = plainText;
    }
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="METHODES PRIVATES">
    /**
     * Convertit la clef privée String en PrivateKey
     */
    private java.security.PrivateKey convert_String_TO_PivateKey(String privateKey){
        try {
            java.security.KeyFactory kf = java.security.KeyFactory.getInstance(ALGORITHM);
            @SuppressWarnings("static-access")
            java.security.spec.PKCS8EncodedKeySpec specPriv = new java.security.spec.PKCS8EncodedKeySpec(new Base64().decode(privateKey));
            return kf.generatePrivate(specPriv);
        } catch (java.security.NoSuchAlgorithmException | java.security.spec.InvalidKeySpecException ex) { java.util.logging.Logger.getLogger(RSA.class.getName()).log(java.util.logging.Level.SEVERE, null, ex); System.exit(1); return null; }
    }
    
    /**
     * Convertit la clef publique String en PublicKey
     */
    private java.security.PublicKey convert_String_TO_PublicKey(String publicKey){
        try {
            java.security.KeyFactory kf = java.security.KeyFactory.getInstance(ALGORITHM);
            @SuppressWarnings("static-access")
            java.security.spec.X509EncodedKeySpec specPub = new java.security.spec.X509EncodedKeySpec(new Base64().decode(publicKey));
            return kf.generatePublic(specPub);
        } catch (java.security.NoSuchAlgorithmException | java.security.spec.InvalidKeySpecException ex) { java.util.logging.Logger.getLogger(RSA.class.getName()).log(java.util.logging.Level.SEVERE, null, ex); System.exit(1); return null; }
    }
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="METHODES PROTECTED">
    /**
     * Génère la clef privée et publique
     */
    protected void generateKeys(){
        try {
            java.security.KeyPairGenerator keyGen = java.security.KeyPairGenerator.getInstance(ALGORITHM);
            keyGen.initialize(size);
            java.security.KeyPair key = keyGen.generateKeyPair();
            this.keyPublic = key.getPublic();
            this.keyPrivate = key.getPrivate();
        } catch (java.security.NoSuchAlgorithmException ex) { java.util.logging.Logger.getLogger(RSA.class.getName()).log(java.util.logging.Level.SEVERE, null, ex); System.exit(1); }
    }
    
    /**
     * Crypte le textPlain
     * @return Retourne le textPlain crypté
     */
    @SuppressWarnings("static-access")
    protected String encrypt(){
        if(plainText!=null){
            if(plainText.length()!=0){
                byte[] cipherText = null;
                try {
                    javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(ALGORITHM);
                    cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, keyPublic);
                    cipherText = cipher.doFinal(plainText.getBytes());
                } catch (java.security.NoSuchAlgorithmException | javax.crypto.NoSuchPaddingException | java.security.InvalidKeyException | javax.crypto.IllegalBlockSizeException | javax.crypto.BadPaddingException e) {System.err.println(e);}
                if(cipherText!=null)if(cipherText.length!=0)return new Base64().encode(cipherText);
                else{ try {throw new RSAPlainTextNullException("Le texte à crypter = null");} catch (RSAPlainTextNullException ex) {java.util.logging.Logger.getLogger(RSA.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);System.exit(1);}}
            }
        }else{ try {throw new RSAPlainTextNullException("Le texte à crypter = null");} catch (RSAPlainTextNullException ex) {java.util.logging.Logger.getLogger(RSA.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);System.exit(1);}}
        return null;
    }
    
    /**
     * Décrypte le textPlain
     * @return Retourne le textPlain décrypté
     */
    @SuppressWarnings("static-access")
    protected String decrypt(){
        if(plainText!=null){
            if(plainText.length()!=0){
                byte[] dectyptedText = null;
                try {
                    javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(ALGORITHM);
                    cipher.init(javax.crypto.Cipher.DECRYPT_MODE, keyPrivate);
                    dectyptedText = cipher.doFinal(new Base64().decode(plainText));
                } catch (java.security.NoSuchAlgorithmException | javax.crypto.NoSuchPaddingException | java.security.InvalidKeyException | javax.crypto.IllegalBlockSizeException | javax.crypto.BadPaddingException ex) {System.err.println(ex);}
                return new String(dectyptedText);
            }else{try {throw new RSAPlainTextNullException("Le tableau à décrypter est vide");} catch (RSAPlainTextNullException ex) {java.util.logging.Logger.getLogger(RSA.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);System.exit(1);}}
        }else{try {throw new RSAPlainTextNullException("Le tableau à décrypter = null");} catch (RSAPlainTextNullException ex) {java.util.logging.Logger.getLogger(RSA.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);System.exit(1);}}
        return null;
    }
    
    /**
     * Convertit la chaine en tableau de byte
     * @param chain Correspond à la chaîne à convertir
     * @return Retourne un tableau de byte
     */
    @SuppressWarnings("static-access")
    protected byte[] convert_String_TO_Byte(String chain){
        if(chain!=null) return new Base64().decode(chain);
        else return null;
    }

    /**
     * Convertit un tableau de byte en chaîne
     * @param array_byte Correspond au tableau de byte à convertir
     * @return Retourne une chaîne
     */
    @SuppressWarnings("static-access")
    protected String convert_Byte_TO_String(byte[] array_byte){
        if(array_byte!=null) return new Base64().encode(array_byte);
        else return null;
    }
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="CLASS">
    /**
     * Il s'agit de l'exeption levé lorsque la clef est null
     * @author BRIGUET
     * @version 1.0
     */
    public class RSAPlainTextNullException extends Exception{
        /**
         * Il s'agit du constructeur par défaut
         * @param message Correspond au message à relever
         */
        protected RSAPlainTextNullException(String message) {
            super(message);
        }
    }
    // </editor-fold>
    
    
    
}