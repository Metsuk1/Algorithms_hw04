package graph;

import graph.models.Graph;
import graph.scc.CondensationBuilder;
import graph.scc.CondensationResult;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CondensationBuilderTest {
    @Test
    void testSingleSCC() {
        // All vertices in one SCC
        Graph g = new Graph(3);
        g.addEdge(g.getVertex(0), g.getVertex(1), 5);
        g.addEdge(g.getVertex(1), g.getVertex(2), 3);
        g.addEdge(g.getVertex(2), g.getVertex(0), 2);

        List<List<Integer>> sccs = List.of(List.of(2, 1, 0));

        CondensationResult result = CondensationBuilder.build(g, sccs);
        Graph dag = result.getDag();

        assertEquals(1, dag.getVertexCount());
        assertEquals(0, dag.getOutgoing(dag.getVertex(0)).size());
        assertEquals(3, result.getOriginalVertices(0).size());
    }

    @Test
    void testGetSuperIdInvalid() {
        Graph g = new Graph(2);
        List<List<Integer>> sccs = List.of(List.of(0), List.of(1));

        CondensationResult result = CondensationBuilder.build(g, sccs);

        assertEquals(-1, result.getSuperId(999));
    }

    @Test
    void testOriginalVerticesRetrieval() {
        Graph g = new Graph(5);
        List<List<Integer>> sccs = List.of(
                List.of(4),
                List.of(2, 1, 0),
                List.of(3)
        );

        CondensationResult result = CondensationBuilder.build(g, sccs);

        assertEquals(List.of(4), result.getOriginalVertices(0));
        assertEquals(List.of(2, 1, 0), result.getOriginalVertices(1));
        assertEquals(List.of(3), result.getOriginalVertices(2));
    }

}
