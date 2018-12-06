/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author cyoun
 */




public class ChatServer {   
    
    //ArrayList to store active clients
    static ArrayList<HandleAClient> clients = new ArrayList<>();
    
    //counter for clients
    static int clientNo = 0; 
    
    public static void main(String[] args) throws IOException {
        
        //server is listening on port 3371
        ServerSocket serverSocket = new ServerSocket(3371);
        
        Socket socket;
        
        //running infinite looop for getting client request
        while(true) {
            
            //accept the incoming request
            socket = serverSocket.accept();
            
            System.out.println("New client request was received: " + socket);
            
            //obtain input and output streams
            DataInputStream fromServer = new DataInputStream
                                             (socket.getInputStream());
            DataOutputStream toServer = new DataOutputStream
                                             (socket.getOutputStream());
            
            //create a handler object for handling this request
            HandleAClient clientHandler = new HandleAClient(socket, 
                                             "client " + clientNo, 
                                             fromServer, toServer);
            
            //create a new thread with this object
            Thread thread = new Thread(clientHandler);
            
            System.out.println("Adding this client to active client list");
            
            //add this client to active clients list
            clients.add(clientHandler);
            
            //start the thread
            thread.start();
            
            //increment the clientNo which is used for naming only
            clientNo++;
        }
    }
}

//clientHandler Class
class HandleAClient implements Runnable {
    
    private String name;
    final DataInputStream inputFromClient;
    final DataOutputStream outputToClient;
    Socket socket;
    
    //constructor
    public HandleAClient(Socket socket, String name, 
            DataInputStream inputFromClient, DataOutputStream outputToClient){
    
        this.socket = socket;
        this.name = name;
        this.inputFromClient = inputFromClient;
        this.outputToClient = outputToClient;
    }

    @Override
    public void run() {
        
        String receivedMsg;
        
        while (true){
            
            try{
            
                //receive the string 
                receivedMsg = inputFromClient.readUTF();
                
                System.out.println(receivedMsg);

                if(receivedMsg.equals("bye")){
                    this.socket.close();
                    break;
                }
                
                //break the string into message and recipient part
                /*
                    StringTokenizer st = new StringTokenizer(received, "#"); 
                    String MsgToSend = st.nextToken(); 
                    String recipient = st.nextToken();
                */                
                
                for(HandleAClient client : ChatServer.clients){
                    client.outputToClient.writeUTF(this.name + ": " 
                                                             + receivedMsg);
                }                
            } catch (IOException ex){
                System.out.println("Could not create data "
                                    + " stream with client! \n");
            }
        }
        
        try{
            //closing resources
            this.inputFromClient.close();
            this.outputToClient.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }    
}
