package graphs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class DirectedGraph {
    private final Map<String, Set<Edge>> graph;

    public DirectedGraph() {
        graph = new HashMap<>();
    }

    public DirectedGraph(String filename) {
        this();
        loadGraphFromFile(filename);
    }

    public void loadGraphFromFile(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                processLine(scanner.nextLine());
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addVertex(String label) {
        graph.putIfAbsent(label, new LinkedHashSet<>());
    }

    public void addEdge(String from, String to, int weight) {
        Edge edge = new Edge(from, to, weight);
        Set<Edge> edges = graph.get(from);
        edges.add(edge);
    }

    public void dfs(String start) {
        List<String> visited = new ArrayList<>();
        dfsUtil(start, visited);
    }

    private void dfsUtil(String node, List<String> visited) {
        if (visited.contains(node)) {
            return;
        }
        visited.add(node);
        System.out.println(node);
        for (Edge edge : graph.get(node)) {
            dfsUtil(edge.to, visited);
        }
    }

    public void printGraph() {
        System.out.println(graph);
    }

    public List<String> getTopOrdering() {
        List<String> visited = new ArrayList<>();
        List<String> topOrder = new ArrayList<>();

        for (String key : graph.keySet()) {
            topDfsUtil(key, visited, topOrder);
        }
        Collections.reverse(topOrder);
        return topOrder;
    }

    public Map<String, Integer> sssp(String start) {
        Map<String, Integer> distances = new HashMap<>();
        List<String> topOrder = getTopOrdering();
        for (String key : graph.keySet()) {
            distances.putIfAbsent(key, null);
        }
        distances.put(start, 0);

        for (String node : topOrder) {
            Integer currentDistance = distances.get(node);
            if (currentDistance != null) {
                Set<Edge> adjEdges = graph.get(node);
                for (Edge edge : adjEdges) {
                    int newDistance = currentDistance + edge.weight;
                    distances.merge(edge.to, newDistance, Math::min);
                }
            }
        }
        return distances;
    }

    private void topDfsUtil(String node, List<String> visited, List<String> topOrder) {
        if (visited.contains(node)) {
            return;
        }
        visited.add(node);
        for (Edge edge : graph.get(node)) {
            topDfsUtil(edge.to, visited, topOrder);
        }
        topOrder.add(node);
    }

    private void processLine(String line) {
        String[] parts = line.split(" ");
        String from = parts[0];
        String to = parts[1];
        int weight = parts.length > 2 ? Integer.parseInt(parts[2]) : 0;

        addVertex(from);
        addVertex(to);
        addEdge(from, to, weight);
    }
}
