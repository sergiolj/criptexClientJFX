package controller;


import javafx.scene.control.MenuBar;
import model.Player;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import view.manager.Navigation;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import shared.config.Config;
import rmi.network.GameSession;
import util.AlertWindow;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.util.Objects;

/**
 * Controller da View de início do app com a apresentação das regras e o botão de início do jogo.
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
    public MenuBar mb_main;

    @FXML
    public void initialize() {

        Image logo = new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream("/img/criptex_medium.png")));
        img_logo.setImage(logo);
        img_logo.setFitWidth(240);

        txf_username.getStyleClass().add("text_field");
        txf_username.setFocusTraversable(false);
        txf_username.setMaxWidth(200);

        btn_start.getStyleClass().add("welcome");
        btn_start.setMinWidth(120);
        btn_start.setMaxHeight(30);

        btn_start.setOnAction((event) -> {
            String username = txf_username.getText().isBlank() ?
                    "ANONYMOUS" : txf_username.getText().toUpperCase();

            Player player = new Player(username);

            try{
                GameSession gameSession = GameSession.getInstance();
                gameSession.connect(player);
                Navigation.loadGameView(gameSession);
            }catch (ConnectException e){
                AlertWindow.showError("Conexão recusada",
                        "Não foi possível conectar com o servidor em " + Config.IP_ADDRESS,
                        btn_start);
            }catch (RemoteException e){
                AlertWindow.showError("Erro de comunicação",
                        "Falha ao tentar se comunicar com o servidor: " + e.getMessage(),
                        btn_start);
            }
        });
    }
}
