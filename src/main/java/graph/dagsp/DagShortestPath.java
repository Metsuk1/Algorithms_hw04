package graph.dagsp;

import graph.models.Edge;
import graph.models.Graph;
import graph.models.Vertex;
import graph.utils.Metrics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Provides shortest path computation on a Directed Acyclic Graph (DAG)
 * using topological order dynamic programming.
 * Time Complexity: O(V + E)
 */
public class DagShortestPath {
    /**
     * Computes shortest paths from the given source vertex using a topological order.
     * @param dag        the directed acyclic graph
     * @param sourceId   the ID of the source vertex
     * @param topoOrder  list of vertex IDs in topological order
     * @param metrics    performance metrics recorder
     * @return a Result object containing distance and predecessor arrays
     */
    public Result compute(Graph dag, int sourceId, List<Integer> topoOrder, Metrics metrics) {
        metrics.startTimer();
        int n = dag.getVertexCount();
        int[] dist = new int[n];
        int[] prev = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(prev, -1);
        dist[sourceId] = 0;

        for (int uId : topoOrder) {
            if (dist[uId] == Integer.MAX_VALUE) continue;
            Vertex u = dag.getVertex(uId);

            for (Edge e : dag.getOutgoing(u)) {
                metrics.incrementRelaxations();
                int vId = e.getTo().getId();
                int cand =  (dist[uId] + e.getWeight());

                if (cand < dist[vId]) {
                    dist[vId] = cand;
                    prev[vId] = uId;
                }
            }
        }

        return new Result(dist, prev);
    }

    /**
     * Immutable result of shortest path computation, containing
     * both distance and predecessor arrays, and path reconstruction.
     */
    public static class Result {
        private final int[] dist;
        private final int[] prev;

        public Result(int[] dist, int[] prev) {
            this.dist = dist;
            this.prev = prev;
        }

        /** return array of shortest distances from source*/
        public int[] getDistances() { return dist; }

        /** return array of predecessors for path reconstruction */
        public int[] getPredecessors() { return prev; }

        /**
         * Reconstructs one optimal path from source to target.
         * If the target is unreachable, returns an empty list.
         * @param source source vertex ID
         * @param target target vertex ID
         * @return list of vertex IDs representing the shortest path
         */
        public List<Integer> reconstructPath(int source, int target) {
            List<Integer> path = new ArrayList<>();
            if (dist[target] == Integer.MAX_VALUE) return path;

            for (int v = target; v != -1; v = prev[v]) {
                path.add(v);
                if (v == source) break;
            }
            Collections.reverse(path);
            return path;
        }
    }
}
