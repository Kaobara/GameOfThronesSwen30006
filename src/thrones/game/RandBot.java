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
    public void playPile(GameOfThrones got, GoTPiles gotPiles, Card selected, int playerIndex) {
        super.playPile(got, gotPiles, selected, playerIndex);
        // Select random pile
        int tempSelectedPileIndex = GoTCard.getRandom().nextInt(2);

        // If it attempts to put a diamond card on top of a hearts card, it will instead skip
        if(gotPiles.getPiles()[tempSelectedPileIndex].getLast().getSuit() == GoTCard.Suit.HEARTS && selected.getSuit() == GoTCard.Suit.DIAMONDS) {
            got.setSelectedPileIndex(GameOfThrones.NON_SELECTION_VALUE);
        } else {
            got.setSelectedPileIndex(tempSelectedPileIndex);
        }
    }
}