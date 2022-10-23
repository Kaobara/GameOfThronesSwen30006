package thrones.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;
import java.util.Collections;
/**
 * Implementation of smart bot.
 * Monday Team 7
 * @author: Mohamad Danielsyah Mahmud
 * @author: Khai Syuen Tan
 * @author: Bhavika Shrestha
 */
public class SmartBot extends BrainBot{
    private Hand knownMagicCards = new Hand(GoTCard.deck);
    private ArrayList<Integer> knownMagicCardsRanks = new ArrayList<>();
    
    // Constructor
    public SmartBot(Hand hand) {
        // Input all magic cards in hand into list
        for(Card card : hand.getCardList()) {
            updatePlayedMagicCards(card);
        }
    }
    
    // Update known 
    public void updatePlayedMagicCards(Card card) {
        if(((GoTCard.Suit) card.getSuit()).isMagic() && !(card.isInHand(knownMagicCards))) {
            knownMagicCards.insert(card, false);
            knownMagicCardsRanks.add(((GoTCard.Rank) card.getRank()).getRankValue());
        }
    }

    @Override
    public String getPlayerType() {
        return "smart";
    }

    @Override
    public void playPile(GameOfThrones got, GoTPiles gotPiles, Card selected, int playerIndex) {
        super.playPile(got, gotPiles, selected, playerIndex);

        // Initialize temporary scores and piles
        Score initialScore = got.getScore().cloneScore(got.getScore());
        Score finalScore = got.getScore().cloneScore(got.getScore());
        GoTPiles tempPiles = gotPiles.clonePiles(gotPiles);
        Card temporaryCard = selected.clone();

        // If smartBot is winning given current piles, pass the turn
        initialScore.battleScores(tempPiles, false);
        if(initialScore.getWinningTeam() == playerIndex%GameOfThrones.nbTeams) {
            return;
        }

        // If suit is club or spades, can only play if corresponding diamond rank has already been played
        if(selected.getSuit() != GoTCard.Suit.DIAMONDS) {
            // check if magic rank of card has been played before
            if(!knownMagicCardsRanks.contains((Integer) ((GoTCard.Rank) selected.getRank()).getRankValue())) {

                // If it has a rank = 10 (10, J, Q, K), check if all 4 rank-10 magic cards have been played
                if(((GoTCard.Rank) selected.getRank()).getRankValue() == 10) {
                    if(!(Collections.frequency(knownMagicCardsRanks, 10) == 4)) {
                        return;
                    }
                }
                return;
            }
        }

        // Predict outcome of immediate battle given the pile the smart bot will play with given selected card
        int predictedPileIndex = super.chooseCorrectPile(gotPiles,  selected, playerIndex);
        if(predictedPileIndex == GameOfThrones.NON_SELECTION_VALUE) {
            return;
        }

        tempPiles.transferCardToPile(temporaryCard, predictedPileIndex, true);
        finalScore.battleScores(tempPiles, false);

        // If the predicted winning team is smart bot's team, play it. Else, pass
        if(finalScore.getWinningTeam() == playerIndex%GameOfThrones.nbTeams) {
            got.setSelectedPileIndex(predictedPileIndex);
        } else {
            got.setSelectedPileIndex(GameOfThrones.NON_SELECTION_VALUE);
        }
    }

}
