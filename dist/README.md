Copyright (C) BRIGUET Systems, Inc - All Rights Reserved
# Magnetar

### Introduction
Magnetar est une librairie qui a pour but de simplifier la vie du développeur lorsqu'il s'agit de créer des applications cliente/serveur. Elle se base sur la couche 4 du modèle OSI et plus particulièrement sur le protocole TCP/IP. Magnetar est un protocole situé entre le niveau 5 et 7. Tout dépend de son utilisation. Il gère intégralement le système de dé/connexions clientes, de sessions, de chiffrements des données... Cependant, à l'heure actuelle, il ne contient que le code atomique. Ainsi donc le plus bas niveau du projet. Si le développeur souhaite, ajouter des salons de connexions ou des profils (comme un administrateur), ...,  celui-ci devra se baser sur le code atomique.

### Création d'un serveur (au niveau atomique)
```java
//Si on veut chiffrer les communications entre les clients
boolean encrypt = true;

//On crée une identité pour le serveur. Elle sera utilisé par les clients lorsqu'ils voudront communiquer avec lui.
Identity identity = new Identity();

//On détermine le port d'écoute du serveur
int port = 12345;

//On crée le serveur
Server server = new Server(identity, port);

//Si on veut traiter les messages reçues des clients
server.addIReceivedListener([YOUR_CODE]);

//Si on veut traiter les logs du serveur
server.addILogListener([YOUR_CODE]);

//Si on veut savoir les états des clients (connectés, déconnectés...) ou savoir si le serveur est bien lancé ou pas
server.addIStatutListener([YOUR_CODE]);

//Si on veut filtrer les connexions entrantes (accepter certains clients et pas d'autres)
server.addIConditionATBC([YOUR_CODE]);

//Si on veut accueillir les clients entrants et leur créer un service (lorsqu'ils sont autorisées à se connecter) 
server.start_listening(encrypt);

//Si on souhaite déconnecter de force un client
server.disconnect_client([CLIENT_IDENTITY_TO_DISCONNECT]);

//Si on souhaite connaitre la liste de tous les clients connectés
Identity[] identities = server.getConnectedClient();

//Si on veut envoyer un message à un client
server.send([RECEIVER_IDENTITY_CLIENT], "Bonjour je m'appelle Serveur !");

//Si on ne souhaite plus accueillir de nouveaux clients mais continuer à servir ceux qui sont connectés
server.stop_listening();

//Si on souhaite fermer tous les services des clients connectés
server.stop_services();

//Si on ne souhaite plus accueillir de nouveaux clients mais continuer à servir ceux qui sont connectés et fermer tous les services des clients connectés
//server.stop_server();
```

Si on veut traiter les messages reçues des clients
```java
server.addIReceivedListener(new IReceived(){
    @Override
    public void received(Identity expeditor, Object obj){
        System.out.println("Je reçois " + obj + " du client " + expeditor);
    }
});
```

Si on veut traiter les logs du serveur
```java
server.addILogListener(new ILog(){
    @Override
    public void readLog(Log log){
        System.out.println("Je reçois le log " + log);
    }
});
```

Si on veut savoir les états des clients (connectés, déconnectés...) ou savoir si le serveur est bien lancé ou pas
```java
server.addIStatutListener(new IStatutServer(){
    @Override
    public void serverIsStart(){
        System.out.println("J'ai démarré !");
    }
    
    @Override
    public void serverIsStop(){
        System.out.println("Je me suis stoppé !");
    }
    
    @Override
    public void hasConnected(Identity identity){
        System.out.println(identity + " vient de se connecter à moi !");
    }
    
    @Override
    public void hasDisconnected(Identity identity, EDisconnectReason reason){
        System.out.println(identity + " vient de se déconnecter de moi avec la raison suivante: " + reason);
    }
});
```

Si on veut filtrer les connexions entrantes (accepter certains clients et pas d'autres)
```java
server.addIConditionATBC(new IConditionATBC(){
    @Override
    public int authorizedToBeConnected(Identity identity){
        //Si l'identité s'appelle Bonjour alors elle pourra se connecter.
        if (identity.getIdentity().equals("Bonjour"){
            return 0;
        } else {
            return 1;
        }
    }
})
```

### Création d'un client (au niveau atomique)
```java
//On crée une identité pour le client. Elle sera utilisé par le serveur lorsqu'il voudra communiquer avec lui.
Identity identity = new Identity();

//On détermine l'adresse ip de connexion
String ip = "127.0.0.1";

//On détermine le port d'écoute du serveur
int port = 12345;

//Création du client
Client client = new Client(identity, ip, port);

//Si on veut traiter les messages reçues du serveur
client.addIReceivedListener([YOUR_CODE]);

//Si on veut traiter les logs du serveur
client.addILogListener([YOUR_CODE]);

//Si on veut savoir les états du client (connectés, déconnectés...)
client.addIStatutListener([YOUR_CODE]);

//Si on veut connaitre la raison du refus de connexion du serveur
client.addIResultATBC([YOUR_CODE]);

//Si on souhaite se connecter au serveur
client.connect_to();

//Si on souhaite récupérer l'identité du serveur
Identity serverIdentity = client.getIdentity_server();

//Si on veut envoyer un message au serveur
client.send(serverIdentity, "Bonjour je m'appelle Client !");

//Si on veut rediriger les logs du serveur sur le client (false désactive la redirection). Attention: Le client ne recevra aucun log s'il n'implémente pas ILogListener (voir plus)
client.redirectLog(true);

//Si on souhaite se déconnecter du serveur
server.disconnect();
```

Si on veut traiter les messages reçues du serveur
```java
client.addIReceivedListener(new IReceived(){
    @Override
    public void received(Identity expeditor, Object obj){
        System.out.println("Je reçois " + obj + " du serveur " + expeditor);
    }
});
```

Si on veut traiter les logs du serveur
```java
client.addILogListener(new ILog(){
    @Override
    public void readLog(Log log){
        System.out.println("Je reçois le log " + log + " du serveur");
    }
});
```

Si on veut savoir les états du client (connectés, déconnectés...)
```java
server.addIStatutListener(new IStatutClient(){
    @Override
    public void hasConnected(Identity identity){
        System.out.println("Je vient de me connecter au serveur " + identity + "!");
    }
    
    @Override
    public void hasDisconnected(Identity identity, EDisconnectReason reason){
        System.out.println("Je vient de me déconnecter du serveur " + identity + " avec la raison suivante: " + reason + "!");
    }
});
```

Si on veut connaitre la raison du refus de connexion du serveur
```java
client.addIResultATBC(new IResultATBC(){
    @Override
    public void refusedToConnected(int code){
        System.out.println("J'ai été refusé de connexion avec le code: " + code);
    }
});
```