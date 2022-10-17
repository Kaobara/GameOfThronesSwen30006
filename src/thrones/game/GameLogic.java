package thrones.game;

import ch.aplu.jcardgame.*;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GameLogic {
    public void setupHands(Hand[] hands, Deck deck, int nbPlayers, int nbStartCards, GoTCard gotCard) {
//        hands = new Hand[nbPlayers];
        for (int i = 0; i < nbPlayers; i++) {
            hands[i] = new Hand(deck);
        }
        gotCard.dealingOut(hands, nbPlayers, nbStartCards);

        for (int i = 0; i < nbPlayers; i++) {
            hands[i].sort(Hand.SortType.SUITPRIORITY, true);
            System.out.println("hands[" + i + "]: " + gotCard.canonical(hands[i]));
        }

//        // FOR HUMAN INTERACTION
//        for (final Hand currentHand : hands) {
//            // Set up human player for interaction
//            currentHand.addCardListener(new CardAdapter() {
//                public void leftDoubleClicked(Card card) {
//                    selected = Optional.of(card);
//                    currentHand.setTouchEnabled(false);
//                }
//                public void rightClicked(Card card) {
//                    selected = Optional.empty(); // Don't care which card we right-clicked for player to pass
//                    currentHand.setTouchEnabled(false);
//                }
//            });
//        }
//        // graphics
//        RowLayout[] layouts = new RowLayout[nbPlayers];
//        for (int i = 0; i < nbPlayers; i++) {
//            layouts[i] = new RowLayout(handLocations[i], handWidth);
//            layouts[i].setRotationAngle(90 * i);
//            hands[i].setView(gameOfThrones, layouts[i]);
//            hands[i].draw();
//        }
        // End graphics
    } // LOGIC FINE, LEAVE IT



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

    private void pickACorrectSuit(int playerIndex, boolean isCharacter, GoTCard gotCard, Hand[] hands, Optional<Card> selected) {
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
}
