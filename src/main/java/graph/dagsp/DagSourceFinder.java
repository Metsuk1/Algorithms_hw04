package graph.dagsp;

import graph.models.Edge;
import graph.models.Graph;
import graph.models.Vertex;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Finds all source vertices (zero in-degree) in a DAG.
 */
public class DagSourceFinder {
    /**
     * Returns all vertices with no incoming edges.
     *
     * @param dag the directed acyclic graph
     * @return set of vertices with in-degree zero
     */
    public Set<Vertex> find(Graph dag) {
        Map<Vertex, Integer> inDegree = new HashMap<>();
        for (Vertex n : dag.getNodes()) inDegree.put(n, 0);

        for (Vertex n : dag.getNodes()) {
            for (Edge e : dag.getOutgoing(n)) {
                inDegree.merge(e.getTo(), 1, Integer::sum);
            }
        }

        Set<Vertex> sources = new HashSet<>();
        for (Map.Entry<Vertex, Integer> e : inDegree.entrySet()) {
            if (e.getValue() == 0) sources.add(e.getKey());
        }
        return sources;
    }
}
