# DSA Patterns Cheatsheet — Universal Templates + Recognition

> **How to use this file:**
> 1. Read the problem → match keywords to the Recognition table
> 2. Copy the template for that pattern
> 3. Swap only the constraint / recurrence line for your problem

---

## Master Recognition Table

| Keywords in Problem | Pattern |
|---|---|
| subarray / substring of **size K** | Sliding Window (Fixed) |
| **longest / shortest** subarray with condition | Sliding Window (Dynamic) |
| **at most K distinct** / no repeat chars | Sliding Window (Dynamic) |
| sorted + **find pair** / two sum | Two Pointers (Opposite) |
| linked list **cycle** / middle / Nth from end | Two Pointers (Fast & Slow) |
| **sum of subarray [i,j]** queried multiple times | Prefix Sum |
| **count subarrays** with sum = K | Prefix Sum + HashMap |
| find **duplicate / group / frequency** | HashMap |
| sorted + search / **O(log n)** hinted | Binary Search |
| find **min/max satisfying** a condition | Binary Search on Answer |
| **all combinations / permutations / subsets** | Backtracking |
| **max / min / count ways**, overlapping subproblems | Dynamic Programming 1D |
| **two sequences** compared / grid paths | Dynamic Programming 2D |
| **shortest path / min steps** / levels / nearest | BFS |
| **connected components** / flood fill / path exists | DFS |
| **next greater / next smaller** / spans / histogram | Monotonic Stack |
| **top K** / Kth largest / merge K sorted | Heap / Priority Queue |
| **dynamic connectivity** / cycle / union components | Union Find |

---

## Time Complexity Quick Reference

| Complexity | Algorithms that produce it |
|---|---|
| O(1) | HashMap get/put, Heap peek, Union Find (amortized) |
| O(log n) | Binary Search, Heap poll/offer |
| O(n) | Sliding Window, Two Pointers, Prefix Sum, HashMap scan |
| O(n log n) | Sorting, building Heap of size n |
| O(n · k) | Sliding Window with inner work, Top-K heap |
| O(n²) | Nested loops, naive DP |
| O(n · m) | 2D DP, BFS/DFS on grid |
| O(2ⁿ) | Backtracking — subsets |
| O(n!) | Backtracking — permutations |

---

## Data Structure → Use When

| Structure | Use When |
|---|---|
| `HashMap` | frequency count, index lookup, complement search |
| `HashSet` | membership check, seen/visited in O(1) |
| `int[]` array | frequency when values are bounded (0–100 000) |
| `Deque` as stack | monotonic stack, iterative DFS |
| `Deque` as queue | BFS |
| `PriorityQueue` | top-K, greedy min/max, Dijkstra |
| `int[][]` | 2D DP, grid BFS/DFS |
| `int[] parent` | Union Find |

---

## Pattern 1 — Sliding Window (Fixed Size)

**Recognize:** "subarray of size K", "max sum of K consecutive elements"

**Trick:** Build first window manually, then slide — add right, remove left (right − K).

```java
int windowSum = 0, maxSum = 0;

for (int i = 0; i < k; i++)          // build first window
    windowSum += nums[i];
maxSum = windowSum;

for (int right = k; right < nums.length; right++) {
    windowSum += nums[right];         // expand right
    windowSum -= nums[right - k];     // shrink left
    maxSum = Math.max(maxSum, windowSum);
}
return maxSum;
```

