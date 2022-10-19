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

    public void setupHandGraphic(GameOfThrones got, Hand[] hands) {
        RowLayout[] layouts = new RowLayout[GameOfThrones.nbPlayers];
        for (int i = 0; i < GameOfThrones.nbPlayers; i++) {
            layouts[i] = new RowLayout(handLocations[i], handWidth);
            layouts[i].setRotationAngle(90 * i);
            hands[i].setView(got, layouts[i]);
            hands[i].draw();
        }
    }

    public void resetPileGraphic(GameOfThrones got, int i, Hand[] piles) {
        piles[i].setView(got, new RowLayout(pileLocations[i], 8 * pileWidth));
        piles[i].draw();
    }


    public void updatePileRankState(int pileIndex, int attackRank, int defenceRank, GameOfThrones got) {
        Actor[] tempPileTextActors = got.getPileTextActors();
        String[] tempPlayerTeams = got.getPlayerTeams();

        TextActor currentPile = (TextActor) tempPileTextActors[pileIndex];
        got.removeActor(currentPile);

        String text = tempPlayerTeams[pileIndex] + " Attack: " + attackRank + " - Defence: " + defenceRank;
        tempPileTextActors[pileIndex] = new TextActor(text, Color.WHITE, got.bgColor, smallFont);
        got.addActor(tempPileTextActors[pileIndex], pileStatusLocations[pileIndex]);
    }

    public void updatePileRankGraphics(GameOfThrones got, GoTPiles gotPiles) {
        for (int j = 0; j < gotPiles.getPiles().length; j++) {
            int[] ranks = gotPiles.calculatePileRanks(j);
            updatePileRankState(j, ranks[GameOfThrones.ATTACK_RANK_INDEX], ranks[GameOfThrones.DEFENCE_RANK_INDEX], got);
        }
    }

    public void scoreGraphic(GameOfThrones got, int playerIndex, String text) {
        got.getScoreActors()[playerIndex] = new TextActor(text, Color.WHITE, got.bgColor, bigFont);
        got.addActor(got.getScoreActors()[playerIndex], scoreLocations[playerIndex]);
    }

    public void initPileScoreGraphic(GameOfThrones got, int playerIndex, String text) {
        got.setPileTextActor(new TextActor(text, Color.WHITE, got.bgColor, smallFont), playerIndex);
        got.addActor(got.getPileTextActors()[playerIndex], pileStatusLocations[playerIndex]);
    }

    private void updateGraphicScore(GameOfThrones got, int player) {
        got.removeActor(got.getScoreActors()[player]);
        String text = "P" + player + "-" + got.getScore().getScores()[player];
        scoreGraphic(got, player, text);
    }

    public void updateGraphicScores(GameOfThrones got) {
        for (int i = 0; i < GameOfThrones.nbPlayers; i++) {
            updateGraphicScore(got, i);
        }
        System.out.println(got.getPlayerTeams()[0] + " score = " + got.getScore().getScores()[0] +
                "; " + got.getPlayerTeams()[1] + " score = " + got.getScore().getScores()[1]);
    }
}
