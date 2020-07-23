//A class that retrieves combinations of players from the CombinationGenerator and checks what their total score is.
//Stores the best result.  When compared to other TeamAnalyzers that used the same CombinationGenerator, the best
//team among each individual TeamAnalyzer's results is the best team over the entirety of the CombinationGenerator's
//domain.
public class TeamAnalyzer implements Runnable {
    private PlayerDatabase playerDataBase;
    private CombinationGenerator combinationGenerator;
    private Player[] bestCombination;
    private double bestScore = -1;
    private int maxCost = 0;
    public TeamAnalyzer(PlayerDatabase playerDataBase, CombinationGenerator cg, int maxCost) {
        this.playerDataBase = playerDataBase;
        this.combinationGenerator = cg;
        this.maxCost = maxCost;
        bestCombination = new Player[cg.getNumberOfPlayersOnTeam()];
    }


    /*
    While the combinationGenerator still has combinations of players to be checked, check whether the sum of their
    costs is both under the user-defined limit and the highest number of points out of any combination checked so far.
    If it is then save the combination and set currentMaxPoints to the sum of the player points.
     */
    @Override
    public void run() {
        //Variable declaration
        int currentPoints, currentPrice;
        int numOfPlayersOnTeam = combinationGenerator.getNumberOfPlayersOnTeam();
        int[] currentBest = new int[numOfPlayersOnTeam];
        int[] currentCombination;

        //Main logic loop.  See function comments above for explanation.
        while ((currentCombination = combinationGenerator.getNextCombination()) != null){
            currentPoints = currentPrice = 0;
            for (int i : currentCombination) {
                currentPoints += playerDataBase.get(i).points;
                currentPrice += playerDataBase.get(i).price;
            }
            if (currentPrice > maxCost) {
                continue;
            }
            if (currentPoints > bestScore) {
                bestScore = currentPoints;
                System.arraycopy(currentCombination, 0, currentBest, 0, currentCombination.length);
            }
        }

        //Set bestCombination equal to currentBest
        for (int i = 0; i < currentBest.length; i++) {
            bestCombination[i] = playerDataBase.get(currentBest[i]);
        }
    }

    //After run() has finished, this returns the Player[] representing the highest-scoring team this TeamAnalyzer found.
    public Player[] getBestCombination() {
        return this.bestCombination;
    }

    //After run() has finished, this returns the score of the highest-scoring team this TeamAnalyzer found.
    public double getBestScore() {
        return bestScore;
    }
}
