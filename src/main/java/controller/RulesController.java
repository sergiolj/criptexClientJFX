package controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import view.manager.Navigation;

import java.awt.*;

public class RulesController {
    @FXML
    public Button btn_Ok;
    private Scene callbackScene;

    @FXML
    public void initialize(){
        btn_Ok.setMinWidth(120);
        btn_Ok.setMaxHeight(30);
        btn_modal();

    }

    public void setCallbackScene(Scene callbackScene) {
        this.callbackScene = callbackScene;
    }

    public void btn_modal(){
        btn_Ok.setOnAction(e -> {
            Stage stage = (Stage) btn_Ok.getScene().getWindow();
            stage.close();
        });
    }
    public void btn_callback(){
        btn_Ok.setOnAction(event -> {
            Navigation.getStage().setScene(callbackScene);
        });
    }

}
