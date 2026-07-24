package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import view.manager.Navigation;

import rmi.network.GameSession;
import util.AlertWindow;

import java.rmi.RemoteException;

public class GameScoreController {
    @FXML
    public GridPane gp_score;
    @FXML
    public Button btn_new_game;

    @FXML
    public void initialize(){
        btn_new_game.setOnAction(event -> {
            try{
                GameSession gameSession = GameSession.getInstance();
                gameSession.disconnect();

                Navigation.loadWelcomeView();
            }catch (RemoteException e){
                AlertWindow.showError("Erro ao desconectar jogador",
                        "Falha ao tentar se comunicar com o servidor: " + e.getMessage(),
                        event);
            }

        });

    }
}
