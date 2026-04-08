# 📚 DSA Study Notes — Master Index

> All notes are organized by concept. Start with Foundations, then pick the pattern group you need.

---

## 🗂️ File Structure

```
notes/
├── 00_Index.md                   ← YOU ARE HERE — master navigation
├── 01_Foundations.md             ← Big-O, Java DS, Arrays, Strings, Trees, Graphs, Recursion, Sorting, Bit Math
├── 02_Pattern_Recognition.md     ← Trigger words, decision flowchart, 3-step problem-solving framework
├── 03_Arrays_Strings_Patterns.md ← Sliding Window, Two Pointers, Prefix Sum, Binary Search, HashMap, Monotonic Stack
├── 04_Graph_Tree_Patterns.md     ← BFS, DFS, Union Find, Topological Sort, Dijkstra, Bellman-Ford, Floyd-Warshall
├── 05_DP_Patterns.md             ← Dynamic Programming 1D + 2D — all approaches, recurrence tables
└── 06_Advanced_DS_Patterns.md    ← Backtracking, Heap/PQ, Trie, Segment Tree, Binary Indexed Tree
```

---

## ⚡ Quick Jump by Topic

| I want to... | Go to |
|---|---|
| Understand Big-O, Java collections, recursion basics | [`01_Foundations.md`](01_Foundations.md) |
| Figure out which pattern to use for a problem | [`02_Pattern_Recognition.md`](02_Pattern_Recognition.md) |
| Work on array/string problems | [`03_Arrays_Strings_Patterns.md`](03_Arrays_Strings_Patterns.md) |
| Work on graph/tree problems | [`04_Graph_Tree_Patterns.md`](04_Graph_Tree_Patterns.md) |
| Work on DP problems | [`05_DP_Patterns.md`](05_DP_Patterns.md) |
| Work on backtracking / heaps / advanced DS | [`06_Advanced_DS_Patterns.md`](06_Advanced_DS_Patterns.md) |

---

## 🧩 Pattern → File Map

| Pattern | File |
|---|---|
| Sliding Window (Fixed + Dynamic) | `03_Arrays_Strings_Patterns.md` |
| Two Pointers (Opposite + Fast/Slow) | `03_Arrays_Strings_Patterns.md` |
| Prefix Sum | `03_Arrays_Strings_Patterns.md` |
| Binary Search | `03_Arrays_Strings_Patterns.md` |
| HashMap / HashSet | `03_Arrays_Strings_Patterns.md` |
| Monotonic Stack | `03_Arrays_Strings_Patterns.md` |
| BFS | `04_Graph_Tree_Patterns.md` |
| DFS | `04_Graph_Tree_Patterns.md` |
| Union Find (DSU) | `04_Graph_Tree_Patterns.md` |
| Topological Sort | `04_Graph_Tree_Patterns.md` |
| Dijkstra / Bellman-Ford / Floyd-Warshall | `04_Graph_Tree_Patterns.md` |
| Dynamic Programming 1D | `05_DP_Patterns.md` |
| Dynamic Programming 2D | `05_DP_Patterns.md` |
| Backtracking | `06_Advanced_DS_Patterns.md` |
| Heap / Priority Queue | `06_Advanced_DS_Patterns.md` |
| Trie | `06_Advanced_DS_Patterns.md` |
| Segment Tree / BIT | `06_Advanced_DS_Patterns.md` |

---

## 📊 Complexity at a Glance

| Pattern | Time | Space |
|---|---|---|
| Sliding Window | O(n) | O(1) or O(k) |
| Two Pointers | O(n) | O(1) |
| Prefix Sum | O(n) build, O(1) query | O(n) |
| HashMap | O(n) | O(n) |
| Binary Search | O(log n) | O(1) |
| Monotonic Stack | O(n) | O(n) |
| BFS | O(V+E) | O(V) |
| DFS | O(V+E) | O(V) stack |
| Dijkstra | O((V+E) log V) | O(V) |
| Union Find | O(α(n)) amortized | O(n) |
| Backtracking | O(2ⁿ) or O(n!) | O(n) stack |
| DP 1D | O(n) | O(n) → O(1) |
| DP 2D | O(m·n) | O(m·n) → O(n) |
| Heap (Top K) | O(n log k) | O(k) |
| Quickselect | O(n) avg | O(1) |
| Trie | O(L) per op | O(n·L) |
| BIT | O(log n) | O(n) |
| Segment Tree | O(log n) | O(n) |

---

## 🔢 n-size → Which Complexity is Acceptable

| n (input size) | Max acceptable complexity | Use pattern |
|---|---|---|
| n ≤ 20 | O(2ⁿ) / O(n!) | Backtracking, Bitmask DP |
| n ≤ 500 | O(n²) | DP 2D, Nested loops |
| n ≤ 10,000 | O(n²) borderline | Try O(n log n) |
| n ≤ 10⁵ | O(n log n) | Sort, Binary Search, Heap |
| n ≤ 10⁶ | O(n) | Sliding Window, HashMap, DP 1D |
| n ≤ 10⁹ | O(log n) | Binary Search only |
