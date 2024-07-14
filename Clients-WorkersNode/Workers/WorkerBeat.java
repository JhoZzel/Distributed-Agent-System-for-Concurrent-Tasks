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
class WorkerBeat extends Thread {
    private final StreamSocket server;
    //private final long SLEEP_TIME = 1000;  // Intervalo en milisegundos

    public WorkerBeat(StreamSocket server) {
        this.server = server;
    }

    @Override
    public void run() {
        try {
            //while (!Thread.currentThread().isInterrupted()) {  // Comprobar si el hilo no ha sido interrumpido
            while (true) {
                StringBuilder sb = server.receive();
                System.out.println(sb.toString());
                System.out.println("Sending the heartbeat to the server....");
                server.send("HeartBeat from the worker node!!\n");
                //Thread.sleep(SLEEP_TIME);  // Dormir el hilo durante el intervalo configurado
            }
        } catch (IOException e) {
            System.out.println("Error de entrada/salida en el HeartBeat: " + e.getMessage());
        /*} catch (InterruptedException e) {
            Thread.currentThread().interrupt();  // Restablecer el estado de interrupción
            System.out.println("HeartBeat interrumpido.");*/
        } finally {
            System.out.println("WorkerBeat finalizado.");
        }
    }
}