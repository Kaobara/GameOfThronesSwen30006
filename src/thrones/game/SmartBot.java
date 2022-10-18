package thrones.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;
import java.util.Optional;

public class SmartBot extends Bot{
    private Hand playedMagicCards;
    private Score initialScore;
    private Score finalScore;
    private ArrayList<Integer> playedMagicCardsRanks = new ArrayList<>();

    public SmartBot(Deck deck) {
        this.playedMagicCards = new Hand(deck);
    }

    public void updatePlayedMagicCards(Card card) {
        if(((GoTCard.Suit) card.getSuit()).isMagic())
        playedMagicCards.insert(card, false);
    }

    @Override
    public String getPlayerType() {
        return "smart";
    }

    @Override
    public void playPile(GameOfThrones gameOfThrones, GoTPiles gotPiles, GoTCard gotCard, Optional<Card> selected, int playerIndex) {
        super.playPile(gameOfThrones, gotPiles, gotCard, selected, playerIndex);
        System.out.println("Played Diamonds: " + playedMagicCards.getCardList());

        if(selected.isEmpty()) { return; }
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
        initialScore = gameOfThrones.getScore().cloneScore(gameOfThrones.getScore());
        finalScore = gameOfThrones.getScore().cloneScore(gameOfThrones.getScore());
        System.out.println("Initial: " + finalScore.getScores()[0] + " " + finalScore.getScores()[1] );
        initialScore.battleScores(gotCard, gotPiles, false);
        finalScore.battleScores(gotCard, gotPiles, false);
        System.out.println("Final: " + finalScore.getScores()[0] + " " + finalScore.getScores()[1] );

        Card currentCard = selected.get();
//        currentCard.
        if(currentCard.getSuit() != GoTCard.Suit.DIAMONDS) {
            // check if magic rank of card has been played before
            if(!playedMagicCardsRanks.contains((Integer) ((GoTCard.Rank) currentCard.getRank()).getRankValue())) {
                return;
            }
        }

//        System.out.print("List of cards: ");
//        for (Card card : playedMagicCards.getCardList()) {
//            System.out.print(card.getRank() + " ");
//        }
//        System.out.println("");

        return;
    }

}
