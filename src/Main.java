import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        int maxCost = getMaxCost();
        int numOfPlayersOnTeam = getNumOfPlayersOnTeam();
        Player[] players = cleanPlayerData(getPlayerArray(), numOfPlayersOnTeam);
        Player[] bestCombination = getBestCombination(players, maxCost, numOfPlayersOnTeam);
        System.out.println("The best combination of players is:");
        for (Player p : bestCombination) {
            System.out.println(p.name);
        }
    }

    //Asks the user for the maximum amount the team can cost, validates the input, and returns it.
    private static int getMaxCost() {
        while (true) {
            System.out.println("What is the maximum cost of the team?");
            int result = -1;
            try {
                String costString = scanner.nextLine();
                result = Integer.parseInt(costString);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.  What is the maximum cost of the team?");
            }
            if (result > 0) {
                return result;
            }
            System.out.println("Invalid input.  Only positive numbers are acceptable.");
        }
    }

    //Prompts the user for the number of players on the team, loops until valid input is received, and returns it.
    private static int getNumOfPlayersOnTeam() {
        System.out.println("How many players should be on the team?");
        while (true) {
            try {
                String numOfPlayersString = scanner.nextLine();
                int numOfPlayers = Integer.parseInt(numOfPlayersString);
                return numOfPlayers;
            } catch (NumberFormatException e) {
                System.out.println("Invalid number.  How many players should be on the team?");
            }
        }
    }

    //Calls getPlayerStringArray() to get a String[] representing the data in the CSV file.  Returns a Player[]
    //created out of this data
    private static Player[] getPlayerArray() {
        String[] playerStringArray = getPlayerStringArray();
        Player[] toReturn = new Player[playerStringArray.length/3];
        for (int i = 0; i < toReturn.length; i++) {
            toReturn[i] = new Player(playerStringArray[i*3], Double.parseDouble(playerStringArray[i*3+1]), Integer.parseInt(playerStringArray[i*3+2]));
        }
        return toReturn;
    }

    //Prompts the user for a CSV file using getFileName().  Returns a String[] representing the values in the file once
    //a valid file has been read.  Exits if an IOException caused by anything besides a invalid file location is thrown.
    private static String[] getPlayerStringArray() {
        StringBuilder sb;
        while (true) {
            sb = new StringBuilder();
            try(FileInputStream fis = new FileInputStream(new File(getFileName()))) {
                int data;
                while ((data = fis.read()) != -1) {
                    sb.append((char) data);
                }
                break;
            } catch (FileNotFoundException e) {
                System.out.println("Invalid file name");
            } catch (IOException e) {
                System.out.println("IOException");
                System.exit(-1);
            }
        }
        return sb.toString().split(",");
    }

    //Prompts the user for the location of a CSV file and returns the location.
    private static String getFileName() {
        System.out.println("What is the name of the player CSV?");
        return scanner.nextLine();
    }

    /*
    This function is the main logic of the program.  For every possible combination of players, it checks
    whether the sum of their costs is under the user-defined limit and if it is the highest number
    of points out of any combination so far.  If it is the highest-scoring combination, the combination
    is saved and currentMaxPoints is set to the sum of the player points.
     */
    private static Player[] getBestCombination(Player[] players, int maxCost, int numOfPlayersOnTeam) {
        //Variable declaration
        int currentPoints, currentPrice;
        int[] currentBest = new int[numOfPlayersOnTeam];
        Player[] toReturn = new Player[numOfPlayersOnTeam];
        double currentMaxPoints = -1;
        CombinationGenerator combinationGenerator = new CombinationGenerator(numOfPlayersOnTeam, players.length);
        int[] currentCombination;

        //Main logic loop.  See function comments above for explanation.
        while ((currentCombination = combinationGenerator.getNextCombination()) != null){
            currentPoints = currentPrice = 0;
            for (int i : currentCombination) {
                currentPoints += players[i].points;
                currentPrice += players[i].price;
            }
            if (currentPrice > maxCost) {
                continue;
            }
            if (currentPoints > currentMaxPoints) {
                currentMaxPoints = currentPoints;
                System.arraycopy(currentCombination, 0, currentBest, 0, currentCombination.length);
            }
        }

        //Return Player[] toReturn representing the best combination of Players to main
        for (int i = 0; i < currentBest.length; i++) {
            toReturn[i] = players[currentBest[i]];
        }
        return toReturn;
    }

    //This method cleans the player data by grouping players according to their cost, then removing
    //the groupSize-numOfPlayersOnTeam lowest scoring players for each group.  Reduces runtime significantly
    //when many players have the same cost.
    private static Player[] cleanPlayerData(Player[] ar, int numOfPlayersOnTeam) {
        HashMap<Integer, ArrayList<Player>> map = new HashMap<>();
        LinkedList<Player> toReturn = new LinkedList<>();
        for (Player p : ar) {
            map.putIfAbsent(p.price, new ArrayList<>());
            map.get(p.price).add(p);
        }
        for (Map.Entry<Integer, ArrayList<Player>> entry : map.entrySet()) {
            if (entry.getValue().size() > numOfPlayersOnTeam) {
                entry.getValue().sort((playerOne, playerTwo) -> playerOne.points < playerTwo.points ? 1 : -1);
                for (int i = numOfPlayersOnTeam; i < entry.getValue().size(); i++) {
                    entry.getValue().remove(i);
                }
            }
            toReturn.addAll(entry.getValue());
        }
        return toReturn.toArray(new Player[0]);
    }
}