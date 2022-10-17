package thrones.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.stream.Collectors;

public class CardInfo {
    enum GoTSuit { CHARACTER, DEFENCE, ATTACK, MAGIC }
    public enum Suit {
        SPADES(GameOfThrones.GoTSuit.DEFENCE),
        HEARTS(GameOfThrones.GoTSuit.CHARACTER),
        DIAMONDS(GameOfThrones.GoTSuit.MAGIC),
        CLUBS(GameOfThrones.GoTSuit.ATTACK);
        Suit(GameOfThrones.GoTSuit gotsuit) {
            this.gotsuit = gotsuit;
        }
        private final GameOfThrones.GoTSuit gotsuit;

        public boolean isDefence(){ return gotsuit == GameOfThrones.GoTSuit.DEFENCE; }

        public boolean isAttack(){ return gotsuit == GameOfThrones.GoTSuit.ATTACK; }

        public boolean isCharacter(){ return gotsuit == GameOfThrones.GoTSuit.CHARACTER; }

        public boolean isMagic(){ return gotsuit == GameOfThrones.GoTSuit.MAGIC; }
    }

    public enum Rank {
        // Reverse order of rank importance (see rankGreater() below)
        // Order of cards is tied to card images
        ACE(1), KING(10), QUEEN(10), JACK(10), TEN(10), NINE(9), EIGHT(8), SEVEN(7), SIX(6), FIVE(5), FOUR(4), THREE(3), TWO(2);
        Rank(int rankValue) {
            this.rankValue = rankValue;
        }
        private final int rankValue;
        public int getRankValue() {
            return rankValue;
        }
    }

    /*
    Canonical String representations of Suit, Rank, Card, and Hand
    */
//    String canonical(GameOfThrones.Suit s) { return s.toString().substring(0, 1); }
//
//    String canonical(GameOfThrones.Rank r) {
//        switch (r) {
//            case ACE: case KING: case QUEEN: case JACK: case TEN:
//                return r.toString().substring(0, 1);
//            default:
//                return String.valueOf(r.getRankValue());
//        }
//    }
//
//    String canonical(Card c) { return canonical((GameOfThrones.Rank) c.getRank()) + canonical((GameOfThrones.Suit) c.getSuit()); }
//
//    String canonical(Hand h) {
//        return "[" + h.getCardList().stream().map(this::canonical).collect(Collectors.joining(",")) + "]";
//    }
}
