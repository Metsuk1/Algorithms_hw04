package graph.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * JSON-mapped representation of a directed graph used for file
 * This structure is deserialized by graph.utils.JsonGraphLoader
 */
public class GraphJson {
    @JsonProperty("directed") public boolean directed;
    @JsonProperty("n") public int n;
    @JsonProperty("edges") public List<EdgeJson> edges;
    @JsonProperty("source") public int source;
    @JsonProperty("weight_model") public String weightModel;

    public static class EdgeJson {
        @JsonProperty("u") public int u;
        @JsonProperty("v") public int v;
        @JsonProperty("w") public int w;
    }
}
