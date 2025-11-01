package graph;

import graph.dagsp.DagLongestPath;
import graph.dagsp.DagShortestPath;
import graph.models.Graph;
import graph.scc.CondensationBuilder;
import graph.scc.CondensationResult;
import graph.scc.SCCTarjan;
import graph.topo.TopologicalSort;
import graph.utils.Metrics;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration tests covering edge cases and end-to-end scenarios
 */
public class IntegrationTest {

    @Test
    void testEmptyGraph() {
        Graph g = new Graph(0);
        SCCTarjan scc = new SCCTarjan();
        List<List<Integer>> sccs = scc.findSCCs(g, new Metrics());

        assertTrue(sccs.isEmpty());
    }

    @Test
    void testSingleVertexNoEdges() {
        Graph g = new Graph(1);

        // SCC
        SCCTarjan scc = new SCCTarjan();
        List<List<Integer>> sccs = scc.findSCCs(g, new Metrics());
        assertEquals(1, sccs.size());

        // Condensation
        CondensationResult result = CondensationBuilder.build(g, sccs);
        assertEquals(1, result.getDag().getVertexCount());

        // Topo
        TopologicalSort topo = new TopologicalSort();
        List<Integer> order = topo.sort(result.getDag(), new Metrics());
        assertEquals(1, order.size());

        // Shortest path
        DagShortestPath sp = new DagShortestPath();
        DagShortestPath.Result spResult = sp.compute(result.getDag(), 0, order, new Metrics());
        assertEquals(0, spResult.getDistances()[0]);

        // Longest path
        DagLongestPath lp = new DagLongestPath();
        assertEquals(0, lp.compute(result.getDag(), 0, new Metrics()));
    }

    @Test
    void testFullCycleGraph() {
        // Complete cycle: all vertices in one SCC
        Graph g = new Graph(5);
        for (int i = 0; i < 5; i++) {
            g.addEdge(g.getVertex(i), g.getVertex((i + 1) % 5), 1);
        }

        SCCTarjan scc = new SCCTarjan();
        List<List<Integer>> sccs = scc.findSCCs(g, new Metrics());

        assertEquals(1, sccs.size());
        assertEquals(5, sccs.get(0).size());

        // Condensation should be single vertex DAG
        CondensationResult result = CondensationBuilder.build(g, sccs);
        assertEquals(1, result.getDag().getVertexCount());
        assertEquals(0, result.getDag().getOutgoing(result.getDag().getVertex(0)).size());
    }

    @Test
    void testCompleteDAG() {
        // Every vertex connects to all vertices with higher index
        Graph g = new Graph(4);
        for (int i = 0; i < 4; i++) {
            for (int j = i + 1; j < 4; j++) {
                g.addEdge(g.getVertex(i), g.getVertex(j), j - i);
            }
        }

        // All separate SCCs
        SCCTarjan scc = new SCCTarjan();
        List<List<Integer>> sccs = scc.findSCCs(g, new Metrics());
        assertEquals(4, sccs.size());

        // Test shortest vs longest from 0
        CondensationResult result = CondensationBuilder.build(g, sccs);
        List<Integer> topoOrder = new TopologicalSort().sort(result.getDag(), new Metrics());

        DagShortestPath sp = new DagShortestPath();
        int sourceSuper = result.getSuperId(0);
        DagShortestPath.Result spResult = sp.compute(result.getDag(), sourceSuper, topoOrder, new Metrics());

        // Shortest to vertex 3 should be direct edge: weight 3
        int target3Super = result.getSuperId(3);
        assertEquals(3, spResult.getDistances()[target3Super]);
    }

    @Test
    void testNegativeWeights() {
        // DAGs can have negative weights
        Graph g = new Graph(4);
        g.addEdge(g.getVertex(0), g.getVertex(1), -5);
        g.addEdge(g.getVertex(0), g.getVertex(2), 10);
        g.addEdge(g.getVertex(1), g.getVertex(3), 3);
        g.addEdge(g.getVertex(2), g.getVertex(3), -2);

        List<Integer> topoOrder = new TopologicalSort().sort(g, new Metrics());
        DagShortestPath sp = new DagShortestPath();
        DagShortestPath.Result result = sp.compute(g, 0, topoOrder, new Metrics());

        // Shortest to 3: 0->1->3 = -5+3 = -2
        assertEquals(-2, result.getDistances()[3]);
    }

    @Test
    void testLargeWeights() {
        // Test with very large weights (but not overflow)
        Graph g = new Graph(3);
        g.addEdge(g.getVertex(0), g.getVertex(1), 1_000_000);
        g.addEdge(g.getVertex(1), g.getVertex(2), 1_000_000);

        List<Integer> topoOrder = new TopologicalSort().sort(g, new Metrics());
        DagShortestPath sp = new DagShortestPath();
        DagShortestPath.Result result = sp.compute(g, 0, topoOrder, new Metrics());

        assertEquals(2_000_000, result.getDistances()[2]);
    }

    @Test
    void testPathReconstructionToSelf() {
        Graph g = new Graph(3);
        g.addEdge(g.getVertex(0), g.getVertex(1), 5);
        g.addEdge(g.getVertex(1), g.getVertex(2), 3);

        List<Integer> topoOrder = new TopologicalSort().sort(g, new Metrics());
        DagShortestPath sp = new DagShortestPath();
        DagShortestPath.Result result = sp.compute(g, 0, topoOrder, new Metrics());

        // Path from source to itself
        List<Integer> path = result.reconstructPath(0, 0);
        assertEquals(List.of(0), path);
    }

    @Test
    void testMetricsConsistency() {
        // Verify metrics are being tracked properly
        Graph g = new Graph(5);
        for (int i = 0; i < 4; i++) {
            g.addEdge(g.getVertex(i), g.getVertex(i + 1), i + 1);
        }

        Metrics m1 = new Metrics();
        SCCTarjan scc = new SCCTarjan();
        scc.findSCCs(g, m1);
        assertTrue(m1.getElapsedMs() >= 0);

        Metrics m2 = new Metrics();
        TopologicalSort topo = new TopologicalSort();
        topo.sort(g, m2);
        assertTrue(m2.getElapsedMs() >= 0);
    }
}
