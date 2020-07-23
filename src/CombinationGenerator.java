public class CombinationGenerator {
    private final int totalPlayers;
    private final int[] currentState;
    boolean firstCall = true;

    public CombinationGenerator(int playersOnTeam, int totalPlayers) {
        //Validate input
        if (playersOnTeam > totalPlayers || playersOnTeam < 1) {
            throw new IllegalArgumentException();
        }

        //Initialize currentState to the first combination of ints
        currentState = new int[playersOnTeam];
        for (int i = 0; i < playersOnTeam; i++) {
            currentState[i] = i;
        }
        this.totalPlayers = totalPlayers;
    }

    /*
    Returns the next combination of players to be checked.  Calling this method repeatedly with @param currentCombination
    initialized to {0,1,2,...} will return all possible arrays with unique values under numOfPlayers exactly once, one
    per call, and will return null when a new combination cannot be made.  Synchronized so that multiple TeamAnalyzers
    can take different subsets of the total possible player combinations and analyze them simultaneously.
     */
    public synchronized int[] getNextCombination() {
        //If this is the first call to this object, return the initial settings of currentState
        if (firstCall) {
            firstCall = false;
            return currentState;
        }

        //Find the last index that can be incremented by one.
        int indexBeingChecked = currentState.length-1;
        while (indexBeingChecked != -1 && currentState[indexBeingChecked] == totalPlayers-currentState.length+indexBeingChecked) {
            indexBeingChecked--;
        }

        //If no index can be incremented (all combinations have been exhausted), return null
        if (indexBeingChecked == -1) {return null;}

        //Increment the index
        currentState[indexBeingChecked]++;

        //For each index after, set it to one more than the previous index.  Return the new state.
        indexBeingChecked++;
        while (indexBeingChecked < currentState.length) {
            currentState[indexBeingChecked] = currentState[indexBeingChecked-1]+1;
            indexBeingChecked++;
        }
        return currentState;
    }

    public int getNumberOfPlayersOnTeam() {
        return currentState.length;
    }
}
