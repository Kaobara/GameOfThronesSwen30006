package thrones.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

abstract public class Bot implements Player{
    @Override
    public void playSuit(GameOfThrones gameOfThrones, int playerIndex, boolean isCharacter, Hand[] hands, GoTCard gotCard) {
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

    @Override
    abstract public void playPile(GameOfThrones gameOfThrones, GoTPiles gotPiles, GoTCard gotCard, Optional<Card> Selected);
}
