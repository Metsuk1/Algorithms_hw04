package graph;

import graph.dagsp.DagLongestPath;
import graph.models.Graph;
import graph.utils.Metrics;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DagLongestPathTest {

    @Test
    void testSingleVertex() {
        Graph g = new Graph(1);

        DagLongestPath lp = new DagLongestPath();
        int length = lp.compute(g, 0, new Metrics());

        assertEquals(0, length);
    }

    @Test
    void testLinearPath() {
        // 0 -5-> 1 -3-> 2 -2-> 3
        Graph g = new Graph(4);
        g.addEdge(g.getVertex(0), g.getVertex(1), 5);
        g.addEdge(g.getVertex(1), g.getVertex(2), 3);
        g.addEdge(g.getVertex(2), g.getVertex(3), 2);

        DagLongestPath lp = new DagLongestPath();
        int length = lp.compute(g, 0, new Metrics());

        assertEquals(10, length); // 5 + 3 + 2
    }

    @Test
    void testDiamondGraph() {
        // 0 -1-> 1 -2-> 3
        // 0 -5-> 2 -1-> 3
        Graph g = new Graph(4);
        g.addEdge(g.getVertex(0), g.getVertex(1), 1);
        g.addEdge(g.getVertex(0), g.getVertex(2), 5);
        g.addEdge(g.getVertex(1), g.getVertex(3), 2);
        g.addEdge(g.getVertex(2), g.getVertex(3), 1);

        DagLongestPath lp = new DagLongestPath();
        int length = lp.compute(g, 0, new Metrics());

        assertEquals(6, length); // 0->2->3: 5+1
    }

    @Test
    void testZeroWeights() {
        // 0 -0-> 1 -0-> 2 -5-> 3
        Graph g = new Graph(4);
        g.addEdge(g.getVertex(0), g.getVertex(1), 0);
        g.addEdge(g.getVertex(1), g.getVertex(2), 0);
        g.addEdge(g.getVertex(2), g.getVertex(3), 5);

        DagLongestPath lp = new DagLongestPath();
        int length = lp.compute(g, 0, new Metrics());

        assertEquals(5, length);
    }

    @Test
    void testMetricsTracking() {
        Graph g = new Graph(3);
        g.addEdge(g.getVertex(0), g.getVertex(1), 5);
        g.addEdge(g.getVertex(1), g.getVertex(2), 3);

        Metrics metrics = new Metrics();
        DagLongestPath lp = new DagLongestPath();
        lp.compute(g, 0, metrics);

        assertTrue(metrics.getRelaxationsCount() > 0);
        assertTrue(metrics.getElapsedMs() >= 0);
    }

}
