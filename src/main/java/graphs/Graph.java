package graphs;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Graph {
    private final Map<Integer, Set<Integer>> adjVertices;

    public Graph(int numVertices) {
        adjVertices = new HashMap<>();

        for (int i = 0; i < numVertices; i++) {
            adjVertices.putIfAbsent(i, new LinkedHashSet<>());
        }
    }

    public void addEdge(int src, int dst) {
        Set<Integer> srcEdges = adjVertices.get(src);
        Set<Integer> dstEdges = adjVertices.get(dst);
        srcEdges.add(dst);
        dstEdges.add(src);
    }

    public void print() {
        System.out.println(adjVertices);
    }

    public void bfs(int root) {
        Set<Integer> visited =  new LinkedHashSet<>();
        Queue<Integer> queue = new LinkedList<>();

        queue.add(root);
        visited.add(root);
        while (!queue.isEmpty()) {
            int vertex = queue.poll();
            for (int v : adjVertices.get(vertex)) {
                if (!visited.contains(v)) {
                    queue.add(v);
                    visited.add(v);
                    System.out.println("BFS visited node: " + v);
                }
            }
        }
    }

    public void dfsIterative(int root) {
        Set<Integer> visited =  new LinkedHashSet<>();
        Stack<Integer> stack = new Stack<>();

        stack.push(root);
        while (!stack.empty()) {
            int vertex = stack.pop();
            if (!visited.contains(vertex)) {
                visited.add(vertex);
                System.out.println("DFS iterative visited: " + vertex);
                for (int v : adjVertices.get(vertex)) {
                    stack.push(v);
                }
            }
        }
    }

    public void dfsRecursive(int root) {
        Set<Integer> visited = new LinkedHashSet<>();
        dfsUtil(root, visited);
    }

    private void dfsUtil(int root, Set<Integer> visited) {
        if (visited.contains(root)) {
            return;
        }
        visited.add(root);
        System.out.println("DFS recursive visited: " + root);

        for (int v : adjVertices.get(root)) {
            dfsUtil(v, visited);
        }
    }

    public int getComponentCount() {
        Set<Integer> visited = new LinkedHashSet<>();
        int count = 0;

        for (int i = 0; i < adjVertices.size(); i++) {
            if (!visited.contains(i)) {
                count++;
                dfsUtil(i, visited);
            }
        }
        return count;
    }

    public List<Integer> shortestPath(int src, int dst) {
        Map<Integer, Integer> prevNodes = getPrevNodes(src);
        return getShortestPath(src, dst, prevNodes);
    }

    private Map<Integer, Integer> getPrevNodes(int root) {
        Set<Integer> visited =  new LinkedHashSet<>();
        Queue<Integer> queue = new LinkedList<>();
        Map<Integer, Integer> prevNodes = new HashMap<>();

        queue.add(root);
        visited.add(root);
        System.out.println("BFS visited node: " + root);
        while (!queue.isEmpty()) {
            int vertex = queue.poll();
            for (int v : adjVertices.get(vertex)) {
                if (!visited.contains(v)) {
                    queue.add(v);
                    visited.add(v);
                    prevNodes.putIfAbsent(v, vertex);
                    System.out.println("BFS visited node: " + v);
                }
            }
        }
        System.out.println(prevNodes);
        return prevNodes;
    }

    private List<Integer> getShortestPath(int src, int dst, Map<Integer, Integer> prevNodes) {
        List<Integer> path = new ArrayList<>();

        for (Integer cur = dst; cur != null; cur = prevNodes.get(cur)) {
            path.add(cur);
        }
        Collections.reverse(path);

        if (path.get(0) != src) {
            path = new ArrayList<>();
        }
        return path;
    }
}
