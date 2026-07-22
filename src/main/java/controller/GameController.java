package controller;

import client.Player;
import service.WordPanel;
import util.KeyboardBuilder;
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
    public GridPane gp_attempts;
    public VBox vb_keyboard;

    private Player player;
    private final KeyboardBuilder keyboard = new KeyboardBuilder();
    private final WordPanel wordPanel = new WordPanel();

    public void setPlayer(Player player){
        this.player = player;

    }

    @FXML
    public void initialize(){
        gp_attempts.getChildren().add((wordPanel.createWordPanel()));

        keyboard.setOnKeyPress(comando -> wordPanel.command(comando));

        vb_keyboard.getChildren().add(keyboard.createVirtualKeyboard());
        Platform.runLater(()->
                vb_keyboard.getScene().setOnKeyPressed(this::linkRealKeyboard));
    }

    private void linkRealKeyboard(KeyEvent event){
        if (event.getCode() == KeyCode.ENTER) {
            wordPanel.command("ENTER");
        }
        else if (event.getCode() == KeyCode.BACK_SPACE) {
            wordPanel.command("DELETE");
        }
        else {
            String keySelected = event.getText().toUpperCase();
            if (keySelected.matches("[A-Z]")) {
                wordPanel.command(keySelected);
            }
        }
    }


}
