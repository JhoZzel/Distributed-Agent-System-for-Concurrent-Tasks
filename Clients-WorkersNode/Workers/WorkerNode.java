/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package heart_beat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jhozzel
 */
public class WorkerNode {
    
    public static int f(String s) { // count the numbers of 'a'
        int cnt = 0;
        for (char c : s.toCharArray()) {
            if (c == 'a') cnt++;
        }
        return cnt;
    }
    
    public static void main(String[] args) {
        try {
            int PUERTO = 8084;
            String ip = "localhost";
            StreamSocket server = new StreamSocket(new Socket(ip, PUERTO));
            System.out.println("Connected to the leader node...");
            StreamSocket serverBeat = new StreamSocket(new Socket(ip, PUERTO));
            System.out.println("Creating the heartbeat channel to the server...");
            System.out.println("Starting the heartbeats...");
            
            new WorkerBeat(serverBeat).start();
            while (true) {
                System.out.println("Waiting for data...");
                StringBuilder mssg = server.receive();
                System.out.println("Received Data: ");
                
                
                System.out.println(mssg.toString());
                System.out.println("");
                
                // Process the data
                Scanner sc = new Scanner(mssg.toString());
                String line = sc.nextLine();
                int n = Integer.parseInt(line);
                ArrayList<String> a = new ArrayList<>();
                for (int i = 0; i < n; i++) {
                    line = sc.nextLine();
                    // Dividir la línea en palabras usando split()
                    String[] palabras = line.split("\\s+"); // Esto divide por espacios en blanco

                    // Agregar cada palabra al ArrayList
                    for (String palabra : palabras) {
                        a.add(palabra);
                    }
                }
                n = a.size();

                line = sc.nextLine();
                int k = Integer.parseInt(line);
                ArrayList<String> b = new ArrayList<>();
                for (int i = 0; i < k; i++) {
                    line = sc.nextLine();
                    
                    b.add(line);
                }
                int[] freq = new int [k];
                for (int i = 0; i < k; i++) {
                    freq[i] = 0;
                }
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < k; j++) {
                        if (b.get(j).equals(a.get(i))) {
                            freq[j]++;
                        }
                    }
                }
                
                StringBuilder ans = new StringBuilder();
                for (int i = 0; i < k; i++) {
                    line = String.valueOf(freq[i]);
                    ans.append(line).append('\n');
                }
                
                System.out.println("Proccesed Data: ");
                System.out.println(ans.toString());
                server.send(ans);
            }
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
