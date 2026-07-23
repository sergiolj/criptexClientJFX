package controller;

import service.Navigation;
import service.PlayerManagerRMI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import shared.config.Config;

public class WelcomeController {
    @FXML
    public Label lb_welcome;
    @FXML
    public Button btn_start;
    public TextField txf_username;

    @FXML
    public void initialize() {
        btn_start.setOnAction((event) -> {
            String username = txf_username.getText().isBlank() ?
                    "Anonymous" : txf_username.getText();

            PlayerManagerRMI playerManager = PlayerManagerRMI.getInstance();
            playerManager.connect(username, Config.IP_ADDRESS, Config.SERVER_PORT);
            Navigation.loadGameView(playerManager);
        });
    }
}
