package graph.dagsp;

import graph.models.Edge;
import graph.models.Graph;
import graph.models.Vertex;

/**
 * Utility class that inverts a DAG:
 * for each edge u->v with weight w, adds v->u with weight -w.
 */
public class DagGraphInverter {
    /**
     * Builds an inverted version of the given DAG.
     * @param dag the original directed acyclic graph
     * @return new graph with inverted edges and negated weights
     */
    public Graph invert(Graph dag) {
        Graph inv = new Graph(dag.getVertexCount());
        for (Vertex u : dag.getNodes()) {
            for (Edge e : dag.getOutgoing(u)) {
                inv.addEdge(e.getTo(), e.getFrom(), (int) -e.getWeight());
            }
        }
        return inv;
    }
}
