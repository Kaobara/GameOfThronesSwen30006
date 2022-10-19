package thrones.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

public class SmartBot extends BrainBot{
    private Hand playedMagicCards = new Hand(GoTCard.deck);
    private GoTPiles tempPiles;
    private ArrayList<Integer> playedMagicCardsRanks = new ArrayList<>();

    public SmartBot(Hand hand) {
        for(Card card : hand.getCardList()) {
            updatePlayedMagicCards(card);
        }
    }

    public void updatePlayedMagicCards(Card card) {
        if(((GoTCard.Suit) card.getSuit()).isMagic() && !(card.isInHand(playedMagicCards))) {
            playedMagicCards.insert(card, false);
            playedMagicCardsRanks.add((Integer) ((GoTCard.Rank) card.getRank()).getRankValue());
        }
    }

    @Override
    public String getPlayerType() {
        return "smart";
    }

    @Override
    public void playPile(GameOfThrones gameOfThrones, GoTPiles gotPiles, Card selected, int playerIndex) {
        super.playPile(gameOfThrones, gotPiles, selected, playerIndex);
        System.out.println(playerIndex + " Played Diamonds: " + playedMagicCards.getCardList());
        System.out.println("Current card: " + selected);



        // Initialize temporary scores and piles
        Score initialScore = gameOfThrones.getScore().cloneScore(gameOfThrones.getScore());
        Score finalScore = gameOfThrones.getScore().cloneScore(gameOfThrones.getScore());
        GoTPiles initialTempPiles = gotPiles.clonePiles(gotPiles);
        GoTPiles finalTempPiles = gotPiles.clonePiles(gotPiles);
        Card temporaryCard = selected.clone();

        // If smartBot is winning, pass the turn
//        Score initialScore = gameOfThrones.getScore().cloneScore();
//        GoTPiles tempPiles = gotPiles.clonePiles();
//        initialScore.battleScores(gotCard, tempPiles, false);
//        System.out.println(initialScore.getScores()[0] +" "+ initialScore.getScores()[1]);
//
//
//        if(initialScore.getWinningTeam() == playerIndex%GameOfThrones.nbTeams) { return; }
//        initialScore = null;
//        tempPiles = null;
//        initialScore = gameOfThrones.getScore().cloneScore();
//        initialScore.battleScores(gotCard, gotPiles, false);
        initialScore.battleScores(initialTempPiles, false);
        if(initialScore.getWinningTeam() == playerIndex%GameOfThrones.nbTeams) {
            System.out.println("were already winning");
            return;
        }

        // If suit is club or spades, can only play if corresponding diamond rank has already been played
        if(selected.getSuit() != GoTCard.Suit.DIAMONDS) {
            for(Integer playedDiamonds:playedMagicCardsRanks){
                System.out.print(playedDiamonds + ", ");
            }
            System.out.println(" ");

            // check if magic rank of card has been played before
            if(!playedMagicCardsRanks.contains((Integer) ((GoTCard.Rank) selected.getRank()).getRankValue())) {
                System.out.println("Corresponding Diamond must be played");
                // if its a 10, Jack, Queen, or King, all Diamond versions of those cards must have been played

                if(((GoTCard.Rank) selected.getRank()).getRankValue() == 10) {
                    if(!(Collections.frequency(playedMagicCardsRanks, 10) == 4)) {
                        System.out.println("All diamonds must be played");
                        return;
                    }
                }
//                if (!(((GoTCard.Rank) selected.getRank()).getRankValue() == 10 && Collections.frequency(playedMagicCardsRanks, 10) == 4)) {
//                    System.out.println("All diamonds must be played");
//                    return;
//                }
                return;
            }
        }

        int predictedPileIndex = super.chooseCorrectPile(gameOfThrones, gotPiles,  selected, playerIndex);
        if(predictedPileIndex == GameOfThrones.NON_SELECTION_VALUE) {
            return;
        }

        finalTempPiles.transferCardToPile(temporaryCard, predictedPileIndex, true);

        System.out.println("Scores before battling: " + initialScore.getScores()[0] +" "+ initialScore.getScores()[1]);
        finalScore.battleScores(finalTempPiles, false);
        System.out.println("Scores after battling: " + finalScore.getScores()[0] +" "+ finalScore.getScores()[1]);
        System.out.println("Winner: " + finalScore.getWinningTeam());


        if(finalScore.getWinningTeam() == playerIndex%GameOfThrones.nbTeams) {
            System.out.println("This play will make us win");
            gameOfThrones.setSelectedPileIndex(predictedPileIndex);
        } else {
            System.out.println("this card will not make us win");
            gameOfThrones.setSelectedPileIndex(GameOfThrones.NON_SELECTION_VALUE);
        }

//        super.chooseCorrectPile(gameOfThrones, gotPiles, gotCard, selected, playerIndex);

//        if(selected.getSuit() == GoTCard.Suit.DIAMONDS) {
//            if(gotPiles.getPiles()[enemyPileIndex].getLast().getSuit() == GoTCard.Suit.HEARTS) {
//                return;
//            } else {
//                gameOfThrones.setSelectedPileIndex(enemyPileIndex);
//            }
//        } else {
//            gameOfThrones.setSelectedPileIndex(teamPileIndex);
//        }

//        System.out.print("List of cards: ");
//        for (Card card : playedMagicCards.getCardList()) {
//            System.out.print(card.getRank() + " ");
//        }
//        System.out.println("");

        return;
    }

}
