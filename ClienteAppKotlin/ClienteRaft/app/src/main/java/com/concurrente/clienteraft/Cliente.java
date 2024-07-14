package com.concurrente.clienteraft;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cliente {
    String texto;

    public Cliente(String texto) {
        this.texto = texto;
    }

    public void run() {
        int PUERTO = 8084;
        String ip = "10.10.0.231";
        Socket socket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            socket = new Socket(ip, PUERTO);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Enviar mensaje al servidor
            out.println(texto);

            // Recibir respuesta del servidor
            StringBuilder mssg = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                mssg.append(line).append("\n");
            }

            System.out.println("Respuesta del servidor: ");
            System.out.println(mssg.toString());
            System.out.println("=== FINISH ====");
        } catch (UnknownHostException e) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, "Host desconocido: " + ip, e);
        } catch (IOException e) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, "Error de E/S al comunicarse con: " + ip, e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, "Error al cerrar recursos", e);
            }
        }
    }

    public static void main(String[] args) {
        Cliente cliente = new Cliente("Tu mensaje aquí");
        cliente.run();
    }
}

