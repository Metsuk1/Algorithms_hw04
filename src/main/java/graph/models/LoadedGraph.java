package graph.models;

public class LoadedGraph {
    private final Graph graph;
    private final int source;
    private final String weightModel;

    public LoadedGraph(Graph graph, int source, String weightModel) {
        this.graph = graph;
        this.source = source;
        this.weightModel = weightModel;
    }

    public Graph getGraph() { return graph; }
    public int getSource() { return source; }
    public String getWeightModel() { return weightModel; }
}
