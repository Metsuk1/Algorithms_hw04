package graph.topo;

import graph.models.Edge;
import graph.models.Graph;
import graph.models.Vertex;
import graph.utils.Metrics;

import java.util.*;

/**
 * Performs topological sorting of a directed acyclic graph (DAG)
 * using Kahn's algorithm (BFS-based approach).
 * The algorithm computes in-degrees for all vertices and repeatedly
 * removes vertices with zero in-degree, producing a valid linear order.
 * Time complexity:O(V+E)
 */
public class TopologicalSort {

    /**
     * Computes a topological order of vertices in a given DAG.
     * @param dag     directed acyclic graph
     * @param metrics performance metrics collector
     * @return list of vertex IDs in topological order
     */
    public List<Integer> sort(Graph dag, Metrics metrics) {
        metrics.startTimer();

        // Initialize in-degree map for all vertices
        Map<Vertex, Integer> inDegree = new HashMap<>();
        for (Vertex n : dag.getNodes()) inDegree.put(n, 0);

        // Count in-degrees
        for (Vertex n : dag.getNodes()) {
            for (Edge e : dag.getOutgoing(n)) {
                inDegree.merge(e.getTo(), 1, Integer::sum);
            }
        }

        // Initialize queue with all vertices having in-degree = 0
        Queue<Vertex> q = new ArrayDeque<>();
        for (Map.Entry<Vertex, Integer> e : inDegree.entrySet()) {
            if (e.getValue() == 0) q.add(e.getKey());
        }

        // Process vertices in BFS order
        List<Integer> order = new ArrayList<>();
        while (!q.isEmpty()) {
            Vertex u = q.poll();
            metrics.incrementQueueOperations();
            order.add(u.getId());

            // Decrease in-degree of all neighbors
            for (Edge e : dag.getOutgoing(u)) {
                metrics.incrementEdgeTraversals();
                Vertex v = e.getTo();
                inDegree.merge(v, -1, Integer::sum);
                if (inDegree.get(v) == 0) {
                    q.add(v);
                    metrics.incrementQueueOperations();
                }
            }
        }
        return order;
    }
}
