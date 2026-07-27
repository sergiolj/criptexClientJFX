package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import shared.remote.Ranking;
import ui.component.RankingPanel;
import view.manager.Navigation;

import java.util.List;

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
        btn_new_game.setOnAction(event ->
                Navigation.loadWelcomeView());
    }
}
