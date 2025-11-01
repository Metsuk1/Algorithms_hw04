package graph.models;

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
