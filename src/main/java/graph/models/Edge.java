package graph.models;

/**
 * Represents a directed weighted edge between two vertices in a graph.
 */
public class Edge {
    private final Vertex from;
    private final Vertex to;
    private final double weight;

    public Edge(Vertex from, Vertex to, double weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public Vertex getFrom() { return from; }
    public Vertex getTo() { return to; }
    public double getWeight() { return weight; }
}
