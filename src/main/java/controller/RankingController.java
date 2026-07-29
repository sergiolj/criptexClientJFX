package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.GridPane;
import shared.remote.Ranking;
import ui.component.RankingPanel;
import view.manager.Navigation;
import java.util.List;

/**
 * Controller da View que apresenta a listagem dos 10 jogadores mais rápidos para descobrir a palavra secreta.
 *
 * @author Sérgio Lopes Júnior
 *
 * @version 1.0
 */
public class RankingController {
    @FXML
    public GridPane gp_score;
    @FXML
    public Button btn_new_game;
    private List<Ranking> list;
    
    public void setRanking(List<Ranking> rankingList){
        this.list = rankingList;
        gp_score.getChildren().add(RankingPanel.create(list));
    }

    @FXML
    public void initialize(){
        btn_new_game.getStyleClass().add("welcome");
        btn_new_game.setMinWidth(120);
        btn_new_game.setMaxHeight(30);
        btn_new_game.setOnAction(event ->
                Navigation.loadWelcomeView());

    }
}
