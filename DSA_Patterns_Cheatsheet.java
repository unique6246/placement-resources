package com.example.websocket;

import java.util.*;

/**
 * ═══════════════════════════════════════════════════════════════════════════
 *   COMPLETE DSA PATTERNS CHEATSHEET — Universal Templates + Recognition
 * ═══════════════════════════════════════════════════════════════════════════
 *
 *  PATTERNS COVERED:
 *   1.  Sliding Window (Fixed)
 *   2.  Sliding Window (Dynamic)
 *   3.  Two Pointers (Opposite ends)
 *   4.  Two Pointers (Fast & Slow)
 *   5.  Prefix Sum
 *   6.  HashMap (Frequency / Count)
 *   7.  Binary Search
 *   8.  Recursion + Backtracking
 *   9.  Dynamic Programming (1D)
 *   10. Dynamic Programming (2D)
 *   11. BFS (Graph / Tree)
 *   12. DFS (Graph / Tree)
 *   13. Monotonic Stack
 *   14. Heap / Priority Queue
 *   15. Union Find (Disjoint Set)
 *
 * ═══════════════════════════════════════════════════════════════════════════
 */
public class DSA_Patterns_Cheatsheet {

    // ═══════════════════════════════════════════════════════════════════════
    // 1. SLIDING WINDOW — FIXED SIZE
    // ═══════════════════════════════════════════════════════════════════════
    /**
     * RECOGNIZE when:
     *   • "subarray / substring of size K"
     *   • "maximum sum of K consecutive elements"
     *   • window size never changes
     *
     * TRICK:
     *   - Build the first window of size K manually.
     *   - Slide: add nums[right], remove nums[right - K].
     *   - Record result after every slide.
     *
     * PROBLEMS: Max sum subarray of size K, Average of subarrays of size K,
     *           Contains duplicate within K distance.
     */
    static int fixedWindow(int[] nums, int k) {
        int windowSum = 0, maxSum = 0;

        // build first window
        for (int i = 0; i < k; i++)
            windowSum += nums[i];

        maxSum = windowSum;

        // slide
        for (int right = k; right < nums.length; right++) {
            windowSum += nums[right];          // expand right
            windowSum -= nums[right - k];      // shrink left
            maxSum = Math.max(maxSum, windowSum);
        }
        return maxSum;
    }


    // ═══════════════════════════════════════════════════════════════════════
    // 2. SLIDING WINDOW — DYNAMIC SIZE  ← YOUR FRUIT PROBLEM IS THIS
    // ═══════════════════════════════════════════════════════════════════════
    /**
     * RECOGNIZE when:
     *   • "longest / shortest subarray with condition"
     *   • "at most K distinct elements"
     *   • "no repeating characters"
     *   • window size changes based on a constraint
     *
     * TRICK:
     *   - Expand right always.
     *   - Shrink left ONLY when window becomes INVALID.
     *   - The while condition = the VIOLATION of your constraint.
     *
     * CONSTRAINT SWAP TABLE (only this line changes per problem):
     *   At most K distinct          → while (map.size() > k)
     *   No duplicates               → while (map.get(x) > 1)
     *   Sum exceeds target          → while (sum > target)
     *   At most K zeros allowed     → while (zeroCount > k)
     *
     * PROBLEMS: Fruit into baskets, Longest substring without repeat,
     *           Longest substring with K distinct, Min window substring.
     */
    static int dynamicWindow(int[] nums, int k) {
        Map<Integer, Integer> window = new HashMap<>();
        int left = 0, result = 0;

        for (int right = 0; right < nums.length; right++) {

            // 1. EXPAND — add nums[right]
            window.merge(nums[right], 1, Integer::sum);

            // 2. SHRINK — fix violation  ← swap this condition per problem
            while (window.size() > k) {
                window.merge(nums[left], -1, Integer::sum);
                if (window.get(nums[left]) == 0)
                    window.remove(nums[left]);
                left++;
            }

            // 3. RECORD — window is valid here
            result = Math.max(result, right - left + 1);
        }
        return result;
    }


