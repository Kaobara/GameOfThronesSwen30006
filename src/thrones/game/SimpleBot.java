package thrones.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;

import java.util.Optional;

public class SimpleBot extends BrainBot{
    @Override
    public String getPlayerType() {
        return "simple";
    }

    @Override
    public void playPile(GameOfThrones got, GoTPiles gotPiles, Card selected, int playerIndex) {
        super.playPile(got, gotPiles, selected, playerIndex);
        int chosenPile = super.chooseCorrectPile(gotPiles, selected, playerIndex);
        got.setSelectedPileIndex(chosenPile);

    }

}
