package graph;

import graph.models.Graph;
import graph.topo.TopologicalSort;
import graph.utils.Metrics;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TopologicalSortTest {
    @Test
    void testSingleVertex() {
        Graph g = new Graph(1);
        TopologicalSort topo = new TopologicalSort();
        List<Integer> order = topo.sort(g, new Metrics());

        assertEquals(1, order.size());
        assertEquals(0, order.get(0));
    }

    @Test
    void testLinearChain() {
        // 0 -> 1 -> 2 -> 3
        Graph g = new Graph(4);
        g.addEdge(g.getVertex(0), g.getVertex(1), 1);
        g.addEdge(g.getVertex(1), g.getVertex(2), 1);
        g.addEdge(g.getVertex(2), g.getVertex(3), 1);

        TopologicalSort topo = new TopologicalSort();
        List<Integer> order = topo.sort(g, new Metrics());

        assertEquals(4, order.size());
        assertTrue(order.indexOf(0) < order.indexOf(1));
        assertTrue(order.indexOf(1) < order.indexOf(2));
        assertTrue(order.indexOf(2) < order.indexOf(3));
    }

    @Test
    void testDisconnectedDAG() {
        // Two separate chains: 0->1 and 2->3
        Graph g = new Graph(4);
        g.addEdge(g.getVertex(0), g.getVertex(1), 1);
        g.addEdge(g.getVertex(2), g.getVertex(3), 1);

        TopologicalSort topo = new TopologicalSort();
        List<Integer> order = topo.sort(g, new Metrics());

        assertEquals(4, order.size());
        assertTrue(order.indexOf(0) < order.indexOf(1));
        assertTrue(order.indexOf(2) < order.indexOf(3));
    }

    @Test
    void testAllVerticesIncluded() {
        Graph g = new Graph(5);
        g.addEdge(g.getVertex(0), g.getVertex(2), 1);
        g.addEdge(g.getVertex(1), g.getVertex(3), 1);
        g.addEdge(g.getVertex(3), g.getVertex(4), 1);

        TopologicalSort topo = new TopologicalSort();
        List<Integer> order = topo.sort(g, new Metrics());

        assertEquals(5, order.size());
        Set<Integer> vertices = new HashSet<>(order);
        assertEquals(5, vertices.size());
        for (int i = 0; i < 5; i++) {
            assertTrue(vertices.contains(i));
        }
    }

    @Test
    void testEmptyGraph() {
        Graph g = new Graph(3);
        // No edges

        TopologicalSort topo = new TopologicalSort();
        List<Integer> order = topo.sort(g, new Metrics());

        assertEquals(3, order.size());
    }

    @Test
    void testMetricsTracking() {
        Graph g = new Graph(3);
        g.addEdge(g.getVertex(0), g.getVertex(1), 1);
        g.addEdge(g.getVertex(1), g.getVertex(2), 1);

        Metrics metrics = new Metrics();
        TopologicalSort topo = new TopologicalSort();
        topo.sort(g, metrics);

        assertTrue(metrics.getElapsedMs() >= 0);
    }
}
