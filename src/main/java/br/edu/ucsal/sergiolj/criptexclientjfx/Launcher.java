package br.edu.ucsal.sergiolj.criptexclientjfx;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Launcher extends Application{
    @Override
    public void start(Stage stage) throws IOException {
        Navigation.setStage(stage);
        Navigation.loadWelcomeView();
    }

    public static void main(String[] args) {
        launch();
    }
}
