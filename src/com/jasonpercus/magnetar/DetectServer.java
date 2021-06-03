/*
 * Copyright (C) JasonPercus Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by JasonPercus, August 2018
 */
package com.jasonpercus.magnetar;



import com.jasonpercus.network.IPCard;
import com.jasonpercus.network.IPv4;
import com.jasonpercus.network.Network;
import com.jasonpercus.network.NetworkCard;



/**
 * Cette classe permet de détecter les serveurs d'une catégorie sur le réseau LAN
 * @author JasonPercus
 * @version 1.0
 */
public class DetectServer {

    
    
    // <editor-fold defaultstate="collapsed" desc="CONSTANTE">
    /**
     * Correspond au nombre de seconde que doit attendre Service pour considérer qu'un HOST a arrêté de diffuser un serveur
     */
    public static int nombreScondeAvantSuppression = 5;
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="ATTRIBUTS">
    /**
     * Correspond au client UDP qui va recevoir les diffusions
     */
    private UDP client;
    
    /**
     * Correspond au listener a qui il faut communiquer les nouvelles diffusions
     */
    private IDiffusionListener ecouteur;
    
    /**
     * Correspond à la liste des Diffusions associées à un temps
     */
    private ListSynchronized<Element> liste;
    
    /**
     * Correspond à la liste des services associées à des éléments
     */
    private ListSynchronized<Service> services;
    
    /**
     * Cette liste tampon permet de vérifier que les diffusions ne sont pas erronée. Si une diffusion est reçut à l'identique à l'un des élements du tampon, alors c'est que le prenier élément était correct
     */
    private ListSynchronized<Diffusion> tampon;
    
    /**
     * Correspond au port d'envoi UDP
     */
    private final int portSend;
    
    /**
     * Correspond au port de réception UDP
     */
    private final int portReceive;
    
    /**
     * Correspond à la taille des paquets UDP
     */
    private final int sizePacket;
    // </editor-fold>

    
    
    // <editor-fold defaultstate="collapsed" desc="CONSTRUCTORS">
    /**
     * Crée un client qui va écouter les diffusions de serveurs
     * @param portSend Correspond au port d'envoi UDP
     * @param portReceive Correspond au port de réception UDP
     */
    public DetectServer(int portSend, int portReceive) {
        this(false, portSend, portReceive, 2048);
    }
    
    /**
     * Crée un client qui va écouter les diffusions de serveurs
     * @param portSend Correspond au port d'envoi UDP
     * @param portReceive Correspond au port de réception UDP
     * @param sizePacket Correspond à la taille des paquets UDP
     */
    public DetectServer(int portSend, int portReceive, int sizePacket) {
        this(false, portSend, portReceive, sizePacket);
    }
    
    /**
     * Crée un client qui va écouter les diffusions de serveurs
     * @param listenMe Détermine si le système peut détecter un serveur sur la même machine que celle qui détecte
     * @param portSend Correspond au port d'envoi UDP
     * @param portReceive Correspond au port de réception UDP
     */
    public DetectServer(boolean listenMe, int portSend, int portReceive) {
        this(listenMe, portSend, portReceive, 2048);
    }
    
    /**
     * Crée un client qui va écouter les diffusions de serveurs
     * @param listenMe Détermine si le système peut détecter un serveur sur la même machine que celle qui détecte
     * @param portSend Correspond au port d'envoi UDP
     * @param portReceive Correspond au port de réception UDP
     * @param sizePacket Correspond à la taille des paquets UDP
     */
    public DetectServer(boolean listenMe, int portSend, int portReceive, int sizePacket) {
        init(listenMe);
        this.portSend = portSend;
        this.portReceive = portReceive;
        this.sizePacket = sizePacket;
    }
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="METHODES PUBLICS">
    /**
     * Modifie l'objet listener qui écoutera les nouvelles diffusions
     * @param listener Correspond au nouveau listener
     */
    public void setClientDiffusionListener(IDiffusionListener listener){
        this.ecouteur = listener;
    }
    
