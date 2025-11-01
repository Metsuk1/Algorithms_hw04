package graph;

import graph.dagsp.DagShortestPath;
import graph.models.Graph;
import graph.topo.TopologicalSort;
import graph.utils.Metrics;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DagShortestPathTest {

    @Test
    void testSingleVertex() {
        Graph g = new Graph(1);
        List<Integer> topoOrder = List.of(0);

        DagShortestPath sp = new DagShortestPath();
        DagShortestPath.Result result = sp.compute(g, 0, topoOrder, new Metrics());

        assertEquals(0, result.getDistances()[0]);
    }

    @Test
    void testLinearPath() {
        // 0 -5-> 1 -3-> 2 -2-> 3
        Graph g = new Graph(4);
        g.addEdge(g.getVertex(0), g.getVertex(1), 5);
        g.addEdge(g.getVertex(1), g.getVertex(2), 3);
        g.addEdge(g.getVertex(2), g.getVertex(3), 2);

        List<Integer> topoOrder = new TopologicalSort().sort(g, new Metrics());
        DagShortestPath sp = new DagShortestPath();
        DagShortestPath.Result result = sp.compute(g, 0, topoOrder, new Metrics());

        int[] dist = result.getDistances();
        assertEquals(0, dist[0]);
        assertEquals(5, dist[1]);
        assertEquals(8, dist[2]);
        assertEquals(10, dist[3]);
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

        List<Integer> topoOrder = new TopologicalSort().sort(g, new Metrics());
        DagShortestPath sp = new DagShortestPath();
        DagShortestPath.Result result = sp.compute(g, 0, topoOrder, new Metrics());

        int[] dist = result.getDistances();
        assertEquals(0, dist[0]);
        assertEquals(1, dist[1]);
        assertEquals(5, dist[2]);
        assertEquals(3, dist[3]); // Shortest: 0->1->3
    }


    @Test
    void testZeroWeights() {
        // 0 -0-> 1 -0-> 2
        Graph g = new Graph(3);
        g.addEdge(g.getVertex(0), g.getVertex(1), 0);
        g.addEdge(g.getVertex(1), g.getVertex(2), 0);

        List<Integer> topoOrder = new TopologicalSort().sort(g, new Metrics());
        DagShortestPath sp = new DagShortestPath();
        DagShortestPath.Result result = sp.compute(g, 0, topoOrder, new Metrics());

        int[] dist = result.getDistances();
        assertEquals(0, dist[0]);
        assertEquals(0, dist[1]);
        assertEquals(0, dist[2]);
    }

    @Test
    void testMetricsTracking() {
        Graph g = new Graph(3);
        g.addEdge(g.getVertex(0), g.getVertex(1), 5);
        g.addEdge(g.getVertex(1), g.getVertex(2), 3);

        List<Integer> topoOrder = new TopologicalSort().sort(g, new Metrics());
        Metrics metrics = new Metrics();
        DagShortestPath sp = new DagShortestPath();
        sp.compute(g, 0, topoOrder, metrics);

        assertTrue(metrics.getRelaxationsCount() > 0);
        assertTrue(metrics.getElapsedMs() >= 0);
    }
}
