/*
 * Copyright (C) BRIGUET Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Briguet, August 2018
 */
package com.jasonpercus.magnetar;



import com.jasonpercus.encryption.Cipher;
import com.jasonpercus.encryption.Key;
import com.jasonpercus.encryption.rsa.RSA;
import com.jasonpercus.network.IPv4;
import static com.jasonpercus.util.OS.IS_ANDROID;



/**
 * Cette classe permet de créer un client qui pourra se connecter en TCP à un serveur
 * @author BRIGUET
 * @version 1.0
 */
public class Client extends TCP {
    
    
    
    // <editor-fold defaultstate="collapsed" desc="ATTRIBUTS">
    /**
     * Correspond à l'adresse ip de connexion
     */
    private final       String host;
    
    /**
     * Correspond au service qui va se charger de dialoguer avec le serveur
     */
    private Service     service;
    
    /**
     * Correspond à l'identité du serveur. Attention = null tant que le connexion au serveur n'a pas été initialisée
     */
    private Identity    identity_server;
    
    /**
     * Correspond aux flux entrants/sortants de la connexion tcp du client vers le serveur
     */
    private Flux        flux;
    
    /**
     * Correspond au listener qui préviendra si la connexion d'un client est autorisée/refusée par le serveur
     */
    private IResultATBC iratbc;
    // </editor-fold>



    // <editor-fold defaultstate="collapsed" desc="CONSTRUCTOR">
    /**
     * Crée un client. Ce client pourra se connecter à un serveur
     * @param identity Correspond à l'identité de ce client
     * @param host Correspond à l'adresse ip du serveur
     * @param port Correspond au port du serveur
     */
    public Client(Identity identity, IPv4 host, int port) {
        super(identity, port);
        this.host       = host.getIpv4();
        this.connected  = false;
        identity.setClientOrServer(1);
    }
    // </editor-fold>



    // <editor-fold defaultstate="collapsed" desc="GETTERS">
    /**
     * Revoie l'identité du serveur
     * @return Retourne l'identité du serveur ou null si le client n'est pas connecté
     */
    public Identity getIdentity_server() {
        return identity_server;
    }
    
    /**
     * Renvoie l'adresse IP de connexion
     * @return Retourne l'adresse IP de connexion
     */
    public IPv4 getHost() {
        return new IPv4(host);
    }

    /**
     * Modifie l'identité du client ou du serveur (Attention: à utiliser avec parcimonie. En effet, il est préférable d'utiliser cette méthode dans le cas où seul le serveur a fait une modification sur l'identité du client et que celle-ci doit être de nouveau poussé au client de manière à ce qu'il soit à jour)
     * @param identity Correspond à la nouvelle identité
     */
    @Override
    public void setIdentity(Identity identity) {
        super.setIdentity(identity);
    }
    // </editor-fold>

    
    
    // <editor-fold defaultstate="collapsed" desc="METHODES PUBLICS">
    /**
     * Ajoute un IResultATBC au client. De cette manière celui-ci est au courant du type de déconnexion 
     * @param listener Correspond à la classe qui implémente IResultATBC
     */
    public void addIResultATBC(IResultATBC listener) {
        this.iratbc = listener;
    }
    
    /**
     * Supprime l'objet IResultATBC du client
     */
    public void removeIResultATBC(){
        this.iratbc = null;
    }
    
    /**
     * Crée une connexion entre le client et le serveur
     */
    public void connect_to(){
        try {
            //On se connecte au serveur
            java.net.Socket socket = new java.net.Socket(host, port);


            //On crée les flux d'entrée sortie
            this.flux = new Flux(socket);


            //Vérifie si les flux doivent être chiffré si oui flux chiffrera les objet
            INIT_ENCRYPT();


            //Les présentation sont faites
            this.identity_server = PRESENTATION();
            
            
            if(!identity.getIdentity().equals(Identity.GENERIC_IDENTITY)){
                //Passe à la douane
                DOUANE(socket);
            }else{
                DESTROY_GENERIC_IDENTITY(socket);
            }
            
            
        } catch (java.io.IOException ex) {
            throw new com.jasonpercus.magnetar.SocketException("Error initializing Socket");
        }
    }

