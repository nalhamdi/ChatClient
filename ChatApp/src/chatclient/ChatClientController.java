/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;

import chatserver.ChatServer;
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
        //initialize connection to server
        try {
            Socket socket = new Socket("127.0.0.1", 3371);
            fromServer = new DataInputStream(socket.getInputStream());
            toServer = new DataOutputStream(socket.getOutputStream());
            
        } catch (IOException ex) {
            showMsg.appendText("Could not connect to server!");
        }
        
        //send and receive messages from clients
        try {
            toServer.writeUTF(userInput.getText());
            toServer.flush();
            
            String receiveMessage = fromServer.readUTF();
            showMsg.appendText(receiveMessage);
            
        } catch (IOException ex) {
            showMsg.appendText("Could not send/receive messages");
        }
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO       
            
    }   
    
}
