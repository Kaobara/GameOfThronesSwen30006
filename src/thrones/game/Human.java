package thrones.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Human implements  Player{
    @Override
    public void playSuit(GameOfThrones gameOfThrones, int playerIndex, boolean isCharacter, Hand[] hands, GoTCard gotCard) {

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


    @Override
    public void playPile(GameOfThrones gameOfThrones, GoTPiles gotPiles, GoTCard gotCard, Optional<Card> Selected) {
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
}
