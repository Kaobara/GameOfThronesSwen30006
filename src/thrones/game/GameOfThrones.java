package thrones.game;

// Oh_Heaven.java

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.Actor;
import utility.PropertiesLoader;

import java.util.*;

@SuppressWarnings("serial")
public class GameOfThrones extends CardGame {
    // setup game properties
    public static final String DEFAULT_PROPERTIES_PATH = "properties/got.properties";
    public static String[] playerTypes;
    public static int watchingTime = 5000;
    public static int seed = 30006;

    // General game properties
    private final String version = "2.0";
    public static final int nbPlayers = 4;
    public static final int nbTeams = 2;
	public static final int nbPlays = 6;

    // General game constants
    public static final int NON_SELECTION_VALUE = -1;
    public static final int ATTACK_RANK_INDEX = 0;
    public static final int DEFENCE_RANK_INDEX = 1;

    // Game attributes
    private static Player[] players = {null, null, null, null};
    private final String[] playerTeams = { "[Players 0 & 2]", "[Players 1 & 3]"};
    public String[] getPlayerTeams() {
        return playerTeams;
    }

    private int nextStartingPlayer;
    private GoTPiles gotPiles = new GoTPiles();
    private int selectedPileIndex = NON_SELECTION_VALUE;
    private Hand[] hands;
    private Optional<Card> selected;
    private Score score = new Score();
    public Score getScore() { return score; }

    // Selected and PileSelectedIndex getters and setters
    public void setSelected(Optional<Card> selected) {
        this.selected = selected;
    }
    public Optional<Card> getSelected() {
        return selected;
    }
    public void setSelectedPileIndex(int selectedPileIndex) {
        this.selectedPileIndex = selectedPileIndex;
    }
    public int getSelectedPileIndex() {
        return selectedPileIndex;
    }

    // Actors
    private Actor[] pileTextActors = { null, null };
    public Actor[] getPileTextActors() {
        return pileTextActors;
    }
    public void setPileTextActor(Actor actor, int i) {pileTextActors[i] = actor; }
    private Actor[] scoreActors = {null, null, null, null};
    public Actor[] getScoreActors() { return scoreActors; }

    private final GameLogic gameLogic = new GameLogic();

    private void setupGame() {
        GoTCard.getInstance().setRandom(seed);
        nextStartingPlayer = GoTCard.getRandom().nextInt(nbPlayers);

        // Setup hands
        hands = new Hand[nbPlayers];
        gameLogic.setupHands(hands, nbPlayers);

        // Create Player array
        PlayerFactory playerFactory = PlayerFactory.getInstance();
        for(int i = 0; i<nbPlayers; i++) {
            players[i] = playerFactory.getPlayer(playerTypes[i], hands[i]);

            // Register smart players
            if(players[i].getPlayerType().equals("smart")) {
                gotPiles.registerObserver((SmartBot) players[i]);
            }
        }

        gameLogic.setupHandInteractionAndGraphics(this, hands);
    }

    public GameOfThrones() {
        super(700, 700, 30);
        setTitle("Game of Thrones (V" + version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
        setStatusText("Initializing...");

        // Initialize scores nd games
        score.initScore(this);
        setupGame();

        // Play the game
        gameLogic.playGame(this, nextStartingPlayer, gotPiles, hands, players, watchingTime);
        score.finalScores(this);

        refresh();
    }

    public static void main(String[] args) {
        String propertiesPath = DEFAULT_PROPERTIES_PATH;

         System.out.println("Working Directory = " + System.getProperty("user.dir"));
         Properties properties;

        if (args == null || args.length == 0) {
              properties = PropertiesLoader.loadPropertiesFile(DEFAULT_PROPERTIES_PATH);
        } else {
              properties = PropertiesLoader.loadPropertiesFile(args[0]);
        }

        // Setup player types
        playerTypes = new String[4];
        for(int i = 0; i<4; i++) {
            playerTypes[i] = properties.getProperty("players."+i);
        }

        // Setup watching time
        GameOfThrones.watchingTime = Integer.parseInt(properties.getProperty("watchingTime"));
        System.out.println("Watching Time = " + watchingTime);

        // Setup seeds
        String seedProp = properties.getProperty("seed");  //Seed property
        if (seedProp != null) { // Use property seed
			  seed = Integer.parseInt(seedProp);
        } else { // and no property
			  seed = new Random().nextInt(); // so randomise
        }

        new GameOfThrones();
    }

}
