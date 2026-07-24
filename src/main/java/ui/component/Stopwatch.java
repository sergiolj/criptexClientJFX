package ui.component;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class Stopwatch {

    private final Timeline timeCounter;
    private final Label stopwatch;
    private int secondsCounting = 0;


    public Stopwatch(Label label) {
        this.timeCounter = new Timeline();
        this.stopwatch = label;

        this.timeCounter.setCycleCount(Timeline.INDEFINITE);

        KeyFrame keyFrame = new KeyFrame((Duration.seconds(1)),
                event -> {
                    secondsCounting++;
                    updateLabel();
                });
        timeCounter.getKeyFrames().add(keyFrame);
    }

    public void start() {
        timeCounter.playFromStart();
    }

    private void updateLabel() {
        int minutes = secondsCounting /60;
        int seconds = secondsCounting % 60;
        stopwatch.setText(String.format("%02d:%02d", minutes,seconds));
    }
}
