package thrones.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.Optional;

public class Human implements  Player{
    @Override
    public String getPlayerType() {
        return "human";
    }

    @Override
    public void playSuit(GameOfThrones got, int playerIndex, boolean isCharacter, Hand[] hands) {

        if (hands[playerIndex].isEmpty()) {
            got.setSelected(Optional.empty());
        } else {
            got.setSelected(null);
            hands[playerIndex].setTouchEnabled(true);
            do {
                if (got.getSelected() == null) {
                    GameOfThrones.delay(100);
                    continue;
                }
                GoTCard.Suit suit = got.getSelected().isPresent() ? (GoTCard.Suit) got.getSelected().get().getSuit() : null;
                if (isCharacter && suit != null && suit.isCharacter() ||         // If we want character, can't pass and suit must be right
                        !isCharacter && (suit == null || !suit.isCharacter())) { // If we don't want character, can pass or suit must not be character
                    break;
                } else {
                    got.setSelected(null);
                    hands[playerIndex].setTouchEnabled(true);
                }
                GameOfThrones.delay(100);
            } while (true);
        }

    }


    @Override
    public void playPile(GameOfThrones got, GoTPiles gotPiles, Card selected, int playerIndex)
    throws BrokeRuleException {
        got.setSelectedPileIndex(GameOfThrones.NON_SELECTION_VALUE);
        for (Hand pile : gotPiles.getPiles()) {
            pile.setTouchEnabled(true);
        }
        while(got.getSelectedPileIndex() == GameOfThrones.NON_SELECTION_VALUE) {
            got.delay(100);
        }

        if(gotPiles.getPiles()[got.getSelectedPileIndex()].getLast().getSuit() == GoTCard.Suit.HEARTS && selected.getSuit() == GoTCard.Suit.DIAMONDS) {
            got.setSelectedPileIndex(GameOfThrones.NON_SELECTION_VALUE);
            throw new BrokeRuleException("Invalid Pile Selection. Turn passed");
        }


        for (Hand pile : gotPiles.getPiles()) {
            pile.setTouchEnabled(false);
        }
    }
}
