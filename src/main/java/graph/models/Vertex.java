package graph.models;

/**
 * Represents a vertex (node) in a directed graph, identified by a unique ID.
 * Equality and hash code are based solely on the vertex ID.
 */
public class Vertex {
    private final int id;

    public Vertex(int id) { this.id = id; }

    public int getId() { return id; }

    @Override
    public boolean equals(Object o) {
        return o instanceof Vertex v && v.id == id;
    }

    @Override
    public int hashCode() { return Integer.hashCode(id); }
    @Override
    public String toString() { return "T" + id; }
}
