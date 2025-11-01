# Graph Algorithms Report
Course: Design and Analysis of Algorithms        
Student:Abayev Bayazit    

This report presents a complete empirical and theoretical analysis of a graph processing pipeline for task dependency scheduling, including:            

Strongly Connected Components (SCC) via Tarjan’s algorithm            
Condensation DAG construction        
Topological Sort using Kahn’s algorithm        
Single-source Shortest Path on DAG (DP over topo order)        
Critical Path (Longest Path) via edge weight negation + shortest path   
All algorithms run in O(V + E) — linear time.

## 1. Data Summary

| Dataset                     | #Nodes | #Edges | Density    | Cyclic        | Weight Model   |
| --------------------------- | ------ | ------ | ---------- | ------------- | -------------- |
| `tasks_small_1_cycle.json`  | 8      | 7      | Sparse     | Yes           | Node durations |
| `tasks_small_2_dag.json`    | 10     | 12     | Medium     | No            | Node durations |
| `tasks_small_3_mixed.json`  | 7      | 9      | Mixed      | Yes (partial) | Node durations |
| `tasks_medium_1_dense.json` | 15     | 28     | Dense      | Yes           | Node durations |
| `tasks_medium_2_scc.json`   | 18     | 22     | Medium     | Mixed         | Node durations |
| `tasks_medium_3_dag.json`   | 20     | 23     | Sparse DAG | No            | Node durations |
| `tasks_large_1_sparse.json` | 30     | 34     | Sparse     | No            | Node durations |
| `tasks_large_2_cyclic.json` | 40     | 49     | Medium     | Yes           | Node durations |
| `tasks_large_3_dense.json`  | 50     | 62     | Dense      | Mixed         | Node durations |

---

## 2. Results Summary

### 2.1 Strongly Connected Components (SCC)

| Dataset        | #SCC | Max Size | Min Size | Avg Size | Time (ms) |
| -------------- | ---- | -------- | -------- | -------- | --------- |
| small_1_cycle  | 6    | 3        | 1        | 1.33     | 2.14      |
| small_2_dag    | 10   | 1        | 1        | 1.0      | 1.14      |
| small_3_mixed  | 5    | 2        | 1        | 1.4      | 0.57      |
| medium_1_dense | 1    | 15       | 15       | 15       | 0.57      |
| medium_2_scc   | 11   | 3        | 1        | 1.64     | 1.19      |
| medium_3_dag   | 20   | 1        | 1        | 1.0      | 2.28      |
| large_1_sparse | 30   | 1        | 1        | 1.0      | 15.09     |
| large_2_cyclic | 24   | 3        | 1        | 1.67     | 3.94      |
| large_3_dense  | 34   | 3        | 1        | 1.47     | 4.12      |

**Observation:**

* Sparse DAGs have mostly trivial SCCs of size 1.
* Cyclic and dense graphs yield multi-node SCCs.
* Tarjan’s algorithm performs well even for large graphs, scaling linearly with `O(V + E)`.

---

### 2.2 Topological Sort

| Dataset        | Edges | Queue Ops | Time (ms) |
| -------------- | ----- | --------- | --------- |
| small_1_cycle  | 4     | 10        | 0.70      |
| small_2_dag    | 12    | 19        | 0.40      |
| small_3_mixed  | 5     | 8         | 0.32      |
| medium_1_dense | 0     | 1         | 0.39      |
| medium_2_scc   | 10    | 18        | 0.39      |
| medium_3_dag   | 23    | 39        | 0.55      |
| large_1_sparse | 34    | 59        | 4.40      |
| large_2_cyclic | 24    | 39        | 0.91      |
| large_3_dense  | 37    | 59        | 0.80      |

**Observation:**

* Kahn’s topological sort is linear in `O(V + E)` and performs consistently.
* Dense DAGs with high connectivity slightly increase queue operations but remain efficient.

---

### 2.3 Shortest Paths (DAG-based)

