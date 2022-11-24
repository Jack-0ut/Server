import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Class that represent single client
 **/
public class ClientThread implements Runnable {
    Socket clientSocket;
    ChatServer chatServer;
    int numberClient;

    public ClientThread(Socket clientSocket, ChatServer chatServer, int numberClient) {
        this.clientSocket = clientSocket;
        this.chatServer = chatServer;
        this.numberClient = numberClient;
    }

    @Override
    public void run() {
        // Set reader to read from client socket
        BufferedReader in;
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException("Can't get the inputStream from clientSocket");
        }

        // Display that client connected and send him  his number
        System.out.println("Client " + numberClient + " connected!");
        try {
            new PrintWriter(clientSocket.getOutputStream(), true).println("Client # " + numberClient);
        } catch (IOException e) {
            throw new RuntimeException("Can't print client number");
        }

        String clientMessage;
        while (true) {
            try {
                clientMessage = in.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (!"exit".equals(clientMessage)) {
                System.out.println("Client #" + numberClient + ": " + clientMessage);
                chatServer.sendMessageForAllClient(numberClient, clientMessage);
            } else {
                // close bufferReader
                try {
                    in.close();
                } catch (IOException e) {
                    throw new RuntimeException("Can't close BufferReader");
                }
                // close socket
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
        }

    }
}
