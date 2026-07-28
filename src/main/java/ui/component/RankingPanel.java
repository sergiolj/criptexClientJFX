package ui.component;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import shared.remote.Ranking;

import java.util.List;

/**
 * Componente de interface para criação do GridPane com os melhores jogadores.
 *
 * @author Sérgio Lopes Júnior
 *
 * @version 1.0
 */
public class RankingPanel {

    public static GridPane create(List<Ranking> rankingList) {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        for (int row = 0; row < 10; row++) {

            StackPane cellName = new StackPane();
            cellName.getStyleClass().add("ranking_cell_player");

            String n = rankingList.get(row).name();
            Text playerName = new Text(n);
            playerName.getStyleClass().add("ranking_player");

            cellName.getChildren().add(playerName);

            gridPane.add(cellName, 0, row);

            StackPane cellTime = new StackPane();
            cellName.getStyleClass().add("ranking_cell_time");

            String t = rankingList.get(row).getElapsedTime();
            Text time = new Text(t);
            time.getStyleClass().add("ranking_time");

            cellTime.getChildren().add(time);

            gridPane.add(cellTime, 1, row);
        }
        return gridPane;
    }
}