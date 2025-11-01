package graph.dagsp;

import graph.models.Graph;
import graph.models.Vertex;
import graph.topo.TopologicalSort;
import graph.utils.Metrics;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Computes the longest (critical) path in a DAG.
 * Implemented by inverting edge weights and running shortest-path logic.
 */
public class DagLongestPath {
    private final DagGraphInverter inverter = new DagGraphInverter();
    private final DagSourceFinder sourceFinder = new DagSourceFinder();
    private final DagShortestPath spService = new DagShortestPath();
    private final TopologicalSort topoSort = new TopologicalSort();

    /**
     * Computes the length of the critical path in the DAG.
     *
     * @param dag      the directed acyclic graph
     * @param metrics  performance metrics recorder
     * @return length of the longest path
     */
    public int compute(Graph dag, Metrics metrics) {
        metrics.startTimer();

        // 1. Invert graph (reverse edges, negate weights)
        Graph inv = inverter.invert(dag);

        // 2. Find all source vertices in the original DAG
        Set<Vertex> sources = sourceFinder.find(dag);

        // 3. Perform topological sort once
        List<Integer> topoOrder = topoSort.sort(inv, new Metrics());

        // 4. Run shortest path on the inverted graph for each source
        int globalMin = Integer.MAX_VALUE;
        for (Vertex src : sources) {
            Metrics localM = new Metrics();
            var result = spService.compute(inv, src.getId(), topoOrder, localM);
            int[] dist = result.getDistances();
            int localMin = Arrays.stream(dist).min().orElse(Integer.MAX_VALUE);
            globalMin = Math.min(globalMin, localMin);
            metrics.incrementRelaxations(localM.getRelaxationsCount());
        }

        // Negate because edges were inverted
        return -globalMin;
    }
}
