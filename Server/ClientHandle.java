/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package heart_beat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author jhozzel
 */

class ClientHandle extends Thread {
    private final StreamSocket client;
    private static final int MAX_CONNECTIONS = 3;
    private static ArrayList<StreamSocket> sockets;
    private static ArrayList<Boolean> activeNodes = new ArrayList<>();
    
    public ClientHandle(StreamSocket client, ArrayList<StreamSocket> sockets, ArrayList<Boolean> activeNodes) {
        this.client = client;
        this.sockets = sockets;
        this.activeNodes = activeNodes;
    }

    @Override
    public void run() {
        try (client) {
            while(true) {
                System.out.println("Reading data from the client...");
                    StringBuilder mssg = client.receive();
                    
                    Scanner sc = new Scanner(mssg.toString());
                    ArrayList<String> lines = new ArrayList<>();
                    String line =  sc.nextLine();
                    int n = Integer.parseInt(line);
                    for (int i = 0; i < n; i++) {
                        line = sc.nextLine();
                        lines.add(line);
                    }
                    
                    StringBuilder infoTarget = new StringBuilder();
                    line = sc.nextLine();
                    int nTargets = Integer.parseInt(line);
                    infoTarget.append(String.valueOf(nTargets)).append('\n');
                    for (int i = 0; i < nTargets; i++) {
                        line = sc.nextLine();
                        infoTarget.append(line).append('\n');
                    } 
                    
                    
                    ArrayList<Integer> socketsOn = new ArrayList<>();
                    for (int i = 0; i < MAX_CONNECTIONS; i++) {
                        socketsOn.add(i);
                    }
                    int totalActives = socketsOn.size();

                    StringBuilder [] parts = new StringBuilder[totalActives];
                    int len = (n + totalActives - 1)  / totalActives;
                    for (int i = 0; i < totalActives; i++) {
                        parts[i] = new StringBuilder();
                        int ini = i * len;
                        int fin = (i + 1) * len;
                        if (i == totalActives - 1) fin = n;
                        int nData = fin - ini;
                        parts[i].append(String.valueOf(nData)).append('\n');
                        for (int k = ini; k < fin; k++) {
                            parts[i].append(lines.get(k)).append('\n');
                        }
                        parts[i].append(infoTarget);
                    }
                    
                    // test
                    System.out.println("---- Testing");
                    for (int i = 0; i < totalActives; i++) {
                        System.out.println("Partition: " + i + ": ");
                        System.out.println(parts[i].toString());
                        System.out.println("");
                    }
                    
                    
                    System.out.println("Assigning taks to every thread....");
                    List<WorkerNodeHandle> handleConnections = new ArrayList<>();
                    for (int i = 0; i < totalActives; i++) {
                        int j = socketsOn.get(i);
                        WorkerNodeHandle hc = new WorkerNodeHandle(sockets.get(j), parts[i]);
                        hc.start();
                        handleConnections.add(hc);
                    }

                    System.out.println("Waiting for the works of each thread....");
                    for (WorkerNodeHandle hc : handleConnections) {
                        try {
                            hc.join();
                        } catch (InterruptedException e) {
                        }
                    }

                    StringBuilder ans = new StringBuilder();
                    System.out.println("Printing results: ");
                    for (WorkerNodeHandle hc : handleConnections) {
                        StringBuilder res = hc.getResult();
                        
                        System.out.println("=> Results:");
                        System.out.println(res);
                        String r ="We found this ocurrences...: \n";
                        ans.append(r).append(res);
                    }
                    
                    client.send(ans);
                    System.out.println("End....\n");
                
            }
        } catch (IOException e) {
        } finally {
            System.out.println("Client out!....");
        }
    }
}