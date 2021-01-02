import graphs.Dungeon;

public class DungeonRunner {
    public static void main(String[] args) {
        Dungeon dungeon = new Dungeon();
        int moveCount = dungeon.solve();
        System.out.println("Move count: " + moveCount);
    }
}
