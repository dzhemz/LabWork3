package UDPApplication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ServerSide {
    private DatagramSocket socket;
    private boolean running;
    private Scanner scanner = new Scanner(System.in);

    private byte[] buffer = new byte[256];

    public ServerSide(int port) throws SocketException {
        socket = new DatagramSocket(port);
    }

    public void start() throws IOException {
        boolean isEnd = false;
        do {
            isEnd = manageActions();
        }while(!isEnd);
        stop();
    }

    private boolean manageActions() throws IOException {
        DatagramPacket packet
                = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        InetAddress address = packet.getAddress();

        String received = new String(packet.getData(), 0, packet.getLength());

        System.out.println(received);

        if (received.equals("Client: Good bye")) return true;

        var yourMessage =  scanner.nextLine();
        if (yourMessage != null) {
            socket.send(new DatagramPacket(("Server: " + yourMessage).getBytes(StandardCharsets.UTF_8),
                    ("Server: " + yourMessage).length(), InetAddress.getByName("localhost"), 500));
        } else return false;

        return yourMessage.equals("Good bye");
    }

    private void stop(){
        socket.close();
        scanner.close();
    }

    public static void main(String[] args) throws IOException {
        var server = new ServerSide(5000);
        server.start();
    }
    
}
