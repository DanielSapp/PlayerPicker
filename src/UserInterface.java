import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

//A class that abstracts away all command-line user interaction.  Each method prompts the user
//for a parameter that is needed for the program, validates it, and returns it.
public class UserInterface {
    private final Scanner scanner = new Scanner(System.in);

    //Ask the user for the maximum amount the team can cost, validate the input, and return it.
    public int getMaxCost() {
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

    //Prompt the user for the number of players on the team, loop until valid input is received, and return the input.
    public int getNumOfPlayersOnTeam() {
        System.out.println("How many players should be on the team?");
        while (true) {
            try {
                String numOfPlayersString = scanner.nextLine();
                int numOfPlayers = Integer.parseInt(numOfPlayersString);
                if (numOfPlayers > 0) {
                    return numOfPlayers;
                } else {
                    System.out.println("Input must be a positive integer");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.  How many players should be on the team?");
            }
        }
    }

    public int getNumberOfThreads() {
        System.out.println("How many threads should be run?");
        while (true) {
            try {
                String result = scanner.nextLine();
                int resultInt = Integer.parseInt(result);
                if (resultInt > 0) {
                    return resultInt;
                } else {
                    System.out.println("Invalid input: number of threads must be a positive integer");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input");
            }
        }
    }

    //Call getPlayerStringArray() to get a String[] representing the data in the CSV file.
    //Exit if there are fewer players than the declared team size. Return a Player[] created out of this data
    public Player[] getPlayerArray(int numOfPlayersOnTeam) {
        String[] playerStringArray = getPlayerStringArray();
        Player[] toReturn = new Player[playerStringArray.length/3];
        if (toReturn.length < numOfPlayersOnTeam) {
            System.out.println("Fatal error: there are not enough players in the file to create a team");
            System.exit(1);
        }
        for (int i = 0; i < toReturn.length; i++) {
            toReturn[i] = new Player(playerStringArray[i*3], Double.parseDouble(playerStringArray[i*3+1]), Integer.parseInt(playerStringArray[i*3+2]));
        }
        return toReturn;
    }

    //Prompt the user for a CSV file using getFileName().  Return a String[] representing the values in the file once
    //a valid file has been read.  Exit if an IOException caused by anything besides a invalid file location is thrown.
    private String[] getPlayerStringArray() {
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

    //Prompt the user for the location of a CSV file and return the location.
    private String getFileName() {
        System.out.println("What is the name of the player CSV?");
        return scanner.nextLine();
    }
}