    // ═══════════════════════════════════════════════════════════════════════
    // 3. TWO POINTERS — OPPOSITE ENDS
    // ═══════════════════════════════════════════════════════════════════════
    /**
     * RECOGNIZE when:
     *   • sorted array + "find pair with sum = target"
     *   • "two sum", "three sum", "container with most water"
     *   • "palindrome check"
     *   • input is SORTED or can be sorted
     *
     * TRICK:
     *   - left starts at 0, right starts at end.
     *   - Move left → when you need a BIGGER value.
     *   - Move right← when you need a SMALLER value.
     *
     * PROBLEMS: Two Sum II, 3Sum, Container with most water,
     *           Valid palindrome, Trapping rain water.
     */
    static int[] twoSumSorted(int[] nums, int target) {
        int left = 0, right = nums.length - 1;

        while (left < right) {
            int sum = nums[left] + nums[right];

            if      (sum == target) return new int[]{left, right};
            else if (sum < target)  left++;   // need bigger → move left right
            else                    right--;  // need smaller → move right left
        }
        return new int[]{-1, -1};
    }


    // ═══════════════════════════════════════════════════════════════════════
    // 4. TWO POINTERS — FAST & SLOW (Floyd's)
    // ═══════════════════════════════════════════════════════════════════════
    /**
     * RECOGNIZE when:
     *   • linked list cycle detection
     *   • "find middle of linked list"
     *   • "find duplicate number" (array as implicit linked list)
     *   • "remove Nth node from end"
     *
     * TRICK:
     *   - slow moves 1 step, fast moves 2 steps.
     *   - If they meet → cycle exists.
     *   - When fast reaches end → slow is at middle.
     *
     * PROBLEMS: Linked list cycle, Find duplicate number,
     *           Middle of linked list, Happy number.
     */
    static boolean hasCycle(int[] nums) {
        // simulated on array for demo — same logic on ListNode
        int slow = nums[0];
        int fast = nums[0];

        do {
            slow = nums[slow];           // 1 step
            fast = nums[nums[fast]];     // 2 steps
        } while (slow != fast);

        return true; // met → cycle
    }


    // ═══════════════════════════════════════════════════════════════════════
    // 5. PREFIX SUM
    // ═══════════════════════════════════════════════════════════════════════
    /**
     * RECOGNIZE when:
     *   • "sum of subarray [i, j]" queried multiple times
     *   • "number of subarrays with sum = K"
     *   • "running total" needed
     *
     * TRICK:
     *   - prefix[i] = sum of nums[0..i-1]
     *   - sum(i, j) = prefix[j+1] - prefix[i]   ← O(1) range query
     *   - For "count subarrays with sum K":
     *       store prefix sums in a map, check if (currentSum - K) seen before.
     *
     * PROBLEMS: Range sum query, Subarray sum equals K,
     *           Product of array except self, Count subarrays with even sum.
     */
    static int subarraySum(int[] nums, int k) {
        Map<Integer, Integer> prefixCount = new HashMap<>();
        prefixCount.put(0, 1);   // empty prefix
        int sum = 0, count = 0;

        for (int num : nums) {
            sum += num;
            // if (sum - k) was seen before, those subarrays sum to k
            count += prefixCount.getOrDefault(sum - k, 0);
            prefixCount.merge(sum, 1, Integer::sum);
        }
        return count;
    }


