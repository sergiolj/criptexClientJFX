package br.edu.ucsal.sergiolj.criptexclientjfx;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class KeyboardBuilder {

    public VBox createVirtualKeyboard(){
        VBox keyboard = new VBox(5);
        keyboard.setAlignment(Pos.CENTER);

        String[] keyboardLines = {
                "QWERTYUIOP",
                "ASDFGHJKL",
                "ZXCVBNM"
        };

        for(int i=0; i<keyboardLines.length; i++){
            HBox keyboardRow = new HBox(5);
            keyboardRow.setAlignment(Pos.CENTER);

            for(char label : keyboardLines[i].toCharArray()){
                Button btn = new Button(String.valueOf(label));
                btn.getStyleClass().add("common");
                btn.setPrefSize(40,40);
                btn.setOnAction(event -> keyPressed(label));
                keyboardRow.getChildren().add(btn);
            }
            if(i==0){
                Button btn = new Button("DELETE");
                btn.getStyleClass().add("delete");
                btn.setPrefSize(80,40);
                btn.setOnAction(event -> erasePressed());
                keyboardRow.getChildren().add(btn);

            }else if (i==2){
                Button btn = new Button("ENTER");
                btn.getStyleClass().add("enter");
                btn.setPrefSize(80,40);
                btn.setOnAction(event -> sendPressed());
                keyboardRow.getChildren().add(btn);
            }
            keyboard.getChildren().add(keyboardRow);
        }
        return keyboard;
    }

    private void sendPressed() {
        System.out.println("Enviando tentativa");
    }

    private void erasePressed() {
        System.out.println("Letra digitada: DELETE");
    }

    private void keyPressed(char label) {
        System.out.println("Letra digitada: "+label);
    }
}
