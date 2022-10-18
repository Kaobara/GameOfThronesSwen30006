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
    private Score score = new Score();

    public Score getScore() { return score; }

    private final String version = "2.0";
    public static final int nbPlayers = 4;
    public static final int nbTeams = 2;
	public final int nbPlays = 6;
    private Deck deck = new Deck(GoTCard.Suit.values(), GoTCard.Rank.values(), "cover");

    private Actor[] pileTextActors = { null, null };
    public Actor[] getPileTextActors() {
        return pileTextActors;
    }
    public void setPileTextActor(Actor actor, int i) {pileTextActors[i] = actor; }

    private Actor[] scoreActors = {null, null, null, null};
    public Actor[] getScoreActors() { return scoreActors; }

    private final int watchingTime = 5000;
    private Hand[] hands;
    private final String[] playerTeams = { "[Players 0 & 2]", "[Players 1 & 3]"};
    public String[] getPlayerTeams() {
        return playerTeams;
    }

    private int nextStartingPlayer = gotCard.random.nextInt(nbPlayers);

    private PlayerFactory playerFactory = new PlayerFactory();

    private Optional<Card> selected;
    private final int NON_SELECTION_VALUE = -1;
    private int selectedPileIndex = NON_SELECTION_VALUE;
    private final int UNDEFINED_INDEX = -1;
    public static final int ATTACK_RANK_INDEX = 0;
    public static final int DEFENCE_RANK_INDEX = 1;

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
        for (Player player:players) {
            if(player.getPlayerType().equals("smart")) {
                gotPiles.registerObserver((SmartBot) player);
            }
        }

        gameLogic.setupHands(hands, deck, nbPlayers, gotCard);
        gameLogic.setupHandHumanInteraction(this, hands);
        gameGraphic.setupHandGraphic(this, hands, nbPlayers);
    }

    private void executeAPlay() {
        gameLogic.part1(this, nextStartingPlayer, gotCard, gotPiles, deck, hands, players);

        gameLogic.part2(this, nextStartingPlayer, gotCard, gotPiles, deck, hands, players);

        gameLogic.part3(this, gotCard, gotPiles);

        // 5: discarded all cards on the piles
        nextStartingPlayer += 1;
        delay(watchingTime);
    }

    public GameOfThrones() {
        super(700, 700, 30);

        setTitle("Game of Thrones (V" + version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
        setStatusText("Initializing...");

        for(int i = 0; i<nbPlayers; i++) {
            players[i] = playerFactory.getPlayer(playerTypes[i], deck);
        }

        score.initScore(this);

        setupGame();

        //Maybe combine initScore and setupGame as a full initialization of game

        // Play 6 rounds
        for (int i = 0; i < nbPlays; i++) {
            executeAPlay();
            gameGraphic.updateGraphicScores(this);
        }

        // Final scores - leave for now
        score.finalScores(this);

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
