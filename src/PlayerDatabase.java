import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

//A class that represents the list of Players to be chosen from.  Ensures the Player[] cannot be altered and
//has a method to remove players that couldn't be possible options on the optimal team.
public class PlayerDatabase {
    private Player[] database;
    public PlayerDatabase() {
    }

    public Player get(int index) {
        if (database == null) {return null;}
        return database[index];
    }

    public int size() {
        return database.length;
    }

    public void initialize(String fileName, int numOfPlayersOnTeam) throws FileNotFoundException {
        database = getPlayerArray(fileName, numOfPlayersOnTeam);
        cleanPlayerData(numOfPlayersOnTeam);
    }

    //Group players according to their cost, then remove the groupSize-numOfPlayersOnTeam lowest scoring players
    //for each group.  Reduces runtime significantly when many players have the same cost.
    private void cleanPlayerData(int numOfPlayersOnTeam) {
        HashMap<Integer, ArrayList<Player>> map = new HashMap<>();
        LinkedList<Player> newData = new LinkedList<>();
        for (Player p : database) {
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
            newData.addAll(entry.getValue());
        }
        database = newData.toArray(new Player[0]);
    }

    //Call getPlayerStringArray() to get a String[] representing the data in the CSV file.
    //Exit if there are fewer players than the declared team size. Return a Player[] created out of this data
    private Player[] getPlayerArray(String fileName, int numOfPlayersOnTeam) throws FileNotFoundException {
        String[] playerStringArray = getPlayerStringArray(fileName);
        Player[] toReturn = new Player[playerStringArray.length];
        if (toReturn.length < numOfPlayersOnTeam) {
            System.out.println("Fatal error: there are not enough players in the file to create a team");
            System.exit(1);
        }
        for (int i = 0; i < toReturn.length; i++) {
            String[] playerAsString = playerStringArray[i].split(",");
            toReturn[i] = new Player(playerAsString[0], Double.parseDouble(playerAsString[1]), Integer.parseInt(playerAsString[2]));
        }
        return toReturn;
    }

    //Prompt the user for a CSV file using getFileName().  Return a String[] representing the values in the file once
    //a valid file has been read.  Exit if an IOException caused by anything besides a invalid file location is thrown.
    //Throws FileNotFoundException, which is passed back up to Main where it is handled.
    private String[] getPlayerStringArray(String fileName) throws FileNotFoundException {
        StringBuilder sb;
        sb = new StringBuilder();
        try(FileInputStream fis = new FileInputStream(new File(fileName))) {
            int data;
            while ((data = fis.read()) != -1) {
                sb.append((char) data);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Invalid file name");
            throw e;
        } catch (IOException e) {
            System.out.println("IOException");
            System.exit(-1);
        }
        return sb.toString().split("\r?\n");
    }
}