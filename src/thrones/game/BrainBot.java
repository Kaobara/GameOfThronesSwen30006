package thrones.game;

import ch.aplu.jcardgame.Card;
/**
 * Template for SimpleBot and SmartBot.
 * Monday Team 7
 * @author: Mohamad Danielsyah Mahmud
 * @author: Khai Syuen Tan
 * @author: Bhavika Shrestha
 */
abstract public class BrainBot extends Bot{
    // If selected card is diamond, select enemy team pile. Else, play on your pile
    protected int chooseCorrectPile(GoTPiles gotPiles, Card selected, int playerIndex) {
        int teamPileIndex = playerIndex%GameOfThrones.nbTeams;
        int enemyPileIndex = (playerIndex+1)%GameOfThrones.nbTeams;

        // If it attempts to put a diamond card on top of a hearts card, it will instead skip
        if(selected.getSuit() == GoTCard.Suit.DIAMONDS) {
            if(gotPiles.getPiles()[enemyPileIndex].getLast().getSuit() == GoTCard.Suit.HEARTS) {
                return GameOfThrones.NON_SELECTION_VALUE;
            } else {
                return enemyPileIndex;
            }
        } else {
            return teamPileIndex;
        }
    }
}
