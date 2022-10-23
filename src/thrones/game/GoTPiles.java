package thrones.game;

import ch.aplu.jcardgame.*;

import java.util.ArrayList;
/**
 * Contains the relevant attributes and methods for handling piles.
 * Monday Team 7
 * @author: Mohamad Danielsyah Mahmud
 * @author: Khai Syuen Tan
 * @author: Bhavika Shrestha
 */
public class GoTPiles {
    final int NB_PILES = 2;
    private Hand[] piles;
    GameGraphic gameGraphic = new GameGraphic();
    private static ArrayList<SmartBot> smartBotObservers = new ArrayList<>();

    public Hand[] getPiles() {
        return piles;
    }

    private void setPiles(Hand[] piles) {this.piles = piles; }
    public GoTPiles clonePiles(GoTPiles gotPiles) {
        GoTPiles clonePiles = new GoTPiles();
        clonePiles.setPiles(piles.clone());
        return clonePiles;
    }

    public void resetPile(GameOfThrones got, Deck deck) {
        if (piles != null) {
            for (Hand pile : piles) {
                pile.removeAll(true);
            }
        }
        piles = new Hand[NB_PILES];
        for (int i = 0; i < NB_PILES; i++) {
            piles[i] = new Hand(deck);

            // TURN THIS INTO GRAPHICS
            gameGraphic.resetPileGraphic(got, i, piles);

            final Hand currentPile = piles[i];
            final int pileIndex = i;

            //setup player interaction
            piles[i].addCardListener(new CardAdapter() {
                public void leftClicked(Card card) {
                    got.setSelectedPileIndex(pileIndex);
                    currentPile.setTouchEnabled(false);
                }
            });
        }

        updatePileRanks(got);
    }

    public int[] calculatePileRanks(int pileIndex) {
        Hand currentPile = piles[pileIndex];
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

        if(atk < 0) {
            atk = 0;
        }
        if(def<0) {
            def = 0;
        }
        return new int[] { atk, def };
    }

    public void updatePileRanks(GameOfThrones got) {
        for (int j = 0; j < piles.length; j++) {
            int[] ranks = calculatePileRanks(j);
            gameGraphic.updatePileRankState(j, ranks[GameOfThrones.ATTACK_RANK_INDEX], ranks[GameOfThrones.DEFENCE_RANK_INDEX], got);
        }
    }

    public void registerObserver(SmartBot smartBot) {
        smartBotObservers.add(smartBot);
    }

    public void notifySmartBot(Card card) {
        for (SmartBot smartBot: smartBotObservers) {
            smartBot.updatePlayedMagicCards(card);
        }
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
