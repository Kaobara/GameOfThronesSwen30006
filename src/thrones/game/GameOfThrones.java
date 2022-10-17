package thrones.game;

// Oh_Heaven.java

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;
import ch.aplu.jgamegrid.TextActor;
import utility.PropertiesLoader;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("serial")
public class GameOfThrones extends CardGame {
    public static Player[] players = {null, null, null, null};
    private GoTCard gotCard = new GoTCard();
    private GoTPiles gotPiles = new GoTPiles();

    private final String version = "2.0";
    public final int nbPlayers = 4;
	public final int nbPlays = 6;
    private Deck deck = new Deck(GoTCard.Suit.values(), GoTCard.Rank.values(), "cover");

    private final Location[] scoreLocations = {
            new Location(575, 675),
            new Location(25, 575),
            new Location(25, 25),
            new Location(575, 125)
    };
    private final Location[] pileStatusLocations = {
            new Location(250, 200),
            new Location(250, 520)
    };

    private Actor[] pileTextActors = { null, null };

    public Actor[] getPileTextActors() {
        return pileTextActors;
    }
    public String[] getPlayerTeams() {
        return playerTeams;
    }

    private Actor[] scoreActors = {null, null, null, null};
    private final int watchingTime = 5000;
    private Hand[] hands;
//    private Hand[] piles;
    private final String[] playerTeams = { "[Players 0 & 2]", "[Players 1 & 3]"};
//    private int nextStartingPlayer = random.nextInt(nbPlayers);
    private int nextStartingPlayer = gotCard.random.nextInt(nbPlayers);

    private int[] scores = new int[nbPlayers];

    Font bigFont = new Font("Arial", Font.BOLD, 36);
    Font smallFont = new Font("Arial", Font.PLAIN, 10);

    //boolean[] humanPlayers = { true, true, true, true};
//    boolean[] humanPlayers = { true, false, false, false};
    boolean[] humanPlayers = { false, false, false, false};


    private void initScore() {
        for (int i = 0; i < nbPlayers; i++) {
             scores[i] = 0;
            String text = "P" + i + "-0";
            scoreActors[i] = new TextActor(text, Color.WHITE, bgColor, bigFont);
            addActor(scoreActors[i], scoreLocations[i]);
        }

        String text = "Attack: 0 - Defence: 0";
        for (int i = 0; i < pileTextActors.length; i++) {
            pileTextActors[i] = new TextActor(text, Color.WHITE, bgColor, smallFont);
            addActor(pileTextActors[i], pileStatusLocations[i]);
        }
    } // LOGIC FINE, LEAVE IT

    private void updateScore(int player) {
        removeActor(scoreActors[player]);
        String text = "P" + player + "-" + scores[player];
        scoreActors[player] = new TextActor(text, Color.WHITE, bgColor, bigFont);
        addActor(scoreActors[player], scoreLocations[player]);
    }

    public void updateScores() {
        for (int i = 0; i < nbPlayers; i++) {
            updateScore(i);
        }
        System.out.println(playerTeams[0] + " score = " + scores[0] + "; " + playerTeams[1] + " score = " + scores[1]);
    }

    private PlayerFactory playerFactory = new PlayerFactory();

    private Optional<Card> selected;
    private final int NON_SELECTION_VALUE = -1;
    private int selectedPileIndex = NON_SELECTION_VALUE;
    private final int UNDEFINED_INDEX = -1;
    private final int ATTACK_RANK_INDEX = 0;
    private final int DEFENCE_RANK_INDEX = 1;

    private final GameLogic gameLogic = new GameLogic();
    private final GameGraphic gameGraphic = new GameGraphic();

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

    public int getATTACK_RANK_INDEX() {
        return ATTACK_RANK_INDEX;
    }

    public int getDEFENCE_RANK_INDEX() {
        return DEFENCE_RANK_INDEX;
    }

    private void setupGame() {
        hands = new Hand[nbPlayers];
        gameLogic.setupHands(hands, deck, nbPlayers, gotCard);
        gameLogic.setupHandHumanInteraction(this, hands);
        gameGraphic.setupHandGraphic(this, hands, nbPlayers);
    }

    private void executeAPlay() {
        gameLogic.part1(this, nextStartingPlayer, gotCard, gotPiles, deck, hands, humanPlayers);

        gameLogic.part2(this, nextStartingPlayer, gotCard, gotPiles, deck, hands, humanPlayers);

        gameLogic.part3(this, gotCard, gotPiles, scores);

        // 5: discarded all cards on the piles
        nextStartingPlayer += 1;
        delay(watchingTime);
    }

    public GameOfThrones() {
        super(700, 700, 30);

        setTitle("Game of Thrones (V" + version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
        setStatusText("Initializing...");

        for(int i = 0; i<nbPlayers; i++) {
            players[i] = playerFactory.getPlayer(playerTypes[i]);
        }
        initScore();

        setupGame();

        //Maybe combine initScore and setupGame as a full initialization of game

        // Play 6 rounds
        for (int i = 0; i < nbPlays; i++) {
            executeAPlay();
            updateScores();
        }

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
        setStatusText(text);

        refresh();
    }

    public static final String DEFAULT_PROPERTIES_PATH = "properties/got.properties";

    public static String[] playerTypes;

    public static void main(String[] args) {
        String propertiesPath = DEFAULT_PROPERTIES_PATH;

         System.out.println("Working Directory = " + System.getProperty("user.dir"));
         Properties properties = new Properties();
         properties.setProperty("watchingTime", "5000");
//        /*
        if (args == null || args.length == 0) {
              properties = PropertiesLoader.loadPropertiesFile(DEFAULT_PROPERTIES_PATH);
        } else {
              properties = PropertiesLoader.loadPropertiesFile(args[0]);
        }

        playerTypes = new String[4];
        for(int i = 0; i<4; i++) {
            playerTypes[i] = properties.getProperty("players."+i);
        }

        for(int i = 0; i<4; i++) {
            System.out.println(playerTypes[i]);
        }

//        String seedProp = properties.getProperty("seed");  //Seed property
////        if (seedProp != null) { // Use property seed
////			  seed = Integer.parseInt(seedProp);
////        } else { // and no property
////			  seed = new Random().nextInt(); // so randomise
////        }
//        */
//        GameOfThrones.seed = 130006;
//        System.out.println("Seed = " + seed);
//        GameOfThrones.random = new Random(seed);
        new GameOfThrones();
    }

}