**Problems:**
- [Maximum Average Subarray I](https://leetcode.com/problems/maximum-average-subarray-i/) — LC 643
- [Contains Duplicate II](https://leetcode.com/problems/contains-duplicate-ii/) — LC 219
- [Maximum Sum of Distinct Subarrays with Length K](https://leetcode.com/problems/maximum-sum-of-distinct-subarrays-with-length-k/) — LC 2461
- [Find All Anagrams in a String](https://leetcode.com/problems/find-all-anagrams-in-a-string/) — LC 438
- [Sliding Window Maximum](https://leetcode.com/problems/sliding-window-maximum/) — LC 239

---

## Pattern 2 — Sliding Window (Dynamic Size)

**Recognize:** "longest/shortest subarray with condition", "at most K distinct", "no repeating chars"

**Trick:** Expand right always. Shrink left **only when window is INVALID**. The `while` condition = your constraint violation.

```java
Map<Integer, Integer> window = new HashMap<>();
int left = 0, result = 0;

for (int right = 0; right < nums.length; right++) {

    // 1. EXPAND
    window.merge(nums[right], 1, Integer::sum);

    // 2. SHRINK  ← only this condition changes per problem
    while (window.size() > k) {
        window.merge(nums[left], -1, Integer::sum);
        if (window.get(nums[left]) == 0)
            window.remove(nums[left]);
        left++;
    }

    // 3. RECORD
    result = Math.max(result, right - left + 1);
}
return result;
```

### Constraint Swap Table

| Problem Condition | `while` Condition to Use |
|---|---|
| At most K distinct elements | `window.size() > k` |
| No duplicate characters | `window.get(c) > 1` |
| Sum exceeds target | `sum > target` |
| At most K zeros (flip) | `zeroCount > k` |
| At most K replacements | `windowLen - maxFreq > k` |

**Problems:**
- [Fruit Into Baskets](https://leetcode.com/problems/fruit-into-baskets/) — LC 904 ← (your problem)
- [Longest Substring Without Repeating Characters](https://leetcode.com/problems/longest-substring-without-repeating-characters/) — LC 3
- [Longest Substring with At Most K Distinct Characters](https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/) — LC 340
- [Minimum Window Substring](https://leetcode.com/problems/minimum-window-substring/) — LC 76
- [Longest Repeating Character Replacement](https://leetcode.com/problems/longest-repeating-character-replacement/) — LC 424
- [Max Consecutive Ones III](https://leetcode.com/problems/max-consecutive-ones-iii/) — LC 1004
- [Permutation in String](https://leetcode.com/problems/permutation-in-string/) — LC 567

---

## Pattern 3 — Two Pointers (Opposite Ends)

**Recognize:** sorted array + "find pair/triplet with sum", "palindrome", "container with most water"

**Trick:** Move `left →` when you need bigger. Move `← right` when you need smaller.

```java
int left = 0, right = nums.length - 1;

while (left < right) {
    int sum = nums[left] + nums[right];

    if      (sum == target) return new int[]{left, right};  // found
    else if (sum < target)  left++;    // need bigger  → move left right
    else                    right--;   // need smaller → move right left
}
```

**Problems:**
- [Two Sum II](https://leetcode.com/problems/two-sum-ii-input-array-is-sorted/) — LC 167
- [3Sum](https://leetcode.com/problems/3sum/) — LC 15
- [3Sum Closest](https://leetcode.com/problems/3sum-closest/) — LC 16
- [Container With Most Water](https://leetcode.com/problems/container-with-most-water/) — LC 11
- [Valid Palindrome](https://leetcode.com/problems/valid-palindrome/) — LC 125
- [Trapping Rain Water](https://leetcode.com/problems/trapping-rain-water/) — LC 42
- [Sort Colors](https://leetcode.com/problems/sort-colors/) — LC 75

---

## Pattern 4 — Two Pointers (Fast & Slow / Floyd's)

**Recognize:** linked list cycle, find middle, find duplicate, remove Nth from end

**Trick:** `slow` moves 1 step, `fast` moves 2 steps. They meet → cycle. `fast` at end → `slow` at middle.

```java
ListNode slow = head, fast = head;

while (fast != null && fast.next != null) {
    slow = slow.next;           // 1 step
    fast = fast.next.next;      // 2 steps
    if (slow == fast) return true;  // cycle detected
}
return false;
```

**Remove Nth from end variant:**

```java
ListNode fast = head, slow = head;
for (int i = 0; i < n; i++) fast = fast.next;  // gap of N
while (fast.next != null) { slow = slow.next; fast = fast.next; }
slow.next = slow.next.next;  // slow is just before the target
```

**Problems:**
- [Linked List Cycle](https://leetcode.com/problems/linked-list-cycle/) — LC 141
- [Linked List Cycle II](https://leetcode.com/problems/linked-list-cycle-ii/) — LC 142
- [Find the Duplicate Number](https://leetcode.com/problems/find-the-duplicate-number/) — LC 287
- [Middle of the Linked List](https://leetcode.com/problems/middle-of-the-linked-list/) — LC 876
- [Remove Nth Node From End of List](https://leetcode.com/problems/remove-nth-node-from-end-of-list/) — LC 19
- [Happy Number](https://leetcode.com/problems/happy-number/) — LC 202

---

## Pattern 5 — Prefix Sum

**Recognize:** "sum of subarray [i,j]" multiple times, "count subarrays with sum = K"

**Trick:** `prefix[i] = sum of nums[0..i-1]` → `sum(i,j) = prefix[j+1] - prefix[i]` in O(1).
For counting: store prefix sums in a map, check if `(currentSum − K)` was seen before.

```java
// Range query version
int[] prefix = new int[n + 1];
for (int i = 0; i < n; i++)
    prefix[i + 1] = prefix[i] + nums[i];
// sum of [i..j] = prefix[j+1] - prefix[i]

// Count subarrays with sum = K
Map<Integer, Integer> prefixCount = new HashMap<>();
prefixCount.put(0, 1);
int sum = 0, count = 0;

for (int num : nums) {
    sum += num;
    count += prefixCount.getOrDefault(sum - k, 0);  // complement seen before?
    prefixCount.merge(sum, 1, Integer::sum);
}
return count;
```

**Problems:**
- [Subarray Sum Equals K](https://leetcode.com/problems/subarray-sum-equals-k/) — LC 560
- [Range Sum Query - Immutable](https://leetcode.com/problems/range-sum-query-immutable/) — LC 303
- [Product of Array Except Self](https://leetcode.com/problems/product-of-array-except-self/) — LC 238
- [Continuous Subarray Sum](https://leetcode.com/problems/continuous-subarray-sum/) — LC 523
- [Subarray Sums Divisible by K](https://leetcode.com/problems/subarray-sums-divisible-by-k/) — LC 974
- [Count Number of Nice Subarrays](https://leetcode.com/problems/count-number-of-nice-subarrays/) — LC 1248

---

## Pattern 6 — HashMap (Frequency / Lookup)

**Recognize:** duplicates, two sum (unsorted), anagram, majority element, first unique

**Trick:** One pass — store `value → index` (for two sum) OR `value → count` (for frequency).

```java
// Two Sum (unsorted)
Map<Integer, Integer> seen = new HashMap<>();
for (int i = 0; i < nums.length; i++) {
    int complement = target - nums[i];
    if (seen.containsKey(complement))
        return new int[]{seen.get(complement), i};
    seen.put(nums[i], i);
}

// Frequency count
Map<Character, Integer> freq = new HashMap<>();
for (char c : s.toCharArray())
    freq.merge(c, 1, Integer::sum);
```

**Problems:**
- [Two Sum](https://leetcode.com/problems/two-sum/) — LC 1
- [Group Anagrams](https://leetcode.com/problems/group-anagrams/) — LC 49
- [Valid Anagram](https://leetcode.com/problems/valid-anagram/) — LC 242
- [Top K Frequent Elements](https://leetcode.com/problems/top-k-frequent-elements/) — LC 347
- [First Unique Character in a String](https://leetcode.com/problems/first-unique-character-in-a-string/) — LC 387
- [Majority Element](https://leetcode.com/problems/majority-element/) — LC 169
- [Longest Consecutive Sequence](https://leetcode.com/problems/longest-consecutive-sequence/) — LC 128

---

## Pattern 7 — Binary Search

**Recognize:** sorted array + search, O(log n) hinted, "find min/max satisfying condition"

**Trick:** `mid = left + (right - left) / 2` — never `(left + right) / 2` (overflow).

```java
// Standard — find exact value
int left = 0, right = nums.length - 1;
while (left <= right) {
    int mid = left + (right - left) / 2;
    if      (nums[mid] == target) return mid;
    else if (nums[mid] < target)  left  = mid + 1;
    else                          right = mid - 1;
}
return -1;

// Left boundary — first index where condition is TRUE
int left = 0, right = nums.length;
while (left < right) {
    int mid = left + (right - left) / 2;
    if (condition(mid)) right = mid;   // might be answer, search left
    else                left  = mid + 1;
}
return left;

// Binary search on ANSWER (search on result range, not the array)
int left = minPossibleAnswer, right = maxPossibleAnswer;
while (left < right) {
    int mid = left + (right - left) / 2;
    if (canAchieve(mid)) right = mid;
    else                 left  = mid + 1;
}
return left;
```

**Problems:**
- [Binary Search](https://leetcode.com/problems/binary-search/) — LC 704
- [Search in Rotated Sorted Array](https://leetcode.com/problems/search-in-rotated-sorted-array/) — LC 33
- [Find Minimum in Rotated Sorted Array](https://leetcode.com/problems/find-minimum-in-rotated-sorted-array/) — LC 153
- [Find Peak Element](https://leetcode.com/problems/find-peak-element/) — LC 162
- [Koko Eating Bananas](https://leetcode.com/problems/koko-eating-bananas/) — LC 875
- [Capacity To Ship Packages Within D Days](https://leetcode.com/problems/capacity-to-ship-packages-within-d-days/) — LC 1011
- [Median of Two Sorted Arrays](https://leetcode.com/problems/median-of-two-sorted-arrays/) — LC 4

---

## Pattern 8 — Backtracking

**Recognize:** "all combinations / permutations / subsets", "generate all valid ___", "find all paths"

**Trick:** **Choose → Explore → Un-choose**. Prune early when constraint already violated.

```java
void backtrack(int start, List<Integer> current, List<List<Integer>> result) {

    // BASE CASE — valid solution found
    result.add(new ArrayList<>(current));

    for (int i = start; i < nums.length; i++) {

        // PRUNE — skip invalid branches early
        if (nums[i] > remaining) break;

        current.add(nums[i]);                       // CHOOSE
        backtrack(i + 1, current, result);          // EXPLORE
        current.remove(current.size() - 1);         // UN-CHOOSE
    }
}
```

### Backtracking Variants

| Problem Type | `start` index | Reuse element? |
|---|---|---|
| Subsets | `i + 1` | No |
| Combinations | `i + 1` | No |
| Combination Sum (reuse) | `i` | Yes |
| Permutations | `0` + `visited[]` | No |

**Problems:**
- [Subsets](https://leetcode.com/problems/subsets/) — LC 78
- [Subsets II](https://leetcode.com/problems/subsets-ii/) — LC 90 (with duplicates)
- [Permutations](https://leetcode.com/problems/permutations/) — LC 46
- [Combination Sum](https://leetcode.com/problems/combination-sum/) — LC 39
- [Combination Sum II](https://leetcode.com/problems/combination-sum-ii/) — LC 40
- [Word Search](https://leetcode.com/problems/word-search/) — LC 79
- [N-Queens](https://leetcode.com/problems/n-queens/) — LC 51
- [Palindrome Partitioning](https://leetcode.com/problems/palindrome-partitioning/) — LC 131
- [Letter Combinations of a Phone Number](https://leetcode.com/problems/letter-combinations-of-a-phone-number/) — LC 17

---

## Pattern 9 — Dynamic Programming (1D)

**Recognize:** max/min/count, "can we reach", overlapping subproblems, optimal substructure

**Trick:** Define what `dp[i]` means → write recurrence → base cases → fill left to right.

```java
// Generic 1D DP
int[] dp = new int[n + 1];
dp[0] = base_case;

for (int i = 1; i <= n; i++) {
    dp[i] = Math.max(dp[i-1], dp[i-2] + nums[i]);  // ← recurrence changes per problem
}
return dp[n];

// Space-optimized (when only prev 1-2 values needed)
int prev2 = ..., prev1 = ...;
for (int i = 2; i <= n; i++) {
    int curr = Math.max(prev1, prev2 + nums[i]);
    prev2 = prev1;
    prev1 = curr;
}
return prev1;
```

### 1D DP Recurrence Table

| Problem | `dp[i]` means | Recurrence |
|---|---|---|
| Climbing Stairs | ways to reach step i | `dp[i] = dp[i-1] + dp[i-2]` |
| House Robber | max money up to house i | `dp[i] = max(dp[i-1], dp[i-2] + nums[i])` |
| Coin Change | min coins for amount i | `dp[i] = min(dp[i - coin] + 1)` for each coin |
| Jump Game | can reach index i | `dp[i] = any dp[j] where j + nums[j] >= i` |
| Longest Increasing Subsequence | LIS ending at i | `dp[i] = max(dp[j] + 1) where nums[j] < nums[i]` |

**Problems:**
- [Climbing Stairs](https://leetcode.com/problems/climbing-stairs/) — LC 70
- [House Robber](https://leetcode.com/problems/house-robber/) — LC 198
- [House Robber II](https://leetcode.com/problems/house-robber-ii/) — LC 213
- [Coin Change](https://leetcode.com/problems/coin-change/) — LC 322
- [Longest Increasing Subsequence](https://leetcode.com/problems/longest-increasing-subsequence/) — LC 300
- [Jump Game](https://leetcode.com/problems/jump-game/) — LC 55
- [Jump Game II](https://leetcode.com/problems/jump-game-ii/) — LC 45
- [Word Break](https://leetcode.com/problems/word-break/) — LC 139
- [Decode Ways](https://leetcode.com/problems/decode-ways/) — LC 91
- [Partition Equal Subset Sum](https://leetcode.com/problems/partition-equal-subset-sum/) — LC 416

---

## Pattern 10 — Dynamic Programming (2D)

**Recognize:** two sequences compared, grid path problems, `dp[i][j]` depends on `dp[i-1][j]` / `dp[i][j-1]`

**Trick:** Rows = sequence A (or grid row), Cols = sequence B (or grid col). Draw the grid on paper first.

```java
int[][] dp = new int[m + 1][n + 1];

for (int i = 1; i <= m; i++) {
    for (int j = 1; j <= n; j++) {

        if (a.charAt(i-1) == b.charAt(j-1))
            dp[i][j] = dp[i-1][j-1] + 1;              // match → extend diagonal
        else
            dp[i][j] = Math.max(dp[i-1][j], dp[i][j-1]); // skip one → take best
    }
}
return dp[m][n];
```

### 2D DP Patterns

| Problem | `dp[i][j]` means | Key recurrence |
|---|---|---|
| LCS | LCS of a[0..i), b[0..j) | match→ diag+1, else max(up, left) |
| Edit Distance | edits to convert a[0..i) to b[0..j) | match→ diag, else min(up,left,diag)+1 |
| Unique Paths | ways to reach cell (i,j) | `dp[i][j] = dp[i-1][j] + dp[i][j-1]` |
| Min Path Sum | min cost to reach (i,j) | `dp[i][j] = grid[i][j] + min(up, left)` |

**Problems:**
- [Longest Common Subsequence](https://leetcode.com/problems/longest-common-subsequence/) — LC 1143
- [Edit Distance](https://leetcode.com/problems/edit-distance/) — LC 72
- [Unique Paths](https://leetcode.com/problems/unique-paths/) — LC 62
- [Minimum Path Sum](https://leetcode.com/problems/minimum-path-sum/) — LC 64
- [Longest Palindromic Subsequence](https://leetcode.com/problems/longest-palindromic-subsequence/) — LC 516
- [Interleaving String](https://leetcode.com/problems/interleaving-string/) — LC 97
- [Coin Change II](https://leetcode.com/problems/coin-change-ii/) — LC 518

---

## Pattern 11 — BFS

**Recognize:** shortest path, minimum steps/moves, level-order, "nearest ___"

**Trick:** Mark visited **before** adding to queue. Process nodes **level by level** using `size = queue.size()`.

```java
Queue<int[]> queue = new LinkedList<>();
boolean[][] visited = new boolean[rows][cols];
int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};

queue.offer(new int[]{startR, startC});
visited[startR][startC] = true;   // mark BEFORE adding
int steps = 0;

while (!queue.isEmpty()) {
    int size = queue.size();        // snapshot level size

    for (int i = 0; i < size; i++) {
        int[] curr = queue.poll();
        // ← process curr here (check if it's the target)

        for (int[] d : dirs) {
            int nr = curr[0] + d[0], nc = curr[1] + d[1];
            if (inBounds(nr, nc) && !visited[nr][nc] && isValid(grid, nr, nc)) {
                visited[nr][nc] = true;
                queue.offer(new int[]{nr, nc});
            }
        }
    }
    steps++;
}
return steps;
```

**Problems:**
- [Binary Tree Level Order Traversal](https://leetcode.com/problems/binary-tree-level-order-traversal/) — LC 102
- [Word Ladder](https://leetcode.com/problems/word-ladder/) — LC 127
- [01 Matrix](https://leetcode.com/problems/01-matrix/) — LC 542
- [Rotting Oranges](https://leetcode.com/problems/rotting-oranges/) — LC 994
- [Shortest Path in Binary Matrix](https://leetcode.com/problems/shortest-path-in-binary-matrix/) — LC 1091
- [Walls and Gates](https://leetcode.com/problems/walls-and-gates/) — LC 286
- [Snakes and Ladders](https://leetcode.com/problems/snakes-and-ladders/) — LC 909
- [Jump Game III](https://leetcode.com/problems/jump-game-iii/) — LC 1306

---

## Pattern 12 — DFS

**Recognize:** connected components, flood fill, path exists, all paths, tree height/diameter

**Trick:** Mark visited before recursing. Return value carries info **up** the call stack.

```java
// Graph DFS (iterative or recursive)
void dfs(int node, boolean[] visited, List<List<Integer>> graph) {
    visited[node] = true;           // mark BEFORE recursing
    for (int neighbor : graph.get(node))
        if (!visited[neighbor])
            dfs(neighbor, visited, graph);
}

// Grid DFS (flood fill style)
void dfs(char[][] grid, int r, int c) {
    if (r < 0 || r >= grid.length ||
        c < 0 || c >= grid[0].length || grid[r][c] != '1') return;
    grid[r][c] = '0';              // sink / mark visited
    dfs(grid, r+1, c); dfs(grid, r-1, c);
    dfs(grid, r, c+1); dfs(grid, r, c-1);
}

// Tree DFS — return value carries result upward
int height(TreeNode node) {
    if (node == null) return 0;
    int left  = height(node.left);
    int right = height(node.right);
    return 1 + Math.max(left, right);  // info bubbles UP
}
```

**Problems:**
- [Number of Islands](https://leetcode.com/problems/number-of-islands/) — LC 200
- [Max Area of Island](https://leetcode.com/problems/max-area-of-island/) — LC 695
- [Clone Graph](https://leetcode.com/problems/clone-graph/) — LC 133
- [Path Sum](https://leetcode.com/problems/path-sum/) — LC 112
- [Binary Tree Maximum Path Sum](https://leetcode.com/problems/binary-tree-maximum-path-sum/) — LC 124
- [Surrounded Regions](https://leetcode.com/problems/surrounded-regions/) — LC 130
- [Pacific Atlantic Water Flow](https://leetcode.com/problems/pacific-atlantic-water-flow/) — LC 417
- [Course Schedule](https://leetcode.com/problems/course-schedule/) — LC 207 (cycle detection)
- [Lowest Common Ancestor](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/) — LC 236

---

## Pattern 13 — Monotonic Stack

**Recognize:** "next greater/smaller element", "daily temperatures", "histogram", spans/ranges

**Trick:**
- **Decreasing stack** (pop when `nums[i] > top`) → finds **next greater element**
- **Increasing stack** (pop when `nums[i] < top`) → finds **next smaller element**
- When you **pop**, the current element is the answer for the popped index.

```java
Deque<Integer> stack = new ArrayDeque<>(); // stores INDICES
int[] result = new int[n];
Arrays.fill(result, -1);

for (int i = 0; i < n; i++) {

    // pop while current element is the "next greater" for top of stack
    while (!stack.isEmpty() && nums[stack.peek()] < nums[i])
        result[stack.pop()] = nums[i];   // nums[i] is the next greater

    stack.push(i);
}
```

**Problems:**
- [Next Greater Element I](https://leetcode.com/problems/next-greater-element-i/) — LC 496
- [Next Greater Element II](https://leetcode.com/problems/next-greater-element-ii/) — LC 503 (circular)
- [Daily Temperatures](https://leetcode.com/problems/daily-temperatures/) — LC 739
- [Largest Rectangle in Histogram](https://leetcode.com/problems/largest-rectangle-in-histogram/) — LC 84
- [Trapping Rain Water](https://leetcode.com/problems/trapping-rain-water/) — LC 42
- [Remove K Digits](https://leetcode.com/problems/remove-k-digits/) — LC 402
- [Sum of Subarray Minimums](https://leetcode.com/problems/sum-of-subarray-minimums/) — LC 907

---

## Pattern 14 — Heap / Priority Queue

**Recognize:** "top K", "Kth largest/smallest", "merge K sorted", "median of stream"

**Trick:**
- **Top K Largest** → min-heap of size K (evict the smallest, keep the K biggest)
- **Top K Smallest** → max-heap of size K (evict the biggest, keep the K smallest)
- When `heap.size() > K` → `poll()`

```java
// Top K Largest
PriorityQueue<Integer> minHeap = new PriorityQueue<>();
for (int num : nums) {
    minHeap.offer(num);
    if (minHeap.size() > k) minHeap.poll();  // remove smallest
}
return minHeap.peek();   // Kth largest

// Top K Smallest
PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
for (int num : nums) {
    maxHeap.offer(num);
    if (maxHeap.size() > k) maxHeap.poll();  // remove largest
}
return maxHeap.peek();   // Kth smallest
```

**Problems:**
- [Kth Largest Element in an Array](https://leetcode.com/problems/kth-largest-element-in-an-array/) — LC 215
- [Top K Frequent Elements](https://leetcode.com/problems/top-k-frequent-elements/) — LC 347
- [K Closest Points to Origin](https://leetcode.com/problems/k-closest-points-to-origin/) — LC 973
- [Merge K Sorted Lists](https://leetcode.com/problems/merge-k-sorted-lists/) — LC 23
- [Find Median from Data Stream](https://leetcode.com/problems/find-median-from-data-stream/) — LC 295
- [Task Scheduler](https://leetcode.com/problems/task-scheduler/) — LC 621
- [Design Twitter](https://leetcode.com/problems/design-twitter/) — LC 355

---

## Pattern 15 — Union Find (DSU)

**Recognize:** connected components, detect cycle (undirected), dynamic edge additions, "accounts merge"

**Trick:**
- `find()` with **path compression** → O(α(n)) ≈ O(1)
- `union()` with **rank** → always attach smaller tree under larger
- Start `components = n`, decrement on each successful union

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
        if (px == py) return false;         // already same component → cycle!

        if      (rank[px] < rank[py]) parent[px] = py;   // UNION BY RANK
        else if (rank[px] > rank[py]) parent[py] = px;
        else { parent[py] = px; rank[px]++; }

        components--;
        return true;
    }
}
```

**Problems:**
- [Number of Provinces](https://leetcode.com/problems/number-of-provinces/) — LC 547
- [Redundant Connection](https://leetcode.com/problems/redundant-connection/) — LC 684
- [Graph Valid Tree](https://leetcode.com/problems/graph-valid-tree/) — LC 261
- [Accounts Merge](https://leetcode.com/problems/accounts-merge/) — LC 721
- [Number of Connected Components](https://leetcode.com/problems/number-of-connected-components-in-an-undirected-graph/) — LC 323
- [Smallest String With Swaps](https://leetcode.com/problems/smallest-string-with-swaps/) — LC 1202
- [Making a Large Island](https://leetcode.com/problems/making-a-large-island/) — LC 827

---

## Problem → Pattern Decision Flowchart

```
Read the problem
        │
        ▼
Is it on a contiguous subarray / substring?
  ├── YES ──► Fixed size K?
  │              ├── YES ──► Sliding Window FIXED
  │              └── NO  ──► Sliding Window DYNAMIC
  │
  └── NO
        │
        ▼
Does it involve PAIRS or need TWO comparisons?
  ├── Sorted input + pair sum ──────────────────► Two Pointers (Opposite)
  ├── Linked list cycle / middle ───────────────► Two Pointers (Fast/Slow)
  └── NO
        │
        ▼
Does it involve SUM of ranges or counting subarrays?
  ├── YES ──► Prefix Sum (+ HashMap if counting)
  └── NO
        │
        ▼
Is the input SORTED and needs O(log n)?
  ├── Find exact value ─────────────────────────► Binary Search
  ├── Find min/max satisfying condition ────────► Binary Search on Answer
  └── NO
        │
        ▼
Does it need ALL solutions (enumerate)?
  ├── YES ──► Backtracking
  └── NO
        │
        ▼
Does it have OVERLAPPING SUBPROBLEMS?
  ├── One sequence / value range ─────────────► DP 1D
  ├── Two sequences / grid ──────────────────► DP 2D
  └── NO
        │
        ▼
Is it a GRAPH / TREE problem?
  ├── Shortest path / min steps ─────────────► BFS
  ├── Components / flood fill / all paths ──► DFS
  ├── Dynamic connectivity / cycle ──────────► Union Find
  └── NO
        │
        ▼
Does it involve NEXT GREATER/SMALLER or spans?
  ├── YES ──► Monotonic Stack
  └── NO
        │
        ▼
Top K / Kth largest / merge K sorted?
  └── YES ──► Heap / Priority Queue
```

---

## 3-Step Process For Any New Problem

```
STEP 1 — RECOGNIZE
  Read problem → extract keywords → match to Recognition Table above

STEP 2 — TEMPLATE
  Copy the skeleton for the matched pattern exactly as written above

STEP 3 — SWAP
  Change ONLY the constraint condition / recurrence / comparison
  Everything else stays identical
```

> The single line that changes between problems of the same pattern:
> - **Sliding Window** → the `while` condition
> - **Binary Search** → the `condition(mid)` check
> - **DP** → the recurrence `dp[i] = f(...)`
> - **Backtracking** → the prune condition + base case
> - **BFS/DFS** → the validity check for neighbors
