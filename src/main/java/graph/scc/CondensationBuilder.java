package graph.scc;

import graph.models.Edge;
import graph.models.Graph;
import graph.models.Vertex;

import java.util.*;

/**
 * Builds a condensation graph (DAG) from the strongly connected components (SCCs)
 * of a directed graph. Each SCC is collapsed into a single vertex, and edges between
 * different components are preserved.
 */
public class CondensationBuilder {

    /**
     * Builds the condensation DAG from the given graph and its SCCs.
     *
     * @param graph the original directed graph
     * @param sccs  list of strongly connected components (each as a list of vertex IDs)
     * @return a DAG where each node represents an SCC of the original graph
     */
    public static Graph build(Graph graph, List<List<Integer>> sccs) {
        int superN = sccs.size();
        Map<Integer, Integer> nodeToSuper = new HashMap<>();
        for (int s = 0; s < superN; s++) {
            for (int v : sccs.get(s)) nodeToSuper.put(v, s);
        }

        Graph dag = new Graph(superN);
        Set<String> used = new HashSet<>();

        for (Vertex u : graph.getNodes()) {
            int su = nodeToSuper.get(u.getId());
            for (Edge e : graph.getOutgoing(u)) {
                int sv = nodeToSuper.get(e.getTo().getId());
                if (su != sv && used.add(su + "->" + sv)) {
                    dag.addEdge(dag.getVertex(su), dag.getVertex(sv), (int) e.getWeight());
                }
            }
        }

        return dag;
    }
}
