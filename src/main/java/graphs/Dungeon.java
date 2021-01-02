package graphs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Dungeon {
    public static String FILENAME = "input/dungeon1.txt";
    // directions: north, south, east, west
    public static int[] directionsRow = { -1, 1, 0, 0 };
    public static int[] directionColumn = { 0, 0, 1, -1 };
    private char[][] map;
    private boolean[][] visited;
    private int rowCount;
    private int columnCount;
    private final int startRow;
    private final int startColumn;
    int nodesLeftInLayer;
    int nodesInNextLayer;
    Queue<Integer> rowQueue;
    Queue<Integer> columnQueue;
    boolean reachedEnd;

    public Dungeon() {
        startRow = 0;
        startColumn = 0;
        loadDungeon();
        printDungeonMap();
    }

    public void printDungeonMap() {
        for (char[] line : map) {
            System.out.println(line);
        }
    }

    public int solve() {
        visited = new boolean[rowCount][columnCount];
        int moveCount = 0;
        nodesLeftInLayer = 1;
        nodesInNextLayer = 0;
        rowQueue = new LinkedList<>();
        columnQueue =  new LinkedList<>();
        reachedEnd = false;

        rowQueue.add(startRow);
        columnQueue.add(startColumn);
        visited[startRow][startColumn] = true;
        printPosition(startRow, startColumn);
        while(!rowQueue.isEmpty() && !columnQueue.isEmpty()) {
            int row  = rowQueue.poll();
            int col = columnQueue.poll();
            if (map[row][col] == 'E') {
                reachedEnd = true;
                break;
            }
            exploreNeighborCells(row, col);
            nodesLeftInLayer--;
            if (nodesLeftInLayer == 0) {
                nodesLeftInLayer = nodesInNextLayer;
                nodesInNextLayer = 0;
                moveCount++;
            }
        }
        return reachedEnd ? moveCount : -1;
    }

    private void loadDungeon() {
        List<String> lines = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File(FILENAME));
            setShape(scanner.nextLine());
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        buildDungeonMap(lines);
    }

    private void buildDungeonMap(List<String> lines) {
        map = new char[rowCount][columnCount];

        for (int i = 0; i < lines.size(); i++) {
            char[] lineChars = lines.get(i).toCharArray();
            for (int j = 0; j < lineChars.length; j++) {
                map[i][j] = lineChars[j];
            }
        }
    }

    private void setShape(String line) {
        String[] parts = line.split(" ");
        rowCount = Integer.parseInt(parts[0]);
        columnCount = Integer.parseInt(parts[1]);
    }

    private void printPosition(int row, int column) {
        System.out.printf("(%d, %d)\n", row, column);
    }

    private void exploreNeighborCells(int row, int col) {
        int nextNodes = nodesInNextLayer;

        for (int i = 0; i < 4; i++) {
            int neighborRow = row + directionsRow[i];
            int neighborColumn = col + directionColumn[i];

            if (neighborRow < 0 || neighborColumn < 0) {
                continue;
            }
            if (neighborRow >= rowCount || neighborColumn >= columnCount) {
                continue;
            }
            if (visited[neighborRow][neighborColumn]) {
                continue;
            }
            if (map[neighborRow][neighborColumn] == '#') {
                continue;
            }

            rowQueue.add(neighborRow);
            columnQueue.add(neighborColumn);
            visited[neighborRow][neighborColumn] = true;
            printPosition(neighborRow, neighborColumn);
            nodesInNextLayer++;
        }
    }
}
