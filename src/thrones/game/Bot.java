package thrones.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
/**
 * Template abstract class for Bot strategies.
 * Monday Team 7
 * @author: Mohamad Danielsyah Mahmud
 * @author: Khai Syuen Tan
 * @author: Bhavika Shrestha
 */
abstract public class Bot implements Player{
    @Override
    // Select a random card from your hand. Same logic for all bots
    public void playSuit(GameOfThrones got, int playerIndex, boolean isCharacter, Hand[] hands) {
        Hand currentHand = hands[playerIndex];
        List<Card> shortListCards = new ArrayList<>();
        for (int i = 0; i < currentHand.getCardList().size(); i++) {
            Card card = currentHand.getCardList().get(i);
            GoTCard.Suit suit = (GoTCard.Suit) card.getSuit();
            if (suit.isCharacter() == isCharacter) {
                shortListCards.add(card);
            }
        }
        if (shortListCards.isEmpty() || !isCharacter && GoTCard.getRandom().nextInt(3) == 0) {
            got.setSelected(Optional.empty());
        } else {
            got.setSelected(Optional.of(shortListCards.get(GoTCard.getRandom().nextInt(shortListCards.size()))));
        }
    }

    @Override
    public void playPile(GameOfThrones got, GoTPiles gotPiles, Card selected, int playerIndex) {
        got.setSelectedPileIndex(GameOfThrones.NON_SELECTION_VALUE);
    }
}
