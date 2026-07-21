package br.edu.ucsal.sergiolj.criptexclientjfx;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

import javafx.scene.layout.VBox;


public class GameController {

    @FXML
    public VBox vb_main;
    @FXML
    public GridPane gp_attempts, gp_keyboard;
    public VBox vb_keyboard;

    KeyboardBuilder kb = new KeyboardBuilder();
    WordPanel wp = new WordPanel();

    @FXML
    public void initialize(){
        gp_attempts.getChildren().add((wp.createWordPanel()));

        vb_keyboard.getChildren().add(kb.createVirtualKeyboard());
        kb.setOnKeyPress(this::updateWord);

        Platform.runLater(()->
                vb_keyboard.getScene().setOnKeyPressed(this::linkRealKeyboard));
    }

    private void updateWord(String label){
        wp.nextLetter(label);
    }

    private void linkRealKeyboard(KeyEvent event){
        if(event.getCode() == KeyCode.ENTER){
            kb.sendPressed();
            return;
        }
        if(event.getCode() == KeyCode.BACK_SPACE){
            kb.erasePressed();
            return;
        }
        String keyPressed = event.getText().toUpperCase();
        if(keyPressed.matches("[A-Z]")){
            char label = keyPressed.charAt(0);
            kb.keyPressed(String.valueOf(label));
        }
    }


}