    // ═══════════════════════════════════════════════════════════════════════
    // 6. HASHMAP — FREQUENCY / COUNT
    // ═══════════════════════════════════════════════════════════════════════
    /**
     * RECOGNIZE when:
     *   • "find duplicates", "find majority element"
     *   • "group anagrams", "valid anagram"
     *   • "first unique character"
     *   • "two sum" (unsorted)
     *
     * TRICK:
     *   - One pass: store value → index (two sum) OR value → count (frequency).
     *   - Check complement / condition on the fly.
     *
     * PROBLEMS: Two Sum, Group Anagrams, Top K frequent elements,
     *           Valid anagram, First unique character.
     */
    static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> seen = new HashMap<>(); // value → index

        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (seen.containsKey(complement))
                return new int[]{seen.get(complement), i};
            seen.put(nums[i], i);
        }
        return new int[]{};
    }


    // ═══════════════════════════════════════════════════════════════════════
    // 7. BINARY SEARCH
    // ═══════════════════════════════════════════════════════════════════════
    /**
     * RECOGNIZE when:
     *   • sorted array + "search for target"
     *   • "find minimum / maximum that satisfies condition" (search on answer)
     *   • "rotated sorted array"
     *   • O(log n) is hinted
     *
     * TRICK:
     *   - Standard: find exact value.
     *   - Left boundary: find FIRST position where condition is true.
     *   - Right boundary: find LAST position where condition is true.
     *   - Search on answer: binary search on the RESULT RANGE, not the array.
     *
     * PROBLEMS: Binary search, Search in rotated array, Find peak element,
     *           Koko eating bananas, Minimum in rotated sorted array.
     */
    static int binarySearch(int[] nums, int target) {
        int left = 0, right = nums.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;  // avoids overflow

            if      (nums[mid] == target) return mid;
            else if (nums[mid] < target)  left  = mid + 1;
            else                          right = mid - 1;
        }
        return -1;
    }

    // find FIRST index where nums[i] >= target  (left boundary)
    static int lowerBound(int[] nums, int target) {
        int left = 0, right = nums.length;

        while (left < right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] < target) left  = mid + 1;
            else                    right = mid;
        }
        return left;
    }


    // ═══════════════════════════════════════════════════════════════════════
    // 8. BACKTRACKING
    // ═══════════════════════════════════════════════════════════════════════
    /**
     * RECOGNIZE when:
     *   • "all combinations / permutations / subsets"
     *   • "generate all valid ___"
     *   • "find all paths"
     *   • brute force with pruning
     *
     * TRICK:
     *   - Choose → Explore → Un-choose  (the backtrack step)
     *   - Prune early when current path already violates constraint.
     *   - Use a `start` index to avoid re-using elements.
     *
     * PROBLEMS: Subsets, Permutations, Combination sum,
     *           N-Queens, Sudoku solver, Word search.
     */
    static List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> result = new ArrayList<>();
        backtrack(candidates, target, 0, new ArrayList<>(), result);
        return result;
    }
    private static void backtrack(int[] nums, int remaining,
                                  int start, List<Integer> current,
                                  List<List<Integer>> result) {
        if (remaining == 0) {
            result.add(new ArrayList<>(current)); // found valid combo
            return;
        }
        for (int i = start; i < nums.length; i++) {
            if (nums[i] > remaining) break;        // PRUNE

            current.add(nums[i]);                  // CHOOSE
            backtrack(nums, remaining - nums[i], i, current, result); // EXPLORE
            current.remove(current.size() - 1);    // UN-CHOOSE
        }
    }


    // ═══════════════════════════════════════════════════════════════════════
    // 9. DYNAMIC PROGRAMMING — 1D
    // ═══════════════════════════════════════════════════════════════════════
    /**
     * RECOGNIZE when:
     *   • "maximum / minimum / count ways"
     *   • "can we reach / achieve ___"
     *   • overlapping subproblems (same sub-question asked repeatedly)
     *   • optimal substructure (answer built from smaller answers)
     *
     * TRICK:
     *   - Define dp[i] = answer for subproblem of size i.
     *   - Write the RECURRENCE: dp[i] = f(dp[i-1], dp[i-2], ...)
     *   - Base cases first, then fill left → right.
     *
     * PROBLEMS: Climbing stairs, House robber, Coin change,
     *           Longest increasing subsequence, Jump game.
     */
    static int rob(int[] nums) {              // House Robber
        if (nums.length == 1) return nums[0];
        int prev2 = nums[0];
        int prev1 = Math.max(nums[0], nums[1]);

        for (int i = 2; i < nums.length; i++) {
            int curr = Math.max(prev1, prev2 + nums[i]);  // rob i or skip i
            prev2 = prev1;
            prev1 = curr;
        }
        return prev1;
    }


    // ═══════════════════════════════════════════════════════════════════════
    // 10. DYNAMIC PROGRAMMING — 2D
    // ═══════════════════════════════════════════════════════════════════════
    /**
     * RECOGNIZE when:
     *   • TWO sequences / strings compared ("edit distance", "LCS")
     *   • grid path problems ("unique paths", "minimum path sum")
     *   • dp[i][j] depends on dp[i-1][j], dp[i][j-1], dp[i-1][j-1]
     *
     * TRICK:
     *   - Rows = one sequence, Cols = other sequence (or grid row/col).
     *   - Fill row by row.
     *   - Draw the recurrence on paper first: what does dp[i][j] mean?
     *
     * PROBLEMS: Longest common subsequence, Edit distance,
     *           Unique paths, Minimum path sum, Interleaving string.
     */
    static int longestCommonSubsequence(String a, String b) {
        int m = a.length(), n = b.length();
        int[][] dp = new int[m + 1][n + 1];  // dp[i][j] = LCS of a[0..i), b[0..j)

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (a.charAt(i - 1) == b.charAt(j - 1))
                    dp[i][j] = dp[i-1][j-1] + 1;       // chars match → extend
                else
                    dp[i][j] = Math.max(dp[i-1][j], dp[i][j-1]); // skip one
            }
        }
        return dp[m][n];
    }


    // ═══════════════════════════════════════════════════════════════════════
    // 11. BFS — GRAPH / TREE
    // ═══════════════════════════════════════════════════════════════════════
    /**
     * RECOGNIZE when:
     *   • "shortest path" in unweighted graph
     *   • "minimum steps / moves"
     *   • level-order traversal
     *   • "nearest ___"
     *
     * TRICK:
     *   - Queue processes nodes LEVEL BY LEVEL.
     *   - Mark visited BEFORE adding to queue (not after) to avoid duplicates.
     *   - Distance = number of levels processed.
     *
     * PROBLEMS: Level order traversal, Word ladder, 01 matrix,
     *           Rotting oranges, Shortest path in binary matrix.
     */
    static int bfs(int[][] grid, int startR, int startC) {
        int rows = grid.length, cols = grid[0].length;
        int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};
        Queue<int[]> queue = new LinkedList<>();
        boolean[][] visited = new boolean[rows][cols];

        queue.offer(new int[]{startR, startC});
        visited[startR][startC] = true;
        int steps = 0;

        while (!queue.isEmpty()) {
            int size = queue.size();            // process level by level
            for (int i = 0; i < size; i++) {
                int[] curr = queue.poll();
                // process curr here

                for (int[] d : dirs) {
                    int nr = curr[0] + d[0];
                    int nc = curr[1] + d[1];
                    if (nr >= 0 && nr < rows && nc >= 0 && nc < cols
                            && !visited[nr][nc] && grid[nr][nc] == 0) {
                        visited[nr][nc] = true;  // mark BEFORE adding
                        queue.offer(new int[]{nr, nc});
                    }
                }
            }
            steps++;
        }
        return steps;
    }


    // ═══════════════════════════════════════════════════════════════════════
    // 12. DFS — GRAPH / TREE
    // ═══════════════════════════════════════════════════════════════════════
    /**
     * RECOGNIZE when:
     *   • "number of islands / connected components"
     *   • "path exists between two nodes"
     *   • "all paths from source to target"
     *   • tree problems (height, diameter, LCA)
     *
     * TRICK:
     *   - Mark visited BEFORE recursing to avoid infinite loops.
     *   - For trees: no visited array needed (no back edges).
     *   - Return value carries info UP the recursion (height, sum, etc).
     *
     * PROBLEMS: Number of islands, Clone graph, Path sum,
     *           Max area of island, Surrounded regions.
     */
    static int numIslands(char[][] grid) {
        int count = 0;
        for (int r = 0; r < grid.length; r++)
            for (int c = 0; c < grid[0].length; c++)
                if (grid[r][c] == '1') {
                    dfs(grid, r, c);   // sink the whole island
                    count++;
                }
        return count;
    }
    private static void dfs(char[][] grid, int r, int c) {
        if (r < 0 || r >= grid.length || c < 0
                || c >= grid[0].length || grid[r][c] != '1') return;
        grid[r][c] = '0';                 // mark visited by sinking
        dfs(grid, r+1, c); dfs(grid, r-1, c);
        dfs(grid, r, c+1); dfs(grid, r, c-1);
    }


    // ═══════════════════════════════════════════════════════════════════════
    // 13. MONOTONIC STACK
    // ═══════════════════════════════════════════════════════════════════════
    /**
     * RECOGNIZE when:
     *   • "next greater element"
     *   • "previous smaller element"
     *   • "largest rectangle in histogram"
     *   • "daily temperatures"
     *   • spans / ranges based on relative order
     *
     * TRICK:
     *   - Decreasing stack → finds NEXT GREATER element.
     *   - Increasing stack → finds NEXT SMALLER element.
     *   - When you pop, the current element is the "next greater" for the popped one.
     *
     * PROBLEMS: Next greater element, Daily temperatures,
     *           Largest rectangle in histogram, Trapping rain water.
     */
    static int[] nextGreaterElement(int[] nums) {
        int n = nums.length;
        int[] result = new int[n];
        Arrays.fill(result, -1);
        Deque<Integer> stack = new ArrayDeque<>(); // stores INDICES

        for (int i = 0; i < n; i++) {
            // pop all elements smaller than nums[i] → nums[i] is their next greater
            while (!stack.isEmpty() && nums[stack.peek()] < nums[i])
                result[stack.pop()] = nums[i];
            stack.push(i);
        }
        return result;
    }


    // ═══════════════════════════════════════════════════════════════════════
    // 14. HEAP / PRIORITY QUEUE
    // ═══════════════════════════════════════════════════════════════════════
    /**
     * RECOGNIZE when:
     *   • "top K largest / smallest"
     *   • "K closest elements"
     *   • "merge K sorted lists"
     *   • "median of data stream"
     *   • greedy problems needing the current min/max fast
     *
     * TRICK:
     *   - Top K LARGEST  → use MIN-heap of size K (keep K largest, evict smallest).
     *   - Top K SMALLEST → use MAX-heap of size K (keep K smallest, evict largest).
     *   - If heap size > K → poll.
     *
     * PROBLEMS: Kth largest element, Top K frequent elements,
     *           K closest points to origin, Merge K sorted lists.
     */
    static int findKthLargest(int[] nums, int k) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>(); // min-heap

        for (int num : nums) {
            minHeap.offer(num);
            if (minHeap.size() > k)
                minHeap.poll();           // remove smallest → keep K largest
        }
        return minHeap.peek();            // top of min-heap = Kth largest
    }


    // ═══════════════════════════════════════════════════════════════════════
    // 15. UNION FIND (Disjoint Set Union — DSU)
    // ═══════════════════════════════════════════════════════════════════════
    /**
     * RECOGNIZE when:
     *   • "number of connected components"
     *   • "detect cycle in undirected graph"
     *   • "dynamic connectivity" (edges added one by one)
     *   • "accounts merge", "friend circles"
     *
     * TRICK:
     *   - find() with PATH COMPRESSION → flattens tree → O(α(n)) ≈ O(1).
     *   - union() with RANK → always attach smaller tree under larger.
     *   - Count components: start with n, decrement on each successful union.
     *
     * PROBLEMS: Number of provinces, Redundant connection,
     *           Accounts merge, Graph valid tree.
     */
    static class UnionFind {
        int[] parent, rank;
        int components;

        UnionFind(int n) {
            parent = new int[n];
            rank   = new int[n];
            components = n;
            for (int i = 0; i < n; i++) parent[i] = i;  // each node is its own root
        }
        int find(int x) {
            if (parent[x] != x)
                parent[x] = find(parent[x]);   // PATH COMPRESSION
            return parent[x];
        }
        boolean union(int x, int y) {
            int px = find(x), py = find(y);
            if (px == py) return false;         // already connected → cycle

            if      (rank[px] < rank[py]) parent[px] = py;  // UNION BY RANK
            else if (rank[px] > rank[py]) parent[py] = px;
            else { parent[py] = px; rank[px]++; }

            components--;
            return true;
        }
    }


    // ═══════════════════════════════════════════════════════════════════════
    //  MASTER RECOGNITION GUIDE
    // ═══════════════════════════════════════════════════════════════════════
    /*
     *  KEYWORD(S) IN PROBLEM                     → PATTERN TO USE
     *  ─────────────────────────────────────────────────────────────────────
     *  subarray of size K                        → Sliding Window (Fixed)
     *  longest subarray with condition           → Sliding Window (Dynamic)
     *  at most K distinct / no repeat            → Sliding Window (Dynamic)
     *  sorted + find pair / two sum              → Two Pointers (Opposite)
     *  linked list cycle / middle                → Two Pointers (Fast/Slow)
     *  sum of subarray [i,j] multiple queries    → Prefix Sum
     *  count subarrays with sum = K              → Prefix Sum + HashMap
     *  find duplicate / group / frequency        → HashMap
     *  sorted + search / O(log n)                → Binary Search
     *  find min/max satisfying a condition       → Binary Search on Answer
     *  all combinations / permutations / paths   → Backtracking
     *  max/min/count, overlapping subproblems    → Dynamic Programming (1D)
     *  two sequences / grid paths                → Dynamic Programming (2D)
     *  shortest path / min steps / levels        → BFS
     *  connected components / flood fill         → DFS
     *  next greater / next smaller / spans       → Monotonic Stack
     *  top K / Kth largest / merge K sorted      → Heap (Priority Queue)
     *  connected components (dynamic) / cycle    → Union Find
     *
     *
     *  TIME COMPLEXITY CHEATSHEET
     *  ─────────────────────────────────────────────────────────────────────
     *  O(1)        HashMap get/put, Heap peek, Union Find (amortized)
     *  O(log n)    Binary Search, Heap poll/offer
     *  O(n)        Sliding Window, Two Pointers, Prefix Sum, HashMap scan
     *  O(n log n)  Sorting, Heap of size n
     *  O(n * k)    Sliding Window with inner work, Top-K with heap
     *  O(n²)       Nested loops, naive DP
     *  O(n * m)    2D DP, BFS/DFS on grid
     *  O(2ⁿ)       Backtracking (subsets), exponential
     *  O(n!)       Backtracking (permutations)
     *
     *
     *  DATA STRUCTURE → USE WHEN
     *  ─────────────────────────────────────────────────────────────────────
     *  HashMap          frequency count, index lookup, complement search
     *  HashSet          membership check, seen/visited tracking
     *  int[] (array)    frequency when values are bounded (0 to 100000)
     *  Deque (stack)    monotonic stack, DFS iterative
     *  Deque (queue)    BFS
     *  PriorityQueue    top-K, greedy min/max, Dijkstra
     *  int[][]          2D DP, grid BFS/DFS
     *  int[] parent     Union Find
     */
}
