package thrones.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;

import java.util.Optional;

public class SimpleBot extends  Bot{
    private  Deck deck;
    @Override
    public void playPile(GameOfThrones gameOfThrones, GoTPiles gotPiles, GoTCard gotCard, Optional<Card> Selected) {
        return;
    }

    public SimpleBot(Deck deck) {
        this.deck = deck;
    }
}
