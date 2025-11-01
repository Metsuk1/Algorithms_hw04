package graph.scc;

import graph.models.Edge;
import graph.models.Graph;
import graph.models.Vertex;
import graph.utils.Metrics;

import java.util.*;

/**
 * Implements Tarjan's algorithm for finding all strongly connected components (SCCs)
 * in a directed graph. The algorithm runs in O(V + E) time.
 */
public class SCCTarjan {
    private int time;
    private final Deque<Vertex> stack = new ArrayDeque<>();
    private final Map<Vertex, Integer> disc = new HashMap<>();
    private final Map<Vertex, Integer> low = new HashMap<>();
    private final Map<Vertex, Boolean> onStack = new HashMap<>();

    /**
     * Finds all SCCs in the given graph using Tarjan's algorithm.
     *
     * @param graph   the input directed graph
     * @param metrics performance metrics collector
     * @return a list of SCCs, where each SCC is represented as a list of vertex IDs
     */
    public List<List<Integer>> findSCCs(Graph graph, Metrics metrics) {
        metrics.startTimer();
        List<List<Integer>> sccs = new ArrayList<>();
        time = 0;
        disc.clear(); low.clear(); onStack.clear(); stack.clear();

        for (Vertex node : graph.getNodes()) {
            if (!disc.containsKey(node)) {
                dfs(node, graph, sccs, metrics);
            }
        }
        return sccs;
    }

    /**
     * Depth-first search helper that assigns discovery times, computes low-link values,
     * and identifies strongly connected components.
     */
    private void dfs(Vertex u, Graph g, List<List<Integer>> sccs, Metrics m) {
        m.incrementDfsVisits();
        disc.put(u, time);
        low.put(u, time++);
        stack.push(u);
        onStack.put(u, true);

        for (Edge e : g.getOutgoing(u)) {
            m.incrementEdgeTraversals();
            Vertex v = e.getTo();
            if (!disc.containsKey(v)) {
                dfs(v, g, sccs, m);
                low.put(u, Math.min(low.get(u), low.get(v)));
            } else if (onStack.get(v)) {
                low.put(u, Math.min(low.get(u), disc.get(v)));
            }
        }

        // Root of an SCC found
        if (low.get(u).equals(disc.get(u))) {
            List<Integer> comp = new ArrayList<>();
            Vertex v;
            do {
                v = stack.pop();
                onStack.put(v, false);
                comp.add(v.getId());
            } while (!v.equals(u));
            sccs.add(comp);
        }
    }
}
