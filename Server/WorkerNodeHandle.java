/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package heart_beat;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


class WorkerNodeHandle extends Thread {
    private final StreamSocket socket;
    private final StringBuilder data;
    private StringBuilder result;

    public WorkerNodeHandle(StreamSocket socket, StringBuilder data) {
        this.socket = socket;
        this.data = data;
    }

    @Override
    public void run() {
        System.out.println("Sending the task to the worker node....");
        try {
            System.out.println("WorkNodeHandle : ");
            System.out.println(data.toString());
            socket.send(data);
        } catch (IOException ex) {
            Logger.getLogger(WorkerNodeHandle.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Receiving work from the worker node");
        try {
            result = socket.receive();
        } catch (IOException ex) {
            Logger.getLogger(WorkerNodeHandle.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(result.toString());

    }
    public StringBuilder getResult() {
        return result;
    }
}