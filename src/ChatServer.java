import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.TreeMap;
/**
 * Class that initialize server
 * Add new clients to mapClient
 **/
public class ChatServer implements Runnable{
    private Map<Integer, Socket> mapClient = new TreeMap<>();
    @Override
    public void run() {
        ServerSocket server = null;
        try {
            server = new ServerSocket(8887);
        } catch (IOException e) {
            throw new RuntimeException("Error!\nCan't open server!");
        }
        System.out.println("Server started.Wait for client to connect!");
        int numberClient = 1;
        Socket client;

        while (true){
            try {
                client = server.accept();
                Thread clientThread = new Thread(new ClientThread(client,this,numberClient));
                clientThread.setDaemon(true);
                clientThread.start();
                mapClient.put(numberClient,client);
                numberClient++;
            } catch (IOException e) {
                break;
                // new RuntimeException("Can't connect client to server!");

            }
        }


    }

    /**
     * Method that gets client number and message
     * and send its information for every client
     * in out system
     **/
    public void sendMessageForAllClient(Integer numberClient,String clientMessage) {
        for (int i = 1;i <= mapClient.size();i++){
            if(numberClient != i){
                System.out.println("Sending message for client # " + i + "\n");
                BufferedWriter sendMessage;
                try{
                    // set BufferWriter to write into outputStream
                    sendMessage = new BufferedWriter(new OutputStreamWriter(mapClient.get(i).getOutputStream()));
                }
                catch(IOException e){
                    throw  new RuntimeException(e);
                }

                try{
                    sendMessage.write("Client # " + numberClient + ":" + clientMessage + "\nEnter message:");
                    sendMessage.flush();
                } catch (IOException e) {
                    throw new RuntimeException("Got an error.Can't write message in buffer!");
                }
            }
        }
    }
}
