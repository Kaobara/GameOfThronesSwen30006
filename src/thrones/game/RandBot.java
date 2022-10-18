package thrones.game;

import ch.aplu.jcardgame.Card;

import java.util.Optional;

public class RandBot extends Bot{
    RandBot() {
        super();
    }

    @Override
    public void playPile(GameOfThrones gameOfThrones, GoTPiles gotPiles, GoTCard gotCard, Optional<Card> Selected, int playerIndex) {
        int tempSelectedPileIndex = GoTCard.random.nextInt(2);

        // If it attempts to put a diamond card on top of a hearts card, it will instead skip
        if(gotPiles.getPiles()[tempSelectedPileIndex].getLast().getSuit() == GoTCard.Suit.HEARTS && Selected.get().getSuit() == GoTCard.Suit.DIAMONDS) {
            gameOfThrones.setSelectedPileIndex(-1);
        } else {
            gameOfThrones.setSelectedPileIndex(tempSelectedPileIndex);
        }
    }
}