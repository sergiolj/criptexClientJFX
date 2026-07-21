package br.edu.ucsal.sergiolj.criptexclientjfx;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class WordPanel {

    private final StackPane[][] backgroundCells = new StackPane[6][5];
    private final Text[][] letters = new Text[6][5];

    private int currentRow = 0;
    private int currentCol = 0;

    public GridPane createWordPanel(){
        GridPane attemptPanel = new GridPane(5,5);
        attemptPanel.setVgap(5);
        attemptPanel.setHgap(5);

        for(int row=0; row<6; row++){
            for(int col=0; col<5; col++){
                StackPane cell = new StackPane();
                cell.setMinSize(60,60);
                cell.getStyleClass().add(StatusLetterCSS.EMPTY.getCssClass());
                cell.getStyleClass().add("cell-base");

                Text letter = new Text("");
                cell.getChildren().add(letter);
                backgroundCells[row][col] = cell;
                letters[row][col] = letter;

                attemptPanel.add(cell,col,row);
            }
        }
        return attemptPanel;
    }

    public void nextLetter(String label){
        if(currentCol<5){
            letters[currentRow][currentCol].setText(label);
            currentCol++;
        }
    }
}