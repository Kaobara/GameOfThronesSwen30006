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
        if (shortListCards.isEmpty() || !isCharacter && GoTCard.getRandom().nextInt(3) == 0) {
            System.out.println("PASS");
            gameOfThrones.setSelected(Optional.empty());
        } else {
            gameOfThrones.setSelected(Optional.of(shortListCards.get(GoTCard.getRandom().nextInt(shortListCards.size()))));
        }
    }

    @Override
    public void playPile(GameOfThrones gameOfThrones, GoTPiles gotPiles, Card selected, int playerIndex) {
        gameOfThrones.setSelectedPileIndex(GameOfThrones.NON_SELECTION_VALUE);
    }
}
