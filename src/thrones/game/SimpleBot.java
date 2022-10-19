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
    public void playPile(GameOfThrones gameOfThrones, GoTPiles gotPiles, Card selected, int playerIndex) {
        super.playPile(gameOfThrones, gotPiles, selected, playerIndex);
        int chosenPile = super.chooseCorrectPile(gameOfThrones, gotPiles, selected, playerIndex);
        gameOfThrones.setSelectedPileIndex(chosenPile);

    }

}
