# 02 — Pattern Recognition & Problem-Solving Framework

> Use this file **first** when you see a new problem. Identify the pattern, then go to the right file.

---

## Table of Contents
- [Keyword → Pattern Trigger Table](#keyword--pattern-trigger-table)
- [n-size → Complexity Guide](#n-size--complexity-guide)
- [Data Structure Selector](#data-structure-selector)
- [Problem-Solving Framework (6 Steps)](#problem-solving-framework-6-steps)
- [Decision Flowchart](#decision-flowchart)
- [Pattern → File Map](#pattern--file-map)
- [Complexity Summary](#complexity-summary)

---

## Keyword → Pattern Trigger Table

> Read the problem → spot these keywords → jump to the right pattern.

| Keyword / Phrase in Problem | Pattern to Use |
|---|---|
| "subarray / substring of **size K**" | Sliding Window (Fixed) |
| "**longest / shortest** subarray with condition" | Sliding Window (Dynamic) |
| "**at most K distinct**" / "no repeating characters" | Sliding Window (Dynamic) |
| "sorted + **find pair/triplet** with sum" | Two Pointers (Opposite Ends) |
| "**palindrome**" / "container with most water" | Two Pointers (Opposite Ends) |
| "linked list **cycle**" / "find middle" / "Nth from end" | Two Pointers (Fast & Slow) |
| "**sum of subarray [i,j]**" queried multiple times | Prefix Sum |
| "**count subarrays** with sum = K" | Prefix Sum + HashMap |
| "**divisible** subarrays" | Prefix Sum mod K |
| "find **duplicate / group / frequency**" | HashMap |
| "**two sum** (unsorted array)" | HashMap |
| "**anagram**" / "group anagrams" | HashMap |
| "sorted array + search" / "**O(log n)**" hinted | Binary Search |
| "find **minimum/maximum satisfying** a condition" | Binary Search on Answer |
| "**rotated sorted** array" | Binary Search (modified) |
| "**all combinations / permutations / subsets**" | Backtracking |
| "**generate all valid** ___" / "find all paths" | Backtracking |
| "**max / min / count ways**" with overlapping subproblems | Dynamic Programming |
| "**can we reach**" / "minimum steps/cost" | DP or BFS |
| "**two sequences**" compared / grid paths | DP 2D |
| "**shortest path / min steps**" (unweighted) | BFS |
| "**minimum steps**" on a grid / in a graph | BFS |
| "**level order**" / "nearest ___" | BFS |
| "**connected components**" / "flood fill" / "path exists" | DFS |
| "all paths from source to target" | DFS + Backtracking |
| "**tree height / diameter / path sum**" | DFS (return value bubbles up) |
| "**next greater / smaller** element" | Monotonic Stack |
| "**daily temperatures**" / "spans" / "histogram" | Monotonic Stack |
| "**top K**" / "**Kth largest / smallest**" | Heap / Quickselect |
| "**merge K sorted**" lists/arrays | Heap |
| "**median** of stream" | Two Heaps |
| "**dynamic connectivity**" / "detect cycle (undirected)" | Union Find (DSU) |
| "**accounts merge**" / group elements | Union Find |
| "**prerequisites**" / "order of tasks" | Topological Sort |
| "**prefix / autocomplete / word search**" | Trie |
| "**range sum queries + updates**" | BIT or Segment Tree |
| "**XOR**" / "single element" / "missing number" | Bit Manipulation |
| "negative weights" in graph | Bellman-Ford |
| "all-pairs shortest path" | Floyd-Warshall |

---

## n-size → Complexity Guide

| n (input size) | Max OK complexity | Typical pattern |
|---|---|---|
| n ≤ 20 | O(2ⁿ) / O(n!) | Backtracking, Bitmask DP |
| n ≤ 500 | O(n²) | DP 2D, Nested Two Pointers |
| n ≤ 5,000 | O(n²) safe | DP 2D, Brute + optimization |
| n ≤ 10⁵ | O(n log n) | Sort, Binary Search, Heap, Merge Sort |
| n ≤ 10⁶ | O(n) | Sliding Window, HashMap, DP 1D, BFS/DFS |
| n ≤ 10⁹ | O(log n) | Binary Search only |

---

## Data Structure Selector

| Structure | Use When |
|---|---|
| `HashMap` | Frequency count, index lookup, complement search (Two Sum) |
| `HashSet` | Membership check, seen/visited in O(1), deduplicate |
| `int[]` array (size 26 or 128) | Frequency when values are bounded (chars, digits) |
| `Deque` as stack | Monotonic stack, iterative DFS, undo operations |
| `Deque` as queue | BFS, level-order traversal |
| `PriorityQueue` (min-heap) | Top-K largest, Dijkstra, greedy minimum |
| `PriorityQueue` (max-heap) | Top-K smallest, median stream |
| `Two PriorityQueues` | Median from data stream |
| `int[][]` (2D array) | 2D DP table, grid BFS/DFS |
| `int[] parent` | Union Find (DSU) |
| `TreeMap` | Sorted keys, floor/ceiling queries, range problems |
| `TreeSet` | Sorted unique elements, nearest element search |
| `Trie` | Prefix search, autocomplete, word dictionary |
| `Deque` (monotonic) | Sliding window max/min, next greater/smaller |

---

## Problem-Solving Framework (6 Steps)

```
╔════════════════════════════════════════════════════════════╗
║  STEP 1 — UNDERSTAND THE PROBLEM                           ║
╚════════════════════════════════════════════════════════════╝
  • Restate the problem in your own words
  • Identify: input type (array? string? graph? tree?)
  • Note constraints: n ≤ 10^4? negative numbers? duplicates?
  • Ask: sorted or unsorted? single or multiple queries?
  • List edge cases: empty input, single element, all same, negatives

╔════════════════════════════════════════════════════════════╗
║  STEP 2 — MATCH THE PATTERN                                ║
╚════════════════════════════════════════════════════════════╝
  • Scan the Keyword Trigger Table above
  • Use n-size guide to shortlist valid complexities
  • If still unclear → think brute force first, then optimize

╔════════════════════════════════════════════════════════════╗
║  STEP 3 — BRUTE FORCE FIRST                                ║
╚════════════════════════════════════════════════════════════╝
  • Write the naive O(n²) or O(2ⁿ) solution
  • Verify it gives correct answers on the examples
  • This also helps identify the bottleneck

╔════════════════════════════════════════════════════════════╗
║  STEP 4 — OPTIMIZE                                         ║
╚════════════════════════════════════════════════════════════╝
  • Identify the bottleneck (usually the inner loop)
  • Apply the pattern template — swap in your specific logic:
      - Sliding Window  → change the while-shrink condition
      - Binary Search   → change the condition(mid) check
      - DP              → define dp[i] and write the recurrence
      - Backtracking    → define prune condition + base case
      - BFS/DFS         → define the validity check

╔════════════════════════════════════════════════════════════╗
║  STEP 5 — CODE                                             ║
╚════════════════════════════════════════════════════════════╝
  • Handle edge cases at the top
  • Use dummy nodes for linked lists
  • Use 1-indexed prefix arrays with size n+1
  • Test mentally: trace through example step by step

╔════════════════════════════════════════════════════════════╗
║  STEP 6 — VERIFY                                           ║
╚════════════════════════════════════════════════════════════╝
  • Test on: empty, single, two elements, all same, sorted, reversed
  • Check off-by-one errors: < vs <=, i+1 vs i, n vs n-1
  • Check for int overflow → cast to long where needed
  • Confirm return type and variable names are correct
```

---

## Decision Flowchart

```
Read the problem
        │
        ▼
Is it on a CONTIGUOUS subarray / substring?
  ├── YES ──► Fixed size K? ──► YES → Sliding Window FIXED
  │                          └── NO  → Sliding Window DYNAMIC
  └── NO ──────────────────────────────────────────────────────┐
                                                               │
        ▼                                                      │
Does it involve PAIRS or need TWO comparisons?                 │
  ├── Sorted input + pair/triplet sum ──► Two Pointers Opposite│
  ├── Linked list cycle / middle      ──► Two Pointers Fast/Slow│
  └── NO ──────────────────────────────────────────────────────┤
                                                               │
        ▼                                                      │
SUM of ranges or COUNTING subarrays?                           │
  ├── YES → Prefix Sum (+ HashMap if counting)                 │
  └── NO ──────────────────────────────────────────────────────┤
                                                               │
        ▼                                                      │
SORTED input + needs O(log n)?                                 │
  ├── Find exact value         ──► Binary Search Classic       │
  ├── Find min/max feasible    ──► Binary Search on Answer     │
  ├── Rotated sorted array     ──► Binary Search Modified      │
  └── NO ──────────────────────────────────────────────────────┤
                                                               │
        ▼                                                      │
ENUMERATE all solutions?                                       │
  ├── YES → Backtracking                                       │
  └── NO ──────────────────────────────────────────────────────┤
                                                               │
        ▼                                                      │
OVERLAPPING SUBPROBLEMS / optimal substructure?                │
  ├── One sequence / value range ──► DP 1D                     │
  ├── Two sequences / grid       ──► DP 2D                     │
  └── NO ──────────────────────────────────────────────────────┤
                                                               │
        ▼                                                      │
GRAPH / TREE problem?                                          │
  ├── Shortest path (unweighted) ──► BFS                       │
  ├── Shortest path (weighted)   ──► Dijkstra                  │
  ├── All paths / flood fill     ──► DFS                       │
  ├── Connected components       ──► DFS or Union Find         │
  ├── Detect cycle (undirected)  ──► Union Find                │
  ├── Detect cycle (directed)    ──► DFS (3-color)             │
  ├── Order of tasks             ──► Topological Sort (BFS)    │
  └── NO ──────────────────────────────────────────────────────┘
                                                               
        ▼
Next GREATER / SMALLER element / histogram?
  └── YES → Monotonic Stack

Top K / Kth largest / merge K sorted?
  └── YES → Heap / Priority Queue

PREFIX SEARCH / autocomplete?
  └── YES → Trie

Range SUM QUERIES + UPDATES?
  └── YES → BIT (Fenwick) or Segment Tree

XOR / single element / missing number?
  └── YES → Bit Manipulation
```

---

## Pattern → File Map

| Pattern | Go to File |
|---|---|
| Sliding Window (Fixed + Dynamic) | [`03_Arrays_Strings_Patterns.md`](03_Arrays_Strings_Patterns.md) |
| Two Pointers (Opposite + Fast/Slow) | [`03_Arrays_Strings_Patterns.md`](03_Arrays_Strings_Patterns.md) |
| Prefix Sum | [`03_Arrays_Strings_Patterns.md`](03_Arrays_Strings_Patterns.md) |
| Binary Search | [`03_Arrays_Strings_Patterns.md`](03_Arrays_Strings_Patterns.md) |
| HashMap / HashSet | [`03_Arrays_Strings_Patterns.md`](03_Arrays_Strings_Patterns.md) |
| Monotonic Stack | [`03_Arrays_Strings_Patterns.md`](03_Arrays_Strings_Patterns.md) |
| BFS | [`04_Graph_Tree_Patterns.md`](04_Graph_Tree_Patterns.md) |
| DFS | [`04_Graph_Tree_Patterns.md`](04_Graph_Tree_Patterns.md) |
| Union Find (DSU) | [`04_Graph_Tree_Patterns.md`](04_Graph_Tree_Patterns.md) |
| Topological Sort | [`04_Graph_Tree_Patterns.md`](04_Graph_Tree_Patterns.md) |
| Dijkstra / Bellman-Ford / Floyd-Warshall | [`04_Graph_Tree_Patterns.md`](04_Graph_Tree_Patterns.md) |
| Dynamic Programming 1D | [`05_DP_Patterns.md`](05_DP_Patterns.md) |
| Dynamic Programming 2D | [`05_DP_Patterns.md`](05_DP_Patterns.md) |
| Backtracking | [`06_Advanced_DS_Patterns.md`](06_Advanced_DS_Patterns.md) |
| Heap / Priority Queue | [`06_Advanced_DS_Patterns.md`](06_Advanced_DS_Patterns.md) |
| Trie | [`06_Advanced_DS_Patterns.md`](06_Advanced_DS_Patterns.md) |
| Segment Tree / BIT | [`06_Advanced_DS_Patterns.md`](06_Advanced_DS_Patterns.md) |

---

## Complexity Summary

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
| Bellman-Ford | O(V·E) | O(V) |
| Floyd-Warshall | O(V³) | O(V²) |
| Union Find | O(α(n)) amortized | O(n) |
| Topological Sort | O(V+E) | O(V) |
| Backtracking | O(2ⁿ) or O(n!) | O(n) stack |
| DP 1D | O(n) | O(n) → O(1) optimized |
| DP 2D | O(m·n) | O(m·n) → O(n) optimized |
| Heap (Top K) | O(n log k) | O(k) |
| Quickselect | O(n) avg | O(1) |
| Trie | O(L) per op | O(n·L) |
| BIT | O(log n) | O(n) |
| Segment Tree | O(log n) | O(n) |

---

*← Back to [Index](00_Index.md)*
