package thrones.game;

import ch.aplu.jcardgame.*;
import java.util.Optional;
/**
 * Handles the logic of the game.
 * Monday Team 7
 * @author: Mohamad Danielsyah Mahmud
 * @author: Khai Syuen Tan
 * @author: Bhavika Shrestha
 */
public class GameLogic {
    private final int nbRounds = 3;
    private final int nbStartCards = 9;
    private final GameGraphic gameGraphic = new GameGraphic();

    private int getPlayerIndex(int index) {
        return index % GameOfThrones.nbPlayers;
    }

    // Setup
    public void setupHands(Hand[] hands, int nbPlayers) {
        GoTCard gotCard = GoTCard.getInstance();

        for (int i = 0; i < nbPlayers; i++) {
            hands[i] = new Hand(GoTCard.deck);
        }
        gotCard.dealingOut(hands, nbPlayers, nbStartCards);

        for (int i = 0; i < nbPlayers; i++) {
            hands[i].sort(Hand.SortType.SUITPRIORITY, true);
            System.out.println("hands[" + i + "]: " + gotCard.canonical(hands[i]));
        }
    }
    private void setupHandHumanInteraction(GameOfThrones got, Hand[] hands){
        for (final Hand currentHand : hands) {
            // Set up human player for interaction
            currentHand.addCardListener(new CardAdapter() {
                public void leftDoubleClicked(Card card) {
                    got.setSelected(Optional.of(card));
                    currentHand.setTouchEnabled(false);
                }
                public void rightClicked(Card card) {
                    got.setSelected( Optional.empty()); // Don't care which card we right-clicked for player to pass
                    currentHand.setTouchEnabled(false);
                }
            });
        }
    }
    public void setupHandInteractionAndGraphics(GameOfThrones got, Hand[] hands) {
        setupHandHumanInteraction(got, hands);
        gameGraphic.setupHandGraphic(got, hands);
    }

    // Runtime game logic
    private void playCharacters(GameOfThrones got, int nextStartingPlayer, GoTPiles gotPiles, Hand[] hands, Player[] players) {
        gotPiles.resetPile(got, GoTCard.deck);

        GoTCard gotCard = GoTCard.getInstance();

        // Check Hands has hearts (important for final play)
        nextStartingPlayer = getPlayerIndex(nextStartingPlayer);
        if (hands[nextStartingPlayer].getNumberOfCardsWithSuit(GoTCard.Suit.HEARTS) == 0)
            nextStartingPlayer = getPlayerIndex(nextStartingPlayer + 1);
        assert hands[nextStartingPlayer].getNumberOfCardsWithSuit(GoTCard.Suit.HEARTS) != 0 : " Starting player has no hearts.";

        // 1: play the first 2 hearts
        for (int i = 0; i < 2; i++) {
            int playerIndex = getPlayerIndex(nextStartingPlayer + i);
            got.setStatusText("Player " + playerIndex + " select a Heart card to play");

            players[playerIndex].playSuit(got, playerIndex, true, hands);

            int pileIndex = playerIndex % 2;
            assert got.getSelected().isPresent() : " Pass returned on selection of character.";
            System.out.println("Player " + playerIndex + " plays " + gotCard.canonical(got.getSelected().get()) + " on pile " + pileIndex);
            got.getSelected().get().setVerso(false);
            got.getSelected().get().transfer(gotPiles.getPiles()[pileIndex], true); // transfer to pile (includes graphic effect)
            gotPiles.updatePileRanks(got);
        }
    }
    private void playEffectCards(GameOfThrones got, int nextStartingPlayer, GoTPiles gotPiles, Hand[] hands, Player[] players) {
        GoTCard gotCard = GoTCard.getInstance();

        // 2: play the remaining nbPlayers * nbRounds - 2
        int remainingTurns = GameOfThrones.nbPlayers * nbRounds - 2;
        int nextPlayer = nextStartingPlayer + 2;

        while(remainingTurns > 0) {
            nextPlayer = getPlayerIndex(nextPlayer);
            got.setStatusText("Player" + nextPlayer + " select a non-Heart card to play.");

            // Select a card from hand
            players[nextPlayer].playSuit(got, nextPlayer, false, hands);

            if (got.getSelected().isPresent()) {

                got.setStatusText("Selected: " + gotCard.canonical(got.getSelected().get()) + ". Player" + nextPlayer + " select a pile to play the card.");
                // Select a pile
                try {
                    players[nextPlayer].playPile(got, gotPiles, got.getSelected().get(), nextPlayer);
                } catch (BrokeRuleException e) {
                    got.setStatusText("Invalid Play. Passing");
                    System.out.println("Invalid play");
                }

                if(got.getSelectedPileIndex() != GameOfThrones.NON_SELECTION_VALUE) {
                    System.out.println("Player " + nextPlayer + " plays " + gotCard.canonical(got.getSelected().get()) + " on pile " + got.getSelectedPileIndex());
                    got.getSelected().get().setVerso(false);
                    gotPiles.transferCardToPile(got.getSelected().get(), got.getSelectedPileIndex(), false);// transfer to pile (includes graphic effect)
                    gameGraphic.updatePileRankGraphics(got, gotPiles); // Update the pile ranks graphics
                }
            } else {
                got.setStatusText("Pass.");
            }
            nextPlayer++;
            remainingTurns--;
        }
    }
    private void finalizeScore(GameOfThrones got, GoTPiles gotPiles) {
        // 3: calculate winning & update scores for players
        gameGraphic.updatePileRankGraphics(got, gotPiles);

        String[] characterResults = got.getScore().battleScores(gotPiles, true);

        if(characterResults != null) {
            got.setStatusText(characterResults[0] + " " + characterResults[1]);
        }
        gameGraphic.updateGraphicScores(got);

    }

    private int playARound(GameOfThrones got, int nextStartingPlayer, GoTPiles gotPiles, Hand[] hands, Player[] players, int watchingTime) {
        playCharacters(got, nextStartingPlayer, gotPiles, hands, players);
        playEffectCards(got, nextStartingPlayer, gotPiles, hands, players);
        finalizeScore(got, gotPiles);

        // 5: discarded all cards on the piles
        nextStartingPlayer += 1;
        GameOfThrones.delay(watchingTime);

        return nextStartingPlayer;
    }

    public void playGame(GameOfThrones got, int nextStartingPlayer, GoTPiles gotPiles, Hand[] hands, Player[] players, int watchingTime) {
        int afterNextStartingPlayer = nextStartingPlayer;

        // Play 6 rounds
        for (int i = 0; i < GameOfThrones.nbPlays; i++) {
            afterNextStartingPlayer = playARound(got, afterNextStartingPlayer, gotPiles, hands, players, watchingTime);
            gameGraphic.updateGraphicScores(got);
        }
    }
}
