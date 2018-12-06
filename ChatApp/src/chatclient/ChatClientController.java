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
//              userInput.setText("");

                //read the message sent to this client
                String receiveMsg = fromServer.readUTF();
                showMsg.appendText(receiveMsg + "\n");
                userInput.setText("");
                
            }catch(IOException ex) {
                System.out.println("Could not send/recieve messages");
            }
        }).start();    
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO     
        
        try {
            Socket socket = new Socket("127.0.0.1", 3371);
            
            //obtaining input and output streams
            fromServer = new DataInputStream(socket.getInputStream());
            toServer = new DataOutputStream(socket.getOutputStream());         
            
        } catch (IOException ex) {
            showMsg.appendText("Could not connect to server!");
        }
            
    }   
    
}
