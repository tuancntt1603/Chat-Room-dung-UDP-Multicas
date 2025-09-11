package chat;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

public class Receiver extends Thread {
    private final MulticastSocket socket;
    private final Consumer<String> callback;
    private volatile boolean running = true;

    public Receiver(MulticastSocket socket, Consumer<String> callback) {
        this.socket = socket;
        this.callback = callback;
    }

    public void run() {
        byte[] buf = new byte[8192];
        try {
            while (running) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                String msg = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);
                if(callback!=null) callback.accept(msg);
            }
        } catch (Exception ignored) {}
    }

    public void shutdown() {
        running = false;
        this.interrupt();
    }
}
