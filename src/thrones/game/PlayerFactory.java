package thrones.game;

import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;

public class PlayerFactory {
    private static PlayerFactory playerFactory;
    private PlayerFactory() {}
    public static PlayerFactory getInstance() {
        if(playerFactory == null) {
            playerFactory = new PlayerFactory();
        }

        return playerFactory;
    }

    public Player getPlayer(String playerType, Hand hand) {
        switch (playerType) {
            case "human":
                return new Human();
            case "random":
                return new RandBot();
            case "simple":
                return new SimpleBot();
            case "smart":
                return new SmartBot(hand);
            default:
                return null;
        }
    }
}
