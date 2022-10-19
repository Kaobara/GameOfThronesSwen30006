package thrones.game;

import ch.aplu.jcardgame.Card;

public class RandBot extends Bot{
    RandBot() {
        super();
    }
    @Override
    public String getPlayerType() {
        return "random";
    }

    @Override
    public void playPile(GameOfThrones gameOfThrones, GoTPiles gotPiles, Card selected, int playerIndex) {
        super.playPile(gameOfThrones, gotPiles, selected, playerIndex);

        int tempSelectedPileIndex = GoTCard.getRandom().nextInt(2);

        // If it attempts to put a diamond card on top of a hearts card, it will instead skip
        if(gotPiles.getPiles()[tempSelectedPileIndex].getLast().getSuit() == GoTCard.Suit.HEARTS && selected.getSuit() == GoTCard.Suit.DIAMONDS) {
            gameOfThrones.setSelectedPileIndex(GameOfThrones.NON_SELECTION_VALUE);
        } else {
            gameOfThrones.setSelectedPileIndex(tempSelectedPileIndex);
        }
    }
}