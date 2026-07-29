package controller;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.util.Duration;
import shared.remote.Ranking;
import ui.component.Animation;
import ui.component.Stopwatch;
import util.AlertWindow;
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
import java.util.List;

/**
 * Controller da View principal do jogo com a lista de palavras. Essa controller coordena a comunicação entre os
 * objetos GameSession, que controla a sessão de jogo e a conexão com o servidor RMI e o objeto WordPanel que controla a
 * lista de palavras.
 *
 * @author Sérgio Lopes Júnior
 *
 * @version 1.0
 */
public class GameController {

    @FXML
    public VBox vb_main;
    @FXML
    public GridPane gp_word_panel;
    public VBox vb_keyboard;
    @FXML
    public Label lb_invalid_word, lbl_stopwatch;
    @FXML
    public MenuBar mb_main;

    private GameSession gameSession;
    private final KeyboardBuilder keyboard = new KeyboardBuilder();
    private final WordPanel wordPanel = new WordPanel();
    private Stopwatch stopwatch;
    private PauseTransition pauseDuration;
    private List<Ranking> rankingList;

    public void setGameSession(GameSession gameSession){
        this.gameSession = gameSession;
    }

    @FXML
    public void initialize(){
        stopwatch = new Stopwatch(lbl_stopwatch);
        stopwatch.start();
        lbl_stopwatch.setMinHeight(10);
        lbl_stopwatch.setMinWidth(70);

        gp_word_panel.getChildren().add((wordPanel.createWordPanel()));

        keyboard.setOnKeyPress(this::processCommand);

        vb_keyboard.getChildren().add(keyboard.createVirtualKeyboard());
        Platform.runLater(()->
                vb_keyboard.getScene().setOnKeyPressed(this::linkRealKeyboard));
    }

    private void processCommand(String command) {
        if (command.equals("ENTER")) {
            if (gameSession.noAttemptsLeft() || gameSession.isWordMatch()) {
                if(pauseDuration !=null && pauseDuration.getStatus() == javafx.animation.Animation.Status.RUNNING){
                    pauseDuration.stop();
                    waitToDisconnect(Duration.ZERO);
                }
            }
            String guess = wordPanel.command("ENTER");
            System.out.println("Palavra retornada " + guess);
            if (guess != null) {
                try {
                    System.out.println("Criando response");
                    GuessResponse response = gameSession.verifyGuess(guess);
                    if (response != null) {
                        if(response.isWordMatch() || gameSession.noAttemptsLeft()){
                            stopwatch.stop();
                            waitToDisconnect(Duration.seconds(5));
                        }
                        System.out.println("Formatando resposta no Painel");
                        wordPanel.formatResponse(response);
                    } else {
                        System.out.println("Palavra não reconhecida.");
                        wordPanel.rollBack();
                        lbl_stopwatch.setVisible(false);
                        lb_invalid_word.setVisible(true);
                        Animation.blink(lb_invalid_word);
                    }
                } catch (RemoteException e) {
                    System.out.println("Erro ao enviar palpite." + e.getMessage());
                }
            }
        } else {
            lb_invalid_word.setVisible(false);
            lbl_stopwatch.setVisible(true);
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

    private void disconnect() {
        try{
            GameSession gameSession = GameSession.getInstance();
            this.rankingList = gameSession.getRanking();
            gameSession.disconnect();

        }catch (RemoteException e){
            AlertWindow.showErrorWithoutNode("Erro ao desconectar jogador",
                    "Falha ao tentar se comunicar com o servidor: " + e.getMessage());
        }
    }

    private void waitToDisconnect(Duration seconds) {
        this.pauseDuration = new PauseTransition(seconds);
        pauseDuration.setOnFinished(event -> {
            disconnect();
            Navigation.loadGameScore(this.rankingList);
        });
        pauseDuration.play();
    }

    public void exitApp(ActionEvent actionEvent) {
        disconnect();
        System.exit(0);
    }

    public void reload(ActionEvent actionEvent) {
        disconnect();
        Navigation.loadWelcomeView();
    }
}
