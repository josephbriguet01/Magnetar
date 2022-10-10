Copyright © JasonPercus Systems, Inc - All Rights Reserved
# **Introduction**

Cette librairie permet de créer des serveurs/clients TCP/UDP. Les serveurs TCP se servent de la technologie non bloquante des ```Channel```. Ainsi les serveurs peuvent supporter des charges plus importantes de clients :

# **Magnetar**
Ce projet permet de récupérer le nom des cartes réseaux, le nom des interfaces, l'adresse MAC, les IP...

### TCP
#### Connection
La classe ```Connection``` représente la connexion entre cet équipement et l'équipement distant. Donc lorsqu'un client TCP se connecte à un serveur, le client obtient une connexion. Il en va de même pour le serveur qui obtient une connexion par client connecté. La classe ```Connection``` est ```abstract```. À chaque connexion le développeur doit (que ce soit du côté client ou serveur) créer un objet ```Connection```.
```java
// Crée un objet connection
Connection connection = new Connection(){

    // Cette méthode est à définir. C'est sur elle qu'elle reçoit les données de l'entité au bout de la connexion
    protected void read(Connection connection, int value){
        
    }
    
};
```
Il est possible de redéfinir les méthodes:
- **clientConnected()** -> *lorsque le client vient de se connecter*
- **clientDisconnected()** -> *lorsque le client vient de se déconnecter*

#### Serveur TCP
```java
// Crée un serveur tcp sur le port 80
TCPServer serveur = new TCPServer(new InetSocketAddress("192.168.1.1", 80)){
    protected Connection newConnection(){
        // Lorsque le serveur reçoit une nouvelle connexion, cette méthode est appelée automatiquement. 
        // Son rôle et de créer la connexion.
        return (voir section ci dessus: Connection)
    }
};

// On démarre le serveur
if(!server.isOpened())
    serveur.open();
    
// On stoppe le serveur
serveur.close();
```
Il est possible de redéfinir les méthodes:
- **clientConnected()** -> *lorsque le client vient de se connecter*
- **clientDisconnected()** -> *lorsque le client vient de se déconnecter*
- **accepterStarting()** -> *lorsque le service qui accepte les connexions entrantes est en train de démarrer*
- **accepterStarted()** -> *lorsque le service qui accepte les connexions entrantes vient de démarrer*
- **processorStarting()** -> *lorsque le service qui gère les clients connectés est en train de démarrer*
- **processorStarted()** -> *lorsque le service qui gère les clients connectés vient de démarrer*
- **customs()** -> *détermine si un client rentrant à le droit de se connecter (c'est un genre de contrôle de douane)*
- **accepted()** -> *lorsqu'une socket est acceptée*
- **removed()** -> *lorsqu'une socket est enlevée*

> Il est important de noter qu'il est préférable de créer son propre serveur en étendant la classe ```TCPServer```.

> Lors du fonctionnement du serveur, deux services sont en actions. Ils sont exécutés dans deux threads séparés du principal. Mais lorsque l'application souhaites s'arrêter, ces deux threads bloquent la fermeture du programme. Pour éviter cela il faut préciser lors de la création du serveur (en utilisant l'un de ses constructeurs) que les services sont des ```daemon```.

#### Client TCP
```java
// Crée un client tcp
TCPClient client = new TCPClient(new InetSocketAddress("192.168.1.1", 80)) {
    protected Connection newConnection(){
        // Lorsque le client est en passe d'être connecté au serveur,  alors cette méthode est appelée automatiquement. 
        // Son rôle et de créer la connexion.
        return (voir section ci dessus: Connection)
    }
};

// Connecte le client au serveur
if(!client.isConnected())
    client.connect();
    
// Déconnecte le client du serveur
client.disconnect();
```
Il est possible de redéfinir les méthodes:
- **clientConnected()** -> *lorsque le client vient de se connecter*
- **clientDisconnected()** -> *lorsque le client vient de se déconnecter*
- **accepted()** -> *lorsque la socket du client est acceptée*
- **removed()** -> *lorsque la socket du client est enlevée*

#### Recevoir des données
Pour recevoir des données, c'est très simple, il n'y a plus rien d'autre à faire puisque la méthode ```read()``` de l'objet ```Connection``` à déjà été redéfinie. À chaque byte reçu cette méthode est appelée.

#### Envoyer des données
Pour envoyer des données, il suffit d'utiliser une des 4 utilisations possibles d'un objet ```Connection```:
- **write(String)** -> *pour envoyer une chaîne de caractères*
- **write(int)** -> *pour envoyer un byte*
- **write(byte[])** -> *pour envoyer un tableau de bytes*
- **write(InputStream)** -> *pour envoyer les bytes d'un flux entrant*

Pour envoyer les données, il suffit d'utiliser la méthode ```flush()```.
> Lorsque beaucoup de données sont à envoyer, la méthode ```flush()``` est utilisée automatique et régulièrement. Pensez seulement à la fin des données de l'utiliser une dernière fois pour vous garantir que toutes les données sont envoyées.

#### ReasonDisconnection
Lorsqu'un client est déconnecté la méthode ```clientDisconnected()``` est appelée automatiquement du côté du client et/ou du serveur. En paramètre est transmit la raison de la déconnexion.

### UDP
L'utilisation de la classe ```UDP``` est très simple d'utilisation. Il n'y a pas de notion de client/serveur. Il suffit simplement d'envoyer des données à un destinataire (mais c'est sans garantie de réception). Le destinataire doit être à l'écoute de données entrantes, sinon celui perd les données envoyées. La plupart du temps, l'utilisation de ce protocol se fait conjointement avec le protocol tcp.

#### Envoi des données
```java
// Crée un objet UDP qui envoie des données vers un destinataire qui écoute le port 80
UDP udp = new UDP(new InetSocketAddress("192.168.1.2", 80));

// Affiche le type d'objet UDP
System.out.println(udp.getType()); // Affiche "SENDER"

// Prépare l'envoie de données vers le destinataire 192.168.1.2
udp.write("Bonjour".getBytes());

// Envoie les données tamponnées
udp.flush();

// Ferme la connexion UDP
udp.close();
```

Pour envoyer les données, il suffit d'utiliser la méthode ```flush()```.
> Lorsque beaucoup de données sont à envoyer, la méthode ```flush()``` est utilisée automatique et régulièrement. Pensez seulement à la fin des données de l'utiliser une dernière fois pour vous garantir que toutes les données sont envoyées.

#### Recevoir des données
```java
// Crée un objet UDP qui écoute le port 80
UDP udp = new UDP(80);

// Affiche le type d'objet UDP
System.out.println(udp.getType()); // Affiche "RECEIVER"

// Lit un tableau de bytes. La méthode read() est bloquante
byte[] datas = udp.read();
    
// Affiche les données reçues. Affiche "Bonjour"
System.out.println(new String(datas));

// Ferme la connexion UDP
udp.close();
```

# **Licence**
Le projet est sous licence "GNU General Public License v3.0"

## Accès au projet GitHub => [ici](https://github.com/josephbriguet01/Magnetar "Accès au projet Git Magnetar")