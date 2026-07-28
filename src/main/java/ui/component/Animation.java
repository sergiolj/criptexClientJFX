package ui.component;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Gerenciador de animações.
 *
 * @author Sérgio Lopes Júnior
 *
 * @version 1.0
 */
public class Animation {

    public void flip(StackPane cell){
        ScaleTransition shrink = new ScaleTransition(Duration.millis(250), cell);
        shrink.setFromY(1.0);
        shrink.setToY(0.0);

        ScaleTransition grow = new ScaleTransition(Duration.millis(250), cell);
        grow.setFromY(0.0);
        grow.setToY(1.0);

        shrink.setOnFinished(e -> {;
            grow.play();
        });
        shrink.play();
    }

    public static void blink (Node node){
        FadeTransition fade = new FadeTransition(Duration.millis(500), node);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.setCycleCount(3);
        fade.setAutoReverse(true);
        fade.play();
    }

}
