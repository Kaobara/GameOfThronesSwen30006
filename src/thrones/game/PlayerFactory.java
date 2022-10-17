package thrones.game;

public class PlayerFactory {
    public Player getPlayer(String playerType) {
        switch (playerType) {
            case "human":
                return new Human();
            case "random":
                return new RandBot();
            case "simple":
                return new SimpleBot();
            case "smart":
                return new SmartBot();
            default:
                return null;
        }
    }
}
