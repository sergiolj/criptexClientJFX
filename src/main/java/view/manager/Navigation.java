package view.manager;

import controller.GameController;
import controller.RankingController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import rmi.network.GameSession;
import shared.remote.Ranking;
import ui.component.RankingPanel;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;

/**
 * Classe responsável pela definição das Scenes.
 *
 * @author Sérgio Lopes Júnior
 *
 * @version 2.0
 */
public class Navigation {
    private static Stage stage;

    public static void setStage(Stage primaryStage){
        stage = primaryStage;
    }

    private static void setupScene(Parent root, String title, double width, double height){
        Scene scene = new Scene(root, width, height);
        scene.getStylesheets().add(Objects.requireNonNull(
                Navigation.class.getResource("/styles/styles.css")).toExternalForm());
        Font.loadFont(Navigation.class.getResourceAsStream("/fonts/PressStart2P-Regular.ttf"), 14);
        stage.setScene(scene);
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

    public static void loadGameView(GameSession gameSession){
        String fxml = "/view/game-view.fxml";
        String title = "Criptex";
        double width = 600;
        double height = 750;
        try{
            FXMLLoader loader = loadFXML(fxml);

            Parent root = loader.load();
            GameController controller = loader.getController();
            controller.setGameSession(gameSession);

            setupScene(root,title,width,height);

            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loadGameScore(List<Ranking> rankingList){
        String fxml = "/view/game-ranking.fxml";
        String title = "Criptex Score";
        double width = 300;
        double height = 450;
        try{
            FXMLLoader loader = loadFXML(fxml);

            Parent root = loader.load();
            RankingController controller = loader.getController();
            controller.setRanking(rankingList);

            setupScene(root,title,width,height);

            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
