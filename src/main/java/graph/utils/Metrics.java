package graph.utils;

/**
 * Collects and reports performance metrics for graph algorithms.
 * Tracks operation counts (DFS visits, edge traversals, relaxations, queue operations)
 * and measures execution time using System.nanoTime().
 */
public class Metrics {
    private long dfsVisits = 0;
    private long edgeTraversals = 0;
    private long relaxations = 0;
    private long queueOps = 0;
    private long startNs = 0;


    public void incrementDfsVisits() { dfsVisits++; }
    public void incrementEdgeTraversals() { edgeTraversals++; }
    public void incrementRelaxations() { relaxations++; }
    public void incrementRelaxations(long count) { relaxations += count; }
    public void incrementQueueOperations() { queueOps++; }
    public void startTimer() { startNs = System.nanoTime(); }
    public double getElapsedMs() { return (System.nanoTime() - startNs) / 1e6; }

    @Override
    public String toString() {
        return String.format("visits=%d, edges=%d, relax=%d, queue=%d, time=%.3fms",
                dfsVisits, edgeTraversals, relaxations, queueOps, getElapsedMs());
    }
}
