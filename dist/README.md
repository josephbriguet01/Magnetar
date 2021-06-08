Copyright (C) BRIGUET Systems, Inc - All Rights Reserved
# [Magnetar](https://github.com/josephbriguet01/Magnetar "Accès au projet Git Magnetar")

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

### Détecter un/des serveur(s) sur le réseau
Il arrive parfois qu'il soit nécessaire de détecter des serveurs sur le réseau. Tout simplement parce que l'on ne possède ni l'adresse IP, ni le port d'écoute du serveur. Pour faire en sorte qu'un serveur réel soit vu sur le réseau avec un nom... Il faut activer sa propagation. En faisant ceci, un autre serveur se crée automatiquement (celui de propagation). Le client à plus qu'à scanner le réseau. Il découvre le serveur de propagation. Il demande l'adresse IP, le port et le nom du serveur réel au serveur de propagation. Celui-ci envoie toutes les informations du serveur réel au client. Et de cette manière le client possède les paramètres de connexion pour pouvoir se connecter au serveur réel.

##### Au niveau serveur
```java
// Pour propager l'identification du serveur réel sur le réseau, il nous faut déjà posséder le serveur réel (Server s = new Server(...)). Puis on active la propagation.
s.startPropagation("Mon serveur", 14863);

// Pour stopper une propagation il suffit de faire
s.stopPropagation();
```

##### Au niveau client
On cherche à détecter les serveurs qui propage leurs informations sur le port 14863
```java
// On crée un objet DetectServer
DetectServer ds = new DetectServer();

//On scanne le réseau LAN et on récupère la liste des serveurs qui propage leurs informations sur le port 14863
IdentificationServer[] iss = ds.scanNetwork(new IPv4("192.168.0.0"), 14863);

// On affiche les informations des serveurs détectés
for(int i=0;i<iss.length;i++){
    System.out.println(iss[i]);
}
```
Et on obtient le résultat suivant:
```
IdentificationServer{ipServer=192.168.0.15, nameServer=Mon Serveur, portServer=15948}
```

## Accès au projet GitHub => [ici](https://github.com/josephbriguet01/Magnetar "Accès au projet Git Magnetar")
