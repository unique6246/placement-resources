# 06 — Advanced Data Structures & Patterns

> Covers: Backtracking · Heap / Priority Queue · Trie · Segment Tree · Binary Indexed Tree (BIT)

---

## Table of Contents
- [1. Backtracking](#1-backtracking)
- [2. Heap / Priority Queue](#2-heap--priority-queue)
- [3. Trie](#3-trie)
- [4. Binary Indexed Tree (BIT / Fenwick)](#4-binary-indexed-tree-bit--fenwick)
- [5. Segment Tree](#5-segment-tree)

---

## 1. Backtracking

**Recognize:** "all combinations", "all permutations", "all subsets", "generate all valid ___", "find all paths"

**Core idea:** **Choose → Explore → Un-choose**.  
Prune early when constraint is already violated (before recursing deeper).

```
State Space Tree:
  Each node = a partial solution
  Each edge = one choice made
  Leaf = complete solution (or dead end)
  BACKTRACK = undo last choice, try next option
```

### Universal Template
```java
void backtrack(/* parameters */) {

    // 1. BASE CASE — record valid solution
    if (complete condition) {
        result.add(new ArrayList<>(current));
        return;
    }

    for (int i = start; i < choices.length; i++) {

        // 2. PRUNE — skip invalid branches EARLY
        if (invalid condition) continue;  // or break

        current.add(choices[i]);           // CHOOSE
        backtrack(/* updated params */);   // EXPLORE
        current.remove(current.size()-1);  // UN-CHOOSE (backtrack)
    }
}
```

### Approach 1 — Subsets O(2ⁿ)
```java
List<List<Integer>> result = new ArrayList<>();

void backtrack(int[] nums, int start, List<Integer> current) {
    result.add(new ArrayList<>(current));   // add at every node (not just leaf)

    for (int i = start; i < nums.length; i++) {
        current.add(nums[i]);
        backtrack(nums, i + 1, current);    // i+1 = don't reuse current element
        current.remove(current.size() - 1);
    }
}
// Call: backtrack(nums, 0, new ArrayList<>());
```

### Approach 2 — Subsets II (with Duplicates)
```java
void backtrack(int[] nums, int start, List<Integer> current) {
    result.add(new ArrayList<>(current));

    for (int i = start; i < nums.length; i++) {
        if (i > start && nums[i] == nums[i-1]) continue;  // ← skip duplicates
        current.add(nums[i]);
        backtrack(nums, i + 1, current);
        current.remove(current.size() - 1);
    }
}
// Must sort first: Arrays.sort(nums);
```

### Approach 3 — Combinations with Target (Reuse Allowed)
```java
void backtrack(int[] candidates, int start, int remaining, List<Integer> current) {
    if (remaining == 0) { result.add(new ArrayList<>(current)); return; }
    if (remaining < 0) return;   // prune: over budget

    for (int i = start; i < candidates.length; i++) {
        current.add(candidates[i]);
        backtrack(candidates, i, remaining - candidates[i], current);  // i = reuse OK
        current.remove(current.size() - 1);
    }
}
```

### Approach 4 — Permutations O(n!)
```java
void backtrack(int[] nums, boolean[] used, List<Integer> current) {
    if (current.size() == nums.length) {
        result.add(new ArrayList<>(current)); return;
    }
    for (int i = 0; i < nums.length; i++) {
        if (used[i]) continue;
        used[i] = true;
        current.add(nums[i]);
        backtrack(nums, used, current);
        current.remove(current.size() - 1);
        used[i] = false;
    }
}
// Call: Arrays.sort(nums); backtrack(nums, new boolean[n], new ArrayList<>());
```

### Approach 5 — Permutations II (with Duplicates)
```java
void backtrack(int[] nums, boolean[] used, List<Integer> current) {
    if (current.size() == nums.length) { result.add(new ArrayList<>(current)); return; }
    for (int i = 0; i < nums.length; i++) {
        if (used[i]) continue;
        // skip duplicate: only use duplicate if previous same-value was used
        if (i > 0 && nums[i] == nums[i-1] && !used[i-1]) continue;
        used[i] = true;
        current.add(nums[i]);
        backtrack(nums, used, current);
        current.remove(current.size() - 1);
        used[i] = false;
    }
}
```

### Approach 6 — Grid / Word Search O(n·m·4^L)
```java
boolean dfs(char[][] board, String word, int r, int c, int idx) {
    if (idx == word.length()) return true;
    if (r < 0 || r >= board.length || c < 0 || c >= board[0].length) return false;
    if (board[r][c] != word.charAt(idx)) return false;

    char temp = board[r][c];
    board[r][c] = '#';                       // mark visited
    boolean found = dfs(board, word, r+1, c, idx+1) ||
                    dfs(board, word, r-1, c, idx+1) ||
                    dfs(board, word, r, c+1, idx+1) ||
                    dfs(board, word, r, c-1, idx+1);
    board[r][c] = temp;                      // restore (backtrack)
    return found;
}
```

### Approach 7 — N-Queens / Constraint Backtracking O(n!)
```java
void backtrack(int row, boolean[] cols, boolean[] diag1, boolean[] diag2, int[][] queens) {
    if (row == n) { result.add(buildBoard(queens)); return; }

    for (int col = 0; col < n; col++) {
        if (cols[col] || diag1[row-col+n] || diag2[row+col]) continue;  // prune

        cols[col] = diag1[row-col+n] = diag2[row+col] = true;
        queens[row][col] = 1;
        backtrack(row + 1, cols, diag1, diag2, queens);
        cols[col] = diag1[row-col+n] = diag2[row+col] = false;
        queens[row][col] = 0;
    }
}
```

### Backtracking Variant Quick Table

| Problem Type | `start` | Reuse? | visited[]? | Sort first? |
|---|---|---|---|---|
| Subsets | `i + 1` | No | No | No |
| Subsets II (dups) | `i + 1` | No | No | **Yes** |
| Combinations | `i + 1` | No | No | No |
| Combination Sum (reuse) | `i` | **Yes** | No | No |
| Combination Sum II | `i + 1` | No | No | **Yes** |
| Permutations | `0` | No | **Yes** | No |
| Permutations II (dups) | `0` | No | **Yes** | **Yes** |

**LeetCode Problems:**
- Subsets — LC 78 / 90
- Permutations — LC 46 / 47
- Combination Sum — LC 39 / 40
- Word Search — LC 79
- N-Queens — LC 51
- Palindrome Partitioning — LC 131
- Letter Combinations of a Phone Number — LC 17
- Generate Parentheses — LC 22
- Sudoku Solver — LC 37

---

## 2. Heap / Priority Queue

**Recognize:** "top K", "Kth largest/smallest", "merge K sorted", "median of stream", greedy minimum

**Core idea:**
- **Top K Largest** → **min-heap** of size K (evict smallest, keep K biggest)
- **Top K Smallest** → **max-heap** of size K (evict largest, keep K smallest)
- When `heap.size() > K` → call `poll()`

### Approach 1 — Sort O(n log n)
```java
Arrays.sort(nums);
return nums[nums.length - k];  // Kth largest
```

### Approach 2 — Min-Heap Top K Largest O(n log k) ✅
```java
PriorityQueue<Integer> minHeap = new PriorityQueue<>();
for (int num : nums) {
    minHeap.offer(num);
    if (minHeap.size() > k) minHeap.poll();   // evict smallest
}
return minHeap.peek();   // Kth largest
```

### Approach 3 — Max-Heap Top K Smallest O(n log k)
```java
PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
for (int num : nums) {
    maxHeap.offer(num);
    if (maxHeap.size() > k) maxHeap.poll();   // evict largest
}
return maxHeap.peek();   // Kth smallest
```

### Approach 4 — Quickselect O(n) average
```java
int kthLargest(int[] nums, int k) {
    return quickselect(nums, 0, nums.length - 1, nums.length - k);
}
int quickselect(int[] nums, int lo, int hi, int targetIdx) {
    int pivot = nums[hi], p = lo;
    for (int i = lo; i < hi; i++)
        if (nums[i] <= pivot) { int t = nums[i]; nums[i] = nums[p]; nums[p++] = t; }
    int t = nums[p]; nums[p] = nums[hi]; nums[hi] = t;  // place pivot
    if (p == targetIdx) return nums[p];
    return p < targetIdx
           ? quickselect(nums, p + 1, hi,  targetIdx)
           : quickselect(nums, lo,    p-1, targetIdx);
}
```

### Approach 5 — Merge K Sorted Lists O(n log k)
```java
PriorityQueue<ListNode> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a.val));
for (ListNode node : lists) if (node != null) pq.offer(node);

ListNode dummy = new ListNode(0), curr = dummy;
while (!pq.isEmpty()) {
    ListNode node = pq.poll();
    curr.next = node;
    curr = curr.next;
    if (node.next != null) pq.offer(node.next);
}
return dummy.next;
```

### Approach 6 — Median from Data Stream O(log n) per insertion
Use **two heaps**: max-heap for lower half, min-heap for upper half.
```java
class MedianFinder {
    PriorityQueue<Integer> lower = new PriorityQueue<>(Collections.reverseOrder()); // max-heap
    PriorityQueue<Integer> upper = new PriorityQueue<>();                            // min-heap

    void addNum(int num) {
        lower.offer(num);
        upper.offer(lower.poll());         // balance: send max of lower to upper
        if (lower.size() < upper.size())   // keep lower.size >= upper.size
            lower.offer(upper.poll());
    }

    double findMedian() {
        return lower.size() > upper.size()
               ? lower.peek()
               : (lower.peek() + upper.peek()) / 2.0;
    }
}
```

### Approach 7 — Task Scheduler (Greedy + Heap) O(n log n)
```java
int[] freq = new int[26];
for (char c : tasks) freq[c - 'A']++;
PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
for (int f : freq) if (f > 0) maxHeap.offer(f);

int time = 0;
while (!maxHeap.isEmpty()) {
    List<Integer> temp = new ArrayList<>();
    for (int i = 0; i < n + 1; i++) {           // one cycle of size n+1
        if (!maxHeap.isEmpty()) temp.add(maxHeap.poll() - 1);
    }
    for (int f : temp) if (f > 0) maxHeap.offer(f);
    time += maxHeap.isEmpty() ? temp.size() : n + 1;
}
return time;
```

**LeetCode Problems:**
- Kth Largest Element in an Array — LC 215
- Top K Frequent Elements — LC 347
- K Closest Points to Origin — LC 973
- Merge K Sorted Lists — LC 23
- Find Median from Data Stream — LC 295
- Task Scheduler — LC 621
- Design Twitter — LC 355
- Smallest Range Covering Elements from K Lists — LC 632

---

## 3. Trie

**Recognize:** "prefix", "starts with", "autocomplete", "word dictionary", "replace words"

**Core idea:** Each character = one node. Path from root to leaf = a word. `isEnd` marks complete words.

### Approach 1 — Array-Based Trie (26 lowercase letters) O(L) per operation
```java
class Trie {
    Trie[] children = new Trie[26];
    boolean isEnd = false;

    // Insert a word
    void insert(String word) {
        Trie node = this;
        for (char c : word.toCharArray()) {
            int idx = c - 'a';
            if (node.children[idx] == null)
                node.children[idx] = new Trie();
            node = node.children[idx];
        }
        node.isEnd = true;
    }

    // Search for exact word
    boolean search(String word) {
        Trie node = this;
        for (char c : word.toCharArray()) {
            int idx = c - 'a';
            if (node.children[idx] == null) return false;
            node = node.children[idx];
        }
        return node.isEnd;
    }

    // Check if any word starts with prefix
    boolean startsWith(String prefix) {
        Trie node = this;
        for (char c : prefix.toCharArray()) {
            int idx = c - 'a';
            if (node.children[idx] == null) return false;
            node = node.children[idx];
        }
        return true;  // no need to check isEnd
    }
}
```

### Approach 2 — HashMap-Based Trie (Any Characters)
```java
class TrieNode {
    Map<Character, TrieNode> children = new HashMap<>();
    boolean isEnd = false;
}

class Trie {
    TrieNode root = new TrieNode();

    void insert(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);
        }
        node.isEnd = true;
    }

    boolean search(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            if (!node.children.containsKey(c)) return false;
            node = node.children.get(c);
        }
        return node.isEnd;
    }
}
```

### Approach 3 — Trie with Wildcard Search (. matches any char)
```java
boolean searchWithWildcard(String word, TrieNode node) {
    for (int i = 0; i < word.length(); i++) {
        char c = word.charAt(i);
        if (c == '.') {
            // try all children
            for (TrieNode child : node.children.values())
                if (searchWithWildcard(word.substring(i + 1), child)) return true;
            return false;
        } else {
            if (!node.children.containsKey(c)) return false;
            node = node.children.get(c);
        }
    }
    return node.isEnd;
}
```

**LeetCode Problems:**
- Implement Trie (Prefix Tree) — LC 208
- Add and Search Word — LC 211
- Word Search II — LC 212
- Replace Words — LC 648
- Design Search Autocomplete System — LC 642
- Longest Word in Dictionary — LC 720

---

## 4. Binary Indexed Tree (BIT / Fenwick)

**Recognize:** "prefix sum queries + point updates", "count of elements ≤ x", frequent range sum with updates

**Core idea:** Each node covers a range determined by its lowest set bit. Update propagates up, query propagates down.

```
Index:    1   2   3   4   5   6   7   8
Covers:  [1] [1-2] [3] [1-4] [5] [5-6] [7] [1-8]
```

### Template O(log n) per query/update
```java
class BIT {
    int[] tree;
    int n;

    BIT(int n) {
        this.n = n;
        tree = new int[n + 1];  // 1-indexed
    }

    // Point update: add delta to index i
    void update(int i, int delta) {
        for (; i <= n; i += i & (-i))   // i & (-i) = lowest set bit
            tree[i] += delta;
    }

    // Prefix query: sum of [1..i]
    int query(int i) {
        int sum = 0;
        for (; i > 0; i -= i & (-i))
            sum += tree[i];
        return sum;
    }

    // Range query: sum of [l..r]
    int rangeQuery(int l, int r) {
        return query(r) - query(l - 1);
    }
}
```

### Build from Array O(n log n)
```java
BIT bit = new BIT(n);
for (int i = 0; i < n; i++)
    bit.update(i + 1, nums[i]);  // 1-indexed
```

### Build from Array O(n) — Faster Build
```java
for (int i = 1; i <= n; i++) {
    tree[i] += nums[i - 1];
    int parent = i + (i & (-i));
    if (parent <= n) tree[parent] += tree[i];
}
```

### Common BIT Use Cases
```java
// Count of elements less than x (coordinate compression needed)
// Count of inversions in array
// Range sum with point updates
// Frequency table with prefix sums
```

**LeetCode Problems:**
- Range Sum Query - Mutable — LC 307
- Count of Smaller Numbers After Self — LC 315
- Reverse Pairs — LC 493
- Count of Range Sum — LC 327

---

## 5. Segment Tree

**Recognize:** range queries (sum/min/max) + range/point updates, more powerful than BIT

**Core idea:** Binary tree where each node stores aggregate for a range. Leaves = individual elements.

### Point Update + Range Sum Query O(log n) ✅
```java
class SegTree {
    int[] tree;
    int n;

    SegTree(int[] nums) {
        n = nums.length;
        tree = new int[4 * n];
        build(nums, 0, 0, n - 1);
    }

    void build(int[] nums, int node, int start, int end) {
        if (start == end) {
            tree[node] = nums[start];
            return;
        }
        int mid = (start + end) / 2;
        build(nums, 2*node+1, start, mid);
        build(nums, 2*node+2, mid+1, end);
        tree[node] = tree[2*node+1] + tree[2*node+2];  // ← merge function
    }

    void update(int node, int start, int end, int idx, int val) {
        if (start == end) { tree[node] = val; return; }
        int mid = (start + end) / 2;
        if (idx <= mid) update(2*node+1, start, mid, idx, val);
        else            update(2*node+2, mid+1, end, idx, val);
        tree[node] = tree[2*node+1] + tree[2*node+2];  // ← recompute
    }

    int query(int node, int start, int end, int l, int r) {
        if (r < start || end < l) return 0;             // out of range: identity (0 for sum)
        if (l <= start && end <= r) return tree[node];  // fully in range
        int mid = (start + end) / 2;
        return query(2*node+1, start, mid, l, r)
             + query(2*node+2, mid+1, end, l, r);       // ← merge function
    }

    // Public API wrappers:
    void update(int idx, int val) { update(0, 0, n-1, idx, val); }
    int  query(int l, int r)      { return query(0, 0, n-1, l, r); }
}
```

### Range Min Query (change merge function)
```java
// In build and update:
tree[node] = Math.min(tree[2*node+1], tree[2*node+2]);
// In query:
if (r < start || end < l) return Integer.MAX_VALUE;  // identity for min
return Math.min(query(2*node+1,...), query(2*node+2,...));
```

### Lazy Propagation (Range Update) O(log n)
Use when you need to update an entire range, not just a point.
```java
class LazySegTree {
    int[] tree, lazy;

    void pushDown(int node) {
        if (lazy[node] != 0) {
            tree[2*node+1] += lazy[node];
            tree[2*node+2] += lazy[node];
            lazy[2*node+1] += lazy[node];
            lazy[2*node+2] += lazy[node];
            lazy[node] = 0;
        }
    }

    void updateRange(int node, int start, int end, int l, int r, int val) {
        if (r < start || end < l) return;
        if (l <= start && end <= r) {
            tree[node] += val;
            lazy[node] += val;
            return;
        }
        pushDown(node);
        int mid = (start + end) / 2;
        updateRange(2*node+1, start, mid, l, r, val);
        updateRange(2*node+2, mid+1, end, l, r, val);
        tree[node] = tree[2*node+1] + tree[2*node+2];
    }
}
```

### BIT vs Segment Tree

| Feature | BIT (Fenwick) | Segment Tree |
|---|---|---|
| Code complexity | Simple | More complex |
| Space | O(n) | O(4n) |
| Point update | O(log n) | O(log n) |
| Range query (sum) | O(log n) | O(log n) |
| Range update | ❌ (needs 2 BITs) | ✅ (with lazy) |
| Range min/max | ❌ | ✅ |
| 2D version | ✅ (2D BIT) | ✅ (harder) |

**Use BIT when:** only need prefix sums + point updates (simpler code).  
**Use Segment Tree when:** need range min/max or range updates.

**LeetCode Problems:**
- Range Sum Query - Mutable — LC 307
- The Skyline Problem — LC 218
- Count of Smaller Numbers After Self — LC 315
- My Calendar III — LC 732

---

*← Back to [DP Patterns](05_DP_Patterns.md) | Back to [Index](00_Index.md)*
