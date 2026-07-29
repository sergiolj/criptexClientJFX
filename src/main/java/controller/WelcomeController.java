package controller;


import javafx.event.ActionEvent;
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

import java.io.IOException;
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
    public ImageView img_logo, img_reload, img_settings, img_information;
    @FXML
    public MenuBar mb_main;


    @FXML
    public void initialize() {
        Image logo = new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream("/img/criptex_medium.png")));

        img_logo.setImage(logo);
        img_logo.setFitWidth(280);

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
                AlertWindow.showError("Conexão recusada - Connection Exception",
                        "Ocorreu um problema ao conectar ao servidor " + Config.getInstance().getServerName() +
                                " no domínio " + Config.getInstance().getIpAddress()+
                        " | " + Config.getInstance().getServerPort(),
                        btn_start);
            }catch (RemoteException e){
                AlertWindow.showError("Erro de comunicação - Remote Exception",
                        "Não foi possível estabelecer conexão com o servidor " + Config.getInstance().getServerName() +
                                " no domínio " + Config.getInstance().getIpAddress()+
                                " | " + Config.getInstance().getServerPort() +
                                "\nVerifique o arquivo conf.cfg",
                        btn_start);
            }
        });
    }
    private void loadIcons(){
        Image reload = new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream("/img/reload_solid.png")));
        Image settings = new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream("/img/settings_solid.png")));
        Image info = new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream("/img/info_solid.png")));

        img_reload.setImage(reload);
        img_reload.setFitWidth(30);
        img_information.setImage(info);
        img_information.setFitWidth(30);
        img_settings.setImage(settings);
        img_settings.setFitWidth(30);
    }

    public void exitApp(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void configServer(ActionEvent actionEvent) {
        AlertWindow.showConfirmationWithoutNode("Configuração do Servidor RMI",
                "Server Name: " + Config.getInstance().getServerName() +
                        "\nServidor Ip: " + Config.getInstance().getIpAddress() +
                        "\nPorta: " + Config.getInstance().getServerPort());

    }
}
