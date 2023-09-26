package UDPApplication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;

public class UDPClient {
    private DatagramSocket datagramSocket;
    private Scanner scanner = new Scanner(System.in);

    private byte[] buffer = new byte[256];

    private int sendTo;
    boolean isEnd = false;

    public UDPClient(int port, int sendTo){
        this.sendTo = sendTo;
        try {
            datagramSocket = new DatagramSocket(port);
        }catch (SocketException socketException){
            System.out.println("Не удалось создать сокет");
        }
    }

    public void start() throws InterruptedException {

        Thread thread = new Thread(()->{
            DatagramPacket packet;
            while (!this.isEnd) {
                buffer = new byte[256];
                packet = new DatagramPacket(buffer, buffer.length);
                try {
                    datagramSocket.receive(packet);
                    var answer = packet.getData();
                    StringBuilder stringBuffer = new StringBuilder();
                    for (byte character:
                         answer) {
                        if (character != 0) stringBuffer.append((char)character);
                    }
                    System.out.println("Companion: " + stringBuffer);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        thread.start();
        do {
            isEnd = manageActions();
        }while (!isEnd);
        thread.join();
        stop();
    }


    private boolean manageActions() throws InterruptedException {

        String message;
        if ((message = scanner.nextLine()) != null){
            message = message;
            DatagramPacket packet;
            try {
                packet = new DatagramPacket(message.getBytes(StandardCharsets.UTF_8),
                        0, message.getBytes(StandardCharsets.UTF_8).length, InetAddress.getByName("localhost"), sendTo);
                datagramSocket.send(packet);
            } catch (IOException ignored){}
            return "good bye".equals(message);
        }
        return false;
    }


    private void stop(){
        scanner.close();
        datagramSocket.close();
    }

    public static void main(String[] args) throws InterruptedException {
        var udpServer = new UDPClient(5001, 5000);
        udpServer.start();
    }
}

