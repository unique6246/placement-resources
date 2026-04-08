# 04 — Graph & Tree Patterns

> Covers: BFS · DFS · Union Find · Topological Sort · Dijkstra · Bellman-Ford · Floyd-Warshall

---

## Table of Contents
- [1. BFS (Breadth-First Search)](#1-bfs-breadth-first-search)
- [2. DFS (Depth-First Search)](#2-dfs-depth-first-search)
- [3. Union Find (DSU)](#3-union-find-dsu)
- [4. Topological Sort](#4-topological-sort)
- [5. Dijkstra (Weighted Shortest Path)](#5-dijkstra-weighted-shortest-path)
- [6. Bellman-Ford (Negative Weights)](#6-bellman-ford-negative-weights)
- [7. Floyd-Warshall (All-Pairs)](#7-floyd-warshall-all-pairs)

---

## 1. BFS (Breadth-First Search)

**Recognize:** shortest path (unweighted), min steps, level order, "nearest ___", multi-source spread

**Core idea:** Use a **Queue** (FIFO). Mark visited **before** enqueue. Process **level by level**.

> ⚠️ Key rule: mark visited BEFORE adding to queue, not after polling — prevents duplicate enqueuing.

### Approach 1 — Simple BFS (Graph) O(V+E)
```java
Queue<Integer> queue = new LinkedList<>();
boolean[] visited = new boolean[n];

queue.offer(start);
visited[start] = true;       // mark BEFORE enqueue
int steps = 0;

while (!queue.isEmpty()) {
    int size = queue.size(); // snapshot this level's count

    for (int i = 0; i < size; i++) {
        int node = queue.poll();
        if (node == target) return steps;

        for (int neighbor : graph.get(node)) {
            if (!visited[neighbor]) {
                visited[neighbor] = true;
                queue.offer(neighbor);
            }
        }
    }
    steps++;
}
return -1;  // target unreachable
```

### Approach 2 — Grid BFS O(m·n)
```java
int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};
Queue<int[]> queue = new LinkedList<>();
boolean[][] visited = new boolean[rows][cols];

queue.offer(new int[]{startR, startC});
visited[startR][startC] = true;
int steps = 0;

while (!queue.isEmpty()) {
    int size = queue.size();
    for (int i = 0; i < size; i++) {
        int[] curr = queue.poll();
        if (curr[0] == endR && curr[1] == endC) return steps;

        for (int[] d : dirs) {
            int nr = curr[0] + d[0], nc = curr[1] + d[1];
            if (nr >= 0 && nr < rows && nc >= 0 && nc < cols
                    && !visited[nr][nc] && isValid(grid, nr, nc)) {
                visited[nr][nc] = true;
                queue.offer(new int[]{nr, nc});
            }
        }
    }
    steps++;
}
return -1;
```

### Approach 3 — Multi-Source BFS O(m·n)
Start BFS from **all source cells simultaneously**.
```java
int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};
Queue<int[]> queue = new LinkedList<>();
int[][] dist = new int[rows][cols];

// enqueue ALL sources first
for (int r = 0; r < rows; r++)
    for (int c = 0; c < cols; c++)
        if (grid[r][c] == sourceValue) {
            queue.offer(new int[]{r, c});
            dist[r][c] = 0;
        } else {
            dist[r][c] = Integer.MAX_VALUE;
        }

while (!queue.isEmpty()) {
    int[] curr = queue.poll();
    for (int[] d : dirs) {
        int nr = curr[0] + d[0], nc = curr[1] + d[1];
        if (nr >= 0 && nr < rows && nc >= 0 && nc < cols
                && dist[nr][nc] == Integer.MAX_VALUE) {
            dist[nr][nc] = dist[curr[0]][curr[1]] + 1;
            queue.offer(new int[]{nr, nc});
        }
    }
}
return dist;  // or dist[targetR][targetC]
```

### Approach 4 — Bidirectional BFS O(b^(d/2)) vs O(b^d)
Expand from both start and end simultaneously — much faster for long paths.
```java
Set<String> beginSet = new HashSet<>(), endSet = new HashSet<>();
beginSet.add(beginWord); endSet.add(endWord);
int len = 1;

while (!beginSet.isEmpty()) {
    if (beginSet.size() > endSet.size()) { // always expand smaller set
        Set<String> tmp = beginSet; beginSet = endSet; endSet = tmp;
    }
    Set<String> next = new HashSet<>();
    for (String word : beginSet) {
        char[] arr = word.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            char orig = arr[i];
            for (char c = 'a'; c <= 'z'; c++) {
                arr[i] = c;
                String newWord = new String(arr);
                if (endSet.contains(newWord)) return len + 1;
                if (wordList.contains(newWord)) next.add(newWord);
            }
            arr[i] = orig;
        }
    }
    beginSet = next;
    len++;
}
return 0;
```

**LeetCode Problems:**
- Binary Tree Level Order Traversal — LC 102
- Word Ladder — LC 127
- 01 Matrix — LC 542
- Rotting Oranges — LC 994
- Shortest Path in Binary Matrix — LC 1091
- Walls and Gates — LC 286
- Snakes and Ladders — LC 909

---

## 2. DFS (Depth-First Search)

**Recognize:** connected components, flood fill, path exists, all paths, tree height/diameter, cycle detection

**Core idea:** Use **recursion** or an explicit **Stack** (LIFO). Mark visited before recursing.

> The **return value** from DFS carries information back up the call stack.

### Approach 1 — Recursive DFS (Graph) O(V+E)
```java
void dfs(int node, boolean[] visited, List<List<Integer>> graph) {
    visited[node] = true;   // mark BEFORE recursing
    for (int neighbor : graph.get(node))
        if (!visited[neighbor])
            dfs(neighbor, visited, graph);
}

// Count components:
int count = 0;
for (int i = 0; i < n; i++) {
    if (!visited[i]) { dfs(i, visited, graph); count++; }
}
return count;
```

### Approach 2 — Iterative DFS (Explicit Stack) O(V+E)
```java
Deque<Integer> stack = new ArrayDeque<>();
boolean[] visited = new boolean[n];

stack.push(start);
while (!stack.isEmpty()) {
    int node = stack.pop();
    if (visited[node]) continue;   // skip if already visited
    visited[node] = true;
    for (int neighbor : graph.get(node))
        if (!visited[neighbor]) stack.push(neighbor);
}
```

### Approach 3 — Grid DFS (Flood Fill / Island Count) O(m·n)
```java
void dfs(char[][] grid, int r, int c) {
    if (r < 0 || r >= grid.length ||
        c < 0 || c >= grid[0].length ||
        grid[r][c] != '1') return;     // out of bounds or water

    grid[r][c] = '0';                  // sink = mark as visited
    dfs(grid, r+1, c);
    dfs(grid, r-1, c);
    dfs(grid, r, c+1);
    dfs(grid, r, c-1);
}

int numIslands(char[][] grid) {
    int count = 0;
    for (int r = 0; r < grid.length; r++)
        for (int c = 0; c < grid[0].length; c++)
            if (grid[r][c] == '1') { dfs(grid, r, c); count++; }
    return count;
}
```

### Approach 4 — Tree DFS (Return Value Bubbles Up) O(n)
```java
int result = Integer.MIN_VALUE;

int dfs(TreeNode node) {
    if (node == null) return 0;
    int left  = Math.max(0, dfs(node.left));    // ignore negative paths
    int right = Math.max(0, dfs(node.right));
    result = Math.max(result, node.val + left + right);  // update global answer
    return node.val + Math.max(left, right);             // return best single branch
}
// Call: dfs(root); return result;
```

### Approach 5 — Cycle Detection DFS (Directed Graph, 3-Color) O(V+E)
```java
// state: 0 = unvisited, 1 = in current DFS path, 2 = fully processed
int[] state = new int[n];
boolean hasCycle = false;

void dfs(int node) {
    state[node] = 1;                             // mark: in current path
    for (int neighbor : graph.get(node)) {
        if (state[neighbor] == 1) { hasCycle = true; return; }  // back edge = cycle!
        if (state[neighbor] == 0) dfs(neighbor);
    }
    state[node] = 2;                             // mark: fully done
    topoOrder.addFirst(node);                    // post-order = reverse topological
}
```

### Approach 6 — All Paths DFS O(2^n · n)
```java
void dfs(int node, List<Integer> path, List<List<Integer>> result) {
    path.add(node);
    if (node == target) {
        result.add(new ArrayList<>(path));
    } else {
        for (int neighbor : graph.get(node))
            dfs(neighbor, path, result);
    }
    path.remove(path.size() - 1);   // backtrack
}
```

**LeetCode Problems:**
- Number of Islands — LC 200
- Max Area of Island — LC 695
- Clone Graph — LC 133
- Path Sum — LC 112
- Binary Tree Maximum Path Sum — LC 124
- Surrounded Regions — LC 130
- Pacific Atlantic Water Flow — LC 417
- Course Schedule — LC 207 (cycle detection)
- Lowest Common Ancestor — LC 236

---

## 3. Union Find (DSU)

**Recognize:** connected components, detect cycle (undirected), dynamic connectivity, merge groups

**Core idea:** Each element points to a parent. `find()` with path compression. `union()` with rank.

### Approach 1 — DFS for Components O(V+E)
```java
int count = 0;
boolean[] visited = new boolean[n];
for (int i = 0; i < n; i++) {
    if (!visited[i]) { dfs(i, visited, graph); count++; }
}
return count;
```

### Approach 2 — Union Find (Basic with Path Compression) O(n)
```java
int[] parent = new int[n];
for (int i = 0; i < n; i++) parent[i] = i;   // each node is its own parent

int find(int x) {
    if (parent[x] != x)
        parent[x] = find(parent[x]);           // PATH COMPRESSION
    return parent[x];
}

void union(int x, int y) {
    parent[find(x)] = find(y);
}
```

### Approach 3 — Union Find with Rank ✅ OPTIMAL O(α(n)) ≈ O(1) amortized
```java
class UnionFind {
    int[] parent, rank;
    int components;

    UnionFind(int n) {
        parent = new int[n];
        rank   = new int[n];
        components = n;
        for (int i = 0; i < n; i++) parent[i] = i;
    }

    int find(int x) {
        if (parent[x] != x)
            parent[x] = find(parent[x]);   // PATH COMPRESSION
        return parent[x];
    }

    boolean union(int x, int y) {
        int px = find(x), py = find(y);
        if (px == py) return false;         // already connected → cycle detected!

        // UNION BY RANK — attach smaller tree under larger
        if      (rank[px] < rank[py]) parent[px] = py;
        else if (rank[px] > rank[py]) parent[py] = px;
        else { parent[py] = px; rank[px]++; }

        components--;
        return true;
    }

    boolean connected(int x, int y) { return find(x) == find(y); }
}
```

### Approach 4 — Detect Cycle in Undirected Graph
```java
UnionFind uf = new UnionFind(n);
for (int[] edge : edges) {
    if (!uf.union(edge[0], edge[1]))
        return false;   // union returned false = already connected = cycle!
}
return true;  // no cycle
```

### Approach 5 — Accounts Merge Pattern
```java
UnionFind uf = new UnionFind(accounts.size());
Map<String, Integer> emailOwner = new HashMap<>();

for (int i = 0; i < accounts.size(); i++)
    for (int j = 1; j < accounts.get(i).size(); j++) {
        String email = accounts.get(i).get(j);
        if (emailOwner.containsKey(email))
            uf.union(i, emailOwner.get(email));
        else
            emailOwner.put(email, i);
    }

Map<Integer, List<String>> groups = new HashMap<>();
for (Map.Entry<String, Integer> e : emailOwner.entrySet())
    groups.computeIfAbsent(uf.find(e.getValue()), k -> new ArrayList<>()).add(e.getKey());

List<List<String>> result = new ArrayList<>();
for (Map.Entry<Integer, List<String>> e : groups.entrySet()) {
    List<String> emails = e.getValue();
    Collections.sort(emails);
    emails.add(0, accounts.get(e.getKey()).get(0));   // prepend account name
    result.add(emails);
}
return result;
```

**LeetCode Problems:**
- Number of Provinces — LC 547
- Redundant Connection — LC 684
- Graph Valid Tree — LC 261
- Accounts Merge — LC 721
- Number of Connected Components — LC 323
- Smallest String With Swaps — LC 1202
- Making a Large Island — LC 827

---

## 4. Topological Sort

**Recognize:** "prerequisites", "order of tasks", "course schedule", DAG ordering

**Core idea:** Process nodes with **in-degree 0** first. Remove them and reduce neighbor in-degrees.

### Approach 1 — BFS / Kahn's Algorithm O(V+E) ✅
```java
int[] inDegree = new int[n];
for (int[] edge : edges) inDegree[edge[1]]++;   // edge[0] → edge[1]

Queue<Integer> queue = new LinkedList<>();
for (int i = 0; i < n; i++)
    if (inDegree[i] == 0) queue.offer(i);        // start with no prerequisites

List<Integer> order = new ArrayList<>();
while (!queue.isEmpty()) {
    int node = queue.poll();
    order.add(node);
    for (int neighbor : graph.get(node))
        if (--inDegree[neighbor] == 0) queue.offer(neighbor);
}

return order.size() == n ? order : new ArrayList<>();  // empty = cycle exists
```

### Approach 2 — DFS Post-Order O(V+E)
```java
int[] state = new int[n];  // 0=unvisited, 1=in-stack, 2=done
Deque<Integer> topoOrder = new ArrayDeque<>();
boolean hasCycle = false;

void dfs(int node) {
    state[node] = 1;
    for (int neighbor : graph.get(node)) {
        if (state[neighbor] == 1) { hasCycle = true; return; }
        if (state[neighbor] == 0) dfs(neighbor);
    }
    state[node] = 2;
    topoOrder.addFirst(node);   // add to front = reverse post-order
}

// Call dfs for all unvisited nodes
for (int i = 0; i < n; i++)
    if (state[i] == 0) dfs(i);
```

### When to Use Which
| Situation | Use |
|---|---|
| Need to detect cycle + get order | Kahn's BFS |
| Just detect cycle | DFS 3-color |
| Need reverse topological (sink → source) | DFS post-order |

**LeetCode Problems:**
- Course Schedule — LC 207
- Course Schedule II — LC 210
- Alien Dictionary — LC 269
- Sequence Reconstruction — LC 444
- Find All Possible Recipes from Given Supplies — LC 2115

---

## 5. Dijkstra (Weighted Shortest Path)

**Recognize:** shortest path in a **weighted graph** with **non-negative weights**

**Core idea:** Greedy BFS with a **min-heap** on (distance, node). Relax edges when a shorter path is found.

### Approach 1 — Dijkstra with Min-Heap O((V+E) log V) ✅
```java
PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
// stores [node, distance]
int[] dist = new int[n];
Arrays.fill(dist, Integer.MAX_VALUE);
dist[src] = 0;
pq.offer(new int[]{src, 0});

while (!pq.isEmpty()) {
    int[] curr = pq.poll();
    int node = curr[0], d = curr[1];
    if (d > dist[node]) continue;          // stale entry — skip

    for (int[] edge : graph.get(node)) {
        int neighbor = edge[0], weight = edge[1];
        if (dist[node] + weight < dist[neighbor]) {
            dist[neighbor] = dist[node] + weight;
            pq.offer(new int[]{neighbor, dist[neighbor]});
        }
    }
}
return dist;   // dist[target] for specific target
```

### Approach 2 — Dijkstra with State (Modified)
Use when state is (node, extra_info) like (node, stops_remaining).
```java
// [node, cost, extra_state]
PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
pq.offer(new int[]{src, 0, 0});

while (!pq.isEmpty()) {
    int[] curr = pq.poll();
    int node = curr[0], cost = curr[1], stops = curr[2];
    if (node == dst) return cost;
    if (stops > k) continue;   // extra constraint: max K stops
    for (int[] edge : graph.get(node))
        pq.offer(new int[]{edge[0], cost + edge[1], stops + 1});
}
return -1;
```

**LeetCode Problems:**
- Network Delay Time — LC 743
- Cheapest Flights Within K Stops — LC 787
- Path With Minimum Effort — LC 1631
- Swim in Rising Water — LC 778
- Find the City With Smallest Number of Neighbors — LC 1334

---

## 6. Bellman-Ford (Negative Weights)

**Recognize:** shortest path with **negative edge weights**, detect negative cycles

**Core idea:** Relax ALL edges V-1 times. An extra V-th pass that still relaxes = negative cycle.

```java
int[] dist = new int[n];
Arrays.fill(dist, Integer.MAX_VALUE);
dist[src] = 0;

// Relax all edges V-1 times
for (int i = 0; i < n - 1; i++) {
    for (int[] edge : edges) {   // edge = [from, to, weight]
        int u = edge[0], v = edge[1], w = edge[2];
        if (dist[u] != Integer.MAX_VALUE && dist[u] + w < dist[v])
            dist[v] = dist[u] + w;
    }
}

// V-th pass: if still relaxes → negative cycle
for (int[] edge : edges) {
    if (dist[edge[0]] != Integer.MAX_VALUE && dist[edge[0]] + edge[2] < dist[edge[1]])
        return -1;  // negative cycle detected
}
return dist[target];
```

**LeetCode Problems:**
- Cheapest Flights Within K Stops (alt) — LC 787
- Negative Weight Cycle — various

---

## 7. Floyd-Warshall (All-Pairs Shortest Path)

**Recognize:** need shortest path between **all pairs** of nodes, small graph (V ≤ 500)

**Core idea:** For each intermediate node k, try routing every (i,j) pair through k.

```java
// Initialize
int[][] dist = new int[n][n];
for (int[] row : dist) Arrays.fill(row, Integer.MAX_VALUE / 2);  // /2 to avoid overflow
for (int i = 0; i < n; i++) dist[i][i] = 0;
for (int[] edge : edges)
    dist[edge[0]][edge[1]] = edge[2];  // directed weighted

// Floyd-Warshall: try every intermediate node k
for (int k = 0; k < n; k++)
    for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++)
            if (dist[i][k] + dist[k][j] < dist[i][j])
                dist[i][j] = dist[i][k] + dist[k][j];

// dist[i][j] = shortest path from i to j
// dist[i][i] < 0 → negative cycle
```

**LeetCode Problems:**
- Find the City With Smallest Number of Neighbors — LC 1334
- Network Delay Time (all-pairs variant)
- Transitive closure problems

---

## Graph Algorithm Decision Guide

```
Need shortest path?
  ├── Unweighted graph / grid → BFS
  ├── Weighted, non-negative   → Dijkstra (min-heap BFS)
  ├── Weighted, negative       → Bellman-Ford
  └── All pairs               → Floyd-Warshall

Need connected components / grouping?
  ├── Static graph             → DFS or BFS
  └── Dynamic (edges added)   → Union Find

Need cycle detection?
  ├── Undirected graph         → Union Find
  └── Directed graph           → DFS (3-color state)

Need ordering (DAG)?
  └── Topological Sort (Kahn's BFS or DFS post-order)
```

---

*← Back to [Index](00_Index.md) | Next: [DP Patterns →](05_DP_Patterns.md)*
