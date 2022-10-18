package thrones.game;

import ch.aplu.jgamegrid.TextActor;

import java.awt.*;

public class Score {
    int [] scores;
    private GameGraphic gameGraphic = new GameGraphic();

    public void initScore(GameOfThrones gameOfThrones) {
        scores = new int[gameOfThrones.nbPlayers];

        for (int i = 0; i < gameOfThrones.nbPlayers; i++) {
            scores[i] = 0;
            String text = "P" + i + "-0";
            gameGraphic.scoreGraphic(gameOfThrones, i, text);
        }

        String text = "Attack: 0 - Defence: 0";
        for (int i = 0; i < gameOfThrones.getPileTextActors().length; i++) {
            gameGraphic.initPileScoreGraphic(gameOfThrones, i, text);
        }
    } // LOGIC FINE, LEAVE IT

    private void updateScore(GameOfThrones gameOfThrones, int player) {
        gameOfThrones.removeActor(gameOfThrones.getScoreActors()[player]);
        String text = "P" + player + "-" + scores[player];
        gameGraphic.scoreGraphic(gameOfThrones, player, text);
    }

    public void updateScores(GameOfThrones gameOfThrones) {
        for (int i = 0; i < gameOfThrones.nbPlayers; i++) {
            updateScore(gameOfThrones, i);
        }
        System.out.println(gameOfThrones.getPlayerTeams()[0] + " score = " + scores[0] + "; " + gameOfThrones.getPlayerTeams()[1] + " score = " + scores[1]);
    }

    public void increaseScore(int teamNumber, int valueIncrease) {
        for(int i = teamNumber%GameOfThrones.nbTeams; i<GameOfThrones.nbPlayers; i += 2) {
            scores[i] += valueIncrease;
        }


//        if(teamNumber%GameOfThrones.nbTeams == 0) {
//            scores[0] += valueIncrease;
//            scores[2] += valueIncrease;
//        } else {
//            scores[1] += valueIncrease;
//            scores[3] += valueIncrease;
//        }
    }

    public void finalScores(GameOfThrones gameOfThrones) {
        // Final scores - leave for now
        String text;
        if (scores[0] > scores[1]) {
            text = "Players 0 and 2 won.";
        } else if (scores[0] == scores[1]) {
            text = "All players drew.";
        } else {
            text = "Players 1 and 3 won.";
        }
        System.out.println("Result: " + text);
        gameOfThrones.setStatusText(text);
    }
}
