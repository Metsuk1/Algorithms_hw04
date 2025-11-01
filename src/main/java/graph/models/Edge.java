package graph.models;

/**
 * Represents a directed weighted edge between two vertices in a graph.
 */
public class Edge {
    private final Vertex from;
    private final Vertex to;
    private final int weight;

    public Edge(Vertex from, Vertex to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public Vertex getFrom() { return from; }
    public Vertex getTo() { return to; }
    public int getWeight() { return weight; }
}
