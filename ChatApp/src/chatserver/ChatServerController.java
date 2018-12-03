/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

/**
 *
 * @author cyoun
 */
public class ChatServerController implements Initializable 
{
    
    private final int PORT = 3371;
    
    @FXML private TextField userInput;
    @FXML private TextArea showMsg; 
    private String fullMsg;
    
    @FXML
    private void handleButtonAction(ActionEvent event)
    {
       System.out.println("You clicked server!");
       
       fullMsg = fullMsg + userInput.getText();
       showMsg.appendText (userInput.getText() + "\n");
       userInput.setText("");
       
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        // TODO       
        
        //FIXME
        //need to write method to include multiple client
                
        new Thread( () -> 
        {
        
            try
            {             
                //create a server socket
                ServerSocket serverSocket = new ServerSocket(PORT);
//                Platform.runLater(()-> 
//                showMsg.appendText("Server started at " + new Date() + '\n'));
                
                //Listen for a connection request
                Socket socket = serverSocket.accept();
                
                //create data input and output streams                
                DataInputStream inputFromClient = new DataInputStream(
                                                    socket.getInputStream());
                DataOutputStream outputToClient = new DataOutputStream(
                                                      socket.getOutputStream());
                
                //send msg quicker without waiting for buffer to be full
                socket.setTcpNoDelay(true);
                
                while (true)
                {
                    //receive text msg from client
                    String receiveMsg = inputFromClient.readUTF();
                    
                    //send text to the client
                    //FIXME: needs to be sent on btn clicked
                    String sendMsg = userInput.getText();
                    outputToClient.writeUTF(sendMsg);
                    
                    Platform.runLater(() ->
                    {
                        showMsg.appendText("Friend: " + receiveMsg + '\n');
                        showMsg.appendText("You: " + sendMsg + '\n');
                    });
                }
            } catch (IOException ex)
            {
                ex.printStackTrace();
            }
            
        }).start();
    }    
    
}
