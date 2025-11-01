package graph.scc;

import graph.models.Graph;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CondensationResult {
    private final Graph dag;
    private final Map<Integer, Integer> originalToSuper;
    private final List<List<Integer>> superToOriginal;

    public CondensationResult(Graph dag, Map<Integer, Integer> originalToSuper,List<List<Integer>> superToOriginal) {
        this.dag = dag;
        this.originalToSuper = Collections.unmodifiableMap(originalToSuper);
        this.superToOriginal = superToOriginal;
    }

    public Graph getDag() { return dag; }
    public Map<Integer, Integer> getMapping() { return originalToSuper; }
    public int getSuperId(int originalId) { return originalToSuper.getOrDefault(originalId, -1); }
    public List<Integer> getOriginalVertices(int superId) {
        return superToOriginal.get(superId);
    }
}
