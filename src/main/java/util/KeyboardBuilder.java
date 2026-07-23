package util;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;

/**
 *
 * @author Sérgio Lopes Júnior
 *
 * @version 1.0
 */
public class KeyboardBuilder {
    private Consumer<String> onKeyPress;

    public KeyboardBuilder() {
        createVirtualKeyboard();
    }

    public void setOnKeyPress(Consumer<String> actionOnKeyPressed) {
        this.onKeyPress = actionOnKeyPressed;
    }

    public VBox createVirtualKeyboard() {
        VBox keyboard = new VBox(5);
        keyboard.setAlignment(Pos.CENTER);

        String[] keyboardLines = {
                "QWERTYUIOP",
                "ASDFGHJKL",
                "ZXCVBNM"
        };

        for (int i = 0; i < keyboardLines.length; i++) {
            HBox keyboardRow = new HBox(5);
            keyboardRow.setAlignment(Pos.CENTER);

            for (char label : keyboardLines[i].toCharArray()) {
                String letter = String.valueOf(label);
                Button btn = new Button(letter);
                //Impede que o ENTER do teclado seja usado para enviar uma tecla selecionada.
                btn.setFocusTraversable(false);
                btn.getStyleClass().add("common");
                btn.setPrefSize(40, 40);
                btn.setOnAction(event -> actionEvent(letter));
                keyboardRow.getChildren().add(btn);
            }
            if (i == 0) {
                Button btn = new Button("DELETE");
                btn.setFocusTraversable(false);
                btn.getStyleClass().add("delete");
                btn.setPrefSize(80, 40);
                btn.setOnAction(event -> actionEvent("DELETE"));
                keyboardRow.getChildren().add(btn);

            } else if (i == 2) {
                Button btn = new Button("ENTER");
                btn.setFocusTraversable(false);
                btn.getStyleClass().add("enter");
                btn.setPrefSize(80, 40);
                btn.setOnAction(event -> actionEvent("ENTER"));
                keyboardRow.getChildren().add(btn);
            }
            keyboard.getChildren().add(keyboardRow);
        }
        return keyboard;
    }

    public void actionEvent(String command) {
        if(onKeyPress != null){
            onKeyPress.accept(command);
        }
    }

}
