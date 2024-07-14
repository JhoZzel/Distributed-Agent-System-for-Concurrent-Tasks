package heart_beat;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

class StreamSocket implements AutoCloseable {
    public Socket socket;
    private DataInputStream input;
    private DataOutputStream output;

    public StreamSocket(Socket socket) throws IOException {
        this.socket = socket;
        input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        output = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }

    public void send(StringBuilder sb) throws IOException {
        byte[] data = sb.toString().getBytes(StandardCharsets.UTF_8);
        output.writeInt(data.length);
        output.write(data);
        output.flush();
    }
    public void send(String s) throws IOException {
        StringBuilder sb = new StringBuilder(s);
        send(sb);
    }
    
    public StringBuilder receive() throws IOException {
        int length = input.readInt();
        byte[] data = new byte[length];
        input.readFully(data);
        return new StringBuilder(new String(data, StandardCharsets.UTF_8));
    }
    
    // ENVIAR LATIDO
    
    public String host() {
        return String.format("%s:%d", socket.getInetAddress(), socket.getPort());
    }

    @Override
    public void close() throws IOException {
        try {
            if (input != null) input.close();
        } finally {
            try {
                if (output != null) output.close();
            } finally {
                if (socket != null) socket.close();
            }
        }
    }
}