    /**
     * Démarre l'écoute
     */
    public void start(){
        this.client.start();
    }
    
    /**
     * Stoppe l'écoute
     */
    @SuppressWarnings("SynchronizeOnNonFinalField")
    public void stop(){
        this.client.stop();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(DetectServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        synchronized(liste){
            while(liste.size()>0){
                liste.remove(0);
            }
        }
        synchronized(tampon){
            while(tampon.size()>0){
                tampon.remove(0);
            }
        }
    }
    
    /**
     * Efface la liste des résultats de la détection de serveurs
     */
    @SuppressWarnings({"SynchronizeOnNonFinalField", "NestedSynchronizedStatement"})
    public synchronized void resetServerResult(){
        synchronized(liste){
            synchronized(tampon){
                synchronized(services){
                    for(int i=0;i<services.size();i++){
                        services.get(i).forceStop();
                    }
                    services.clear();
                    liste.clear();
                    tampon.clear();
                }
            }
        }
    }
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="METHODES PRIVATES">
    /**
     * Renvoie la liste des ips du système
     * @return Retourne la liste des ips du système
     */
    private java.util.List<String> getIPs(){
        java.util.List<String> ips = new java.util.ArrayList<>();
        java.util.List<IPCard> ipCards = new java.util.ArrayList<>();
        java.util.List<NetworkCard> cards = new Network().getHardwareList();
        
        for(NetworkCard card : cards){
            if(card.getMacAddress() != null && card.isUp() && !card.getIpCards().isEmpty()){
                ipCards.addAll(card.getIpCards());
            }
        }
        if(!cards.isEmpty()){
            for(IPCard ipCard : ipCards){
                ips.add(ipCard.getIp().getIpv4());
            }
        }
        return ips;
    }
    
    /**
     * Prépare la détection de serveur
     * @param listenMe Détermine si le système peut détecter un serveur sur la même machine que celle qui détecte
     */
    private void init(boolean listenMe){
        this.liste = new ListSynchronized<>();
        this.tampon = new ListSynchronized<>();
        this.services = new ListSynchronized<>();
        this.client = new UDP(sizePacket, portSend, portReceive, new IPv4("127.0.0.1"));
        java.util.List<String> ips = getIPs();
        this.client.addIReceivedListener(new IReceivedUDP() {
            @Override
            public void received(byte[] data) {
                
            }

            @Override
            @SuppressWarnings("SynchronizeOnNonFinalField")
            public void received(Object obj, int statut) {
                if(obj instanceof Diffusion){
                    Diffusion diffusion = (Diffusion) obj;
                    if(listenMe || (!listenMe && !ips.contains(diffusion.getIp()))){
                        if(ecouteur != null) ecouteur.flux(diffusion);
                        synchronized(tampon){
                            boolean contains = false;
                            for(int i = 0; i < tampon.size(); i++){
                                if(tampon.get(i).equals(diffusion)){
                                    contains = true;
                                    break;
                                }
                            }
                            if(contains){
                                Element element = new Element(diffusion);
                                synchronized(liste){
                                    int indexof = -1;
                                    for(int i = 0; i < liste.size(); i++){
                                        if(liste.get(i).equals(element)){
                                            indexof = i;
                                            break;
                                        }
                                    }
                                    if(indexof == -1){
                                        if(ecouteur != null) ecouteur.detectionServer(diffusion, 1);
                                        Service service = new Service(element);
                                        liste.add(element);
                                        service.start();
                                        synchronized(services){
                                            services.add(service);
                                        }
                                    }else{
                                        liste.get(indexof).reset();
                                    }
                                }
                            }else{
                                tampon.add(diffusion);
                            }
                        }
                    }
                }
            }
        });
    }
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="CLASS">
    /**
     * Cette classe représente une diffusion associée à un temps en secondes depuis lequel la dernière même diffusion à été reçu
     * @author JasonPercus
     * @version 1.0
     */
    private class Element {
        
        
        
        // <editor-fold defaultstate="collapsed" desc="ATTRIBUTS">
        /**
         * Correspond à un élément de diffusion
         */
        private final Diffusion partie;
        
        /**
         * Correspond au temps passé depuis la dernière diffusion de ce même élement
         */
        private int time;
        // </editor-fold>

        
        
        // <editor-fold defaultstate="collapsed" desc="CONSTRUCTOR">
        /**
         * Crée un objet Element
         * @param partie Correspond à un serveur diffusé
         */
        public Element(Diffusion partie) {
            this.partie = partie;
            this.time = 0;
        }
        // </editor-fold>
        
        
        
        // <editor-fold defaultstate="collapsed" desc="METHODES PUBLICS">
        /**
         * Renvoie le temps passé depuis la dernière diffusion de ce même élement
         * @return Retourne le temps passé depuis la dernière diffusion de ce même élement
         */
        public int time(){
            return this.time;
        }
        
        /**
         * Service à obtenue une nouvelle diffusion. Le temps repasse à 0
         */
        public void reset(){
            this.time = 0;
        }
        
        /**
         * Ajoute une seconde depuis que la dernière diffusion a été reçu
         */
        public void add(){
            this.time++;
        }

        /**
         * Renvoie le résultat de la méthode hashCode
         * @return Retourne le résultat de la méthode hashCode
         */
        @Override
        public int hashCode() {
            int hash = 7;
            hash = 89 * hash + java.util.Objects.hashCode(this.partie);
            return hash;
        }

        /**
         * Détermine si deux objets sont identiques
         * @param obj Correspond au second objet à comparer avec le courant
         * @return Retourne true s'ils sont identiques, sinon false
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Element other = (Element) obj;
            return java.util.Objects.equals(this.partie, other.partie);
        }
        // </editor-fold>
        
        
        
    }
    
    /**
     * Cette classe permet de détecter l'arrêt d'une diffusion d'un serveur. Si un HOST diffuse un serveur en continue, Service vérifie que le diffuseur diffuse bien. Si Service ne reçoit plus de diffusion pendant 5 secondes, alors Service considère que HOST a arrêté la diffusion. Il envoie donc un évènement
     * @author JasonPercus
     * @version 1.0
     */
    private class Service extends Thread {
        
        
        
        // <editor-fold defaultstate="collapsed" desc="ATTRIBUTS">
        /**
         * Détermine si oui ou non le thread est en activité
         */
        private boolean run;
        
        /**
         * Correspond à un élément de diffusion avec un temps (ce temps va s'incrémenter jusqu'à 5 secondes à moins de recevoir une nouvelle diffusion)
         */
        private final Element element;
        // </editor-fold>

        
        
        // <editor-fold defaultstate="collapsed" desc="CONSTRUCTOR">
        /**
         * Crée un service
         * @param element Correspond à un élement de diffusion
         */
        public Service(Element element) {
            this.element = element;
            this.run = true;
        }
        // </editor-fold>
        
        
        
        // <editor-fold defaultstate="collapsed" desc="METHODES PUBLICS">
        /**
         * Vérifie que le serveur HOST continue de diffuser
         */
        @Override
        @SuppressWarnings({"SynchronizeOnNonFinalField", "SleepWhileInLoop"})
        public void run(){
            while(this.run){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    java.util.logging.Logger.getLogger(DetectServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
                if(this.run)
                    element.add();
                synchronized(liste){
                    if(element.time() >= nombreScondeAvantSuppression){
                        liste.remove(element);
                        this.run = false;
                        if(ecouteur != null) ecouteur.detectionServer(element.partie, -1);
                    }
                }
            }
        }
        
        /**
         * Arrête le thread
         */
        public void forceStop(){
            this.run = false;
        }
        // </editor-fold>
        
        
        
    }
    // </editor-fold>
    
    
    
}