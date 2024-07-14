package heart_beat;

import java.io.IOException;
import java.util.ArrayList;

class WorkerBeatHandle extends Thread {
    private final StreamSocket node;
    private final int SLEEP_TIME = 1000;  // Intervalo entre heartbeats en milisegundos
    private final int TIMEOUT = 5000;  // Tiempo límite para recibir respuesta en milisegundos
    private final int id;
    private static ArrayList<Boolean> on;

    public WorkerBeatHandle(StreamSocket node, int id, ArrayList<Boolean> on) {
        this.node = node;
        this.id = id;
        this.on = on;
    }

    @Override
    public void run() {
        try {
            while (true) {
                node.send("Sending heartbeat from the server...");
                StringBuilder respuesta = waitResponse(TIMEOUT);
                if (respuesta == null) {
                    
                    System.out.println("Nodo " + id + " no responde. Marcado como muerto.");
                    on.set(id, false);
                    break; // Salir del bucle y terminar el hilo
                } else {
                    System.out.println("Node " + id + ": " + respuesta.toString());
                }
                Thread.sleep(SLEEP_TIME); // Intervalo entre heartbeats
            }
        } catch (IOException e) {
            System.out.println("Error de entrada/salida en el HeartBeat: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();  // Restablecer el estado de interrupción
            System.out.println("HeartBeat interrumpido.");
        } finally {
            try {
                node.close(); // Cerrar el socket al terminar
            } catch (IOException e) {
                System.out.println("Error cerrando el socket: " + e.getMessage());
            }
            System.out.println("WorkerBeat finalizado.");
        }
    }

    private StringBuilder waitResponse(int timeout) {
        try {
            node.socket.setSoTimeout(timeout); // Establecer timeout en el socket
            StringBuilder respuesta = node.receive();
            return respuesta;
        } catch (IOException e) {
            return null; // Timeout o error en la recepción
        }
    }
}
