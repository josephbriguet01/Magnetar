/*
 * Copyright (C) BRIGUET Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Briguet, December 2020
 */
package com.jasonpercus.magnetar;



import com.jasonpercus.util.Strings;



/**
 * Cette classe permet de créer un objet unique sur le réseau correspondant à une demande synchrone et à sa réponse
 * @author BRIGUET
 * @version 1.0
 */
public class SyncTrame implements java.io.Serializable {
    
    
    
    // <editor-fold defaultstate="collapsed" desc="SERIAL_VERSION_UID">
    /**
     * Correspond au numéro de série qui identifie le type de dé/sérialization utilisé pour l'objet
     */
    private static final long serialVersionUID = 1L;
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="ATTRIBUTS">
    /**
     * Correspond à l'id de la trame
     */
    private final String id;
    
    /**
     * Correspond à l'objet de la trame
     */
    private final Object object;
    
    /**
     * Correspond à la direction de la trame (-1 = réponse; 1 = question)
     */
    private final int direction;
    // </editor-fold>

    
    
    // <editor-fold defaultstate="collapsed" desc="CONSTRUCTOR">
    /**
     * Crée une trame synchrone
     * @param id Correspond à l'id de la trame
     * @param object Correspond à l'objet de la trame
     * @param direction Correspond à la direction de la trame (-1 = réponse; 1 = question)
     */
    private SyncTrame(String id, Object object, int direction) {
        this.id = id;
        this.object = object;
        this.direction = direction;
    }
    // </editor-fold>

    
    
    // <editor-fold defaultstate="collapsed" desc="GETTERS">
    /**
     * Renvoie l'id de la trame
     * @return Retourne l'id de la trame
     */
    public String getId() {
        return id;
    }

    /**
     * Renvoie l'objet de la trame
     * @return Retourne l'objet de la trame
     */
    public Object getObject() {
        return object;
    }
    
    /**
     * Renvoie si oui ou non la trame est une question
     * @return Retourne true si c'est le cas, sinon false
     */
    public boolean isQuestion(){
        return this.direction == 1;
    }
    
    /**
     * Renvoie si oui ou non la trame est une réponse
     * @return Retourne true si c'est le cas, sinon false
     */
    public boolean isResponse(){
        return this.direction == -1;
    }
    
    /**
     * Renvoie si oui ou non la trame est une réponse de la trame de questionnement
     * @param trame Correspond à la trame de questionnement
     * @return Retourne true si c'est le cas, sinon false
     */
    public boolean isResponse(SyncTrame trame){
        return (trame != null && trame.isQuestion() && isResponse() && trame.id.equals(id));
    }
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="METHODES PUBLICS STATICS">
    /**
     * Génère une trame de questionnement
     * @param object Correspond à l'objet envoyé
     * @return Retourne la trame de questionnement
     */
    public static SyncTrame generateQuestion(Object object){
        return new SyncTrame(Strings.generate(20), object, 1);
    }
    
    /**
     * Génère une trame de réponse
     * @param answer Correspond à la trame de questionnement
     * @param object Correspond à l'objet de réponse
     * @return Retourne la trame de réponse
     */
    public static SyncTrame generateResponse(SyncTrame answer, Object object){
        return new SyncTrame(answer.id, object, -1);
    }
    // </editor-fold>
    
    
    
}