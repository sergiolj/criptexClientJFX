package criptex;

import view.manager.Navigation;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

/**
 *
 * @author Sérgio Lopes Júnior
 *
 * @version 1.0
 */
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
