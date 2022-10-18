package thrones.game;

import ch.aplu.jcardgame.Card;

import java.util.Optional;

public class RandBot extends Bot{
    RandBot() {
        super();
    }
    @Override
    public String getPlayerType() {
        return "random";
    }

    @Override
    public void playPile(GameOfThrones gameOfThrones, GoTPiles gotPiles, GoTCard gotCard, Optional<Card> selected, int playerIndex) {
        super.playPile(gameOfThrones, gotPiles, gotCard, selected, playerIndex);

        int tempSelectedPileIndex = GoTCard.random.nextInt(2);

        // If it attempts to put a diamond card on top of a hearts card, it will instead skip
        if(gotPiles.getPiles()[tempSelectedPileIndex].getLast().getSuit() == GoTCard.Suit.HEARTS && selected.get().getSuit() == GoTCard.Suit.DIAMONDS) {
            gameOfThrones.setSelectedPileIndex(-1);
        } else {
            gameOfThrones.setSelectedPileIndex(tempSelectedPileIndex);
        }
    }
}