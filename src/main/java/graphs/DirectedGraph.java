package graphs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class DirectedGraph {
    private final Map<String, Set<Edge>> graph;
    // Algorithm housekeeping
    private Map<String, Integer> ids;
    private Map<String, Integer> lows;
    private List<String> visited;
    private int currentId;
    private int outEdgeCount;

    public DirectedGraph() {
        graph = new HashMap<>();
    }

    public DirectedGraph(String filename, boolean directed) {
        this();
        loadGraphFromFile(filename, directed);
    }

    public void loadGraphFromFile(String filename, boolean directed) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                processLine(scanner.nextLine(), directed);
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

    public void addEdgeUndirected(String from, String to, double weight) {
        Edge srcEdge = new Edge(from, to, weight);
        Edge dstEdge = new Edge(to, from, weight);
        Set<Edge> srcEdges = graph.get(from);
        Set<Edge> dstEdges = graph.get(to);
        srcEdges.add(srcEdge);
        dstEdges.add(dstEdge);
    }

    public void dfs(String start) {
        visited = new ArrayList<>();
        dfsUtil(start);
    }

    private void dfsUtil(String node) {
        if (visited.contains(node)) {
            return;
        }
        visited.add(node);
        System.out.println(node);
        for (Edge edge : graph.get(node)) {
            dfsUtil(edge.to);
        }
    }

    public void printGraph() {
        System.out.println(graph);
    }

    public List<String> getTopOrdering() {
        visited = new ArrayList<>();
        List<String> topOrder = new ArrayList<>();

        for (String key : graph.keySet()) {
            topDfsUtil(key, topOrder);
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

    private void topDfsUtil(String node, List<String> topOrder) {
        if (visited.contains(node)) {
            return;
        }
        visited.add(node);
        for (Edge edge : graph.get(node)) {
            topDfsUtil(edge.to, topOrder);
        }
        topOrder.add(node);
    }

    private void processLine(String line, boolean directed) {
        String[] parts = line.split(" ");
        String from = parts[0];
        String to = parts[1];
        double weight = parts.length > 2 ? Double.parseDouble(parts[2]) : 0.0;

        addVertex(from);
        addVertex(to);
        if (directed) {
            addEdge(from, to, weight);
        } else {
            addEdgeUndirected(from, to, weight);
        }
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

    public List<String> findBridges() {
        List<String> bridges = new ArrayList<>();
        currentId = 0;
        visited = new ArrayList<>();
        ids  = new HashMap<>();
        lows = new HashMap<>();
        for (String key : graph.keySet()) {
            ids.putIfAbsent(key, 0);
            ids.putIfAbsent(key, 0);
        }

        for (String key : graph.keySet()) {
            if (!visited.contains(key)) {
                bridgeDfsUtil(key, null, bridges);
            }
        }
        return bridges;
    }

    private void bridgeDfsUtil(String root, String parent, List<String> bridges) {
        visited.add(root);
        currentId++;
        lows.put(root, currentId);
        ids.put(root, currentId);

        for (Edge toEdge : graph.get(root)) {
            String toNode = toEdge.to;
            if (toNode.equals(parent)) {
                System.out.println("to parent: " + root + ":" + toNode);
                continue;
            }
            if (!visited.contains(toNode)) {
                bridgeDfsUtil(toNode, root, bridges);
                lows.put(root, Math.min(lows.get(root), lows.get(toNode)));
                if (ids.get(root) < lows.get(toNode)) {
                    bridges.add(root);
                    bridges.add(toNode);
                }
            } else {
                lows.put(root, Math.min(lows.get(root), ids.get(toNode)));
            }
        }
    }

    public Set<String> findArticulationPoints() {
        Set<String> articulationPoints = new HashSet<>();
        currentId = -1;
        visited = new ArrayList<>();
        ids  = new HashMap<>();
        lows = new HashMap<>();
        for (String key : graph.keySet()) {
            ids.putIfAbsent(key, 0);
            ids.putIfAbsent(key, 0);
        }

        for (String key : graph.keySet()) {
            if (!visited.contains(key)) {
                outEdgeCount = 0;
                articulationPointDfsUtil(key, key, null, articulationPoints);
                if (outEdgeCount < 2) {
                    articulationPoints.remove(key);
                }
            }
        }
        System.out.println("Internal state:");
        System.out.println(ids);
        System.out.println(lows);
        return articulationPoints;
    }

    private void articulationPointDfsUtil(String root, String at, String parent, Set<String> articulationPoints) {
        if (root.equals(parent)) {
            outEdgeCount++;
        }
        visited.add(at);
        currentId++;
        lows.put(at, currentId);
        ids.put(at, currentId);
        System.out.printf("Processing %s, id: %d\n", at, currentId);
        System.out.println("Visited: " + visited);

        for (Edge toEdge : graph.get(at)) {
            String toNode = toEdge.to;
            System.out.printf("At %s, processing %s\n", at, toNode);
            if (toNode.equals(parent)) {
                System.out.println("Do not need to process parent");
                continue;
            }
            if (!visited.contains(toNode)) {
                System.out.printf("Traversing from %s to %s\n", at , toNode);
                articulationPointDfsUtil(root, toNode, at, articulationPoints);
                System.out.printf("At %s backtracking from %s, updating low value to: %d\n",
                        at, toNode, Math.min(lows.get(at), lows.get(toNode)));
                lows.put(at, Math.min(lows.get(at), lows.get(toNode)));
                if (ids.get(at) <= lows.get(toNode)) {
                    System.out.printf("adding %s as articulation point\n", at);
                    articulationPoints.add(at);
                }
            } else {
                System.out.printf("At %s, already visited: %s, updating low value to: %d\n", at, toNode,
                        Math.min(lows.get(at), ids.get(toNode)));
                lows.put(at, Math.min(lows.get(at), ids.get(toNode)));
            }
        }
     }
}
