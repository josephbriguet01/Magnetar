/*
 * Copyright (C) BRIGUET Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Briguet, August 2018
 */
package com.jasonpercus.magnetar;



import com.jasonpercus.encryption.jps.JPS;
import com.jasonpercus.encryption.jps.KeyJPS;
import static com.jasonpercus.magnetar.Identity.GENERIC_IDENTITY;
import static com.jasonpercus.util.OS.IS_ANDROID;



/**
 * Cette classe permet de créer un serveur
 * @author BRIGUET
 * @version 1.0
 */
public class Server extends TCP {
    
    
    
    // <editor-fold defaultstate="collapsed" desc="ATTRIBUTS">
    /**
     * Correspond à la classe qui va écouter l'arrivée des nouveaux clients
     */
    private AcceptOther ao;
    
    /**
     * Correspond à la classe qui vérifie qi un client à le droit de se connecter
     */
    IConditionATBC      icatbc;
    
    /**
     * Correspond au serveur de diffusion
     */
    private ServerDiffusion serveurDiffusion;
    
    /**
     * Correspond à l'objet diffusé
     */
    private Diffusion diffusion;
    
    /**
     * Correspond à la classe qui sert de chiffrement
     */
    protected final static Class ENCRYPTOR = JPS.class;
    
    /**
     * Correspond à la classe qui sert de chiffrement
     */
    protected final static Class ENCRYPTOR_KEY = KeyJPS.class;
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="CONSTRUCTOR">
    /**
     * Crée un serveur
     * @param identity Correspond à l'identité du serveur
     * @param port Correspond au port du serveur
     */
    public Server(Identity identity, int port) {
        super(identity, port);
        identity.setClientOrServer(2);
    }
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="METHODES PUBLICS">
    /**
     * Ajoute un listener IConditionATBC
     * @param listener Correspond au listener
     */
    public void addIConditionATBC(IConditionATBC listener) {
        this.icatbc = listener;
    }
    
    /**
     * Enlève un listener IConditionATBC
     */
    public void removeIConditionATBC(){
        this.icatbc = null;
    }
    
    /**
     * Dévoile la présence du serveur
     * @param broadcast Détermine si la socket enverra des broadcasts
     * @param portSend Correspond au port d'envoi UDP
     * @param portReceive Correspond au port de réception UDP
     * @param sizePacket Correspond à la taille des paquets UDP
     * @param datas Correspond aux données du serveur à diffuser
     */
    public void startPropagation(boolean broadcast, int portSend, int portReceive, int sizePacket, Object... datas){
        this.diffusion = new Diffusion(datas);
        this.serveurDiffusion = new ServerDiffusion(this.diffusion, portSend, portReceive, sizePacket);
        this.serveurDiffusion.startDiffusion(broadcast);
    }
    
    /**
     * Modifie les objets de diffusions
     * @param datas Correspond aux nouveaux objets diffusés
     */
    public void changeObjectsPropagation(Object... datas){
        this.diffusion.setDatas(datas);
    }
    
    /**
     * Renvoie les objets de diffusions
     * @return Retourne les objets de diffusions
     */
    public Object[] getObjectsPropagation(){
        return this.diffusion.getDatas();
    }
    
    /**
     * Le serveur n'est plus dévoilé sur le réseau
     */
    public void stopPropagation(){
        this.serveurDiffusion.stopDiffusion();
    }
    
    /**
     * Lance l'écoute des nouveaux clients et crée des services associés aux nouveaux clients
     * @param encrypt_flux Correspond à la variable qui dit si oui ou non les flux doivent être chiffrée
     */
    public void start_listening(boolean encrypt_flux){
        ao = new AcceptOther(this, port, routage_table, list_i_statut, list_i_received, list_i_log, encrypt_flux);
        ao.start();
    }
    
    /**
     * Stoppe l'écoute des nouveaux clients
     */
    public void stop_listening(){
        if(this.ao != null){
            this.ao.stop_listening();
            stopPropagation();
        }
    }
    
    /**
     * Stoppe les services des clients connectés au serveur
     */
    public void stop_services(){
        if(this.ao != null){
            this.ao.stop_services();
        }
    }
    
    /**
     * Stoppe l'écoute des nouveaux clients et Stoppe les services des clients connectés au serveur
     */
    public void stop_server(){
        if(this.ao != null){
            this.ao.stop_server();
            stopPropagation();
        }
    }
    
