package thrones.game;

import ch.aplu.jcardgame.Card;

abstract public class BrainBot extends Bot{
    public int chooseCorrectPile(GameOfThrones gameOfThrones, GoTPiles gotPiles, Card selected, int playerIndex) {
        int teamPileIndex = playerIndex%gameOfThrones.nbTeams;
        int enemyPileIndex = (playerIndex+1)%gameOfThrones.nbTeams;

        // If it attempts to put a diamond card on top of a hearts card, it will instead skip
        if(selected.getSuit() == GoTCard.Suit.DIAMONDS) {
            if(gotPiles.getPiles()[enemyPileIndex].getLast().getSuit() == GoTCard.Suit.HEARTS) {
                return GameOfThrones.NON_SELECTION_VALUE;
            } else {
                return enemyPileIndex;
//                gameOfThrones.setSelectedPileIndex(enemyPileIndex);
            }
        } else {
            return teamPileIndex;
//            gameOfThrones.setSelectedPileIndex(teamPileIndex);
        }
    }
}
