/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

/**
 *
 * @author cyoun
 */
public class ChatClientController implements Initializable {
    
    //IO streams
    DataOutputStream toServer = null;
    DataInputStream fromServer = null;
    String token;
    
    private int id;
    private static int clientNum = 1;
    
    private String consoleMsg;
    
    @FXML private TextField userInput;
    @FXML private TextArea showMsg;
    @FXML private Button sendButton;
        
    @FXML
    private void handleButtonAction(ActionEvent event) {        
        
        new Thread ( () -> {
            
            try {    
                String sendMsg = userInput.getText();
                
                //write on the output streams
                toServer.writeUTF(sendMsg); 

                userInput.setText("");
                
            }catch(IOException ex) {
                System.out.println("Could not send/recieve messages");
            }
        }).start();    
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO     
        new ClientThread().start();
        id = clientNum - 1;
        clientNum = clientNum+1;
    }    
    
    
    class ClientThread extends Thread {
        
 
        @Override
        public void run() {
            try {
            Socket socket = new Socket("127.0.0.1", 3371);
            
            token  = Integer.toString(socket.getLocalPort());
            System.out.println(socket.getLocalPort());
            
            //obtaining input and output streams
            fromServer = new DataInputStream(socket.getInputStream());
            toServer = new DataOutputStream(socket.getOutputStream()); 
            
            while (true) {
                String message = fromServer.readUTF();
                showMsg.appendText(determineString(message) + "\n");;
                System.out.println(consoleMsg);
            }
            
            } catch (IOException ex) {
                showMsg.appendText("Could not connect to server!");
            } 
        }
        
        private String determineString(String str){
            String message = "";
            String [] sentStr = str.split(":");
            String tokenizer = sentStr[0];
            
            consoleMsg = "client "+id+sentStr[1];
            
            if(token.equals(tokenizer)){
                message = str.replace(tokenizer, "You");
            }else{
                message = str.replace(tokenizer, "Friend");
            }
            return message;
        }
 
    }    
    
}  
    
    
    

