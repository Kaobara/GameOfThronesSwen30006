package thrones.game;

import ch.aplu.jcardgame.Deck;

public class PlayerFactory {
    public Player getPlayer(String playerType, Deck deck) {
        switch (playerType) {
            case "human":
                return new Human();
            case "random":
                return new RandBot();
            case "simple":
                return new SimpleBot(deck);
            case "smart":
                return new SmartBot(deck);
            default:
                return null;
        }
    }
}
