package graph.dagsp;

import graph.models.Edge;
import graph.models.Graph;
import graph.models.Vertex;
import graph.topo.TopologicalSort;
import graph.utils.Metrics;

import java.util.*;

/**
 * Computes the longest (critical) path in a DAG using dynamic programming
 * over topological order.
 */
public class DagLongestPath {

    /**
     * Computes the length of the longest path from sourceId to any reachable vertex.
     *
     * @param dag      the directed acyclic graph
     * @param sourceId ID of the starting vertex
     * @param metrics  performance metrics recorder
     * @return length of the longest path from source
     */
    public int compute(Graph dag, int sourceId, Metrics metrics) {
        metrics.startTimer();

        int n = dag.getVertexCount();
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MIN_VALUE);
        dist[sourceId] = 0;

        // Get topological order
        graph.topo.TopologicalSort topoSort = new graph.topo.TopologicalSort();
        List<Integer> topoOrder = topoSort.sort(dag, new Metrics());

        // DP: maximize distances in topological order
        for (int uId : topoOrder) {
            if (dist[uId] == Integer.MIN_VALUE) continue;

            Vertex u = dag.getVertex(uId);
            for (Edge e : dag.getOutgoing(u)) {
                metrics.incrementRelaxations();
                int vId = e.getTo().getId();
                int newDist = dist[uId] + e.getWeight();

                if (newDist > dist[vId]) {
                    dist[vId] = newDist;
                }
            }
        }

        // Find maximum distance (longest path length)
        int maxDist = 0;
        for (int d : dist) {
            if (d != Integer.MIN_VALUE && d > maxDist) {
                maxDist = d;
            }
        }

        return maxDist;
    }

    /**
     * Reconstructs the longest path from sourceId.
     * Returns the path that reaches the farthest vertex.
     *
     * @param dag      the directed acyclic graph
     * @param sourceId ID of the starting vertex
     * @param metrics  performance metrics recorder
     * @return list of vertex IDs representing the longest path
     */
    public List<Integer> reconstructLongestPath(Graph dag, int sourceId, Metrics metrics) {
        int n = dag.getVertexCount();
        int[] dist = new int[n];
        int[] prev = new int[n];
        Arrays.fill(dist, Integer.MIN_VALUE);
        Arrays.fill(prev, -1);
        dist[sourceId] = 0;

        // Get topological order
        graph.topo.TopologicalSort topoSort = new graph.topo.TopologicalSort();
        List<Integer> topoOrder = topoSort.sort(dag, new Metrics());

        // DP with predecessor tracking
        for (int uId : topoOrder) {
            if (dist[uId] == Integer.MIN_VALUE) continue;

            Vertex u = dag.getVertex(uId);
            for (Edge e : dag.getOutgoing(u)) {
                int vId = e.getTo().getId();
                int newDist = dist[uId] + e.getWeight();

                if (newDist > dist[vId]) {
                    dist[vId] = newDist;
                    prev[vId] = uId;
                }
            }
        }

        // Find vertex with maximum distance
        int target = sourceId;
        int maxDist = 0;
        for (int i = 0; i < n; i++) {
            if (dist[i] != Integer.MIN_VALUE && dist[i] > maxDist) {
                maxDist = dist[i];
                target = i;
            }
        }

        // Reconstruct path
        List<Integer> path = new ArrayList<>();
        for (int v = target; v != -1; v = prev[v]) {
            path.add(v);
            if (v == sourceId) break;
        }
        Collections.reverse(path);

        return path;
    }
}
