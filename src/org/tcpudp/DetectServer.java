/*
 * Copyright (C) BRIGUET Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Briguet, September 2018
 */
package org.tcpudp;



import network.Equipment;
import network.IPv4;
import network.ResultScan;
import network.Scanner;



/**
 * Cette classe permet de détecter les serveurs d'une catégorie sur le réseau LAN
 * @author Briguet
 * @version 1.0
 */
public class DetectServer {

    
    
    // <editor-fold defaultstate="collapsed" desc="CONSTRUCTOR">
    /**
     * Crée un objet DetectServer
     */
    public DetectServer() {
    }
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="METHODE PUBLIC">
    /**
     * Scanne le réseau et récupère le nom des serveurs ainsi que leur port d'écoute...
     * @param ipNetwork Correspond à l'adresse IP du réseau à scanner
     * @param portAuthServer Correspond au port de propagation. Il doit être différent du port du serveur
     * @return Retourne un tableau d'identifications de serveurs
     */
    public IdentificationServer[] scanNetwork(IPv4 ipNetwork, int portAuthServer){
        Scanner scanner = new Scanner();
        ResultScan rs = scanner.listLanHardware(ipNetwork, "/a");
        Equipment[] es = rs.getEquipments();
        
        java.util.List<IdentificationServer> lis = new java.util.ArrayList<>();
        
        synchronized(lis){
            int cpt = 0;
            for(int i=0;i<es.length;i++){
                Equipment e = es[i];
                boolean portIsAlive = portMagnetarIsOpen(e.getIp(), portAuthServer);
                if(portIsAlive){
                    cpt++;
                    new Thread(() -> {
                        Client c = new Client(new Identity(), e.getIp(), portAuthServer);
                        c.addIReceivedListener((Identity expeditor, Object obj) -> {
                            if(obj instanceof IdentificationServer){
                                IdentificationServer is = (IdentificationServer) obj;
                                is.setIpServer(e.getIp());
                                lis.add(is);
                                c.disconnect();
                            }
                        });
                        c.connect_to();
                    }).start();
                }
            }
            while(lis.size()<cpt){
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                    java.util.logging.Logger.getLogger(DetectServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
            }
        }
        java.util.Collections.sort(lis);
        IdentificationServer[] iss = new IdentificationServer[lis.size()];
        for(int i=0;i<lis.size();i++){
            iss[i] = lis.get(i);
        }
        return iss;
    }
    // </editor-fold>
    
    
    
    // <editor-fold defaultstate="collapsed" desc="METHODE PRIVATE">
    /**
     * Renvoie si le port Magnetar d'un équipement est ouvert
     * @param ipNetwork Correspond à l'adresse IP de l'équipement
     * @param portAuthServer Correspond au port à tester
     * @return Retourne true si le port Magnetar est ouvert
     */
    private boolean portMagnetarIsOpen(IPv4 ip, int port){
        try {
            try (java.net.Socket socket = new java.net.Socket()) {
                socket.connect(new java.net.InetSocketAddress(ip.toString(), port), 10);
            }
            return true;
        } catch (java.io.IOException ex) {
            return false;
        }
    }
    // </editor-fold>
    
    
    
}