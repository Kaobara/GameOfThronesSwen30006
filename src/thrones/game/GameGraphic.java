package thrones.game;

import ch.aplu.jcardgame.Hand;
import ch.aplu.jcardgame.RowLayout;
import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;
import ch.aplu.jgamegrid.TextActor;

import java.awt.*;

public class GameGraphic {

    // Graphic Attributes
    private final int handWidth = 400;
    private final int pileWidth = 40;
    Font bigFont = new Font("Arial", Font.BOLD, 36);
    Font smallFont = new Font("Arial", Font.PLAIN, 10);

    // Graphic
    private final Location[] handLocations = {
            new Location(350, 625),
            new Location(75, 350),
            new Location(350, 75),
            new Location(625, 350)
    };

    private final Location[] scoreLocations = {
            new Location(575, 675),
            new Location(25, 575),
            new Location(25, 25),
            new Location(575, 125)
    };
    private final Location[] pileLocations = {
            new Location(350, 280),
            new Location(350, 430)
    };
    private final Location[] pileStatusLocations = {
            new Location(250, 200),
            new Location(250, 520)
    };

    public void setupHandGraphic(GameOfThrones gameOfThrones, Hand[] hands) {
        RowLayout[] layouts = new RowLayout[GameOfThrones.nbPlayers];
        for (int i = 0; i < GameOfThrones.nbPlayers; i++) {
            layouts[i] = new RowLayout(handLocations[i], handWidth);
            layouts[i].setRotationAngle(90 * i);
            hands[i].setView(gameOfThrones, layouts[i]);
            hands[i].draw();
        }
    }

    public void resetPileGraphic(GameOfThrones gameOfThrones, int i, Hand[] piles) {
        piles[i].setView(gameOfThrones, new RowLayout(pileLocations[i], 8 * pileWidth));
        piles[i].draw();
    }


    public void updatePileRankState(int pileIndex, int attackRank, int defenceRank, GameOfThrones gameOfThrones) {
        Actor[] tempPileTextActors = gameOfThrones.getPileTextActors();
        String[] tempPlayerTeams = gameOfThrones.getPlayerTeams();

        TextActor currentPile = (TextActor) tempPileTextActors[pileIndex];
        gameOfThrones.removeActor(currentPile);

        String text = tempPlayerTeams[pileIndex] + " Attack: " + attackRank + " - Defence: " + defenceRank;
        tempPileTextActors[pileIndex] = new TextActor(text, Color.WHITE, gameOfThrones.bgColor, smallFont);
        gameOfThrones.addActor(tempPileTextActors[pileIndex], pileStatusLocations[pileIndex]);
    }

    public void updatePileRankGraphics(GameOfThrones gameOfThrones, GoTPiles gotPiles) {
        for (int j = 0; j < gotPiles.getPiles().length; j++) {
            int[] ranks = gotPiles.calculatePileRanks(j);
            updatePileRankState(j, ranks[GameOfThrones.ATTACK_RANK_INDEX], ranks[GameOfThrones.DEFENCE_RANK_INDEX], gameOfThrones);
        }
    }

    public void scoreGraphic(GameOfThrones gameOfThrones, int playerIndex, String text) {
        gameOfThrones.getScoreActors()[playerIndex] = new TextActor(text, Color.WHITE, gameOfThrones.bgColor, bigFont);
        gameOfThrones.addActor(gameOfThrones.getScoreActors()[playerIndex], scoreLocations[playerIndex]);
    }

    public void initPileScoreGraphic(GameOfThrones gameOfThrones, int playerIndex, String text) {
        gameOfThrones.setPileTextActor(new TextActor(text, Color.WHITE, gameOfThrones.bgColor, smallFont), playerIndex);
        gameOfThrones.addActor(gameOfThrones.getPileTextActors()[playerIndex], pileStatusLocations[playerIndex]);
    }

    private void updateGraphicScore(GameOfThrones gameOfThrones, int player) {
        gameOfThrones.removeActor(gameOfThrones.getScoreActors()[player]);
        String text = "P" + player + "-" + gameOfThrones.getScore().getScores()[player];
        scoreGraphic(gameOfThrones, player, text);
    }

    public void updateGraphicScores(GameOfThrones gameOfThrones) {
        for (int i = 0; i < GameOfThrones.nbPlayers; i++) {
            updateGraphicScore(gameOfThrones, i);
        }
        System.out.println(gameOfThrones.getPlayerTeams()[0] + " score = " + gameOfThrones.getScore().getScores()[0] +
                "; " + gameOfThrones.getPlayerTeams()[1] + " score = " + gameOfThrones.getScore().getScores()[1]);
    }
}
