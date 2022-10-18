package thrones.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;

import java.util.Optional;

public class SimpleBot extends  Bot{
    @Override
    public String getPlayerType() {
        return "simple";
    }

    @Override
    public void playPile(GameOfThrones gameOfThrones, GoTPiles gotPiles, GoTCard gotCard, Optional<Card> selected, int playerIndex) {
        super.playPile(gameOfThrones, gotPiles, gotCard, selected, playerIndex);
        int teamPileIndex = playerIndex%gameOfThrones.nbTeams;
        int enemyPileIndex = (playerIndex+1)%gameOfThrones.nbTeams;

        if(!selected.isPresent()) {
            return;
        }
        // If it attempts to put a diamond card on top of a hearts card, it will instead skip
        if(selected.get().getSuit() == GoTCard.Suit.DIAMONDS) {
            if(gotPiles.getPiles()[enemyPileIndex].getLast().getSuit() == GoTCard.Suit.HEARTS) {
                return;
            } else {
                gameOfThrones.setSelectedPileIndex(enemyPileIndex);
            }
        } else {
            gameOfThrones.setSelectedPileIndex(teamPileIndex);
        }
    }

}
