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
    
    //list to hold multiple client
    private ArrayList<HandleAClient> clients = new ArrayList<>();    
    private int amountOfClient = 0;
    
    @FXML private TextArea showMsg; 
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
              
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO       
        
        new Thread( () -> {
            
            try{

                ServerSocket serverSocket = new ServerSocket(PORT);
                showMsg.appendText("Server started at " + new Date() + '\n');

                while (true)
                {
                    Socket socket = serverSocket.accept();
                    showMsg.appendText("Worked \n");

                    //increase amountOfClient forEach new connection 
                    amountOfClient++;

                    new Thread(new HandleAClient(socket)).start();
                }

            } catch (IOException ex) {
                showMsg.appendText("Couldn't create ServerSocket! \n");
            }
        }).start();             
    }
    
    //create individual client socket
    class HandleAClient implements Runnable {

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
                    
                    outputToClient.writeUTF(receiveMsg);
                    showMsg.appendText(receiveMsg + "\n");                  
                }
                
            } catch (IOException ex){
                showMsg.appendText("Could not create data "
                                    + " stream with client! \n");
            }
            
        }    
    }  
}
