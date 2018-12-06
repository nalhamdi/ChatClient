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
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

/**
 *
 * @author cyoun
 */
public class ChatServerController implements Initializable {
    
    private final int PORT = 3371; 
    
    //IO streams
    DataOutputStream toServer = null;
    DataInputStream fromServer = null;
    
    //list to hold multiple client
    private ArrayList<HandleAClient> clients = new ArrayList<>();    
    private int amountOfClient = 1;
    
    @FXML private TextArea showMsg; 
    @FXML private TextField userInput;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        
        try {
            Socket socket = new Socket("127.0.0.1", 3371);
            fromServer = new DataInputStream(socket.getInputStream());
            toServer = new DataOutputStream(socket.getOutputStream());
            
        } catch (IOException ex) {
            showMsg.appendText("Could not connect to server!");
        }
        
        try {
            toServer.writeUTF(userInput.getText());
            toServer.flush();
            
            String receiveMessage = fromServer.readUTF();
            showMsg.appendText(receiveMessage + "\n");
            userInput.setText("");
            
        } catch (IOException ex) {
            showMsg.appendText("Could not send/receive messages");
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO       
        
        new Thread( () -> {
            
            try{

                ServerSocket serverSocket = new ServerSocket(PORT);
                showMsg.appendText("Server started at " + new Date() + '\n');
             // System.out.println(serverSocket.getInetAddress().toString());

                while (true)
                {
                    Socket socket = serverSocket.accept();
                    System.out.println("Worked \n");
                    showMsg.appendText("Connect with client " + 
                                       amountOfClient + "\n");

                    //increase amountOfClient forEach new connection 
                    amountOfClient++;
                    
//                   new Thread(new HandleAClient(socket)).start();
                }

            } catch (IOException ex) {
                showMsg.appendText("Couldn't create ServerSocket! \n");
            }
        }).start();             
    }
    
    //create individual client socket
    /* class HandleAClient implements Runnable {

        private Socket socket;
        DataInputStream inputFromClient;
        DataOutputStream outputToClient;
        
        public HandleAClient(Socket socket){
            this.socket = socket;
        }
        
        
        @Override
        public void run() {
            
            try {
                inputFromClient = new DataInputStream(socket.getInputStream());
                outputToClient = new DataOutputStream(socket.getOutputStream());
                
                while(true) {
                    
                    String receiveMsg = inputFromClient.readUTF();
                    
//            clients = Collections.synchronizedList(new HandleAClient(socket));
                    
                    //clients object contains null since it's not been updated
                    for(int i = 0; i < clients.size(); i++)
                        clients.get(i).outputToClient.writeUTF(receiveMsg);
                    
                    outputToClient.writeUTF(receiveMsg);
                    showMsg.appendText(receiveMsg + "\n");                  
                }
                
            } catch (IOException ex){
                showMsg.appendText("Could not create data "
                                    + " stream with client! \n");
            }
            
        }    
    }  */
}
