package br.edu.ucsal.sergiolj.criptexclientjfx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class WelcomeController {
    @FXML
    public Label lb_welcome;
    @FXML
    public Button btn_start;

    @FXML
    public void initialize(){
        btn_start.setOnAction((event)->
                Navigation.loadGameView());
    }
}