    /**
     * Déconnecte le client du serveur
     */
    public void disconnect(){
        if(isConnected()){
            send(new MetaTCP(EMetaType.META_REQUEST_DISCONNECT));
        }
    }
    
    /**
     * Récupère ou pas les logs du serveur
     * @param enable Correspond à true si l'on veut récupérer les log, false (par défaut) sinon
     */
    public void redirectLog(boolean enable){
        send(new MetaTCP(EMetaType.META_REDIRECTION_LOG, enable));
    }

    /**
     * Envoie un objet au serveur si l'utilisateur est connecté
     * @param obj Correspond à l'objet à envoyer (Il doit être serialisable)
     */
    public void send(Object obj){
        @SuppressWarnings("Convert2Lambda")
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(Client.this.connected){
                    try{
                        service.getFlux().send(obj);
                    }catch(java.lang.NullPointerException ex){
                        throw new ErrorConnectionException("No Connection");
                    }
                }else{
                    throw new ErrorConnectionException("You are not connected");
                }
            }
        };
        if(IS_ANDROID)
            new Thread(runnable).start();
        else
            runnable.run();
    }
    
    /**
     * Envoie un objet de manière synchrone si l'utilisateur est connecté
     * @param obj Correspond à l'objet à envoyer (Il doit être serialisable)
     * @return Retourne une réponse
     * @throws ErrorDisconnectionException Si la réponse n'a pu être obtenu avant une déconnexion
     * @throws ErrorNoImplementation Si le serveur n'implémente à aucun moment IReceivedSync
     */
    public Object sendSync(Object obj) throws ErrorDisconnectionException, ErrorNoImplementation {
        final SyncTrame question = SyncTrame.generateQuestion(obj);
        final Object[] result = new Object[1];
        final boolean[] finish = new boolean[1];
        final Exception[] exceptions = new Exception[1];
        
        Receiver receiver = new Receiver(question.getId()) {
            @Override
            public void received(Identity expeditor, Object obj) {
                if(obj != null && obj instanceof SyncTrame){
                    SyncTrame response = (SyncTrame) obj;
                    if(response.isResponse(question)){
                        result[0] = response.getObject();
                        if(result[0] instanceof ErrorNoImplementation)
                            exceptions[0] = (Exception) response.getObject();
                        finish[0] = true;
                    }
                }
            }
            
            @Override
            public void hasConnected(Identity identity) {
                
            }
            
            @Override
            public void hasDisconnected(Identity identity, EDisconnectReason reason) {
                exceptions[0] = new ErrorDisconnectionException("No object received because the disconnection occured before !");
                finish[0] = true;
            }
        };
        
        this.addIReceivedListener(receiver);
        this.addIStatutListener(receiver);
        send(question);
        while(!finish[0]){
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        }
        this.removeIReceivedListener(receiver);
        this.removeIStatutListener(receiver);
        if(exceptions[0] != null){
            if(exceptions[0] instanceof ErrorDisconnectionException)
                throw (ErrorDisconnectionException) exceptions[0];
            else
                throw (ErrorNoImplementation) exceptions[0];
        }else
            return result[0];
    }
    // </editor-fold>



    // <editor-fold defaultstate="collapsed" desc="METHODES PRIVATES">
    /**
     * Les présentations entre le serveur et le client se font dans cette méthode. Ils échangent leur identité pour pouvoir se connaitre
     * @return Retourne l'identité du serveur (car on se situe dans cette classe au niveau du client)
     */
    private Identity PRESENTATION(){
        Identity identity_other = (Identity) this.flux.receive();
        this.flux.send(getIdentity());
        return identity_other;
    }

    /**
     * Vérifie si les flux doivent être chiffré si oui flux chiffrera les objet
     */
    private void INIT_ENCRYPT(){
        boolean enc = (boolean) this.flux.receive();
        if (enc) {
            Cipher cipherEncryptor = new RSA();
            Key keyPublic   = cipherEncryptor.generatePublicKey(10);
            Key keyPrivate  = cipherEncryptor.generatePrivateKey(10);
            this.flux.send(keyPublic);
            String keyEncrypted = (String) this.flux.receive();
            Key keyUsable = NEW_CIPHER_KEY(cipherEncryptor.decrypt(keyPrivate, keyEncrypted));
            this.flux.setKey(keyUsable, NEW_CIPHER());
        } else {
            this.flux.send(0);
        }
        this.flux.setEncrypt(enc);
    }
    
    /**
     * Le serveur vérifie que l'on a le droit de se connecter
     * @param socket Correspond à la socket de communication
     */
    private void DOUANE(java.net.Socket socket){
        MetaTCP meta = (MetaTCP) this.flux.receive();
        if(meta.getMeta().equals(EMetaType.META_CONNECTION_AUTHORIZED)){
        
            //On renvoie la confirmation
            this.flux.send(meta);
            
            //Crée et démarre un service server
            this.service = new Service(this, null, this.identity_server, socket, this.flux, list_i_received, list_i_statut, list_i_log);
            service.start();
            
            //Le client est connecté
            this.connected = true;
            
        }else{
            
            //On récupère la cause du refus
            int result = (int) meta.getValue();
            
            //On renvoie la confirmation
            this.flux.send(meta);
            
            /*try {
                this.flux.closeFlux();
                socket.close();
            } catch (java.io.IOException ex) {
                throw new SocketException("Error close socket");
            }*/
            
            disconnect();
            
            //Affiche le refus
            if(iratbc != null) iratbc.refusedToConnected(result);
            
        }
    }
    
    /**
     * Détruit la connexion d'un client générique du côté client
     * @param socket Correspond à l'extrémité de la connexion côté client
     */
    private void DESTROY_GENERIC_IDENTITY(java.net.Socket socket){
        MetaTCP meta = (MetaTCP) this.flux.receive();
        if(meta.getMeta().equals(EMetaType.META_REQUEST_DISCONNECT)){
            try {
                this.flux.closeFlux();
                socket.close();
            } catch (java.io.IOException ex) {
                throw new SocketException("Error close socket");
            }
        }
    }
    
    /**
     * Crée un nouvel objet de dé/chiffrement
     * @return Retourne un nouvel objet de dé/chiffrement
     */
    private Cipher NEW_CIPHER(){
        try {
            return (Cipher) Server.ENCRYPTOR.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AcceptOther.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * Crée un nouvel objet de dé/chiffrement
     * @param key Correspond à la clef de dé/chiffrement
     * @return Retourne un nouvel objet de dé/chiffrement
     */
    private Key NEW_CIPHER_KEY(String key){
        try {
            java.lang.reflect.Constructor constructor = Server.ENCRYPTOR_KEY.getConstructor(String.class);
            return (Key) constructor.newInstance(key);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | java.lang.reflect.InvocationTargetException | NoSuchMethodException | SecurityException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        return null;
    }
    // </editor-fold>

    
    
    // <editor-fold defaultstate="collapsed" desc="CLASS">
    /**
     * Cette classe permet de recevoir des objets d'un émetteur et d'être au courant des éventuelles déconnexions
     * @author BRIGUET
     * @version 1.0
     */
    protected abstract class Receiver implements IReceived, IStatutClient {
        
        
        
    //ATTRIBUT
        /**
         * Correspond à l'identifiant unique du receiver
         */
        private final String id;

        
        
    //CONSTRUCTOR
        /**
         * Crée un Receiver unique
         * @param id Correspond à l'id unique du receiver
         */
        public Receiver(String id) {
            this.id = id;
        }

        
        
    //METHODES PUBLICS
        /**
         * Renvoie le hashCode du Receiver
         * @return Retourne le hashCode du Receiver
         */
        @Override
        public int hashCode() {
            int hash = 7;
            hash = 97 * hash + java.util.Objects.hashCode(this.id);
            return hash;
        }

        /**
         * Détermine si deux Receivers sont identiques
         * @param obj Correspond au Receiver à comparer avec le courant
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
            final Receiver other = (Receiver) obj;
            return java.util.Objects.equals(this.id, other.id);
        }
        
        
        
    }
    // </editor-fold>
    
    
    
}