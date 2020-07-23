import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class PlayerDatabase {
    private Player[] database;
    public PlayerDatabase(Player[] database) {
        this.database = database;
    }

    public Player get(int index) {
        return database[index];
    }

    public int size() {
        return database.length;
    }

    //Group players according to their cost, then remove the groupSize-numOfPlayersOnTeam lowest scoring players
    //for each group.  Reduces runtime significantly when many players have the same cost.
    public void cleanPlayerData(int numOfPlayersOnTeam) {
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
}