package controller;

import javafx.scene.control.Label;
import ui.component.TimeCounter;
import view.manager.Navigation;
import ui.component.WordPanel;
import shared.dto.GuessResponse;
import rmi.network.GameSession;
import ui.component.KeyboardBuilder;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

import javafx.scene.layout.VBox;

import java.rmi.RemoteException;

/**
 *
 * @author Sérgio Lopes Júnior
 *
 * @version 1.0
 */
public class GameController {

    @FXML
    public VBox vb_main;
    @FXML
    public GridPane gp_attempts;
    public VBox vb_keyboard;
    @FXML
    public Label lb_invalid_word, lbl_stopwatch;

    private GameSession gameSession;
    private final KeyboardBuilder keyboard = new KeyboardBuilder();
    private final WordPanel wordPanel = new WordPanel();
    private TimeCounter stopwatch;

    public void setGameSession(GameSession gameSession){
        this.gameSession = gameSession;
    }

    @FXML
    public void initialize(){
        stopwatch = new TimeCounter(lbl_stopwatch);
        stopwatch.start();

        gp_attempts.getChildren().add((wordPanel.createWordPanel()));

        keyboard.setOnKeyPress(this::processCommand);

        vb_keyboard.getChildren().add(keyboard.createVirtualKeyboard());
        Platform.runLater(()->
                vb_keyboard.getScene().setOnKeyPressed(this::linkRealKeyboard));
    }

    private void processCommand(String command) {
        if (command.equals("ENTER")) {
            String guess = wordPanel.command("ENTER");
            System.out.println("Palavra retornada " + guess);
            if (gameSession.noAttemptsLeft()) {
                Navigation.loadGameScore();
            }
            if (guess != null) {
                try {
                    System.out.println("Criando response");
                    GuessResponse response = gameSession.verifyGuess(guess);
                    if (response != null) {
                        System.out.println("Formatando resposta no Painel");
                        wordPanel.formatResponse(response);
                    } else {
                        System.out.println("Palavra não reconhecida.");
                        wordPanel.rollBack();
                        lb_invalid_word.setVisible(true);
                    }
                } catch (RemoteException e) {
                    System.out.println("Erro ao enviar palpite." + e.getMessage());
                }
            }
        } else {
            lb_invalid_word.setVisible(false);
            wordPanel.command(command);
        }
    }

    private void linkRealKeyboard(KeyEvent event){
        if (event.getCode() == KeyCode.ENTER) {
                processCommand("ENTER");
        }
        else if (event.getCode() == KeyCode.BACK_SPACE) {
            processCommand("DELETE");
        }
        else {
            String keySelected = event.getText().toUpperCase();
            if (keySelected.matches("[A-Z]")) {
                processCommand(keySelected);
            }
        }
    }
}
