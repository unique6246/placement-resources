# DSA — All Patterns: Every Approach + Template

> **How to use:** Each pattern lists ALL common approaches (brute → optimal), with a ready-to-paste Java template for each.

---

## Table of Contents
1. [Sliding Window (Fixed)](#1-sliding-window-fixed-size)
2. [Sliding Window (Dynamic)](#2-sliding-window-dynamic-size)
3. [Two Pointers (Opposite Ends)](#3-two-pointers-opposite-ends)
4. [Two Pointers (Fast & Slow)](#4-two-pointers-fast--slow-floyds)
5. [Prefix Sum](#5-prefix-sum)
6. [HashMap (Frequency / Lookup)](#6-hashmap-frequency--lookup)
7. [Binary Search](#7-binary-search)
8. [Backtracking](#8-backtracking)
9. [Dynamic Programming 1D](#9-dynamic-programming-1d)
10. [Dynamic Programming 2D](#10-dynamic-programming-2d)
11. [BFS](#11-bfs)
12. [DFS](#12-dfs)
13. [Monotonic Stack](#13-monotonic-stack)
14. [Heap / Priority Queue](#14-heap--priority-queue)
15. [Union Find (DSU)](#15-union-find-dsu)
16. [Graph Algorithms (Bonus)](#16-graph-algorithms-bonus)
17. [Trie](#17-trie)
18. [Segment Tree / BIT](#18-segment-tree--binary-indexed-tree-bit)

---

## 1. Sliding Window (Fixed Size)

### Approach 1 — Brute Force O(n·k)
Check every subarray of size k with a nested loop.
```java
int maxSum = Integer.MIN_VALUE;
for (int i = 0; i <= nums.length - k; i++) {
    int sum = 0;
    for (int j = i; j < i + k; j++) sum += nums[j];
    maxSum = Math.max(maxSum, sum);
}
return maxSum;
```

### Approach 2 — Sliding Window O(n) ✅ OPTIMAL
Build first window, then slide: add right element, remove leftmost.
```java
int windowSum = 0, maxSum = 0;

for (int i = 0; i < k; i++)           // build first window
    windowSum += nums[i];
maxSum = windowSum;

for (int right = k; right < nums.length; right++) {
    windowSum += nums[right];          // add new right
    windowSum -= nums[right - k];      // remove old left
    maxSum = Math.max(maxSum, windowSum);
}
return maxSum;
```

### Approach 3 — Sliding Window with HashMap (Distinct Elements) O(n)
When the window also needs a frequency constraint (e.g., all elements distinct).
```java
Map<Integer, Integer> window = new HashMap<>();
int windowSum = 0, maxSum = 0;
int left = 0;

for (int right = 0; right < nums.length; right++) {
    window.merge(nums[right], 1, Integer::sum);
    windowSum += nums[right];

    if (right - left + 1 == k) {
        if (window.size() == k)            // all distinct
            maxSum = Math.max(maxSum, windowSum);
        // shrink exactly one from left
        windowSum -= nums[left];
        window.merge(nums[left], -1, Integer::sum);
        if (window.get(nums[left]) == 0) window.remove(nums[left]);
        left++;
    }
}
return maxSum;
```

### Approach 4 — Deque-based Window Maximum O(n)
When you need the **max/min inside** a fixed window at every position.
```java
Deque<Integer> deque = new ArrayDeque<>(); // stores indices
int[] result = new int[nums.length - k + 1];

for (int i = 0; i < nums.length; i++) {
    // remove indices out of window
    while (!deque.isEmpty() && deque.peekFirst() < i - k + 1)
        deque.pollFirst();
    // maintain decreasing order in deque
    while (!deque.isEmpty() && nums[deque.peekLast()] < nums[i])
        deque.pollLast();
    deque.offerLast(i);
    if (i >= k - 1)
        result[i - k + 1] = nums[deque.peekFirst()];
}
return result;
```

---

## 2. Sliding Window (Dynamic Size)

### Approach 1 — Brute Force O(n²) or O(n³)
Try every subarray, check if condition satisfied.
```java
int result = 0;
for (int i = 0; i < nums.length; i++) {
    Set<Integer> seen = new HashSet<>();
    for (int j = i; j < nums.length; j++) {
        seen.add(nums[j]);
        if (seen.size() <= k)
            result = Math.max(result, j - i + 1);
        else break;
    }
}
return result;
```

### Approach 2 — Sliding Window (Expand/Shrink) O(n) ✅ OPTIMAL
Expand right always. Shrink left **only when window is INVALID**.
```java
Map<Integer, Integer> window = new HashMap<>();
int left = 0, result = 0;

for (int right = 0; right < nums.length; right++) {

    // 1. EXPAND — add right element
    window.merge(nums[right], 1, Integer::sum);

    // 2. SHRINK — fix violation  ← THIS CONDITION CHANGES PER PROBLEM
    while (window.size() > k) {
        window.merge(nums[left], -1, Integer::sum);
        if (window.get(nums[left]) == 0) window.remove(nums[left]);
        left++;
    }

    // 3. RECORD
    result = Math.max(result, right - left + 1);
}
return result;
```

### Approach 3 — Sliding Window for MINIMUM window O(n)
Shrink left as much as possible while condition is satisfied.
```java
// Minimum window substring
Map<Character, Integer> need = new HashMap<>();
for (char c : t.toCharArray()) need.merge(c, 1, Integer::sum);

int left = 0, have = 0, required = need.size();
int minLen = Integer.MAX_VALUE, start = 0;
Map<Character, Integer> window = new HashMap<>();

for (int right = 0; right < s.length(); right++) {
    char c = s.charAt(right);
    window.merge(c, 1, Integer::sum);
    if (need.containsKey(c) && window.get(c).equals(need.get(c))) have++;

    while (have == required) {                    // valid → try to shrink
        if (right - left + 1 < minLen) { minLen = right - left + 1; start = left; }
        char lc = s.charAt(left++);
        window.merge(lc, -1, Integer::sum);
        if (need.containsKey(lc) && window.get(lc) < need.get(lc)) have--;
    }
}
return minLen == Integer.MAX_VALUE ? "" : s.substring(start, start + minLen);
```

### Constraint Swap Reference

| Condition | `while` shrink condition |
|---|---|
| At most K distinct | `window.size() > k` |
| No duplicate chars | `window.get(c) > 1` |
| Sum exceeds target | `sum > target` |
| At most K zeros | `zeroCount > k` |
| At most K replacements | `(right-left+1) - maxFreq > k` |

---

## 3. Two Pointers (Opposite Ends)

### Approach 1 — Brute Force O(n²)
Try every pair.
```java
for (int i = 0; i < nums.length; i++)
    for (int j = i + 1; j < nums.length; j++)
        if (nums[i] + nums[j] == target) return new int[]{i, j};
```

### Approach 2 — Two Pointers on Sorted Array O(n) ✅ OPTIMAL
```java
int left = 0, right = nums.length - 1;
while (left < right) {
    int sum = nums[left] + nums[right];
    if      (sum == target) return new int[]{left, right};
    else if (sum < target)  left++;    // need bigger
    else                    right--;   // need smaller
}
return new int[]{-1, -1};
```

### Approach 3 — 3Sum (Fix one + two pointers) O(n²)
```java
Arrays.sort(nums);
List<List<Integer>> result = new ArrayList<>();

for (int i = 0; i < nums.length - 2; i++) {
    if (i > 0 && nums[i] == nums[i-1]) continue;   // skip duplicates
    int left = i + 1, right = nums.length - 1;
    while (left < right) {
        int sum = nums[i] + nums[left] + nums[right];
        if (sum == 0) {
            result.add(Arrays.asList(nums[i], nums[left], nums[right]));
            while (left < right && nums[left]  == nums[left+1])  left++;
            while (left < right && nums[right] == nums[right-1]) right--;
            left++; right--;
        } else if (sum < 0) left++;
        else                right--;
    }
}
return result;
```

### Approach 4 — Palindrome Check O(n)
```java
int left = 0, right = s.length() - 1;
while (left < right) {
    if (s.charAt(left) != s.charAt(right)) return false;
    left++; right--;
}
return true;
```

### Approach 5 — Trapping Rain Water (Two Pointers) O(n)
```java
int left = 0, right = nums.length - 1;
int leftMax = 0, rightMax = 0, water = 0;
while (left < right) {
    if (nums[left] < nums[right]) {
        if (nums[left] >= leftMax) leftMax = nums[left];
        else water += leftMax - nums[left];
        left++;
    } else {
        if (nums[right] >= rightMax) rightMax = nums[right];
        else water += rightMax - nums[right];
        right--;
    }
}
return water;
```

---

## 4. Two Pointers (Fast & Slow / Floyd's)

### Approach 1 — Visited Set O(n) space
```java
Set<ListNode> seen = new HashSet<>();
while (head != null) {
    if (seen.contains(head)) return true;
    seen.add(head);
    head = head.next;
}
return false;
```

### Approach 2 — Floyd's Cycle Detection O(1) space ✅ OPTIMAL
```java
ListNode slow = head, fast = head;
while (fast != null && fast.next != null) {
    slow = slow.next;
    fast = fast.next.next;
    if (slow == fast) return true;   // cycle!
}
return false;
```

### Approach 3 — Find Cycle Entry Point O(n)
After detection, reset one pointer to head, move both 1 step → they meet at cycle start.
```java
ListNode slow = head, fast = head;
while (fast != null && fast.next != null) {
    slow = slow.next; fast = fast.next.next;
    if (slow == fast) {
        slow = head;                    // reset
        while (slow != fast) { slow = slow.next; fast = fast.next; }
        return slow;                    // cycle entry
    }
}
return null;
```

### Approach 4 — Find Middle O(n)
```java
ListNode slow = head, fast = head;
while (fast != null && fast.next != null) {
    slow = slow.next;
    fast = fast.next.next;
}
return slow;   // slow is middle
```

### Approach 5 — Remove Nth From End O(n)
```java
ListNode dummy = new ListNode(0);
dummy.next = head;
ListNode fast = dummy, slow = dummy;
for (int i = 0; i <= n; i++) fast = fast.next;   // gap of N+1
while (fast != null) { slow = slow.next; fast = fast.next; }
slow.next = slow.next.next;   // skip target node
return dummy.next;
```

---

## 5. Prefix Sum

### Approach 1 — Brute Force O(n²)
```java
int count = 0;
for (int i = 0; i < nums.length; i++) {
    int sum = 0;
    for (int j = i; j < nums.length; j++) {
        sum += nums[j];
        if (sum == k) count++;
    }
}
return count;
```

### Approach 2 — Prefix Sum Array (Range Query) O(1) per query after O(n) build
```java
int[] prefix = new int[n + 1];
for (int i = 0; i < n; i++)
    prefix[i + 1] = prefix[i] + nums[i];

// sum of nums[i..j] in O(1):
int rangeSum = prefix[j + 1] - prefix[i];
```

### Approach 3 — Prefix Sum + HashMap (Count Subarrays = K) O(n) ✅ OPTIMAL
```java
Map<Integer, Integer> prefixCount = new HashMap<>();
prefixCount.put(0, 1);       // empty prefix
int sum = 0, count = 0;

for (int num : nums) {
    sum += num;
    count += prefixCount.getOrDefault(sum - k, 0);  // complement seen?
    prefixCount.merge(sum, 1, Integer::sum);
}
return count;
```

### Approach 4 — Prefix Sum mod K (Divisible Subarrays) O(n)
```java
Map<Integer, Integer> modCount = new HashMap<>();
modCount.put(0, 1);
int sum = 0, count = 0;

for (int num : nums) {
    sum = ((sum + num) % k + k) % k;   // handle negatives
    count += modCount.getOrDefault(sum, 0);
    modCount.merge(sum, 1, Integer::sum);
}
return count;
```

### Approach 5 — 2D Prefix Sum (Grid Sum Queries) O(1) per query
```java
int[][] prefix = new int[m+1][n+1];
for (int i = 1; i <= m; i++)
    for (int j = 1; j <= n; j++)
        prefix[i][j] = grid[i-1][j-1] + prefix[i-1][j] + prefix[i][j-1] - prefix[i-1][j-1];

// sum of rectangle (r1,c1) to (r2,c2):
int rectSum = prefix[r2+1][c2+1] - prefix[r1][c2+1] - prefix[r2+1][c1] + prefix[r1][c1];
```

---

## 6. HashMap (Frequency / Lookup)

### Approach 1 — Two Sum Brute Force O(n²)
```java
for (int i = 0; i < nums.length; i++)
    for (int j = i + 1; j < nums.length; j++)
        if (nums[i] + nums[j] == target) return new int[]{i, j};
```

### Approach 2 — Two Sum HashMap O(n) ✅ OPTIMAL
```java
Map<Integer, Integer> seen = new HashMap<>();  // value → index
for (int i = 0; i < nums.length; i++) {
    int complement = target - nums[i];
    if (seen.containsKey(complement)) return new int[]{seen.get(complement), i};
    seen.put(nums[i], i);
}
return new int[]{-1, -1};
```

### Approach 3 — Frequency Count O(n)
```java
Map<Character, Integer> freq = new HashMap<>();
for (char c : s.toCharArray())
    freq.merge(c, 1, Integer::sum);
```

### Approach 4 — Group Anagrams O(n·k log k)
```java
Map<String, List<String>> map = new HashMap<>();
for (String word : strs) {
    char[] arr = word.toCharArray();
    Arrays.sort(arr);
    String key = new String(arr);
    map.computeIfAbsent(key, x -> new ArrayList<>()).add(word);
}
return new ArrayList<>(map.values());
```

### Approach 5 — Longest Consecutive Sequence O(n)
```java
Set<Integer> set = new HashSet<>(Arrays.asList(nums));
// wrap nums in list first if needed
int longest = 0;
for (int num : set) {
    if (!set.contains(num - 1)) {     // start of sequence
        int len = 1;
        while (set.contains(num + len)) len++;
        longest = Math.max(longest, len);
    }
}
return longest;
```

### Approach 6 — Fixed-Size Array as HashMap (bounded chars) O(n)
Use when values are bounded (e.g., lowercase letters → size 26).
```java
int[] freq = new int[26];
for (char c : s.toCharArray()) freq[c - 'a']++;
// check
for (char c : t.toCharArray()) if (--freq[c - 'a'] < 0) return false;
return true;
```

---

## 7. Binary Search

### Approach 1 — Linear Search O(n)
```java
for (int i = 0; i < nums.length; i++)
    if (nums[i] == target) return i;
return -1;
```

### Approach 2 — Classic Binary Search (exact value) O(log n) ✅
```java
int left = 0, right = nums.length - 1;
while (left <= right) {
    int mid = left + (right - left) / 2;
    if      (nums[mid] == target) return mid;
    else if (nums[mid] < target)  left  = mid + 1;
    else                          right = mid - 1;
}
return -1;
```

### Approach 3 — Left Boundary (first true) O(log n)
Find the leftmost index where `condition(mid)` is true.
```java
int left = 0, right = nums.length;   // right is exclusive
while (left < right) {
    int mid = left + (right - left) / 2;
    if (condition(mid)) right = mid;   // might be answer, keep searching left
    else                left  = mid + 1;
}
return left;  // first index where condition is true
```

### Approach 4 — Right Boundary (last true) O(log n)
```java
int left = 0, right = nums.length - 1;
while (left < right) {
    int mid = left + (right - left + 1) / 2;  // +1 to bias right
    if (condition(mid)) left  = mid;
    else                right = mid - 1;
}
return left;  // last index where condition is true
```

### Approach 5 — Binary Search on Answer O(log(range) · f(n))
Search on the answer range instead of the array.
```java
int left = minPossibleAnswer, right = maxPossibleAnswer;
while (left < right) {
    int mid = left + (right - left) / 2;
    if (canAchieve(mid)) right = mid;   // mid works, try smaller
    else                 left  = mid + 1;
}
return left;

// canAchieve(mid) — greedy check function:
boolean canAchieve(int mid) {
    // verify whether mid is a feasible answer in O(n) or O(n log n)
    return true; // or false
}
```

### Approach 6 — Rotated Sorted Array O(log n)
```java
int left = 0, right = nums.length - 1;
while (left <= right) {
    int mid = left + (right - left) / 2;
    if (nums[mid] == target) return mid;
    if (nums[left] <= nums[mid]) {         // left half sorted
        if (nums[left] <= target && target < nums[mid]) right = mid - 1;
        else                                             left  = mid + 1;
    } else {                               // right half sorted
        if (nums[mid] < target && target <= nums[right]) left  = mid + 1;
        else                                              right = mid - 1;
    }
}
return -1;
```

---

## 8. Backtracking

### Approach 1 — Recursive Backtracking (Subsets / Combinations) O(2ⁿ)
```java
List<List<Integer>> result = new ArrayList<>();

void backtrack(int[] nums, int start, List<Integer> current) {
    result.add(new ArrayList<>(current));           // record at every node

    for (int i = start; i < nums.length; i++) {
        current.add(nums[i]);                       // CHOOSE
        backtrack(nums, i + 1, current);            // EXPLORE (i+1 = no reuse)
        current.remove(current.size() - 1);         // UN-CHOOSE
    }
}
// Call: backtrack(nums, 0, new ArrayList<>());
```

### Approach 2 — Combinations with Target Sum (Reuse allowed) O(n^(target/min))
```java
void backtrack(int[] candidates, int start, int remaining, List<Integer> current) {
    if (remaining == 0) { result.add(new ArrayList<>(current)); return; }
    if (remaining < 0)  return;   // prune

    for (int i = start; i < candidates.length; i++) {
        current.add(candidates[i]);
        backtrack(candidates, i, remaining - candidates[i], current);  // i = reuse
        current.remove(current.size() - 1);
    }
}
```

### Approach 3 — Permutations O(n!)
```java
void backtrack(int[] nums, boolean[] used, List<Integer> current) {
    if (current.size() == nums.length) { result.add(new ArrayList<>(current)); return; }

    for (int i = 0; i < nums.length; i++) {
        if (used[i]) continue;
        // skip duplicates: if (i > 0 && nums[i] == nums[i-1] && !used[i-1]) continue;
        used[i] = true;
        current.add(nums[i]);
        backtrack(nums, used, current);
        current.remove(current.size() - 1);
        used[i] = false;
    }
}
// Call: Arrays.sort(nums); backtrack(nums, new boolean[n], new ArrayList<>());
```

### Approach 4 — Grid/Word Backtracking O(n·m·4^L)
```java
boolean dfs(char[][] board, String word, int r, int c, int idx) {
    if (idx == word.length()) return true;
    if (r < 0 || r >= board.length || c < 0 || c >= board[0].length) return false;
    if (board[r][c] != word.charAt(idx)) return false;

    char temp = board[r][c];
    board[r][c] = '#';                    // mark visited
    boolean found = dfs(board, word, r+1, c, idx+1) ||
                    dfs(board, word, r-1, c, idx+1) ||
                    dfs(board, word, r, c+1, idx+1) ||
                    dfs(board, word, r, c-1, idx+1);
    board[r][c] = temp;                   // restore
    return found;
}
```

### Approach 5 — N-Queens / Constraint Backtracking O(n!)
```java
void backtrack(int row, boolean[] cols, boolean[] diag1, boolean[] diag2) {
    if (row == n) { result.add(buildBoard(queens)); return; }

    for (int col = 0; col < n; col++) {
        if (cols[col] || diag1[row-col+n] || diag2[row+col]) continue; // prune
        cols[col] = diag1[row-col+n] = diag2[row+col] = true;
        queens[row] = col;
        backtrack(row + 1, cols, diag1, diag2);
        cols[col] = diag1[row-col+n] = diag2[row+col] = false;
    }
}
```

### Backtracking Variant Quick Table

| Problem | start | Reuse | Visited Array |
|---|---|---|---|
| Subsets | `i + 1` | No | No |
| Combinations | `i + 1` | No | No |
| Combination Sum (reuse) | `i` | Yes | No |
| Permutations | `0` | No | Yes |
| Permutations with Dups | `0` | No | Yes + sort |

---

## 9. Dynamic Programming (1D)

### Approach 1 — Memoization (Top-Down) O(n)
```java
int[] memo = new int[n + 1];
Arrays.fill(memo, -1);

int dp(int i) {
    if (i <= 1) return i;
    if (memo[i] != -1) return memo[i];
    return memo[i] = dp(i - 1) + dp(i - 2);
}
```

### Approach 2 — Tabulation (Bottom-Up) O(n) ✅ OPTIMAL
```java
int[] dp = new int[n + 1];
dp[0] = base0;
dp[1] = base1;

for (int i = 2; i <= n; i++)
    dp[i] = dp[i-1] + dp[i-2];   // ← recurrence changes per problem

return dp[n];
```

### Approach 3 — Space-Optimized (Rolling Variables) O(1) space
```java
int prev2 = base0, prev1 = base1;
for (int i = 2; i <= n; i++) {
    int curr = prev1 + prev2;    // ← recurrence
    prev2 = prev1;
    prev1 = curr;
}
return prev1;
```

### Approach 4 — Coin Change (Unbounded Knapsack) O(n·coins)
```java
int[] dp = new int[amount + 1];
Arrays.fill(dp, Integer.MAX_VALUE);
dp[0] = 0;

for (int i = 1; i <= amount; i++)
    for (int coin : coins)
        if (coin <= i && dp[i - coin] != Integer.MAX_VALUE)
            dp[i] = Math.min(dp[i], dp[i - coin] + 1);

return dp[amount] == Integer.MAX_VALUE ? -1 : dp[amount];
```

### Approach 5 — LIS (Longest Increasing Subsequence) O(n²) then O(n log n)
```java
// O(n²) DP
int[] dp = new int[n];
Arrays.fill(dp, 1);
for (int i = 1; i < n; i++)
    for (int j = 0; j < i; j++)
        if (nums[j] < nums[i]) dp[i] = Math.max(dp[i], dp[j] + 1);
return Arrays.stream(dp).max().getAsInt();

// O(n log n) — Patience Sort / Binary Search
int[] tails = new int[n];
int len = 0;
for (int num : nums) {
    int pos = Arrays.binarySearch(tails, 0, len, num);
    if (pos < 0) pos = -(pos + 1);
    tails[pos] = num;
    if (pos == len) len++;
}
return len;
```

### Approach 6 — 0/1 Knapsack O(n·W)
```java
int[][] dp = new int[n + 1][W + 1];
for (int i = 1; i <= n; i++)
    for (int w = 0; w <= W; w++) {
        dp[i][w] = dp[i-1][w];                              // skip item i
        if (weights[i-1] <= w)
            dp[i][w] = Math.max(dp[i][w], dp[i-1][w - weights[i-1]] + values[i-1]);
    }
return dp[n][W];

// Space-optimized 1D (iterate W backwards)
int[] dp = new int[W + 1];
for (int i = 0; i < n; i++)
    for (int w = W; w >= weights[i]; w--)
        dp[w] = Math.max(dp[w], dp[w - weights[i]] + values[i]);
```

### 1D DP Recurrence Reference

| Problem | `dp[i]` meaning | Recurrence |
|---|---|---|
| Climbing Stairs | ways to reach step i | `dp[i-1] + dp[i-2]` |
| House Robber | max money up to i | `max(dp[i-1], dp[i-2] + nums[i])` |
| Coin Change | min coins for amount i | `min(dp[i-coin]+1)` for each coin |
| Word Break | can form s[0..i) | `dp[j] && dict.has(s[j..i))` |
| Decode Ways | ways to decode s[0..i) | `dp[i-1] (1 digit) + dp[i-2] (2 digits)` |
| Jump Game | max reach from i | `max(reach, i + nums[i])` |
| LIS | LIS ending at i | `max(dp[j]+1) where nums[j] < nums[i]` |

---

## 10. Dynamic Programming (2D)

### Approach 1 — LCS (Two Sequences) O(m·n)
```java
int[][] dp = new int[m + 1][n + 1];
for (int i = 1; i <= m; i++) {
    for (int j = 1; j <= n; j++) {
        if (a.charAt(i-1) == b.charAt(j-1))
            dp[i][j] = dp[i-1][j-1] + 1;          // match → extend diagonal
        else
            dp[i][j] = Math.max(dp[i-1][j], dp[i][j-1]);  // skip one
    }
}
return dp[m][n];
```

### Approach 2 — Edit Distance O(m·n)
```java
int[][] dp = new int[m + 1][n + 1];
for (int i = 0; i <= m; i++) dp[i][0] = i;
for (int j = 0; j <= n; j++) dp[0][j] = j;

for (int i = 1; i <= m; i++)
    for (int j = 1; j <= n; j++) {
        if (a.charAt(i-1) == b.charAt(j-1))
            dp[i][j] = dp[i-1][j-1];               // no op
        else
            dp[i][j] = 1 + Math.min(dp[i-1][j-1],  // replace
                           Math.min(dp[i-1][j],      // delete
                                    dp[i][j-1]));    // insert
    }
return dp[m][n];
```

### Approach 3 — Unique Paths / Grid DP O(m·n)
```java
int[][] dp = new int[m][n];
for (int i = 0; i < m; i++) dp[i][0] = 1;
for (int j = 0; j < n; j++) dp[0][j] = 1;

for (int i = 1; i < m; i++)
    for (int j = 1; j < n; j++)
        dp[i][j] = dp[i-1][j] + dp[i][j-1];   // from top + from left

return dp[m-1][n-1];
```

### Approach 4 — Min Path Sum O(m·n)
```java
int[][] dp = new int[m][n];
dp[0][0] = grid[0][0];
for (int i = 1; i < m; i++) dp[i][0] = dp[i-1][0] + grid[i][0];
for (int j = 1; j < n; j++) dp[0][j] = dp[0][j-1] + grid[0][j];

for (int i = 1; i < m; i++)
    for (int j = 1; j < n; j++)
        dp[i][j] = grid[i][j] + Math.min(dp[i-1][j], dp[i][j-1]);

return dp[m-1][n-1];
```

### Approach 5 — Space-Optimized 2D DP O(n) space
```java
// Use only 1D array (rolling row)
int[] dp = new int[n + 1];
for (int i = 1; i <= m; i++)
    for (int j = 1; j <= n; j++)
        dp[j] = (a.charAt(i-1) == b.charAt(j-1))
                 ? prev_dp[j-1] + 1            // need to track prev value
                 : Math.max(dp[j], dp[j-1]);
```

### 2D DP Reference Table

| Problem | `dp[i][j]` meaning | Key recurrence |
|---|---|---|
| LCS | LCS of a[0..i), b[0..j) | match → diag+1, else max(up,left) |
| Edit Distance | edits for a[0..i) → b[0..j) | match → diag, else min(up,left,diag)+1 |
| Unique Paths | ways to reach (i,j) | `dp[i-1][j] + dp[i][j-1]` |
| Min Path Sum | min cost to (i,j) | `grid[i][j] + min(up,left)` |
| Palindromic Subseq | LPS of s[i..j] | match → diag+2, else max(up,left) |
| Interleaving String | s3[0..i+j) formed | from s1 or s2 transitions |

---

## 11. BFS

### Approach 1 — Simple BFS (Level Order / Shortest Path) O(V+E)
```java
Queue<Integer> queue = new LinkedList<>();
boolean[] visited = new boolean[n];

queue.offer(start);
visited[start] = true;   // mark BEFORE enqueue
int steps = 0;

while (!queue.isEmpty()) {
    int size = queue.size();           // level size snapshot
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
return -1;
```

### Approach 2 — Grid BFS O(m·n)
```java
Queue<int[]> queue = new LinkedList<>();
boolean[][] visited = new boolean[rows][cols];
int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};

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
                    && !visited[nr][nc] && grid[nr][nc] == valid) {
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
Start BFS from **multiple sources** simultaneously.
```java
Queue<int[]> queue = new LinkedList<>();
// enqueue ALL sources first
for (int r = 0; r < rows; r++)
    for (int c = 0; c < cols; c++)
        if (grid[r][c] == source) { queue.offer(new int[]{r, c}); visited[r][c] = true; }

while (!queue.isEmpty()) {
    int[] curr = queue.poll();
    for (int[] d : dirs) {
        int nr = curr[0] + d[0], nc = curr[1] + d[1];
        if (inBounds(nr, nc) && !visited[nr][nc]) {
            visited[nr][nc] = true;
            dist[nr][nc] = dist[curr[0]][curr[1]] + 1;
            queue.offer(new int[]{nr, nc});
        }
    }
}
```

### Approach 4 — Bidirectional BFS O(b^(d/2)) vs O(b^d)
```java
Set<String> beginSet = new HashSet<>(), endSet = new HashSet<>();
beginSet.add(beginWord); endSet.add(endWord);
int len = 1;

while (!beginSet.isEmpty()) {
    if (beginSet.size() > endSet.size()) { Set<String> tmp = beginSet; beginSet = endSet; endSet = tmp; }
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

### Approach 5 — Dijkstra (Weighted Shortest Path) O((V+E) log V)
```java
PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
int[] dist = new int[n]; Arrays.fill(dist, Integer.MAX_VALUE);
dist[src] = 0;
pq.offer(new int[]{src, 0});

while (!pq.isEmpty()) {
    int[] curr = pq.poll();
    int node = curr[0], d = curr[1];
    if (d > dist[node]) continue;           // stale entry
    for (int[] edge : graph.get(node)) {
        int neighbor = edge[0], weight = edge[1];
        if (dist[node] + weight < dist[neighbor]) {
            dist[neighbor] = dist[node] + weight;
            pq.offer(new int[]{neighbor, dist[neighbor]});
        }
    }
}
return dist[target];
```

---

## 12. DFS

### Approach 1 — Recursive DFS (Graph) O(V+E)
```java
void dfs(int node, boolean[] visited, List<List<Integer>> graph) {
    visited[node] = true;
    for (int neighbor : graph.get(node))
        if (!visited[neighbor])
            dfs(neighbor, visited, graph);
}
```

### Approach 2 — Iterative DFS (Stack) O(V+E)
```java
Deque<Integer> stack = new ArrayDeque<>();
boolean[] visited = new boolean[n];

stack.push(start);
while (!stack.isEmpty()) {
    int node = stack.pop();
    if (visited[node]) continue;
    visited[node] = true;
    for (int neighbor : graph.get(node))
        if (!visited[neighbor]) stack.push(neighbor);
}
```

### Approach 3 — Grid DFS / Flood Fill O(m·n)
```java
void dfs(char[][] grid, int r, int c) {
    if (r < 0 || r >= grid.length ||
        c < 0 || c >= grid[0].length || grid[r][c] != '1') return;
    grid[r][c] = '0';             // sink = mark visited
    dfs(grid, r+1, c); dfs(grid, r-1, c);
    dfs(grid, r, c+1); dfs(grid, r, c-1);
}
```

### Approach 4 — Tree DFS (Return Value Bubbles Up) O(n)
```java
int result = Integer.MIN_VALUE;

int dfs(TreeNode node) {
    if (node == null) return 0;
    int left  = Math.max(0, dfs(node.left));    // ignore negative paths
    int right = Math.max(0, dfs(node.right));
    result = Math.max(result, node.val + left + right);  // update global max
    return node.val + Math.max(left, right);             // return best branch
}
```

### Approach 5 — Topological Sort DFS (Cycle Detection) O(V+E)
```java
// 0 = unvisited, 1 = in-stack, 2 = done
int[] state = new int[n];
boolean hasCycle = false;

void dfs(int node) {
    state[node] = 1;                              // mark in-stack
    for (int neighbor : graph.get(node)) {
        if (state[neighbor] == 1) { hasCycle = true; return; }  // back edge!
        if (state[neighbor] == 0) dfs(neighbor);
    }
    state[node] = 2;                              // mark done
    topoOrder.addFirst(node);                     // post-order = topo order
}
```

### Approach 6 — All Paths DFS O(2^n · n)
```java
void dfs(int node, List<Integer> path, List<List<Integer>> result) {
    path.add(node);
    if (node == target) { result.add(new ArrayList<>(path)); }
    else for (int neighbor : graph.get(node)) dfs(neighbor, path, result);
    path.remove(path.size() - 1);   // backtrack
}
```

---

## 13. Monotonic Stack

### Approach 1 — Brute Force (Next Greater) O(n²)
```java
int[] result = new int[n];
for (int i = 0; i < n; i++) {
    result[i] = -1;
    for (int j = i + 1; j < n; j++)
        if (nums[j] > nums[i]) { result[i] = nums[j]; break; }
}
```

### Approach 2 — Monotonic Decreasing Stack (Next Greater) O(n) ✅ OPTIMAL
Pop when `nums[i] > top` → current element is **next greater** for all popped.
```java
Deque<Integer> stack = new ArrayDeque<>();   // stores INDICES
int[] result = new int[n];
Arrays.fill(result, -1);

for (int i = 0; i < n; i++) {
    while (!stack.isEmpty() && nums[stack.peek()] < nums[i])
        result[stack.pop()] = nums[i];        // nums[i] is next greater
    stack.push(i);
}
```

### Approach 3 — Monotonic Increasing Stack (Next Smaller) O(n)
```java
Deque<Integer> stack = new ArrayDeque<>();
int[] result = new int[n];
Arrays.fill(result, -1);

for (int i = 0; i < n; i++) {
    while (!stack.isEmpty() && nums[stack.peek()] > nums[i])
        result[stack.pop()] = nums[i];        // nums[i] is next smaller
    stack.push(i);
}
```

### Approach 4 — Circular Array (Next Greater II) O(n)
```java
Deque<Integer> stack = new ArrayDeque<>();
int[] result = new int[n];
Arrays.fill(result, -1);

for (int i = 0; i < 2 * n; i++) {            // loop twice
    while (!stack.isEmpty() && nums[stack.peek()] < nums[i % n])
        result[stack.pop()] = nums[i % n];
    if (i < n) stack.push(i);
}
```

### Approach 5 — Largest Rectangle in Histogram O(n)
```java
Deque<Integer> stack = new ArrayDeque<>();
int maxArea = 0;
int[] heights = Arrays.copyOf(h, h.length + 1); // append 0 sentinel

for (int i = 0; i < heights.length; i++) {
    while (!stack.isEmpty() && heights[stack.peek()] > heights[i]) {
        int height = heights[stack.pop()];
        int width  = stack.isEmpty() ? i : i - stack.peek() - 1;
        maxArea = Math.max(maxArea, height * width);
    }
    stack.push(i);
}
return maxArea;
```

### Approach 6 — Sum of Subarray Minimums O(n)
```java
// Use two monotonic stacks to find left/right boundaries for each element
int[] left = new int[n], right = new int[n];
Deque<Integer> stack = new ArrayDeque<>();

for (int i = 0; i < n; i++) {
    while (!stack.isEmpty() && arr[stack.peek()] >= arr[i]) stack.pop();
    left[i] = stack.isEmpty() ? i + 1 : i - stack.peek();
    stack.push(i);
}
stack.clear();
for (int i = n - 1; i >= 0; i--) {
    while (!stack.isEmpty() && arr[stack.peek()] > arr[i]) stack.pop();
    right[i] = stack.isEmpty() ? n - i : stack.peek() - i;
    stack.push(i);
}
long ans = 0, MOD = 1_000_000_007L;
for (int i = 0; i < n; i++)
    ans = (ans + (long) arr[i] * left[i] * right[i]) % MOD;
return (int) ans;
```

---

## 14. Heap / Priority Queue

### Approach 1 — Sort O(n log n)
Sort and pick Kth element.
```java
Arrays.sort(nums);
return nums[nums.length - k];
```

### Approach 2 — Min-Heap of Size K (Top K Largest) O(n log k) ✅
```java
PriorityQueue<Integer> minHeap = new PriorityQueue<>();
for (int num : nums) {
    minHeap.offer(num);
    if (minHeap.size() > k) minHeap.poll();   // evict smallest
}
return minHeap.peek();   // Kth largest
```

### Approach 3 — Max-Heap of Size K (Top K Smallest) O(n log k)
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
int quickselect(int[] nums, int lo, int hi, int k) {
    int pivot = nums[hi], p = lo;
    for (int i = lo; i < hi; i++)
        if (nums[i] <= pivot) { int tmp = nums[i]; nums[i] = nums[p]; nums[p++] = tmp; }
    int tmp = nums[p]; nums[p] = nums[hi]; nums[hi] = tmp;
    if (p == k) return nums[p];
    return p < k ? quickselect(nums, p + 1, hi, k) : quickselect(nums, lo, p - 1, k);
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

### Approach 6 — Find Median from Data Stream O(log n) per add
Use two heaps: max-heap for lower half, min-heap for upper half.
```java
PriorityQueue<Integer> lower = new PriorityQueue<>(Collections.reverseOrder()); // max-heap
PriorityQueue<Integer> upper = new PriorityQueue<>();                            // min-heap

void addNum(int num) {
    lower.offer(num);
    upper.offer(lower.poll());          // balance: send max of lower to upper
    if (lower.size() < upper.size())
        lower.offer(upper.poll());      // keep lower >= upper in size
}
double findMedian() {
    return lower.size() > upper.size()
           ? lower.peek()
           : (lower.peek() + upper.peek()) / 2.0;
}
```

---

## 15. Union Find (DSU)

### Approach 1 — DFS/BFS for Connected Components O(V+E)
Count components by running DFS/BFS from each unvisited node.
```java
int count = 0;
boolean[] visited = new boolean[n];
for (int i = 0; i < n; i++) {
    if (!visited[i]) { dfs(i, visited, graph); count++; }
}
return count;
```

### Approach 2 — Union Find (Basic) O(n) with path compression
```java
int[] parent = new int[n];
for (int i = 0; i < n; i++) parent[i] = i;

int find(int x) {
    if (parent[x] != x) parent[x] = find(parent[x]);   // path compression
    return parent[x];
}
void union(int x, int y) { parent[find(x)] = find(y); }
```

### Approach 3 — Union Find with Rank ✅ OPTIMAL O(α(n)) ≈ O(1) amortized
```java
class UnionFind {
    int[] parent, rank;
    int components;

    UnionFind(int n) {
        parent = new int[n]; rank = new int[n];
        components = n;
        for (int i = 0; i < n; i++) parent[i] = i;
    }

    int find(int x) {
        if (parent[x] != x) parent[x] = find(parent[x]);  // PATH COMPRESSION
        return parent[x];
    }

    boolean union(int x, int y) {
        int px = find(x), py = find(y);
        if (px == py) return false;                         // cycle detected!
        if      (rank[px] < rank[py]) parent[px] = py;     // UNION BY RANK
        else if (rank[px] > rank[py]) parent[py] = px;
        else { parent[py] = px; rank[px]++; }
        components--;
        return true;
    }
}
```

### Approach 4 — Cycle Detection in Undirected Graph
```java
UnionFind uf = new UnionFind(n);
for (int[] edge : edges)
    if (!uf.union(edge[0], edge[1])) return false;  // union returned false = cycle
return true;
```

### Approach 5 — Accounts Merge / Group Items
```java
UnionFind uf = new UnionFind(n);
Map<String, Integer> emailOwner = new HashMap<>();

for (int i = 0; i < accounts.size(); i++)
    for (int j = 1; j < accounts.get(i).size(); j++) {
        String email = accounts.get(i).get(j);
        if (emailOwner.containsKey(email)) uf.union(i, emailOwner.get(email));
        else emailOwner.put(email, i);
    }

Map<Integer, List<String>> groups = new HashMap<>();
for (String email : emailOwner.keySet())
    groups.computeIfAbsent(uf.find(emailOwner.get(email)), k -> new ArrayList<>()).add(email);

List<List<String>> result = new ArrayList<>();
for (int root : groups.keySet()) {
    List<String> emails = groups.get(root);
    Collections.sort(emails);
    emails.add(0, accounts.get(root).get(0));
    result.add(emails);
}
return result;
```

---

## 16. Graph Algorithms (Bonus)

### Topological Sort — BFS / Kahn's Algorithm O(V+E)
```java
int[] inDegree = new int[n];
for (int[] edge : edges) inDegree[edge[1]]++;

Queue<Integer> queue = new LinkedList<>();
for (int i = 0; i < n; i++) if (inDegree[i] == 0) queue.offer(i);

List<Integer> order = new ArrayList<>();
while (!queue.isEmpty()) {
    int node = queue.poll();
    order.add(node);
    for (int neighbor : graph.get(node))
        if (--inDegree[neighbor] == 0) queue.offer(neighbor);
}
return order.size() == n ? order : new ArrayList<>();  // empty if cycle
```

### Bellman-Ford (Negative Weights) O(V·E)
```java
int[] dist = new int[n]; Arrays.fill(dist, Integer.MAX_VALUE); dist[src] = 0;
for (int i = 0; i < n - 1; i++)
    for (int[] edge : edges)   // [from, to, weight]
        if (dist[edge[0]] != Integer.MAX_VALUE && dist[edge[0]] + edge[2] < dist[edge[1]])
            dist[edge[1]] = dist[edge[0]] + edge[2];
// n-th pass detects negative cycle
```

### Floyd-Warshall (All-Pairs Shortest Path) O(V³)
```java
int[][] dist = new int[n][n];
// initialize: dist[i][j] = edge weight, dist[i][i] = 0, else INF
for (int k = 0; k < n; k++)
    for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++)
            if (dist[i][k] != INF && dist[k][j] != INF)
                dist[i][j] = Math.min(dist[i][j], dist[i][k] + dist[k][j]);
```

---

## 17. Trie

### Approach 1 — Array-based Trie (lowercase letters) O(L) per op
```java
class Trie {
    Trie[] children = new Trie[26];
    boolean isEnd = false;

    void insert(String word) {
        Trie node = this;
        for (char c : word.toCharArray()) {
            int idx = c - 'a';
            if (node.children[idx] == null) node.children[idx] = new Trie();
            node = node.children[idx];
        }
        node.isEnd = true;
    }

    boolean search(String word) {
        Trie node = this;
        for (char c : word.toCharArray()) {
            int idx = c - 'a';
            if (node.children[idx] == null) return false;
            node = node.children[idx];
        }
        return node.isEnd;
    }

    boolean startsWith(String prefix) {
        Trie node = this;
        for (char c : prefix.toCharArray()) {
            int idx = c - 'a';
            if (node.children[idx] == null) return false;
            node = node.children[idx];
        }
        return true;
    }
}
```

### Approach 2 — HashMap-based Trie (general characters)
```java
class TrieNode {
    Map<Character, TrieNode> children = new HashMap<>();
    boolean isEnd = false;
}
```

---

## 18. Segment Tree / Binary Indexed Tree (BIT)

### Binary Indexed Tree (Fenwick Tree) — Range Sum O(log n) per query/update
```java
class BIT {
    int[] tree;
    int n;
    BIT(int n) { this.n = n; tree = new int[n + 1]; }

    void update(int i, int delta) {      // 1-indexed
        for (; i <= n; i += i & (-i)) tree[i] += delta;
    }

    int query(int i) {                   // prefix sum [1..i]
        int sum = 0;
        for (; i > 0; i -= i & (-i)) sum += tree[i];
        return sum;
    }

    int rangeQuery(int l, int r) { return query(r) - query(l - 1); }
}
```

### Segment Tree — Range Query + Point Update O(log n)
```java
class SegTree {
    int[] tree;
    int n;
    SegTree(int[] nums) {
        n = nums.length; tree = new int[4 * n];
        build(nums, 0, 0, n - 1);
    }
    void build(int[] nums, int node, int start, int end) {
        if (start == end) { tree[node] = nums[start]; return; }
        int mid = (start + end) / 2;
        build(nums, 2*node+1, start, mid);
        build(nums, 2*node+2, mid+1, end);
        tree[node] = tree[2*node+1] + tree[2*node+2];
    }
    void update(int node, int start, int end, int idx, int val) {
        if (start == end) { tree[node] = val; return; }
        int mid = (start + end) / 2;
        if (idx <= mid) update(2*node+1, start, mid, idx, val);
        else            update(2*node+2, mid+1, end, idx, val);
        tree[node] = tree[2*node+1] + tree[2*node+2];
    }
    int query(int node, int start, int end, int l, int r) {
        if (r < start || end < l) return 0;
        if (l <= start && end <= r) return tree[node];
        int mid = (start + end) / 2;
        return query(2*node+1, start, mid, l, r) + query(2*node+2, mid+1, end, l, r);
    }
}
```

---

## Quick Decision Guide

```
Contiguous subarray/substring?
  Fixed size K         → Sliding Window Fixed
  Variable size        → Sliding Window Dynamic

Pairs / two comparisons?
  Sorted + pair sum    → Two Pointers Opposite
  Linked list          → Fast & Slow Pointers

Range sums / count subarrays?
                       → Prefix Sum (+ HashMap)

Sorted array, O(log n)?
  Exact value          → Binary Search Classic
  Min/max feasible     → Binary Search on Answer

Enumerate all solutions?
                       → Backtracking

Overlapping subproblems?
  1 sequence           → DP 1D
  2 sequences / grid   → DP 2D

Graph / Tree?
  Shortest path        → BFS / Dijkstra
  Components / paths   → DFS
  Dynamic groups       → Union Find
  Ordering             → Topological Sort

Next greater/smaller?  → Monotonic Stack
Top K / Kth?           → Heap / Quickselect
Prefix search?         → Trie
Range queries + update → BIT / Segment Tree
```

---

## Complexity Summary

| Pattern | Time | Space |
|---|---|---|
| Sliding Window | O(n) | O(1) or O(k) |
| Two Pointers | O(n) | O(1) |
| Prefix Sum | O(n) build, O(1) query | O(n) |
| HashMap | O(n) | O(n) |
| Binary Search | O(log n) | O(1) |
| Backtracking | O(2ⁿ) or O(n!) | O(n) stack |
| DP 1D | O(n) | O(n) → O(1) |
| DP 2D | O(m·n) | O(m·n) → O(n) |
| BFS | O(V+E) | O(V) |
| DFS | O(V+E) | O(V) stack |
| Dijkstra | O((V+E) log V) | O(V) |
| Monotonic Stack | O(n) | O(n) |
| Heap (Top K) | O(n log k) | O(k) |
| Quickselect | O(n) avg | O(1) |
| Union Find | O(α(n)) amortized | O(n) |
| Trie | O(L) per op | O(n·L) |
| BIT | O(log n) | O(n) |
| Segment Tree | O(log n) | O(n) |
