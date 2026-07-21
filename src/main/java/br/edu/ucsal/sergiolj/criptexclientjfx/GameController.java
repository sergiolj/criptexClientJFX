package br.edu.ucsal.sergiolj.criptexclientjfx;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

import javafx.scene.layout.VBox;


public class GameController {

    @FXML
    public VBox vb_main;
    @FXML
    public GridPane gp_attempts, gp_keyboard;
    public VBox vb_keyboard;

    @FXML
    public void initialize(){
        KeyboardBuilder kb = new KeyboardBuilder();
        vb_keyboard.getChildren().add(kb.createVirtualKeyboard());
    }



}
