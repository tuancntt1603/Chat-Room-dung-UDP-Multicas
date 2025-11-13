package chat;

import java.net.*;
import java.util.function.Consumer;

public class Receiver extends Thread {
    private final MulticastSocket socket;
    private final Consumer<String> callback;
    private volatile boolean running = true;

    public Receiver(MulticastSocket socket, Consumer<String> callback) {
        this.socket = socket;
        this.callback = callback;
    }

    public void shutdown() {
        running = false;
        this.interrupt();
    }

    @Override
    public void run() {
        byte[] buf = new byte[65536]; // max size ~64KB
        while(running) {
            try {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                String msg = new String(packet.getData(), 0, packet.getLength(), "UTF-8");
                callback.accept(msg);
            } catch(Exception e) {
                if(running) e.printStackTrace();
                break;
            }
        }
    }
}
