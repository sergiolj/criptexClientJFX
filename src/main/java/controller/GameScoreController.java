package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import service.Navigation;
import service.PlayerManagerRMI;

public class GameScoreController {
    @FXML
    public GridPane gp_score;
    @FXML
    public Button btn_new_game;

    @FXML
    public void initialize(){
        btn_new_game.setOnAction(event -> {
            PlayerManagerRMI playerManager = PlayerManagerRMI.getInstance();
            playerManager.disconnect();
            Navigation.loadWelcomeView();
        });

    }
}
