package graph;

import graph.models.Graph;
import graph.scc.SCCTarjan;
import graph.utils.Metrics;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SCCTarjanTest {

    @Test
    void testSingleVertex() {
        Graph g = new Graph(1);
        SCCTarjan scc = new SCCTarjan();
        List<List<Integer>> sccs = scc.findSCCs(g, new Metrics());

        assertEquals(1, sccs.size());
        assertEquals(List.of(0), sccs.get(0));
    }

    @Test
    void testTwoVerticesNoEdges() {
        Graph g = new Graph(2);
        SCCTarjan scc = new SCCTarjan();
        List<List<Integer>> sccs = scc.findSCCs(g, new Metrics());

        assertEquals(2, sccs.size());
    }

    @Test
    void testSimpleCycle() {
        // 0 -> 1 -> 2 -> 0
        Graph g = new Graph(3);
        g.addEdge(g.getVertex(0), g.getVertex(1), 1);
        g.addEdge(g.getVertex(1), g.getVertex(2), 1);
        g.addEdge(g.getVertex(2), g.getVertex(0), 1);

        SCCTarjan scc = new SCCTarjan();
        List<List<Integer>> sccs = scc.findSCCs(g, new Metrics());

        assertEquals(1, sccs.size());
        assertEquals(3, sccs.get(0).size());
        assertTrue(sccs.get(0).containsAll(List.of(0, 1, 2)));
    }

    @Test
    void testLinearDAG() {
        // 0 -> 1 -> 2 -> 3
        Graph g = new Graph(4);
        g.addEdge(g.getVertex(0), g.getVertex(1), 1);
        g.addEdge(g.getVertex(1), g.getVertex(2), 1);
        g.addEdge(g.getVertex(2), g.getVertex(3), 1);

        SCCTarjan scc = new SCCTarjan();
        List<List<Integer>> sccs = scc.findSCCs(g, new Metrics());

        assertEquals(4, sccs.size());
        for (List<Integer> component : sccs) {
            assertEquals(1, component.size());
        }
    }

    @Test
    void testSelfLoop() {
        // Vertex with self-loop: 0 -> 0
        Graph g = new Graph(2);
        g.addEdge(g.getVertex(0), g.getVertex(0), 1);
        g.addEdge(g.getVertex(0), g.getVertex(1), 1);

        SCCTarjan scc = new SCCTarjan();
        List<List<Integer>> sccs = scc.findSCCs(g, new Metrics());

        // 0 is its own SCC, 1 is separate
        assertEquals(2, sccs.size());
    }

    @Test
    void testDisconnectedComponents() {
        // Two separate components with no connection
        Graph g = new Graph(6);
        // Component 1: 0 -> 1 -> 2 -> 0
        g.addEdge(g.getVertex(0), g.getVertex(1), 1);
        g.addEdge(g.getVertex(1), g.getVertex(2), 1);
        g.addEdge(g.getVertex(2), g.getVertex(0), 1);
        // Component 2: 3 -> 4 -> 5 -> 3
        g.addEdge(g.getVertex(3), g.getVertex(4), 1);
        g.addEdge(g.getVertex(4), g.getVertex(5), 1);
        g.addEdge(g.getVertex(5), g.getVertex(3), 1);

        SCCTarjan scc = new SCCTarjan();
        List<List<Integer>> sccs = scc.findSCCs(g, new Metrics());

        assertEquals(2, sccs.size());
        assertEquals(3, sccs.get(0).size());
        assertEquals(3, sccs.get(1).size());
    }

    @Test
    void testMetricsTracking() {
        Graph g = new Graph(3);
        g.addEdge(g.getVertex(0), g.getVertex(1), 1);
        g.addEdge(g.getVertex(1), g.getVertex(2), 1);

        Metrics metrics = new Metrics();
        SCCTarjan scc = new SCCTarjan();
        scc.findSCCs(g, metrics);

        assertTrue(metrics.getElapsedMs() >= 0);
    }

}
