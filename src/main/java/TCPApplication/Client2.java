package TCPApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client2 {

    private Socket socket;

    private PrintWriter out;
    private BufferedReader in;

    private Scanner scanner = new Scanner(System.in);
    private boolean isEnd = false;

    public Client2(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void start() throws IOException, InterruptedException {
        Thread tread = new Thread(() -> {
            String message;
            while (!this.isEnd){
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
        tread.join();
        stop();

    }
    private boolean managerActions() throws IOException {

        if (scanner.hasNextLine()){
            String yourAnswer = "Client2: " + scanner.nextLine();
            out.println(yourAnswer);
            return yourAnswer.equals("Client2: Good bye");
        }
        return false;
    }

    public void stop() throws IOException {
        in.close();
        out.close();
        socket.close();
        scanner.close();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Client2 Client2 = new Client2("127.0.0.1", 5000);
        Client2.start();
    }
}
