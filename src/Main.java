import java.util.*;

public class Main {
    public static void main(String[] args) {
        UserInterface ui = new UserInterface();
        int maxCost = ui.getMaxCost();
        int numOfPlayersOnTeam = ui.getNumOfPlayersOnTeam();
        int numOfThreads = ui.getNumberOfThreads();
        PlayerDatabase pdb = new PlayerDatabase(ui.getPlayerArray(numOfPlayersOnTeam));
        pdb.cleanPlayerData(numOfPlayersOnTeam);

        System.out.println("The best combination of players is:");
        for (Player p : getBestCombination(pdb, maxCost, numOfPlayersOnTeam, numOfThreads)) {
            System.out.println(p.name);
        }
    }

    /*
    Create an array of TeamAnalyzers that will check all possible combinations of players between them, using
    the CombinationGenerator to synchronize their efforts.  Stall until all TeamAnalyzers are done working.
    Return the highest-scoring team out of all of the TeamAnalyzer's individual best teams,
    which will be the best team for all player combinations.
     */
    private static Player[] getBestCombination(PlayerDatabase pdb, int maxCost, int numOfPlayersOnTeam, int numOfThreads) {
        CombinationGenerator cg = new CombinationGenerator(numOfPlayersOnTeam, pdb.size());
        TeamAnalyzer[] analyzerArray = new TeamAnalyzer[numOfThreads];
        Thread[] threads = new Thread[numOfThreads];
        for (int i = 0; i < threads.length; i++) {
            analyzerArray[i] = new TeamAnalyzer(pdb, cg, maxCost);
            threads[i] = new Thread(analyzerArray[i]);
            threads[i].start();
        }
        waitForThreadCompletion(threads);
        return extractBestCombinationFromAnalyzers(analyzerArray);
    }

    //Take the best team from each TeamAnalyzer in @param analyzers and return the highest-scoring team out of all of them.
    private static Player[] extractBestCombinationFromAnalyzers(TeamAnalyzer[] analyzers) {
        int best = 0;
        double bestScore = 0;
        for (int i = 0; i < analyzers.length; i++) {
            if (analyzers[i].getBestScore() > bestScore) {
                bestScore = analyzers[i].getBestScore();
                best = i;
            }
        }
        return analyzers[best].getBestCombination();
    }

    //Take a Thread[] and return once all of them are done executing.
    private static void waitForThreadCompletion(Thread[] threads) {
        for (Thread t : threads) {
            while (t.isAlive()) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    System.out.println("Interrupted while waiting for threads to finish");
                    e.printStackTrace();
                    System.exit(0);
                }
            }
        }
    }
}