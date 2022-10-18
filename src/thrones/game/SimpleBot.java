package thrones.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;

import java.util.Optional;

public class SimpleBot extends  Bot{

    @Override
    public void playPile(GameOfThrones gameOfThrones, GoTPiles gotPiles, GoTCard gotCard, Optional<Card> Selected, int playerIndex) {
        int teamPileIndex = playerIndex%gameOfThrones.nbTeams;
        int enemyPileIndex = (playerIndex+1)%gameOfThrones.nbTeams;

        if(!Selected.isPresent()) {
            return;
        }
        // If it attempts to put a diamond card on top of a hearts card, it will instead skip
        if(Selected.get().getSuit() == GoTCard.Suit.DIAMONDS) {
            if(gotPiles.getPiles()[enemyPileIndex].getLast().getSuit() == GoTCard.Suit.HEARTS) {
                gameOfThrones.setSelectedPileIndex(-1);
            } else {
                gameOfThrones.setSelectedPileIndex(enemyPileIndex);
            }
        } else {
            gameOfThrones.setSelectedPileIndex(teamPileIndex);
        }
    }

}
