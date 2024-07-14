package heart_beat;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cliente {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        try {
            int PUERTO = 8084;
            String ip = "10.10.0231";
            StreamSocket server = new StreamSocket(new Socket(ip, PUERTO));
            System.out.println("Conectado al servidor, texto mensajes:");
            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));

            String nombreArchivo = "/home/cleber/Descargas/finalv11/test_CS/src/main/java/heart_beat/archivo.txt";
            File archivo = new File(nombreArchivo);
            int cantLinea = 0;

            while (true) {
                StringBuilder mssg = new StringBuilder();
                try {
                    FileReader fr = new FileReader(archivo);
                    BufferedReader br = new BufferedReader(fr);
                    String linea;
                    while ((linea = br.readLine()) != null) {
                        cantLinea++;
                    }
                    System.out.println();
                    fr = new FileReader(archivo);
                    br = new BufferedReader(fr);
                    String and = ""+cantLinea;
                    mssg.append(and).append("\n");
                    while ((linea = br.readLine()) != null) {
                        mssg.append(linea).append('\n');
                    }
                    br.close();
                    fr.close();
                } catch (IOException e) {
                    System.err.println("Error al leer el archivo: " + e.getMessage());
                }
                System.out.print("Ingrese cuantas palabras quiere buscar: ");
                String nPalabras = consoleInput.readLine();
                mssg.append(nPalabras).append('\n');
                for(int i = 0; i < Integer.parseInt(nPalabras); i++){
                    System.out.print("Ingrese la palabras "+i+" que quiere buscar: ");
                    String line_ = in.nextLine();
                    mssg.append(line_).append('\n');
                }
                System.out.println(mssg);
                System.out.println("Sending data to the server");
                server.send(mssg);

                // Reading data from the server
                mssg = server.receive();
                System.out.println("Respuesta del servidor: ");
                System.out.println(mssg.toString());
                System.out.println("=== FINISH ====");
            }


        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}