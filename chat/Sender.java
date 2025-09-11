package chat;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;

public class Sender extends Thread {
    private final MulticastSocket socket;
    private final InetAddress group;
    private final int port;
    private final BlockingQueue<String> queue;
    private volatile boolean running = true;

    public Sender(MulticastSocket socket, InetAddress group, int port, BlockingQueue<String> queue) {
        this.socket = socket;
        this.group = group;
        this.port = port;
        this.queue = queue;
    }

    public void run() {
        try {
            while (running) {
                String msg = queue.take();
                byte[] data = msg.getBytes(StandardCharsets.UTF_8);
                DatagramPacket packet = new DatagramPacket(data, data.length, group, port);
                socket.send(packet);
            }
        } catch (Exception ignored) {}
    }

    public void shutdown() {
        running = false;
        this.interrupt();
    }
}
