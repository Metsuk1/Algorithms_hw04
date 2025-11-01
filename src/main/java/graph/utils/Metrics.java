package graph.utils;

public class Metrics {
    private long dfsVisits = 0;
    private long edgeTraversals = 0;
    private long relaxations = 0;
    private long queueOps = 0;
    private long startNs = 0;

    public void incrementDfsVisits() { dfsVisits++; }
    public void incrementEdgeTraversals() { edgeTraversals++; }
    public void incrementRelaxations() { relaxations++; }
    public void incrementQueueOperations() { queueOps++; }
    public void startTimer() { startNs = System.nanoTime(); }
    public double getElapsedMs() { return (System.nanoTime() - startNs) / 1e6; }

    @Override
    public String toString() {
        return String.format("visits=%d, edges=%d, relax=%d, queue=%d, time=%.3fms",
                dfsVisits, edgeTraversals, relaxations, queueOps, getElapsedMs());
    }
}
