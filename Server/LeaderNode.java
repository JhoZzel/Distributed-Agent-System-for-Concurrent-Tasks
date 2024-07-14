/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package heart_beat;

import static heart_beat.WorkerNode.f;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jhozzel
 */

public class LeaderNode {
    private static final int MAX_CONNECTIONS = 3;
    private static final ArrayList<StreamSocket> sockets = new ArrayList<>();
    private static final ArrayList<StreamSocket> socketsBeats = new ArrayList<>();
    
    private static ArrayList<Boolean> activeNodes = new ArrayList<>();
    
    public static void main(String[] args) {
        try {
            int PUERTO = 8084;
            ServerSocket serverConnection = new ServerSocket(PUERTO);
            System.out.printf("Server running on port %d\n", PUERTO);
            
            System.out.println("=== WORKER NODES ===");
            for (int i = 0; i < MAX_CONNECTIONS; i++) {
                System.out.println("Waiting for connection...");
                StreamSocket socket = new StreamSocket(serverConnection.accept());
                System.out.printf("New connection from %s\n", socket.host());
                StreamSocket socketBeat = new StreamSocket(serverConnection.accept());
                System.out.println("Receiving the socket for the heartbeat....");
                System.out.printf("New connection from %s\n", socketBeat.host());
                System.out.println("Worker node: " + i + " has benn connected....");
                
                socketsBeats.add(socketBeat);
                activeNodes.add(true);
                sockets.add(socket);
            }
            System.out.println("All the workers nodes are connected... :D");
            for (int i = 0; i < MAX_CONNECTIONS; i++) {
                new WorkerBeatHandle(socketsBeats.get(i), i, activeNodes).start();
            }
            
            System.out.println("=== CLIENTS ====");
            while(true) {
                System.out.println("\nWaiting for client connection...");
                StreamSocket client = new StreamSocket(serverConnection.accept());
                System.out.println("States of each node: ");
                for (boolean b : activeNodes) {
                    System.out.println(b ? "ON" : "OFF"); 
                }
                System.out.printf("New connection from %s\n", client.host());
                System.out.println("Client node: " + 0 + " has benn connected....");

                ClientHandle ch = new ClientHandle(client, sockets, activeNodes);
                ch.start();
            }
            
            
        } catch (IOException ex) {
            Logger.getLogger(LeaderNode.class.getName()).log(Level.SEVERE, null, ex);
        }  
        
    }
}
