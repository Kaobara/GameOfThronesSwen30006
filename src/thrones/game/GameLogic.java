package thrones.game;

import ch.aplu.jcardgame.*;
import java.util.Optional;

public class GameLogic {
    public void setupHands(Hand[] hands, Deck deck, int nbPlayers, GoTCard gotCard) {
        for (int i = 0; i < nbPlayers; i++) {
            hands[i] = new Hand(deck);
        }
        gotCard.dealingOut(hands, nbPlayers, nbStartCards);

        for (int i = 0; i < nbPlayers; i++) {
            hands[i].sort(Hand.SortType.SUITPRIORITY, true);
            System.out.println("hands[" + i + "]: " + gotCard.canonical(hands[i]));
        }
    }
    public void setupHandHumanInteraction(GameOfThrones gameOfThrones, Hand[] hands){
        for (final Hand currentHand : hands) {
            // Set up human player for interaction
            currentHand.addCardListener(new CardAdapter() {
                public void leftDoubleClicked(Card card) {
                    gameOfThrones.setSelected(Optional.of(card));
                    currentHand.setTouchEnabled(false);
                }
                public void rightClicked(Card card) {
                    gameOfThrones.setSelected( Optional.empty());
//                    selected = Optional.empty(); // Don't care which card we right-clicked for player to pass
                    currentHand.setTouchEnabled(false);
                }
            });
        }
    }

    private int nbPlayers = 4;
    private int nbRounds = 3;
    private int nbStartCards = 9;
    private int NON_SELECTION_VALUE = -1;
    private final GameGraphic gameGraphic = new GameGraphic();

    private int getPlayerIndex(int index) {
        return index % nbPlayers;
    }

    public void part1(GameOfThrones gameOfThrones, int nextStartingPlayer, GoTCard gotCard, GoTPiles gotPiles, Deck deck, Hand[] hands, Player[] players) {
        gotPiles.resetPile(gameOfThrones, deck);

        // Check Hands has hearts (important for final play)
        nextStartingPlayer = getPlayerIndex(nextStartingPlayer);
        if (hands[nextStartingPlayer].getNumberOfCardsWithSuit(GoTCard.Suit.HEARTS) == 0)
            nextStartingPlayer = getPlayerIndex(nextStartingPlayer + 1);
        assert hands[nextStartingPlayer].getNumberOfCardsWithSuit(GoTCard.Suit.HEARTS) != 0 : " Starting player has no hearts.";

        // 1: play the first 2 hearts
        for (int i = 0; i < 2; i++) {
            int playerIndex = getPlayerIndex(nextStartingPlayer + i);
            gameOfThrones.setStatusText("Player " + playerIndex + " select a Heart card to play");

            players[playerIndex].playSuit(gameOfThrones, playerIndex, true, hands, gotCard);

            int pileIndex = playerIndex % 2;
            assert gameOfThrones.getSelected().isPresent() : " Pass returned on selection of character.";
            System.out.println("Player " + playerIndex + " plays " + gotCard.canonical(gameOfThrones.getSelected().get()) + " on pile " + pileIndex);
            gameOfThrones.getSelected().get().setVerso(false);
            gameOfThrones.getSelected().get().transfer(gotPiles.getPiles()[pileIndex], true); // transfer to pile (includes graphic effect)
            gotPiles.updatePileRanks(gameOfThrones);
        }
    }

