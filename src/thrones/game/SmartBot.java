package thrones.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;
import java.util.Optional;

public class SmartBot extends Bot{
    private Hand playedMagicCards;
    private ArrayList<Integer> playedMagicCardsRanks = new ArrayList<>();

    public SmartBot(Deck deck) {
        this.playedMagicCards = new Hand(deck);
    }

    public void updatePlayedMagicCards(Card card) {
        playedMagicCards.insert(card, false);
    }

    @Override
    public String getPlayerType() {
        return "smart";
    }

    @Override
    public void playPile(GameOfThrones gameOfThrones, GoTPiles gotPiles, GoTCard gotCard, Optional<Card> selected, int playerIndex) {
        super.playPile(gameOfThrones, gotPiles, gotCard, selected, playerIndex);

        if(selected.isEmpty()) { return; }
        // If smartBot is winning, pass the turn
        if(gameOfThrones.getScore().getWinningTeam() == playerIndex%GameOfThrones.nbTeams) { return; }

        Card currentCard = selected.get();
        if(currentCard.getSuit() != GoTCard.Suit.DIAMONDS) {
            // check if magic rank of card has been played before
            if(!playedMagicCardsRanks.contains((Integer) ((GoTCard.Rank) currentCard.getRank()).getRankValue())) {
                return;
            }
        }

        System.out.print("List of cards: ");
        for (Card card : playedMagicCards.getCardList()) {
            System.out.print(card.getRank() + " ");
        }
        System.out.println("");

        return;
    }

}
