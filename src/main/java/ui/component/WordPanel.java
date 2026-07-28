package ui.component;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import shared.dto.GuessResponse;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 *
 * @author Sérgio Lopes Júnior
 *
 * @version 1.0
 */
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

    /**
     * Retorna a palavra adicionada pelo usuário após a verificação de que a palavra contém 5 caracteres.
     * @return String guess, caso a palavra esteja completa ou nulo.
     */
    public String getWord() {
        if (currentCol == 5) {
            String guess = Arrays.stream(letters[currentRow])
                    .map(Text::getText)
                    .collect(Collectors.joining());

            currentRow++;
            currentCol = 0;
            System.out.println(guess);
            return guess;
        }
        return null;
    }

    /**
     * Processa os comandos enviados pelo controller para atualização do painel de palavras.
     * @param label comando enviado pelo controller.
     * @return Caso o comando seja ENTER, aciona o método getWord que valida a palavra e retorna a String ou nulo para
     * os demais comandos.
     */
    public String command(String label) {
        if (currentRow >= 6) {
            return null;
        }
        if (currentCol == 5 && !label.equals("ENTER") && !label.equals("DELETE")) {
            return null;
        }

        String word = null;

        switch (label) {
            case "ENTER":
                word = getWord();
                break;

            case "DELETE":
                if (currentCol > 0) {
                    currentCol--;
                    letters[currentRow][currentCol].setText("");
                }
                break;
            default:
                if (currentCol < 5) {
                    letters[currentRow][currentCol].setText(label);
                    currentCol++;
                }
                break;
            }
        return word;
    }

    public void formatResponse(GuessResponse response){
        int responseRow = currentRow -1;
        int [] status = response.getStatus();

        Animation play = new Animation();
        for (int i = 0; i < 5; i++) {

            StackPane currentCell = backgroundCells[responseRow][i];
            currentCell.getStyleClass().remove(StatusLetter.EMPTY.getCssClass());

            switch (status[i]){
                case 0 -> {
                    currentCell.getStyleClass().add(StatusLetter.NON_EXISTENT.getCssClass());
                    play.flip(currentCell);
                }
                case 1 -> {
                    currentCell.getStyleClass().add(StatusLetter.WRONG_PLACE.getCssClass());
                    play.flip(currentCell);
                }
                case 2 -> {
                    currentCell.getStyleClass().add(StatusLetter.RIGHT.getCssClass());
                    play.flip(currentCell);
                }
            }
        }
    }

    public void rollBack() {
        currentCol = 5;
        currentRow--;
    }
}