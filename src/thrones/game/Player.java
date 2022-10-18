package thrones.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface Player {
    public String getPlayerType();
    public void playSuit(GameOfThrones gameOfThrones, int playerIndex, boolean isCharacter, Hand[] hands, GoTCard gotCard);
    public void playPile(GameOfThrones gameOfThrones, GoTPiles gotPiles, GoTCard gotCard, Optional<Card> Selected, int playerIndex);
}