| Dataset        | Relax Ops | Time (ms) | Source Node | Path Length | Path                 |
| -------------- | --------- | --------- | ----------- | ----------- | -------------------- |
| small_1_cycle  | 3         | 0.62      | 4           | 8           | [4,5,6,7]            |
| small_2_dag    | 12        | 0.39      | 0           | 12          | [0,1,3,7]            |
| small_3_mixed  | 2         | 0.33      | 3           | 3           | [3,5,4]              |
| medium_1_dense | 0         | 0.46      | 8           | 0           | [5..0]               |
| medium_2_scc   | 5         | 0.40      | 12          | 11          | [12..17]             |
| medium_3_dag   | 17        | 0.52      | 5           | 19          | [5,7,12,14,16,18,19] |
| large_1_sparse | 22        | 3.26      | 10          | 51          | [10..19]             |
| large_2_cyclic | 15        | 0.79      | 25          | 34          | [25..38]             |
| large_3_dense  | 15        | 0.68      | 35          | 36          | [35..48]             |

**Observation:**

* Shortest path computations on DAGs using topological relaxation scale linearly.
* In cyclic cases, SCC compression effectively reduces the problem to a DAG.

---

### 2.4 Critical Path (Longest Path in DAG)

| Dataset        | Relax Ops | Time (ms) | Critical Path Length | Path            |
| -------------- | --------- | --------- | -------------------- | --------------- |
| small_1_cycle  | 3         | 0.50      | 8                    | [4,5,6,7]       |
| small_2_dag    | 12        | 0.27      | 17                   | [0,1,4,5,6,8,9] |
| small_3_mixed  | 2         | 0.30      | 4                    | [3,5,4]         |
| medium_1_dense | 0         | 0.41      | 0                    | [5..0]          |
| medium_2_scc   | 5         | 0.30      | 11                   | [12..17]        |
| medium_3_dag   | 17        | 0.36      | 19                   | [5..19]         |
| large_1_sparse | 22        | 1.50      | 51                   | [10..29]        |
| large_2_cyclic | 15        | 0.50      | 34                   | [25..39]        |
| large_3_dense  | 15        | 0.38      | 36                   | [35..49]        |

**Observation:**

* Critical path analysis (longest path) is efficiently obtained via dynamic programming in topological order.
* Dense graphs lead to longer paths but similar runtime complexity.

---

## 3. Analysis and Discussion

### 3.1 Bottlenecks and Performance

* **SCC Computation** dominates total runtime for large graphs since it must traverse the entire structure.
* **Topological Sort** and **Path Computations** are very lightweight once SCC compression is done.
* **Graph Density** slightly affects edge traversal cost but not asymptotic complexity.

### 3.2 Effect of Structure

| Structure                | SCC Sizes        | Performance Impact          | Notes                         |
| ------------------------ | ---------------- | --------------------------- | ----------------------------- |
| Sparse DAG               | Mostly size 1    | Fastest overall             | Ideal for scheduling problems |
| Dense Cyclic             | Multi-node SCCs  | SCC cost increases          | Requires condensation         |
| Fully Strongly Connected | Single large SCC | DAG degenerates to one node | Topo/Path trivial             |

---

## 4. Conclusions

### When to Use Each Method

| Method                          | Best Use Case                            | Complexity | Remarks                           |
| ------------------------------- | ---------------------------------------- | ---------- | --------------------------------- |
| **Tarjan SCC**                  | Detecting cycles or modular structure    | O(V + E)   | Best for pre-processing any graph |
| **Topological Sort (Kahn/DFS)** | Task scheduling, dependency resolution   | O(V + E)   | Requires acyclic graph            |
| **Shortest Paths (DAG)**        | Time/cost optimization with dependencies | O(V + E)   | Works only on DAGs                |
| **Critical Path Analysis**      | Project management, latency estimation   | O(V + E)   | Longest-path in DAG context       |

### Practical Recommendations

* Always **compress SCCs** before performing DAG algorithms.
* For **large sparse graphs**, performance remains near-linear — suitable for real-time or large-scale dependency analysis.
* **Dense cyclic graphs** benefit most from SCC + condensation preprocessing.
* For **project scheduling**, combine SCC detection + topo sort + longest-path DP to identify bottlenecks.

---

**Overall conclusion:**
The implemented algorithms (Tarjan, Kahn, DAG-based shortest/longest paths) demonstrate consistent `O(V + E)` scaling across all datasets. Structural variations (density, cyclicity) mainly affect constant factors, not asymptotic behavior. The system efficiently extracts dependency order, computes critical paths, and identifies performance bottlenecks across diverse graph types.
