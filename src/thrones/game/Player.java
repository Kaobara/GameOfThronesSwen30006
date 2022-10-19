package thrones.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface Player {
    public String getPlayerType();
    public void playSuit(GameOfThrones got, int playerIndex, boolean isCharacter, Hand[] hands);
    public void playPile(GameOfThrones got, GoTPiles gotPiles, Card selected, int playerIndex) throws BrokeRuleException;
}
