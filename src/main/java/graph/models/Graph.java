package graph.models;

import java.util.*;

public class Graph {
    private final int nodeCount;
    private final Map<Vertex, List<Edge>> outgoing;
    private final Map<Vertex, List<Edge>> incoming;

    public Graph(int nodeCount) {
        this.nodeCount = nodeCount;
        this.outgoing = new HashMap<>();
        this.incoming = new HashMap<>();
        for (int i = 0; i < nodeCount; i++) {
            Vertex node = new Vertex(i);
            outgoing.put(node, new ArrayList<>());
            incoming.put(node, new ArrayList<>());
        }
    }

    public void addEdge(Vertex from, Vertex to, int weight) {
        Edge edge = new Edge(from, to, weight);
        outgoing.get(from).add(edge);
        incoming.get(to).add(edge);
    }

    public List<Edge> getOutgoing(Vertex node) {
        return Collections.unmodifiableList(outgoing.get(node));
    }

    public List<Edge> getIncoming(Vertex node) {
        return Collections.unmodifiableList(incoming.get(node));
    }

    public Collection<Vertex> getNodes() {
        return Collections.unmodifiableSet(outgoing.keySet());
    }

    public int getVertexCount() { return nodeCount; }
    public Vertex getVertex(int id) { return new Vertex(id); }
}
