package TCPApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;

    private PrintWriter out;
    private BufferedReader in;

    private Scanner scanner = new Scanner(System.in);

    public Client(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void start() throws IOException {
        boolean isEnd;
        System.out.println("Напишите первое сообщение");
        out.println("Client: " + scanner.nextLine());
        do {
            isEnd = managerActions();
        } while (isEnd);
        stop();
    }
    private boolean managerActions() throws IOException {

        var message = in.readLine();

        if (message != null) {
            System.out.println(message);
        }

        if (scanner.hasNextLine()){
            String yourAnswer = "Client: " + scanner.nextLine();
            out.println(yourAnswer);
            return !yourAnswer.equals("Client: Good bye");
        }
        return true;
    }

    public void stop() throws IOException {
        in.close();
        out.close();
        socket.close();
        scanner.close();
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client("127.0.0.1", 5000);
        client.start();
    }
}
