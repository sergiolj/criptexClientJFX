package controller;

import service.PlayerManagerRMI;
import service.WordPanel;
import shared.dto.GuessResponseDTO;
import util.KeyboardBuilder;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

import javafx.scene.layout.VBox;

import java.rmi.RemoteException;


public class GameController {

    @FXML
    public VBox vb_main;
    @FXML
    public GridPane gp_attempts;
    public VBox vb_keyboard;

    private PlayerManagerRMI playerManager;
    private final KeyboardBuilder keyboard = new KeyboardBuilder();
    private final WordPanel wordPanel = new WordPanel();

    public void setPlayer(PlayerManagerRMI playerManager){
        this.playerManager = playerManager;

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
            String guess = wordPanel.command("ENTER");
            System.out.println("Palavra retornada");
            if(guess != null){
                try{
                    System.out.println("Criando response");
                    GuessResponseDTO response =  playerManager.getPlayer().verifyGuess(guess);
                    System.out.println("Formatando resposta no Painel");
                    wordPanel.formatResponse(response);
                }catch (RemoteException e){
                    System.out.println("Erro ao enviar palpite." + e.getMessage());
                }
            }
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
