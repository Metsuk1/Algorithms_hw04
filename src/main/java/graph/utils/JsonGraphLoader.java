package graph.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import graph.models.Graph;
import graph.models.GraphJson;
import graph.models.LoadedGraph;

import java.nio.file.Paths;

public class JsonGraphLoader {
    private static final ObjectMapper mapper = createMapper();

    private static ObjectMapper createMapper(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

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


