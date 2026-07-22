package service;

import util.StatusLetter;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import shared.dto.GuessResponseDTO;

import java.util.Arrays;
import java.util.stream.Collectors;

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
                cell.getStyleClass().add(StatusLetter.EMPTY.getCssClass());
                cell.getStyleClass().add("cell-base");

                Text letter = new Text("");
                letter.getStyleClass().add("panelLetter");
                cell.getChildren().add(letter);
                backgroundCells[row][col] = cell;
                letters[row][col] = letter;

                attemptPanel.add(cell,col,row);
            }
        }
        return attemptPanel;
    }

    public void command(String label) {
        if (currentRow >= 6) {
            return;
        }
        if (currentCol == 5 && !label.equals("ENTER") && !label.equals("DELETE")) {
            return;
        }

        switch (label) {
            case "ENTER" -> {
                if(currentCol == 5){
                    sendGuess();
                    currentRow++;
                    currentCol = 0;
                }
                formatResponse(responseTest());
            }
            case "DELETE" -> {
                if (currentCol > 0) {
                    currentCol--;
                    letters[currentRow][currentCol].setText("");
                }
            }
            default -> {
                if (currentCol < 5) {
                    letters[currentRow][currentCol].setText(label);
                    currentCol++;
                }
            }
        }
    }

    private void sendGuess() {
        String guess = Arrays.stream(letters[currentRow])
                .map(Text::getText)
                .collect(Collectors.joining());
        System.out.println("Sending to server..." + guess);
    }

    private void formatResponse(GuessResponseDTO response){
        int responseRow = currentRow -1;
        int [] status = response.getStatus();

        for (int i = 0; i < 5; i++) {
            backgroundCells[responseRow][i].getStyleClass()
                    .remove(StatusLetter.EMPTY.getCssClass());
            switch (status[i]){
                case 0 -> backgroundCells[responseRow][i].getStyleClass()
                        .add(StatusLetter.NON_EXISTENT.getCssClass());
                case 1 -> backgroundCells[responseRow][i].getStyleClass()
                        .add(StatusLetter.WRONG_PLACE.getCssClass());
                case 2 -> backgroundCells[responseRow][i].getStyleClass()
                        .add(StatusLetter.RIGHT.getCssClass());
            }
        }
    }

    private GuessResponseDTO responseTest(){
        return new GuessResponseDTO(new int[]{1, 0, 0, 2, 2},"",false);
    }
}