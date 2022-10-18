package thrones.game;

import ch.aplu.jcardgame.Hand;
import ch.aplu.jcardgame.RowLayout;
import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;
import ch.aplu.jgamegrid.TextActor;

import java.awt.*;

public class GameGraphic {
    private final int handWidth = 400;
    private final int pileWidth = 40;
    Font bigFont = new Font("Arial", Font.BOLD, 36);
    Font smallFont = new Font("Arial", Font.PLAIN, 10);
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

    public void setupHandGraphic(GameOfThrones gameOfThrones, Hand[] hands, int nbPlayers) {
        RowLayout[] layouts = new RowLayout[nbPlayers];
        for (int i = 0; i < nbPlayers; i++) {
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
            System.out.println(ranks[GameOfThrones.ATTACK_RANK_INDEX]);
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
}
