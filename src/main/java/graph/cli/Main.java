package graph.cli;

import graph.utils.JsonGraphLoader;
import graph.dagsp.*;
import graph.models.*;
import graph.scc.*;
import graph.topo.TopologicalSort;
import graph.utils.*;
import java.nio.file.*;
import java.util.List;

public class Main {
    private static final JsonGraphLoader LOADER = new JsonGraphLoader();

    public static void main(String[] args) throws Exception {
        Path dataDir = Paths.get("data");
        if (!Files.isDirectory(dataDir)) {
            System.err.println("Error: 'data/' directory not found!");
            return;
        }

        for (Path file : Files.list(dataDir)
                .filter(p -> p.toString().endsWith(".json"))
                .sorted()
                .toList()) {

            System.out.println("\n" + "=".repeat(70));
            System.out.println(" FILE: " + file.getFileName());
            System.out.println("=".repeat(70));

            LoadedGraph loaded = LOADER.load(file.toString());
            Graph originalGraph = loaded.getGraph();
            int sourceId = loaded.getSource();

            // SCC + Condensation
            Metrics sccMetrics = new Metrics();
            List<List<Integer>> sccs = new SCCTarjan().findSCCs(originalGraph, sccMetrics);
            printSCCs(sccs);

            CondensationResult result = CondensationBuilder.build(originalGraph, sccs);
            Graph dag = result.getDag();
            int sourceSuper = result.getSuperId(sourceId);

            if (sourceSuper == -1) {
                System.out.println("Source vertex " + sourceId + " not found in graph!");
                continue;
            }

            //Topological Sort
            Metrics topoMetrics = new Metrics();
            List<Integer> topoOrder = new TopologicalSort().sort(dag, topoMetrics);
            System.out.println("Topological order: " + topoOrder);

            // Shortest path
            Metrics spMetrics = new Metrics();
            DagShortestPath.Result spResult = new DagShortestPath()
                    .compute(dag, sourceSuper, topoOrder, spMetrics);

            int[] dist = spResult.getDistances();
            printShortestPath(sourceId, dist);

            // find farthest
            int farthest = -1;
            int maxDist = -1;
            for (int i = 0; i < dist.length; i++) {
                if (dist[i] != Integer.MAX_VALUE && dist[i] > maxDist) {
                    maxDist = dist[i];
                    farthest = i;
                }
            }

            // path in super vertices
            List<Integer> spSuperPath = spResult.reconstructPath(sourceSuper, farthest);

            // path in original vertices
            List<Integer> spOriginalPath = spSuperPath.stream()
                    .flatMap(id -> result.getOriginalVertices(id).stream())
                    .toList();
            System.out.println("Shortest path (original): " + spOriginalPath);

            // Critical path
            Metrics lpMetrics = new Metrics();
            DagLongestPath lp = new DagLongestPath();
            int criticalLength = lp.compute(dag, sourceSuper, lpMetrics);
            List<Integer> lpSuperPath = lp.reconstructLongestPath(dag, sourceSuper, lpMetrics);
            List<Integer> lpOriginalPath = lpSuperPath.stream()
                    .flatMap(id -> result.getOriginalVertices(id).stream())
                    .toList();

            System.out.println("Critical path length: " + criticalLength);
            System.out.println("Critical path (original): " + lpOriginalPath);

            // Metrics Summary
            System.out.println("\n--- Performance Metrics ---");
            System.out.println("SCC          : " + sccMetrics);
            System.out.println("Topo sort    : " + topoMetrics);
            System.out.println("Shortest path: " + spMetrics);
            System.out.println("Critical path: " + lpMetrics);
          }
    }

    private static void printSCCs(List<List<Integer>> sccs) {
        System.out.println("Strongly Connected Components (" + sccs.size() + "):");
        for (int i = 0; i < sccs.size(); i++) {
            List<Integer> comp = sccs.get(i);
            System.out.printf("  C%-2d [size=%2d]: %s%n", i, comp.size(), comp);
        }
    }

    private static void printShortestPath(int source, int[] dist) {
        System.out.print("Shortest paths from source " + source + ": [");
        for (int i = 0; i < dist.length; i++) {
            String val = dist[i] == Integer.MAX_VALUE ? "INF" : String.valueOf(dist[i]);
            System.out.print(val);
            if (i < dist.length - 1) System.out.print(", ");
        }
        System.out.println("]");
    }
}