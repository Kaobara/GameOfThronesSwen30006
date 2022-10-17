package thrones.game;

// Oh_Heaven.java

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;
import ch.aplu.jgamegrid.TextActor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@SuppressWarnings("serial")
public class GameOfThrones extends CardGame {
    private GoTCard gotCard = new GoTCard();
    private GoTPiles gotPiles = new GoTPiles();

    private final String version = "1.0";
    public final int nbPlayers = 4;
    public final int nbStartCards = 9;
	public final int nbPlays = 6;
	public final int nbRounds = 3;
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

    private void updateScores() {
        for (int i = 0; i < nbPlayers; i++) {
            updateScore(i);
        }
        System.out.println(playerTeams[0] + " score = " + scores[0] + "; " + playerTeams[1] + " score = " + scores[1]);
    }

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
        gameLogic.setupHands(hands, deck, nbPlayers, nbStartCards, gotCard);
        gameLogic.setupHandHumanInteraction(this, hands);
        gameGraphic.setupHandGraphic(this, hands, nbPlayers);
    }

    private void pickACorrectSuit(int playerIndex, boolean isCharacter) {
        Hand currentHand = hands[playerIndex];
        List<Card> shortListCards = new ArrayList<>();
        for (int i = 0; i < currentHand.getCardList().size(); i++) {
            Card card = currentHand.getCardList().get(i);
            GoTCard.Suit suit = (GoTCard.Suit) card.getSuit();
            if (suit.isCharacter() == isCharacter) {
                shortListCards.add(card);
            }
        }
        if (shortListCards.isEmpty() || !isCharacter && gotCard.random.nextInt(3) == 0) {
            selected = Optional.empty();
        } else {
            selected = Optional.of(shortListCards.get(gotCard.random.nextInt(shortListCards.size())));
        }
    }

    private void selectRandomPile(Optional<Card> selected) {
        int tempSelectedPileIndex = gotCard.random.nextInt(2);
        System.out.println(gotPiles.getPiles()[tempSelectedPileIndex].getLast().getSuit() + " JSDLIFJD");


        if(gotPiles.getPiles()[tempSelectedPileIndex].getLast().getSuit() == GoTCard.Suit.HEARTS && selected.get().getSuit() == GoTCard.Suit.DIAMONDS) {
            System.out.println("AAA");
            selectedPileIndex = NON_SELECTION_VALUE;
        } else {
            selectedPileIndex = tempSelectedPileIndex;
        }


//        selectedPileIndex = gotCard.random.nextInt(2);
    }

    private void waitForCorrectSuit(int playerIndex, boolean isCharacter) {
        if (hands[playerIndex].isEmpty()) {
            selected = Optional.empty();
        } else {
            selected = null;
            hands[playerIndex].setTouchEnabled(true);
            do {
                if (selected == null) {
                    delay(100);
                    continue;
                }
                GoTCard.Suit suit = selected.isPresent() ? (GoTCard.Suit) selected.get().getSuit() : null;
                if (isCharacter && suit != null && suit.isCharacter() ||         // If we want character, can't pass and suit must be right
                        !isCharacter && (suit == null || !suit.isCharacter())) { // If we don't want character, can pass or suit must not be character
                    // if (suit != null && suit.isCharacter() == isCharacter) {
                    break;
                } else {
                    selected = null;
                    hands[playerIndex].setTouchEnabled(true);
                }
                delay(100);
            } while (true);
        }
    }

    private void waitForPileSelection() {
        selectedPileIndex = NON_SELECTION_VALUE;
        for (Hand pile : gotPiles.getPiles()) {
            pile.setTouchEnabled(true);
        }
        while(selectedPileIndex == NON_SELECTION_VALUE) {
            delay(100);
        }
        for (Hand pile : gotPiles.getPiles()) {
            pile.setTouchEnabled(false);
        }
    }

    private int getPlayerIndex(int index) {
        return index % nbPlayers;
    }

    private void executeAPlay() {
//        gotPiles.resetPile(this, deck);
//
//        // Check Hands has hearts (important for final play)
//        nextStartingPlayer = getPlayerIndex(nextStartingPlayer);
//        if (hands[nextStartingPlayer].getNumberOfCardsWithSuit(GoTCard.Suit.HEARTS) == 0)
//            nextStartingPlayer = getPlayerIndex(nextStartingPlayer + 1);
//        assert hands[nextStartingPlayer].getNumberOfCardsWithSuit(GoTCard.Suit.HEARTS) != 0 : " Starting player has no hearts.";
//
//        // 1: play the first 2 hearts
//        for (int i = 0; i < 2; i++) {
//            int playerIndex = getPlayerIndex(nextStartingPlayer + i);
//            setStatusText("Player " + playerIndex + " select a Heart card to play");
//
//            // if humanPlayer = true, wait for correct suit
//            if (humanPlayers[playerIndex]) {
//                waitForCorrectSuit(playerIndex, true);
//                // else bot logic
//            } else {
//                pickACorrectSuit(playerIndex, true);
//            }
//
//            int pileIndex = playerIndex % 2;
//            assert selected.isPresent() : " Pass returned on selection of character.";
//            System.out.println("Player " + playerIndex + " plays " + gotCard.canonical(selected.get()) + " on pile " + pileIndex);
//            selected.get().setVerso(false);
//            selected.get().transfer(gotPiles.getPiles()[pileIndex], true); // transfer to pile (includes graphic effect)
////                    piles[pileIndex], true); // transfer to pile (includes graphic effect)
//            gotPiles.updatePileRanks(this);
////            updatePileRanks();
//        }

        gameLogic.part1(this, nextStartingPlayer, gotCard, gotPiles, deck, hands, humanPlayers);


//        // 2: play the remaining nbPlayers * nbRounds - 2
//        int remainingTurns = nbPlayers * nbRounds - 2;
//        int nextPlayer = nextStartingPlayer + 2;
//
//        while(remainingTurns > 0) {
//            nextPlayer = getPlayerIndex(nextPlayer);
//            setStatusText("Player" + nextPlayer + " select a non-Heart card to play.");
//            if (humanPlayers[nextPlayer]) {
//                waitForCorrectSuit(nextPlayer, false);
//            } else {
//                pickACorrectSuit(nextPlayer, false);
//            }
//
//            if (selected.isPresent()) {
//                setStatusText("Selected: " + gotCard.canonical(selected.get()) + ". Player" + nextPlayer + " select a pile to play the card.");
//                // Human
//                if (humanPlayers[nextPlayer]) {
//                    waitForPileSelection();
//                } else {
//                    // bot
//                    selectRandomPile(selected);
//                }
//
//                if(selectedPileIndex != NON_SELECTION_VALUE) {
//                    System.out.println("Player " + nextPlayer + " plays " + gotCard.canonical(selected.get()) + " on pile " + selectedPileIndex);
//                    selected.get().setVerso(false);
//
//
//                    // REQUIRES OBSERVER IF SELECTED == DIAMOND FOR SNART BOTS
//
//                    selected.get().transfer(gotPiles.getPiles()[selectedPileIndex], true);   // transfer to pile (includes graphic effect)
////                        piles[selectedPileIndex], true); // transfer to pile (includes graphic effect)
//                    gotPiles.updatePileRanks(this);
////                updatePileRanks();}
//                }
//            } else {
//                setStatusText("Pass.");
//            }
//            nextPlayer++;
//            remainingTurns--;
//        }

        gameLogic.part2(this, nbRounds, nextStartingPlayer, gotCard, gotPiles, deck, hands, humanPlayers);

        // 3: calculate winning & update scores for players
        gotPiles.updatePileRanks(this);
//        updatePileRanks();
        int[] pile0Ranks = gotPiles.calculatePileRanks(0);
//                calculatePileRanks(0);
        int[] pile1Ranks = gotPiles.calculatePileRanks(1);
//                calculatePileRanks(1);
        System.out.println("piles[0]: " + gotCard.canonical(gotPiles.getPiles()[0]));
//                piles[0]));
        System.out.println("piles[0] is " + "Attack: " + pile0Ranks[ATTACK_RANK_INDEX] + " - Defence: " + pile0Ranks[DEFENCE_RANK_INDEX]);
        System.out.println("piles[1]: " + gotCard.canonical(gotPiles.getPiles()[0]));
//                piles[1]));
        System.out.println("piles[1] is " + "Attack: " + pile1Ranks[ATTACK_RANK_INDEX] + " - Defence: " + pile1Ranks[DEFENCE_RANK_INDEX]);
        GoTCard.Rank pile0CharacterRank = (GoTCard.Rank) gotPiles.getPiles()[0].getCardList().get(0).getRank();
//                piles[0].getCardList().get(0).getRank();
        GoTCard.Rank pile1CharacterRank = (GoTCard.Rank) gotPiles.getPiles()[1].getCardList().get(0).getRank();
//                piles[1].getCardList().get(0).getRank();
        String character0Result;
        String character1Result;

        // determine winner
        if (pile0Ranks[ATTACK_RANK_INDEX] > pile1Ranks[DEFENCE_RANK_INDEX]) {
            scores[0] += pile1CharacterRank.getRankValue();
            scores[2] += pile1CharacterRank.getRankValue();
            character0Result = "Character 0 attack on character 1 succeeded.";
        } else {
            scores[1] += pile1CharacterRank.getRankValue();
            scores[3] += pile1CharacterRank.getRankValue();
            character0Result = "Character 0 attack on character 1 failed.";
        }

        if (pile1Ranks[ATTACK_RANK_INDEX] > pile0Ranks[DEFENCE_RANK_INDEX]) {
            scores[1] += pile0CharacterRank.getRankValue();
            scores[3] += pile0CharacterRank.getRankValue();
            character1Result = "Character 1 attack on character 0 succeeded.";
        } else {
            scores[0] += pile0CharacterRank.getRankValue();
            scores[2] += pile0CharacterRank.getRankValue();
            character1Result = "Character 1 attack character 0 failed.";
        }

        updateScores();
        System.out.println(character0Result);
        System.out.println(character1Result);
        setStatusText(character0Result + " " + character1Result);

        // 5: discarded all cards on the piles
        nextStartingPlayer += 1;
        delay(watchingTime);
    }

    public GameOfThrones() {
        super(700, 700, 30);

        setTitle("Game of Thrones (V" + version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
        setStatusText("Initializing...");
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

    public static void main(String[] args) {
        // System.out.println("Working Directory = " + System.getProperty("user.dir"));
        // final Properties properties = new Properties();
        // properties.setProperty("watchingTime", "5000");
        /*
        if (args == null || args.length == 0) {
            //  properties = PropertiesLoader.loadPropertiesFile("cribbage.properties");
        } else {
            //  properties = PropertiesLoader.loadPropertiesFile(args[0]);
        }

        String seedProp = properties.getProperty("seed");  //Seed property
        if (seedProp != null) { // Use property seed
			  seed = Integer.parseInt(seedProp);
        } else { // and no property
			  seed = new Random().nextInt(); // so randomise
        }
        */
//        GameOfThrones.seed = 130006;
//        System.out.println("Seed = " + seed);
//        GameOfThrones.random = new Random(seed);
        new GameOfThrones();
    }

}