    public void part2(GameOfThrones gameOfThrones, int nextStartingPlayer, GoTCard gotCard, GoTPiles gotPiles, Deck deck, Hand[] hands, Player[] players) {
        // 2: play the remaining nbPlayers * nbRounds - 2
        int remainingTurns = nbPlayers * nbRounds - 2;
        int nextPlayer = nextStartingPlayer + 2;

        while(remainingTurns > 0) {
            nextPlayer = getPlayerIndex(nextPlayer);
            gameOfThrones.setStatusText("Player" + nextPlayer + " select a non-Heart card to play.");

            // Select a card from hand
            players[nextPlayer].playSuit(gameOfThrones, nextPlayer, false, hands, gotCard);

            if (gameOfThrones.getSelected().isPresent()) {
                gameOfThrones.setStatusText("Selected: " + gotCard.canonical(gameOfThrones.getSelected().get()) + ". Player" + nextPlayer + " select a pile to play the card.");
                // Select a pile
                players[nextPlayer].playPile(gameOfThrones, gotPiles, gotCard, gameOfThrones.getSelected(), nextPlayer);
                if(gameOfThrones.getSelectedPileIndex() != NON_SELECTION_VALUE) {
                    System.out.println("Player " + nextPlayer + " plays " + gotCard.canonical(gameOfThrones.getSelected().get()) + " on pile " + gameOfThrones.getSelectedPileIndex());
                    gameOfThrones.getSelected().get().setVerso(false);
                    // REQUIRES OBSERVER IF SELECTED == DIAMOND FOR SNART BOTS
                    gotPiles.transferCardToPile(gameOfThrones.getSelected().get(), gameOfThrones.getSelectedPileIndex(), false);// transfer to pile (includes graphic effect)
                    gameGraphic.updatePileRankGraphics(gameOfThrones, gotPiles); // Update the pile ranks graphics
                }
            } else {
                gameOfThrones.setStatusText("Pass.");
            }
            nextPlayer++;
            remainingTurns--;
        }
    }

    public void part3(GameOfThrones gameOfThrones, GoTCard gotCard, GoTPiles gotPiles) {
        // 3: calculate winning & update scores for players
        gameGraphic.updatePileRankGraphics(gameOfThrones, gotPiles);

        int[] pile0Ranks = gotPiles.calculatePileRanks(0);
        int[] pile1Ranks = gotPiles.calculatePileRanks(1);

        System.out.println("piles[0]: " + gotCard.canonical(gotPiles.getPiles()[0]));
        System.out.println("piles[0] is " + "Attack: " + pile0Ranks[GameOfThrones.ATTACK_RANK_INDEX] + " - Defence: " + pile0Ranks[GameOfThrones.DEFENCE_RANK_INDEX]);
        System.out.println("piles[1]: " + gotCard.canonical(gotPiles.getPiles()[1]));
        System.out.println("piles[1] is " + "Attack: " + pile1Ranks[GameOfThrones.ATTACK_RANK_INDEX] + " - Defence: " + pile1Ranks[GameOfThrones.DEFENCE_RANK_INDEX]);

        GoTCard.Rank pile0CharacterRank = (GoTCard.Rank) gotPiles.getPiles()[0].getCardList().get(0).getRank();
        GoTCard.Rank pile1CharacterRank = (GoTCard.Rank) gotPiles.getPiles()[1].getCardList().get(0).getRank();
        String character0Result;
        String character1Result;

        // determine winner
        if (pile0Ranks[GameOfThrones.ATTACK_RANK_INDEX] > pile1Ranks[GameOfThrones.DEFENCE_RANK_INDEX]) {
            gameOfThrones.getScore().increaseScore(0, pile1CharacterRank.getRankValue());
            character0Result = "Character 0 attack on character 1 succeeded.";
        } else {
            gameOfThrones.getScore().increaseScore(1, pile1CharacterRank.getRankValue());
            character0Result = "Character 0 attack on character 1 failed.";
        }

        if (pile1Ranks[GameOfThrones.ATTACK_RANK_INDEX] > pile0Ranks[GameOfThrones.DEFENCE_RANK_INDEX]) {
            gameOfThrones.getScore().increaseScore(1, pile0CharacterRank.getRankValue());
            character1Result = "Character 1 attack on character 0 succeeded.";
        } else {
            gameOfThrones.getScore().increaseScore(0, pile0CharacterRank.getRankValue());
            character1Result = "Character 1 attack character 0 failed.";
        }

        gameGraphic.updateGraphicScores(gameOfThrones);
        System.out.println(character0Result);
        System.out.println(character1Result);
        gameOfThrones.setStatusText(character0Result + " " + character1Result);
    }

//    public void putSelectedToPile()
}
