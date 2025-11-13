package chat;

import java.net.*;
import java.util.concurrent.LinkedBlockingQueue;

public class Sender extends Thread {
    private final MulticastSocket socket;
    private final InetAddress group;
    private final int port;
    private final LinkedBlockingQueue<String> queue;
    private volatile boolean running = true;

    public Sender(MulticastSocket socket, InetAddress group, int port, LinkedBlockingQueue<String> queue) {
        this.socket = socket;
        this.group = group;
        this.port = port;
        this.queue = queue;
    }

    public void shutdown() {
        running = false;
        this.interrupt();
    }

    @Override
    public void run() {
        while(running) {
            try {
                String msg = queue.take();
                byte[] buf = msg.getBytes("UTF-8");
                DatagramPacket packet = new DatagramPacket(buf, buf.length, group, port);
                socket.send(packet);
            } catch(InterruptedException e) {
                // thread interrupted, exit loop
                break;
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
