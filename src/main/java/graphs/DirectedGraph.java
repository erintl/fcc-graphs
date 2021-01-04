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

    public void addEdge(String from, String to, double weight) {
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

    public Map<String, Double> sssp(String start) {
        Map<String, Double> distances = new HashMap<>();
        List<String> topOrder = getTopOrdering();
        for (String key : graph.keySet()) {
            distances.putIfAbsent(key, null);
        }
        distances.put(start, 0.0);

        for (String node : topOrder) {
            Double currentDistance = distances.get(node);
            if (currentDistance != null) {
                Set<Edge> adjEdges = graph.get(node);
                for (Edge edge : adjEdges) {
                    double newDistance = currentDistance + edge.weight;
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
        double weight = parts.length > 2 ? Double.parseDouble(parts[2]) : 0.0;

        addVertex(from);
        addVertex(to);
        addEdge(from, to, weight);
    }

    public Map<String, Double> dijkstraShortestPath(String start) {
        List<String> visited = new ArrayList<>();
        Map<String, Double> distances = new HashMap<>();
        PriorityQueue<DValue> priorityQueue = new PriorityQueue<>();
        for (String key : graph.keySet()) {
            distances.putIfAbsent(key, Double.POSITIVE_INFINITY);
        }
        distances.put(start, 0.0);
        priorityQueue.add(new DValue(start, 0.0));
        while (!priorityQueue.isEmpty()) {
            DValue currentNode = priorityQueue.poll();
            System.out.println(currentNode);
            visited.add(currentNode.getLabel());
            for (Edge edge : graph.get(currentNode.getLabel())) {
                if (visited.contains(edge.to)) {
                    continue;
                }
                double newDistance = distances.get(currentNode.getLabel()) + edge.weight;
                System.out.println("New Distance: " + currentNode.getLabel() + ":" + edge.to + "-" + newDistance);
                if (newDistance < distances.get(edge.to)) {
                    distances.put(edge.to, newDistance);
                    priorityQueue.add(new DValue(edge.to, newDistance));
                }
            }
        }
        return distances;
    }

    public Map<String, Double> bellmanFord(String start) {
        Map<String, Double> distances = new HashMap<>();
        List<Edge> edges = new ArrayList<>();
        for (String key : graph.keySet()) {
            edges.addAll(graph.get(key));
            distances.putIfAbsent(key, Double.POSITIVE_INFINITY);
        }
        distances.put(start, 0.0);

        for (int v = 0; v < graph.size() - 1; v++) {
            for (Edge edge : edges) {
                if (distances.get(edge.from) + edge.weight < distances.get(edge.to)) {
                    distances.put(edge.to, distances.get(edge.from) + edge.weight);
                }
            }
        }

        for (int v = 0; v < graph.size() - 1; v++) {
            for (Edge edge : edges) {
                if (distances.get(edge.from) + edge.weight < distances.get(edge.to)) {
                    distances.put(edge.to, Double.NEGATIVE_INFINITY);
                }
            }
        }
        return distances;
    }
}
