package controller;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import service.Navigation;
import service.PlayerManagerRMI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import shared.config.Config;

import java.util.Objects;

/**
 *
 * @author Sérgio Lopes Júnior
 *
 * @version 1.0
 */
public class WelcomeController {

    @FXML
    public Button btn_start;
    @FXML
    public TextField txf_username;
    @FXML
    public ImageView img_logo;

    @FXML
    public void initialize() {

        Image logo = new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream("/img/criptex_medium.png")));
        img_logo.setImage(logo);
        img_logo.setFitWidth(370);

        btn_start.setOnAction((event) -> {
            String username = txf_username.getText().isBlank() ?
                    "Anonymous" : txf_username.getText();

            PlayerManagerRMI playerManager = PlayerManagerRMI.getInstance();
            playerManager.connect(username, Config.IP_ADDRESS, Config.SERVER_PORT);
            Navigation.loadGameView(playerManager);
        });
    }
}
