import graphs.DirectedGraph;
import graphs.Graph;

public class SimpleGraphs {
    public static void main(String[] args) {
        DirectedGraph graph = new DirectedGraph("input/top_sort.txt");
        System.out.println(graph.getTopOrdering());
    }

    public static void setupConnectedComponents(Graph graph) {
        graph.addEdge(6, 7);
        graph.addEdge(7, 11);
        graph.addEdge(6, 11);
        graph.addEdge(4, 8);
        graph.addEdge(4, 0);
        graph.addEdge(8, 0);
        graph.addEdge(8, 14);
        graph.addEdge(0, 14);
        graph.addEdge(0, 13);
        graph.addEdge(14, 13);
        graph.addEdge(17, 5);
        graph.addEdge(1, 5);
        graph.addEdge(5, 16);
        graph.addEdge(3, 9);
        graph.addEdge(9, 15);
        graph.addEdge(15, 10);
        graph.addEdge(15, 2);
        graph.addEdge(9, 2);
    }

    public static void setupBfs(Graph graph) {
        graph.addEdge(0, 9);
        graph.addEdge(0, 7);
        graph.addEdge(0, 11);
        graph.addEdge(9, 10);
        graph.addEdge(9, 8);
        graph.addEdge(7, 3);
        graph.addEdge(7, 6);
        graph.addEdge(11, 7);
        graph.addEdge(10, 1);
        graph.addEdge(8, 1);
        graph.addEdge(8, 12);
        graph.addEdge(3, 2);
        graph.addEdge(3, 4);
        graph.addEdge(6, 5);
    }
}
