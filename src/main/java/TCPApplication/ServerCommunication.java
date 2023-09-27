package TCPApplication;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServerCommunication {
    private ServerSocket serverSocket;

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    private Scanner scanner = new Scanner(System.in);
    private boolean isEnd = false;


    public ServerCommunication(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        clientSocket = serverSocket.accept();
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public void start() throws IOException, InterruptedException {

        Thread tread = new Thread(() -> {
            String message;
            while (!this.isEnd) {
                try {
                    message = in.readLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (message != null) {
                    System.out.println(message);
                }
            }
        });
        tread.start();
        do {
            this.isEnd = managerActions();
        } while (!this.isEnd);
        stop();
        tread.join();
    }

    private boolean managerActions() throws IOException {
        /*
        var message = in.readLine();

        if (message != null) {
            System.out.println(message);
        }
        */

        if (scanner.hasNextLine()){
            String yourAnswer = "Server: " + scanner.nextLine();
            out.println(yourAnswer);
            return yourAnswer.equals("Server: Good bye");
        }
        return false;
    }

    public void stop() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
        scanner.close();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        ServerCommunication server = new ServerCommunication(5000);
        server.start();
    }
}
