package thrones.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;
import java.util.Optional;

public class SmartBot extends Bot{
    @Override
    public void playPile(GameOfThrones gameOfThrones, GoTPiles gotPiles, GoTCard gotCard, Optional<Card> Selected) {
        return;
    }

    private Hand playedMagicCards;

    public SmartBot(Deck deck) {
        this.playedMagicCards = new Hand(deck);
    }
}
