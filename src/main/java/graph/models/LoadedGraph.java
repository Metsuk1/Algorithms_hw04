package graph.models;

/**
 * Wrapper object holding a loaded graph together with
 * its designated source vertex and weight model metadata.
 */
public class LoadedGraph {
    private final Graph graph;
    private final int source;
    private final String weightModel;

    /**
     * Creates a container for a fully loaded graph instance.
     * @param graph       the graph structure
     * @param source      source vertex ID for path algorithms
     * @param weightModel weight type (edge-based or node-based)
     */
    public LoadedGraph(Graph graph, int source, String weightModel) {
        this.graph = graph;
        this.source = source;
        this.weightModel = weightModel;
    }

    //getters
    public Graph getGraph() { return graph; }
    public int getSource() { return source; }
    public String getWeightModel() { return weightModel; }
}
