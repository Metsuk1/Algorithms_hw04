package graph.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import graph.models.Graph;
import graph.models.GraphJson;
import graph.models.LoadedGraph;

import java.nio.file.Paths;

/**
 * Utility class for loading directed graphs from JSON files.
 * Uses Jackson to parse a JSON file describing a graph structure into
 *  Graph object with vertices and weighted edges.
 */
public class JsonGraphLoader {
    private static final ObjectMapper mapper = createMapper();

    /**
     * Configures a Jackson ObjectMapper with standard deserialization options.
     * @return a pre-configured ObjectMapper instance
     */
    private static ObjectMapper createMapper(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    /**
     * Loads a graph from a given JSON file path.
     * @param path path to the JSON file describing the graph
     * @return a LoadedGraph object containing the graph, source node, and weight model
     * @throws RuntimeException if the file cannot be read or parsed
     */
    public LoadedGraph load(String path) {
        try {
            GraphJson json = mapper.readValue(Paths.get(path).toFile(), GraphJson.class);
            Graph graph = new Graph(json.n);

            for (GraphJson.EdgeJson e : json.edges) {
                graph.addEdge(graph.getVertex(e.u), graph.getVertex(e.v), e.w);
            }

            return new LoadedGraph(graph, json.source, json.weightModel);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load: " + path, e);
        }
    }
}


