package service;

import controller.GameController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/**
 *
 * @author Sérgio Lopes Júnior
 *
 * @version 1.0
 */
public class Navigation {
    private static Stage stage;

    public static void setStage(Stage primaryStage){
        stage = primaryStage;
    }

    private static void setupScene(Parent root, String title, double width, double height){
        Scene scene = new Scene(root, width, height);
        stage.setScene(scene);
        scene.getStylesheets().add(Objects.requireNonNull(
                Navigation.class.getResource("/styles/styles.css")).toExternalForm());
        stage.setTitle(title);
        stage.centerOnScreen();
        stage.sizeToScene();
        stage.setResizable(false);
    }

    private static FXMLLoader loadFXML(String fxml) {
        if(stage == null){
            throw new IllegalStateException("Stage principal não foi configurado corretamente no SceneManager. Verifique" +
                    "o uso do SceneManager.setStage(Stage stage) no MainApp. ");
        }
        URL fxmlURL = Navigation.class.getResource(fxml);
        if(fxmlURL == null){
            throw new IllegalArgumentException("Arquivo Fxml não encontrado em: " + fxml);
        }
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Navigation.class.getResource(fxml));
        return loader;
    }

    public static void loadWelcomeView(){
        String fxml = "/view/welcome-view.fxml";
        String title = "Criptex - Descubra a palavra secreta.";
        double width = 300;
        double height = 450;
        try{
            FXMLLoader loader = loadFXML(fxml);

            Parent root = loader.load();

            setupScene(root,title,width,height);

            root.setStyle("-fx-background-color: " +
                    "linear-gradient(from 0% 0% to 100% 100%, #0a0f1e 0%, #3c3f4a 50%, #000000 100%)");
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loadGameView(PlayerManagerRMI playerManager){
        String fxml = "/view/game-view.fxml";
        String title = "Criptex";
        double width = 600;
        double height = 750;
        try{
            FXMLLoader loader = loadFXML(fxml);

            Parent root = loader.load();
            GameController controller = loader.getController();
            controller.setPlayer(playerManager);

            setupScene(root,title,width,height);

            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loadGameScore(){
        String fxml = "/view/game-score.fxml";
        String title = "Criptex Score";
        double width = 300;
        double height = 450;
        try{
            FXMLLoader loader = loadFXML(fxml);

            Parent root = loader.load();

            setupScene(root,title,width,height);

            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
