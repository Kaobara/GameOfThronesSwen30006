package thrones.game;

public class Score {
    private GameGraphic gameGraphic = new GameGraphic();
    private int [] scores;

    public Score(){}

    private void copyScores(int[] scores) {this.scores = scores; }
    public Score cloneScore(Score score) {
        Score cloneScore = new Score();
        cloneScore.copyScores(scores.clone());
        return cloneScore;
    }

    public int[] getScores() {
        return scores;
    }

    public void initScore(GameOfThrones got) {
        scores = new int[GameOfThrones.nbPlayers];

        for (int i = 0; i < GameOfThrones.nbPlayers; i++) {
            scores[i] = 0;
            String text = "P" + i + "-0";
            gameGraphic.scoreGraphic(got, i, text);
        }

        String text = "Attack: 0 - Defence: 0";
        for (int i = 0; i < got.getPileTextActors().length; i++) {
            gameGraphic.initPileScoreGraphic(got, i, text);
        }
    } // LOGIC FINE, LEAVE IT

    // Given a team number and character ranks, increase the score of that team
    private void increaseScore(int teamNumber, int valueIncrease) {
        for(int i = teamNumber%GameOfThrones.nbTeams; i<GameOfThrones.nbPlayers; i += 2) {
            this.scores[i] += valueIncrease;
        }
    }

    // Given piles, find the outcome, and increase the score depending on successful attacks
    public String [] battleScores(GoTPiles gotPiles, boolean printBool) {
        int[] pile0Ranks = gotPiles.calculatePileRanks(0);
        int[] pile1Ranks = gotPiles.calculatePileRanks(1);

        GoTCard gotCard = GoTCard.getInstance();

        if(printBool) {
            System.out.println("piles[0]: " + gotCard.canonical(gotPiles.getPiles()[0])); // QUESTION: SHOULD GOTCARD BE STATIC AND PUBLIC?
            System.out.println("piles[0] is " + "Attack: " + pile0Ranks[GameOfThrones.ATTACK_RANK_INDEX] + " - Defence: " + pile0Ranks[GameOfThrones.DEFENCE_RANK_INDEX]);
            System.out.println("piles[1]: " + gotCard.canonical(gotPiles.getPiles()[1]));
            System.out.println("piles[1] is " + "Attack: " + pile1Ranks[GameOfThrones.ATTACK_RANK_INDEX] + " - Defence: " + pile1Ranks[GameOfThrones.DEFENCE_RANK_INDEX]);
        }

        GoTCard.Rank pile0CharacterRank = (GoTCard.Rank) gotPiles.getPiles()[0].getCardList().get(0).getRank();
        GoTCard.Rank pile1CharacterRank = (GoTCard.Rank) gotPiles.getPiles()[1].getCardList().get(0).getRank();
        String character0Result;
        String character1Result;

        // determine winner
        if (pile0Ranks[GameOfThrones.ATTACK_RANK_INDEX] > pile1Ranks[GameOfThrones.DEFENCE_RANK_INDEX]) {
            increaseScore(0, pile1CharacterRank.getRankValue());
            character0Result = "Character 0 attack on character 1 succeeded.";
        } else {
            increaseScore(1, pile1CharacterRank.getRankValue());
            character0Result = "Character 0 attack on character 1 failed.";
        }

        if (pile1Ranks[GameOfThrones.ATTACK_RANK_INDEX] > pile0Ranks[GameOfThrones.DEFENCE_RANK_INDEX]) {
            increaseScore(1, pile0CharacterRank.getRankValue());
            character1Result = "Character 1 attack on character 0 succeeded.";
        } else {
            increaseScore(0, pile0CharacterRank.getRankValue());
            character1Result = "Character 1 attack character 0 failed.";
        }

        if(printBool) {
            System.out.println(character0Result);
            System.out.println(character1Result);
            String [] characterResults =  {character0Result, character1Result};
            return(characterResults);
        } else {
            return null;
        }
    }

    // Given score, find which team is winning (team 0 or 1)
    public int getWinningTeam() {
        if (scores[0] > scores[1]) {
            return 0;
        } else if (scores[1] > scores[0]) {
            return 1;
        } else {
            return -1; // -1 is DRAW
        }
    }

    public void finalScores(GameOfThrones got) {
        // Final scores - leave for now
        String text;
        if (scores[0] > scores[1]) {
            text = "Players 0 and 2 won.";
        } else if (scores[0] == scores[1]) {
            text = "All players drew.";
        } else {
            text = "Players 1 and 3 won.";
        }
        System.out.println("Result: " + text);
        got.setStatusText(text);
    }
}
