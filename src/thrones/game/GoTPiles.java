package thrones.game;

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.TextActor;

import java.awt.*;

public class GoTPiles {
    final int NB_PILES = 2;
    private Hand[] piles;
    GameGraphic gameGraphic = new GameGraphic();

    public Hand[] getPiles() {
        return piles;
    }

    public void resetPile(GameOfThrones gameOfThrones, Deck deck) {
        if (piles != null) {
            for (Hand pile : piles) {
                pile.removeAll(true);
            }
        }
        piles = new Hand[NB_PILES];
        for (int i = 0; i < NB_PILES; i++) {
            piles[i] = new Hand(deck);

            // TURN THIS INTO GRAPHICS
            gameGraphic.resetPileGraphic(gameOfThrones, i, piles);

            final Hand currentPile = piles[i];
            final int pileIndex = i;

            //setup player interaction
            piles[i].addCardListener(new CardAdapter() {
                public void leftClicked(Card card) {
                    gameOfThrones.setSelectedPileIndex(pileIndex);
                    currentPile.setTouchEnabled(false);
                }
            });
        }

        updatePileRanks(gameOfThrones);
    }

    public int[] calculatePileRanks(int pileIndex) {
        Hand currentPile = piles[pileIndex];
        int i = currentPile.isEmpty() ? 0 : ((GoTCard.Rank) currentPile.get(0).getRank()).getRankValue();
        return new int[] { i, i };
    }

    public void updatePileRanks(GameOfThrones gameOfThrones) {
        for (int j = 0; j < piles.length; j++) {
            int[] ranks = calculatePileRanks(j);
            System.out.println(ranks[gameOfThrones.getATTACK_RANK_INDEX()]);
            gameGraphic.updatePileRankState(j, ranks[gameOfThrones.getATTACK_RANK_INDEX()], ranks[gameOfThrones.getDEFENCE_RANK_INDEX()], gameOfThrones);
        }
    }


}
