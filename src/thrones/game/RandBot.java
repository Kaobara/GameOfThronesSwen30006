package thrones.game;

import ch.aplu.jcardgame.Card;

import java.util.Optional;

public class RandBot extends Bot{
    RandBot() {
        super();
    }

    @Override
    public void playPile(GameOfThrones gameOfThrones, GoTPiles gotPiles, GoTCard gotCard, Optional<Card> Selected) {
        int tempSelectedPileIndex = gotCard.random.nextInt(2);
        System.out.println(gotPiles.getPiles()[tempSelectedPileIndex].getLast().getSuit() + " JSDLIFJD");


        if(gotPiles.getPiles()[tempSelectedPileIndex].getLast().getSuit() == GoTCard.Suit.HEARTS && Selected.get().getSuit() == GoTCard.Suit.DIAMONDS) {
            System.out.println("AAA");
            gameOfThrones.setSelectedPileIndex(-1);
        } else {
            gameOfThrones.setSelectedPileIndex(tempSelectedPileIndex);
        }


//        selectedPileIndex = gotCard.random.nextInt(2);

    }
}