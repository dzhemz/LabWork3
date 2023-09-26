package UDPApplication;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ClientSide {
    private DatagramSocket socket;
    private InetAddress address;

    private Scanner scanner = new Scanner(System.in);

    private byte[] buf;

    public ClientSide() throws SocketException, UnknownHostException {
        socket = new DatagramSocket();
        address = InetAddress.getByName("localhost");
    }

    public void start() throws IOException {
        boolean isEnd = false;
        do {
            isEnd = manageActions();
        }while (!isEnd);
        stop();
    }

    private boolean manageActions() throws IOException {
        var line = scanner.nextLine();
        buf = ("Client: " + line).getBytes(StandardCharsets.UTF_8);
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 5000);

        socket.send(packet);

        packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        String received = new String(packet.getData());
        System.out.println(received);
        return received.equals("Server: Good bye") || line.equals("Good bye");
    }

    private void stop(){
        socket.close();
        scanner.close();
    }

    public static void main(String[] args) throws IOException {
        var client = new ClientSide();
        client.start();
    }
}
