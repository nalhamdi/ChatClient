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
import javafx.scene.control.TextArea;

/**
 *
 * @author cyoun
 */
public class ChatClientController implements Initializable {

    //IO streams
    private DataInputStream fromServer = null;
    private DataOutputStream toServer = null;

    @FXML
    private TextField userInput;
    @FXML
    private TextArea showMsg;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked client!");
        showMsg.appendText(userInput.getText() + "\n");
        userInput.setText("");
    }
   
     @FXML
    void setOnAction(ActionEvent event) {

    }

    //FIXME
    //need to write method for showing message on textarea
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        userInput.setOnAction(e -> {
            try {
                String clientMsg = showMsg.getText();

                toServer.writeBytes(clientMsg);
                toServer.flush();

                String serverMsg = fromServer.readUTF();

                showMsg.appendText("ewrt" + clientMsg);
                showMsg.appendText("sdgs" + serverMsg);

            } catch (IOException ex) {
                System.err.println(ex);
            }
        });
        try {
            //create a socket to connect to the server
            Socket socket = new Socket("localhost", 3371);

            //create an inputStream to receive data from the server
            fromServer = new DataInputStream(socket.getInputStream());

            //create an outputStrean to send data to server
            toServer = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            showMsg.appendText(ex.toString() + '\n');
        }
    }

}
