package thrones.game;

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;
import ch.aplu.jgamegrid.TextActor;

import javax.swing.text.html.Option;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GameLogic {
    public void setupHands(Hand[] hands, Deck deck, int nbPlayers, int nbStartCards, GoTCard gotCard) {
        for (int i = 0; i < nbPlayers; i++) {
            hands[i] = new Hand(deck);
        }
        gotCard.dealingOut(hands, nbPlayers, nbStartCards);

        for (int i = 0; i < nbPlayers; i++) {
            hands[i].sort(Hand.SortType.SUITPRIORITY, true);
            System.out.println("hands[" + i + "]: " + gotCard.canonical(hands[i]));
        }
    }
    public void setupHandHumanInteraction(GameOfThrones gameOfThrones, Hand[] hands){
        for (final Hand currentHand : hands) {
            // Set up human player for interaction
            currentHand.addCardListener(new CardAdapter() {
                public void leftDoubleClicked(Card card) {
                    gameOfThrones.setSelected(Optional.of(card));
                    currentHand.setTouchEnabled(false);
                }
                public void rightClicked(Card card) {
                    gameOfThrones.setSelected( Optional.empty());
//                    selected = Optional.empty(); // Don't care which card we right-clicked for player to pass
                    currentHand.setTouchEnabled(false);
                }
            });
        }
    }

    private int nbPlayers = 4;

    private int getPlayerIndex(int index) {
        return index % nbPlayers;
    }

    private void pickACorrectSuit(int playerIndex, boolean isCharacter, Hand[] hands, GameOfThrones gameOfThrones, GoTCard gotCard) {
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
            gameOfThrones.setSelected(Optional.empty());
//            selected = Optional.empty();
        } else {
            gameOfThrones.setSelected(Optional.of(shortListCards.get(gotCard.random.nextInt(shortListCards.size()))));
//            selected = Optional.of(shortListCards.get(gotCard.random.nextInt(shortListCards.size())));
        }
    }

    private void selectRandomPile(GameOfThrones gameOfThrones, GoTCard gotCard) {
        gameOfThrones.setSelectedPileIndex(gotCard.random.nextInt(2));
//        selectedPileIndex = gotCard.random.nextInt(2);
    }

    private void waitForCorrectSuit(int playerIndex, boolean isCharacter, Hand[] hands, GameOfThrones gameOfThrones, GoTCard gotCard) {
        if (hands[playerIndex].isEmpty()) {
            gameOfThrones.setSelected(Optional.empty());
//            selected = Optional.empty();
        } else {
            gameOfThrones.setSelected(null);
//            selected = null;
            hands[playerIndex].setTouchEnabled(true);
            do {
                if (gameOfThrones.getSelected() == null) {
//                        selected == null) {
                    gameOfThrones.delay(100);
                    continue;
                }
                GoTCard.Suit suit = gameOfThrones.getSelected().isPresent() ? (GoTCard.Suit) gameOfThrones.getSelected().get().getSuit() : null;
                if (isCharacter && suit != null && suit.isCharacter() ||         // If we want character, can't pass and suit must be right
                        !isCharacter && (suit == null || !suit.isCharacter())) { // If we don't want character, can pass or suit must not be character
                    // if (suit != null && suit.isCharacter() == isCharacter) {
                    break;
                } else {
                    gameOfThrones.setSelected(null);
//                    selected = null;
                    hands[playerIndex].setTouchEnabled(true);
                }
                gameOfThrones.delay(100);
            } while (true);
        }
    }

    private void waitForPileSelection(GameOfThrones gameOfThrones, GoTPiles gotPiles) {
        gameOfThrones.setSelectedPileIndex(-1);
//        selectedPileIndex = NON_SELECTION_VALUE;
        for (Hand pile : gotPiles.getPiles()) {
            pile.setTouchEnabled(true);
        }
        while(gameOfThrones.getSelectedPileIndex() == -1) {
            gameOfThrones.delay(100);
        }
        for (Hand pile : gotPiles.getPiles()) {
            pile.setTouchEnabled(false);
        }
    }

    public void part1(GameOfThrones gameOfThrones, int nextStartingPlayer, GoTCard gotCard, GoTPiles gotPiles, Deck deck, Hand[] hands, boolean[] humanPlayers) {

        gotPiles.resetPile(gameOfThrones, deck);

        // Check Hands has hearts (important for final play)
        nextStartingPlayer = getPlayerIndex(nextStartingPlayer);
        if (hands[nextStartingPlayer].getNumberOfCardsWithSuit(GoTCard.Suit.HEARTS) == 0)
            nextStartingPlayer = getPlayerIndex(nextStartingPlayer + 1);
        assert hands[nextStartingPlayer].getNumberOfCardsWithSuit(GoTCard.Suit.HEARTS) != 0 : " Starting player has no hearts.";



        // 1: play the first 2 hearts
        for (int i = 0; i < 2; i++) {
            int playerIndex = getPlayerIndex(nextStartingPlayer + i);
            gameOfThrones.setStatusText("Player " + playerIndex + " select a Heart card to play");

            // if humanPlayer = true, wait for correct suit
            if (humanPlayers[playerIndex]) {
                waitForCorrectSuit(playerIndex, true, hands, gameOfThrones, gotCard);
//                waitForCorrectSuit(playerIndex, true);
                // else bot logic
            } else {
                pickACorrectSuit(playerIndex, true, hands, gameOfThrones, gotCard);
//                pickACorrectSuit(playerIndex, true);
            }

            int pileIndex = playerIndex % 2;
            assert gameOfThrones.getSelected().isPresent() : " Pass returned on selection of character.";
            System.out.println("Player " + playerIndex + " plays " + gotCard.canonical(gameOfThrones.getSelected().get()) + " on pile " + pileIndex);
            gameOfThrones.getSelected().get().setVerso(false);
            gameOfThrones.getSelected().get().transfer(gotPiles.getPiles()[pileIndex], true); // transfer to pile (includes graphic effect)
//                    piles[pileIndex], true); // transfer to pile (includes graphic effect)
            gotPiles.updatePileRanks(gameOfThrones);
//            updatePileRanks();
        }

    }



}
