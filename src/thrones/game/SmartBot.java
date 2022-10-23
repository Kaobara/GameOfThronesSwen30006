package thrones.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;
import java.util.Collections;

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
        System.out.println(playerIndex + " Played Diamonds: " + knownMagicCards.getCardList());
        System.out.println("Current card: " + selected);

        // Initialize temporary scores and piles
        Score initialScore = got.getScore().cloneScore(got.getScore());
        Score finalScore = got.getScore().cloneScore(got.getScore());
        GoTPiles tempPiles = gotPiles.clonePiles(gotPiles);
//        GoTPiles initialTempPiles = gotPiles.clonePiles(gotPiles);
//        GoTPiles finalTempPiles = gotPiles.clonePiles(gotPiles);
        Card temporaryCard = selected.clone();

        // If smartBot is winning, pass the turn
//        initialScore.battleScores(initialTempPiles, false);
        initialScore.battleScores(tempPiles, false);
        if(initialScore.getWinningTeam() == playerIndex%GameOfThrones.nbTeams) {
            System.out.println("were already winning");
            return;
        }

        // If suit is club or spades, can only play if corresponding diamond rank has already been played
        if(selected.getSuit() != GoTCard.Suit.DIAMONDS) {
            for(Integer playedDiamonds:knownMagicCardsRanks){
                System.out.print(playedDiamonds + ", ");
            }
            System.out.println(" ");

            // check if magic rank of card has been played before
            if(!knownMagicCardsRanks.contains((Integer) ((GoTCard.Rank) selected.getRank()).getRankValue())) {
                System.out.println("Corresponding Diamond must be played");

                if(((GoTCard.Rank) selected.getRank()).getRankValue() == 10) {
                    if(!(Collections.frequency(knownMagicCardsRanks, 10) == 4)) {
                        System.out.println("All diamonds must be played");
                        return;
                    }
                }
                return;
            }
        }

        int predictedPileIndex = super.chooseCorrectPile(gotPiles,  selected, playerIndex);
        if(predictedPileIndex == GameOfThrones.NON_SELECTION_VALUE) {
            return;
        }

//        finalTempPiles.transferCardToPile(temporaryCard, predictedPileIndex, true);
        tempPiles.transferCardToPile(temporaryCard, predictedPileIndex, true);

        System.out.println("Scores before battling: " + initialScore.getScores()[0] +" "+ initialScore.getScores()[1]);
        finalScore.battleScores(tempPiles, false);
//        finalScore.battleScores(finalTempPiles, false);
        System.out.println("Scores after battling: " + finalScore.getScores()[0] +" "+ finalScore.getScores()[1]);
        System.out.println("Winner: " + finalScore.getWinningTeam());


        if(finalScore.getWinningTeam() == playerIndex%GameOfThrones.nbTeams) {
            System.out.println("This play will make us win");
            got.setSelectedPileIndex(predictedPileIndex);
        } else {
            System.out.println("this card will not make us win");
            got.setSelectedPileIndex(GameOfThrones.NON_SELECTION_VALUE);
        }
        
        return;
    }

}