    /**
     * Déconnecte un client du serveur
     * @param identity_client Correspond au client à déconnecter
     */
    @SuppressWarnings("SynchronizeOnNonFinalField")
    public void disconnect_client(Identity identity_client){
        if(identity_client != null){
            try{
                synchronized(routage_table){
                    int size = routage_table.size();
                    for(int i=0;i<size;i++){
                        if(routage_table.get(i).getIdentity_other().equals(identity_client)){
                            routage_table.get(i).send_disconnection();
                            break;
                        }
                    }
                }
            }catch(java.lang.NullPointerException | SendingException e){

            }
            
        }else{
            throw new com.jasonpercus.magnetar.ErrorDisconnectionException("Client = null");
        }
    }
    
    /**
     * Envoie un objet à tous les clients connectés
     * @param obj Correspond à l'objet à envoyer
     */
    public void send(final Object obj){
        send(Identity.getBroadcastIdentity(), obj);
    }
    
    /**
     * Envoie un objet à un client connecté
     * @param receiver Correspond à l'identité de ce client
     * @param obj Correspond à l'objet à envoyer
     */
    @SuppressWarnings("SynchronizeOnNonFinalField")
    public void send(final Identity receiver, final Object obj){
        @SuppressWarnings("Convert2Lambda")
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try{
                    if(receiver.getIdentity().equals(Identity.BROADCAST_IDENTITY)){
                        synchronized(routage_table){
                            int size = routage_table.size();
                            for(int i=0;i<size;i++){
                                routage_table.get(i).getFlux().send(obj);
                            }
                        }
                    }else{
                        synchronized(routage_table){
                            int size = routage_table.size();
                            for(int i=0;i<size;i++){
                                if(routage_table.get(i).getIdentity_other().equals(receiver)){
                                    routage_table.get(i).getFlux().send(obj);
                                    break;
                                }
                            }
                        }
                    }
                }catch(java.lang.NullPointerException | SendingException e){

                }
            }
        };
        if(IS_ANDROID)
            new Thread(runnable).start();
        else
            runnable.run();
    }
    
    /**
     * Envoie un objet de manière synchrone à tous les clients connectés (Remarque: Le système attendra une réponse de tous avant de se débloquer) (Remarque: Toutes les exceptions seront levées)
     * @param obj Correspond à l'objet à envoyer (Il doit être serialisable)
     * @return Retourne une/des réponse(s) en fonction du broadcast ou pas
     * @throws ErrorDisconnectionException Si la réponse n'a pu être obtenu avant une déconnexion
     * @throws ErrorNoImplementation Si le serveur n'implémente à aucun moment IReceivedSync
     */
    public Object[] sendSync(final Object obj) throws ErrorDisconnectionException, ErrorNoImplementation {
        return sendSync(Identity.getBroadcastIdentity(), obj, true, true);
    }
    
    /**
     * Envoie un objet de manière synchrone à un client connecté (Remarque: Si l'envoi est en broadcast, alors le système attendra une réponse de tous avant de se débloquer) (Remarque: Toutes les exceptions seront levées)
     * @param receiver Correspond à l'identité de ce client
     * @param obj Correspond à l'objet à envoyer (Il doit être serialisable)
     * @return Retourne une/des réponse(s) en fonction du broadcast ou pas
     * @throws ErrorDisconnectionException Si la réponse n'a pu être obtenu avant une déconnexion
     * @throws ErrorNoImplementation Si le serveur n'implémente à aucun moment IReceivedSync
     */
    public Object[] sendSync(final Identity receiver, final Object obj) throws ErrorDisconnectionException, ErrorNoImplementation {
        return sendSync(receiver, obj, true, true);
    }
    
    /**
     * Envoie un objet de manière synchrone à un client connecté (Remarque: Toutes les exceptions seront levées)
     * @param receiver Correspond à l'identité de ce client
     * @param obj Correspond à l'objet à envoyer (Il doit être serialisable)
     * @param waitAll Détermine si dans le cas où l'objet est en envoyé en Broadcast, si le serveur doit attendre que tous les clients ont répondu
     * @return Retourne une/des réponse(s) en fonction du broadcast ou pas
     * @throws ErrorDisconnectionException Si la réponse n'a pu être obtenu avant une déconnexion
     * @throws ErrorNoImplementation Si le serveur n'implémente à aucun moment IReceivedSync
     */
    public Object[] sendSync(final Identity receiver, final Object obj, final boolean waitAll) throws ErrorDisconnectionException, ErrorNoImplementation {
        return sendSync(receiver, obj, waitAll, true);
    }
    
    /**
     * Envoie un objet de manière synchrone à un client connecté
     * @param receiver Correspond à l'identité de ce client
     * @param obj Correspond à l'objet à envoyer (Il doit être serialisable)
     * @param waitAll Détermine si dans le cas où l'objet est en envoyé en Broadcast, si le serveur doit attendre que tous les clients ont répondu
     * @param throwException True = lorsque la moindre exception est levé, le résultat ne peut être obtenu; False = Même s'il y a une exception, renvoie les résultats
     * @return Retourne une/des réponse(s) en fonction du broadcast ou pas
     * @throws ErrorDisconnectionException Si la réponse n'a pu être obtenu avant une déconnexion
     * @throws ErrorNoImplementation Si le serveur n'implémente à aucun moment IReceivedSync
     */
    public Object[] sendSync(final Identity receiver, final Object obj, final boolean waitAll, final boolean throwException) throws ErrorDisconnectionException, ErrorNoImplementation {
        final SyncTrame question = SyncTrame.generateQuestion(obj);
        final Object[] result;
        final Exception[] exceptions;
        if(waitAll){
            int size = getConnectedClient().size();
            result = new Object[size];
            exceptions = new Exception[size];
        }else{
            result = new Object[1];
            exceptions = new Exception[1];
        }
        final boolean[] finish = new boolean[1];
        final int[] counter = new int[1];
        
        Receiver receiverServer = new Receiver(question.getId()) {
            @Override
            public void received(Identity expeditor, Object obj) {
                if(obj != null && obj instanceof SyncTrame){
                    SyncTrame response = (SyncTrame) obj;
                    if(response.isResponse(question)){
                        result[counter[0]] = response.getObject();
                        if(result[counter[0]] instanceof ErrorNoImplementation)
                            exceptions[counter[0]] = (Exception) response.getObject();
                        counter[0] = counter[0] + 1;
                        
                        if(!waitAll || (waitAll && counter[0]>=result.length))
                            finish[0] = true;
                    }
                }
            }
            
            @Override
            public void hasConnected(Identity identity) {
                
            }
            
            @Override
            public void hasDisconnected(Identity identity, EDisconnectReason reason) {
                exceptions[counter[0]] = new ErrorDisconnectionException("No object received because the disconnection occured before !");
                counter[0] = counter[0] + 1;
                if(!waitAll || (waitAll && counter[0]>=result.length))
                    finish[0] = true;
            }
        };
        
        this.addIReceivedListener(receiverServer);
        this.addIStatutListener(receiverServer);
        send(receiver, question);
        while(!finish[0]){
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        }
        this.removeIReceivedListener(receiverServer);
        this.removeIStatutListener(receiverServer);
        java.util.List<Object> objs = new java.util.ArrayList<>();
        for(int i=0;i<exceptions.length;i++){
            Exception exception = exceptions[i];
            if(exception != null){
                if(throwException){
                    if(exception instanceof ErrorDisconnectionException)
                        throw (ErrorDisconnectionException) exception;
                    else
                        throw (ErrorNoImplementation) exception;
                }
            }else{
                objs.add(result[i]);
            }
        }
        Object[] objsArray = new Object[objs.size()];
        for(int i=0;i<objs.size();i++){
            objsArray[i] = objs.get(i);
        }
        return objsArray;
    }
    
    /**
     * Renvoie la liste des identités des clients connectés
     * @return Retourne la liste des identités des clients connectés
     */
    @SuppressWarnings("SynchronizeOnNonFinalField")
    public synchronized java.util.List<Identity> getConnectedClient(){
        synchronized(routage_table){
            java.util.List<Service> services = routage_table.getList();
            synchronized(services){
                java.util.List<Identity> tempo = new java.util.ArrayList<>();
                synchronized(tempo){
                    int size = services.size();
                    for(int i=0;i<size;i++){
                        Service s = services.get(i);
                        Identity id = s.getIdentity_other();
                        if(!id.getIdentity().equals(GENERIC_IDENTITY)){
                            tempo.add(id);
                        }
                    }
                    return tempo;
                }
            }
        }
    }
    // </editor-fold>

    
    
    // <editor-fold defaultstate="collapsed" desc="METHODES PROTECTED">
    /**
     * Renvoie le processus d'écoute des nouveaux clients connectés
     * @return Retourne le processus
     */
    protected AcceptOther getAo() {
        return ao;
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