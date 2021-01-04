package graphs;

public class Edge {
    public String from;
    public String to;
    public double weight;

    public Edge(String from, String to, double weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "(" + from + ", " + to + ", " + weight + ")";
    }
}
