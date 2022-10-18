package thrones.game;

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.TextActor;

import java.awt.*;
import java.util.ArrayList;

public class GoTPiles {
    public ArrayList<SmartBot> smartBotObservers;

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
        System.out.println(pileIndex + " " + currentPile.getCardList());
        int atk = 0;
        int def = 0;
        if(!currentPile.isEmpty()) {
            Card previousCard = null;
            Card lastNonMagicCard = null;

            for(Card card : currentPile.getCardList()) {
                // First card in the pile (character)
                if(card == currentPile.getCardList().get(0)) {
                    atk = ((GoTCard.Rank)card.getRank()).getRankValue();
                    def = ((GoTCard.Rank)card.getRank()).getRankValue();
                    previousCard = card;
                    lastNonMagicCard = card;
                    continue;
                }

                if (((GoTCard.Suit) card.getSuit()).isMagic()) {
                    if (((GoTCard.Suit) lastNonMagicCard.getSuit()).isAttack()) {
                        if (((GoTCard.Rank) card.getRank()).getRankValue() == ((GoTCard.Rank) previousCard.getRank()).getRankValue()) {
                            atk -= 2 * ((GoTCard.Rank) card.getRank()).getRankValue();
                        } else {
                            atk -= ((GoTCard.Rank) card.getRank()).getRankValue();
                        }
                    } else if (((GoTCard.Suit) lastNonMagicCard.getSuit()).isDefence()) {
                        if (((GoTCard.Rank) card.getRank()).getRankValue() == ((GoTCard.Rank) previousCard.getRank()).getRankValue()) {
                            def -= 2 * ((GoTCard.Rank) card.getRank()).getRankValue();
                        } else {
                            def -= ((GoTCard.Rank) card.getRank()).getRankValue();
                        }
                    }
                }

                else if (((GoTCard.Suit) card.getSuit()).isAttack()) {
                    if (((GoTCard.Rank) card.getRank()).getRankValue() == ((GoTCard.Rank) previousCard.getRank()).getRankValue()) {
                        atk += 2 * ((GoTCard.Rank) card.getRank()).getRankValue();
                    } else {
                        atk += ((GoTCard.Rank) card.getRank()).getRankValue();
                    }

                    lastNonMagicCard = card;
                } else if (((GoTCard.Suit) card.getSuit()).isDefence()) {
                    if (((GoTCard.Rank) card.getRank()).getRankValue() == ((GoTCard.Rank) previousCard.getRank()).getRankValue()) {
                        def += 2 * ((GoTCard.Rank) card.getRank()).getRankValue();
                    } else {
                        def += ((GoTCard.Rank) card.getRank()).getRankValue();
                    }

                    lastNonMagicCard = card;
                }


                previousCard = card;
            }
        }

        return new int[] { atk, def };
    }

    public void updatePileRanks(GameOfThrones gameOfThrones) {
        for (int j = 0; j < piles.length; j++) {
            int[] ranks = calculatePileRanks(j);
            System.out.println(ranks[gameOfThrones.getATTACK_RANK_INDEX()]);
            gameGraphic.updatePileRankState(j, ranks[gameOfThrones.getATTACK_RANK_INDEX()], ranks[gameOfThrones.getDEFENCE_RANK_INDEX()], gameOfThrones);
        }
    }

    public void registerObserver(SmartBot smartBot) {
        smartBotObservers.add(smartBot);
    }

    public void unregisterObserver(SmartBot smartBot) {
        if(smartBotObservers.contains(smartBot)) {
            smartBotObservers.remove(smartBot);
        }
    }

    public void notifySmartBot(Card card) {
        return;
    }

    public void transferCardToPile(Card card, int pileIndex, boolean predicted) {
        if(!predicted) {
            card.transfer(piles[pileIndex], true);
            notifySmartBot(card);
        } else {
            card.transfer(piles[pileIndex], false);
        }
    }
}
