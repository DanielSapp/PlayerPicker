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

    //Prompt the user for the location of a CSV file and return the location.
    public String getFileName() {
        System.out.println("What is the name of the player CSV?");
        return scanner.nextLine();
    }
}
