package chatclient;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLDocumentController {
    
    private String chatbox;

    @FXML
    private Button button;

    @FXML
    private Label label;

    @FXML
    private TextArea chatArea;

    @FXML
    private TextField chatInput;
    
    private String messageBox;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        concatMessage(messageBox);
    }
    
    private void replaceId(String str){
        String str1 = str.replace("YOU" , "FRIEND");
        String str2 = str.replace("FRIEND", "YOU");
    }
    
    
    
    private void concatMessage(String message){
        message+= "\n YOU: "+ message;
    }

}
