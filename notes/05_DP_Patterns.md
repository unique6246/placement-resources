# 05 — Dynamic Programming Patterns

> Covers: DP 1D · DP 2D — all approaches, recurrence tables, space optimization

---

## Table of Contents
- [How to Think About DP](#how-to-think-about-dp)
- [1. DP 1D — Linear Sequences](#1-dp-1d--linear-sequences)
- [2. DP 1D — Kadane's (Max Subarray)](#2-dp-1d--kadanes-max-subarray)
- [3. DP 1D — Knapsack (0/1 & Unbounded)](#3-dp-1d--knapsack-01--unbounded)
- [4. DP 1D — LIS (Longest Increasing Subsequence)](#4-dp-1d--lis-longest-increasing-subsequence)
- [5. DP 2D — Two Sequences (LCS / Edit Distance)](#5-dp-2d--two-sequences-lcs--edit-distance)
- [6. DP 2D — Grid Paths](#6-dp-2d--grid-paths)
- [7. DP 2D — Interval DP (Palindromes)](#7-dp-2d--interval-dp-palindromes)

---

## How to Think About DP

```
DP = Recursion + Memoization  OR  Tabulation (bottom-up)

Ask yourself 4 questions:
  1. What does dp[i] (or dp[i][j]) represent?
  2. What is the recurrence? (how does dp[i] depend on previous states?)
  3. What are the base cases?
  4. What is the final answer? (dp[n]? max(dp)? dp[m][n]?)

3 Implementation approaches (in order of learning):
  1. Memoization (top-down recursion + cache)    — easiest to derive
  2. Tabulation (bottom-up loop)                 — most efficient in practice
  3. Space-optimized tabulation                  — only keep last 1–2 rows
```

---

## 1. DP 1D — Linear Sequences

**Recognize:** climbing stairs, house robber, min cost, can/cannot reach, decode ways

### Approach 1 — Memoization (Top-Down) O(n) time, O(n) space
```java
int[] memo = new int[n + 1];
Arrays.fill(memo, -1);

int dp(int i) {
    if (i <= 1) return i;                          // base case
    if (memo[i] != -1) return memo[i];             // cache hit
    return memo[i] = dp(i - 1) + dp(i - 2);       // recurse + cache
}
// Call: return dp(n);
```

### Approach 2 — Tabulation (Bottom-Up) O(n) ✅
```java
int[] dp = new int[n + 1];
dp[0] = base0;   // set base cases
dp[1] = base1;

for (int i = 2; i <= n; i++)
    dp[i] = dp[i-1] + dp[i-2];   // ← ONLY THIS LINE CHANGES per problem

return dp[n];
```

### Approach 3 — Space-Optimized O(1) space ✅✅ BEST
```java
int prev2 = base0, prev1 = base1;

for (int i = 2; i <= n; i++) {
    int curr = prev1 + prev2;    // ← recurrence
    prev2 = prev1;
    prev1 = curr;
}
return prev1;
```

### 1D DP Recurrence Reference

| Problem | `dp[i]` means | Recurrence | Base Cases |
|---|---|---|---|
| **Climbing Stairs** | ways to reach step i | `dp[i-1] + dp[i-2]` | dp[1]=1, dp[2]=2 |
| **House Robber** | max money up to house i | `max(dp[i-1], dp[i-2] + nums[i])` | dp[0]=nums[0] |
| **House Robber II** (circular) | run twice: [0..n-2] and [1..n-1] | same recurrence | — |
| **Min Cost Climbing Stairs** | min cost to reach step i | `min(dp[i-1], dp[i-2]) + cost[i]` | dp[0]=cost[0] |
| **Coin Change** | min coins for amount i | `min(dp[i - coin] + 1)` for each coin | dp[0]=0 |
| **Word Break** | can s[0..i) be formed from dict | `dp[j] && dict.has(s[j..i))` for j < i | dp[0]=true |
| **Decode Ways** | ways to decode s[0..i) | 1-digit valid: `+dp[i-1]`, 2-digit valid: `+dp[i-2]` | dp[0]=1 |
| **Jump Game** | max reach from left up to i | `max(reach, i + nums[i])` → greedy | — |
| **Jump Game II** | min jumps to reach end | greedy: track current reach and next reach | — |
| **Max Product Subarray** | max product ending at i | `max(nums[i], maxPrev*nums[i], minPrev*nums[i])` | — |
| **Partition Equal Subset** | can reach sum j using items[0..i] | `dp[j] OR dp[j - nums[i]]` | dp[0]=true |

### Coin Change Full Template
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

### House Robber Full Template
```java
if (nums.length == 1) return nums[0];
int prev2 = nums[0], prev1 = Math.max(nums[0], nums[1]);

for (int i = 2; i < nums.length; i++) {
    int curr = Math.max(prev1, prev2 + nums[i]);
    prev2 = prev1;
    prev1 = curr;
}
return prev1;
```

**🔗 LeetCode Problems:**

| # | Problem | Difficulty | Use Approach |
|---|---------|:----------:|:------------:|
| LC 45  | [Jump Game II](https://leetcode.com/problems/jump-game-ii/)                                 | 🟡 Medium | Approach 2 — greedy |
| LC 55  | [Jump Game](https://leetcode.com/problems/jump-game/)                                       | 🟡 Medium | Approach 2 — greedy |
| LC 70  | [Climbing Stairs](https://leetcode.com/problems/climbing-stairs/)                           | 🟢 Easy   | Approach 3 — space optimized |
| LC 91  | [Decode Ways](https://leetcode.com/problems/decode-ways/)                                   | 🟡 Medium | Approach 2 — tabulation |
| LC 139 | [Word Break](https://leetcode.com/problems/word-break/)                                     | 🟡 Medium | Approach 2 — tabulation |
| LC 152 | [Maximum Product Subarray](https://leetcode.com/problems/maximum-product-subarray/)         | 🟡 Medium | Approach 2 — track min+max |
| LC 198 | [House Robber](https://leetcode.com/problems/house-robber/)                                 | 🟡 Medium | Approach 3 — space optimized |
| LC 213 | [House Robber II](https://leetcode.com/problems/house-robber-ii/)                           | 🟡 Medium | Approach 2 — run twice |
| LC 300 | [Longest Increasing Subsequence](https://leetcode.com/problems/longest-increasing-subsequence/) | 🟡 Medium | Approach 1 or 2 |
| LC 322 | [Coin Change](https://leetcode.com/problems/coin-change/)                                   | 🟡 Medium | Approach 2 — tabulation |
| LC 416 | [Partition Equal Subset Sum](https://leetcode.com/problems/partition-equal-subset-sum/)     | 🟡 Medium | Approach 2 — boolean dp |

---

## 2. DP 1D — Kadane's (Max Subarray)

**Recognize:** "maximum sum subarray", "maximum product subarray"

**Core idea:** At each index, decide: extend previous subarray or start fresh.

### Max Sum Subarray — Kadane's O(n)
```java
int maxSum = nums[0], currSum = nums[0];

for (int i = 1; i < nums.length; i++) {
    currSum = Math.max(nums[i], currSum + nums[i]);  // extend or restart
    maxSum  = Math.max(maxSum, currSum);
}
return maxSum;
```

### Max Product Subarray O(n)
Track both max and min (negatives flip sign).
```java
int maxProd = nums[0], minProd = nums[0], result = nums[0];

for (int i = 1; i < nums.length; i++) {
    if (nums[i] < 0) { int tmp = maxProd; maxProd = minProd; minProd = tmp; } // swap on negative
    maxProd = Math.max(nums[i], maxProd * nums[i]);
    minProd = Math.min(nums[i], minProd * nums[i]);
    result  = Math.max(result, maxProd);
}
return result;
```

**🔗 LeetCode Problems:**

| # | Problem | Difficulty | Use Approach |
|---|---------|:----------:|:------------:|
| LC 53  | [Maximum Subarray](https://leetcode.com/problems/maximum-subarray/)                               | 🟡 Medium | Kadane's |
| LC 152 | [Maximum Product Subarray](https://leetcode.com/problems/maximum-product-subarray/)               | 🟡 Medium | Max product variant |
| LC 918 | [Maximum Sum Circular Subarray](https://leetcode.com/problems/maximum-sum-circular-subarray/)     | 🟡 Medium | Kadane's + total − min |

---

## 3. DP 1D — Knapsack (0/1 & Unbounded)

### 0/1 Knapsack — O(n·W), O(W) space
Each item can be **taken or skipped** (not reused).
```java
// 2D version
int[][] dp = new int[n + 1][W + 1];
for (int i = 1; i <= n; i++)
    for (int w = 0; w <= W; w++) {
        dp[i][w] = dp[i-1][w];                              // skip item i
        if (weights[i-1] <= w)
            dp[i][w] = Math.max(dp[i][w],
                                dp[i-1][w - weights[i-1]] + values[i-1]);  // take item i
    }
return dp[n][W];

// ── Space-Optimized 1D (iterate W BACKWARDS to prevent reuse) ──
int[] dp = new int[W + 1];
for (int i = 0; i < n; i++)
    for (int w = W; w >= weights[i]; w--)              // BACKWARDS = no reuse
        dp[w] = Math.max(dp[w], dp[w - weights[i]] + values[i]);
return dp[W];
```

### Unbounded Knapsack — O(n·W)
Each item can be **used multiple times** (reuse allowed).
```java
int[] dp = new int[W + 1];
for (int w = 1; w <= W; w++)
    for (int i = 0; i < n; i++)
        if (weights[i] <= w)
            dp[w] = Math.max(dp[w], dp[w - weights[i]] + values[i]); // FORWARD = reuse OK
return dp[W];
```

### Subset Sum / Partition Target
"Can we make exactly sum S using a subset?"
```java
boolean[] dp = new boolean[target + 1];
dp[0] = true;

for (int num : nums)
    for (int j = target; j >= num; j--)   // backwards = 0/1 (no reuse)
        dp[j] = dp[j] || dp[j - num];

return dp[target];
```

**🔗 LeetCode Problems:**

| # | Problem | Difficulty | Use Approach |
|---|---------|:----------:|:------------:|
| LC 322  | [Coin Change](https://leetcode.com/problems/coin-change/)                                     | 🟡 Medium | Unbounded — min coins |
| LC 416  | [Partition Equal Subset Sum](https://leetcode.com/problems/partition-equal-subset-sum/)       | 🟡 Medium | 0/1 Knapsack boolean |
| LC 474  | [Ones and Zeroes](https://leetcode.com/problems/ones-and-zeroes/)                             | 🟡 Medium | 0/1 Knapsack 2D capacity |
| LC 494  | [Target Sum](https://leetcode.com/problems/target-sum/)                                       | 🟡 Medium | 0/1 Knapsack count ways |
| LC 518  | [Coin Change II](https://leetcode.com/problems/coin-change-ii/)                               | 🟡 Medium | Unbounded — count ways |
| LC 1049 | [Last Stone Weight II](https://leetcode.com/problems/last-stone-weight-ii/)                   | 🟡 Medium | 0/1 Knapsack partition min diff |

---

## 4. DP 1D — LIS (Longest Increasing Subsequence)

**Recognize:** "longest strictly increasing subsequence", "minimum number of chains"

### Approach 1 — DP O(n²)
```java
int[] dp = new int[n];
Arrays.fill(dp, 1);  // every element is an LIS of length 1

for (int i = 1; i < n; i++)
    for (int j = 0; j < i; j++)
        if (nums[j] < nums[i])           // strictly increasing
            dp[i] = Math.max(dp[i], dp[j] + 1);

return Arrays.stream(dp).max().getAsInt();
```

### Approach 2 — Binary Search / Patience Sort O(n log n) ✅ OPTIMAL
```java
int[] tails = new int[n];   // tails[i] = smallest tail of all LIS of length i+1
int len = 0;

for (int num : nums) {
    // binary search: where does num fit in tails?
    int pos = Arrays.binarySearch(tails, 0, len, num);
    if (pos < 0) pos = -(pos + 1);   // insertionPoint
    tails[pos] = num;
    if (pos == len) len++;            // extended the LIS
}
return len;
```

**🔗 LeetCode Problems:**

| # | Problem | Difficulty | Use Approach |
|---|---------|:----------:|:------------:|
| LC 300 | [Longest Increasing Subsequence](https://leetcode.com/problems/longest-increasing-subsequence/)                   | 🟡 Medium | Approach 1 or 2 |
| LC 354 | [Russian Doll Envelopes](https://leetcode.com/problems/russian-doll-envelopes/)                                   | 🔴 Hard   | Approach 2 |
| LC 452 | [Minimum Number of Arrows to Burst Balloons](https://leetcode.com/problems/minimum-number-of-arrows-to-burst-balloons/) | 🟡 Medium | Greedy variant |
| LC 673 | [Number of Longest Increasing Subsequence](https://leetcode.com/problems/number-of-longest-increasing-subsequence/) | 🟡 Medium | Approach 1 — count array |

---

## 5. DP 2D — Two Sequences (LCS / Edit Distance)

**Recognize:** two strings/arrays compared, "common subsequence", "edit distance", "interleaving"

**Core idea:** Rows = sequence A, Cols = sequence B. `dp[i][j]` represents the answer for `a[0..i)` and `b[0..j)`.

### Template
```java
int[][] dp = new int[m + 1][n + 1];
// fill base cases: dp[0][j] and dp[i][0]

for (int i = 1; i <= m; i++) {
    for (int j = 1; j <= n; j++) {
        if (a.charAt(i-1) == b.charAt(j-1))
            dp[i][j] = /* match case */;
        else
            dp[i][j] = /* mismatch case */;
    }
}
return dp[m][n];
```

### LCS (Longest Common Subsequence) O(m·n)
```java
int[][] dp = new int[m + 1][n + 1];
for (int i = 1; i <= m; i++)
    for (int j = 1; j <= n; j++) {
        if (a.charAt(i-1) == b.charAt(j-1))
            dp[i][j] = dp[i-1][j-1] + 1;                    // match → diagonal + 1
        else
            dp[i][j] = Math.max(dp[i-1][j], dp[i][j-1]);    // skip one character
    }
return dp[m][n];
```

### Edit Distance (Levenshtein) O(m·n)
```java
int[][] dp = new int[m + 1][n + 1];
for (int i = 0; i <= m; i++) dp[i][0] = i;   // delete all of a
for (int j = 0; j <= n; j++) dp[0][j] = j;   // insert all of b

for (int i = 1; i <= m; i++)
    for (int j = 1; j <= n; j++) {
        if (a.charAt(i-1) == b.charAt(j-1))
            dp[i][j] = dp[i-1][j-1];                        // no operation
        else
            dp[i][j] = 1 + Math.min(dp[i-1][j-1],          // replace
                           Math.min(dp[i-1][j],              // delete from a
                                    dp[i][j-1]));            // insert into a
    }
return dp[m][n];
```

### Space-Optimized LCS O(n) space
```java
int[] prev = new int[n + 1], curr = new int[n + 1];
for (int i = 1; i <= m; i++) {
    for (int j = 1; j <= n; j++) {
        if (a.charAt(i-1) == b.charAt(j-1))
            curr[j] = prev[j-1] + 1;
        else
            curr[j] = Math.max(prev[j], curr[j-1]);
    }
    int[] tmp = prev; prev = curr; curr = tmp;  // swap rows
}
return prev[n];
```

### 2D DP Reference Table

| Problem | `dp[i][j]` meaning | Match case | Mismatch case |
|---|---|---|---|
| **LCS** | LCS of a[0..i), b[0..j) | `diag + 1` | `max(up, left)` |
| **Edit Distance** | edits: a[0..i) → b[0..j) | `diag` (no op) | `1 + min(diag, up, left)` |
| **Longest Common Substring** | common substring ending at i,j | `diag + 1` | `0` |
| **Shortest Common Supersequence** | length of SCS | `diag + 1` | `1 + min(up, left)` |
| **Is Subsequence** | can a[0..i) match b[0..j)? | `diag` | `left` (skip b[j]) |
| **Wildcard Matching** | does a[0..i) match pattern[0..j) | special for `*` | — |

**🔗 LeetCode Problems:**

| # | Problem | Difficulty | Use Approach |
|---|---------|:----------:|:------------:|
| LC 10   | [Regular Expression Matching](https://leetcode.com/problems/regular-expression-matching/)       | 🔴 Hard   | 2D DP with . and * |
| LC 44   | [Wildcard Matching](https://leetcode.com/problems/wildcard-matching/)                           | 🔴 Hard   | 2D DP with * |
| LC 72   | [Edit Distance](https://leetcode.com/problems/edit-distance/)                                   | 🟡 Medium | Edit distance template |
| LC 97   | [Interleaving String](https://leetcode.com/problems/interleaving-string/)                       | 🟡 Medium | 2D DP two sources |
| LC 115  | [Distinct Subsequences](https://leetcode.com/problems/distinct-subsequences/)                   | 🔴 Hard   | LCS variant — count |
| LC 1092 | [Shortest Common Supersequence](https://leetcode.com/problems/shortest-common-supersequence/)   | 🔴 Hard   | LCS + reconstruct |
| LC 1143 | [Longest Common Subsequence](https://leetcode.com/problems/longest-common-subsequence/)         | 🟡 Medium | LCS template |

---

## 6. DP 2D — Grid Paths

**Recognize:** grid + "number of ways", "min cost path", "can we reach"

### Unique Paths O(m·n)
```java
int[][] dp = new int[m][n];
for (int i = 0; i < m; i++) dp[i][0] = 1;   // left column
for (int j = 0; j < n; j++) dp[0][j] = 1;   // top row

for (int i = 1; i < m; i++)
    for (int j = 1; j < n; j++)
        dp[i][j] = dp[i-1][j] + dp[i][j-1]; // from top + from left

return dp[m-1][n-1];

// Space-optimized O(n):
int[] dp = new int[n];
Arrays.fill(dp, 1);
for (int i = 1; i < m; i++)
    for (int j = 1; j < n; j++)
        dp[j] += dp[j-1];
return dp[n-1];
```

### Unique Paths II (with Obstacles) O(m·n)
```java
int[][] dp = new int[m][n];
// top-left start
dp[0][0] = obstacleGrid[0][0] == 1 ? 0 : 1;
// fill first column
for (int i = 1; i < m; i++)
    dp[i][0] = obstacleGrid[i][0] == 1 ? 0 : dp[i-1][0];
// fill first row
for (int j = 1; j < n; j++)
    dp[0][j] = obstacleGrid[0][j] == 1 ? 0 : dp[0][j-1];
// fill rest
for (int i = 1; i < m; i++)
    for (int j = 1; j < n; j++)
        dp[i][j] = obstacleGrid[i][j] == 1 ? 0 : dp[i-1][j] + dp[i][j-1];

return dp[m-1][n-1];
```

### Minimum Path Sum O(m·n)
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

**🔗 LeetCode Problems:**

| # | Problem | Difficulty | Use Approach |
|---|---------|:----------:|:------------:|
| LC 62  | [Unique Paths](https://leetcode.com/problems/unique-paths/)                       | 🟡 Medium | top+left sum |
| LC 63  | [Unique Paths II](https://leetcode.com/problems/unique-paths-ii/)                 | 🟡 Medium | with obstacles |
| LC 64  | [Minimum Path Sum](https://leetcode.com/problems/minimum-path-sum/)               | 🟡 Medium | min cost grid |
| LC 120 | [Triangle](https://leetcode.com/problems/triangle/)                               | 🟡 Medium | bottom-up |
| LC 174 | [Dungeon Game](https://leetcode.com/problems/dungeon-game/)                       | 🔴 Hard   | reverse DP |
| LC 221 | [Maximal Square](https://leetcode.com/problems/maximal-square/)                   | 🟡 Medium | min of 3 neighbors + 1 |

---

## 7. DP 2D — Interval DP (Palindromes)

**Recognize:** palindrome problems, "burst balloons", "matrix chain multiplication", problems on intervals [i, j]

**Core idea:** Build answers for small intervals first, then larger ones. `dp[i][j]` depends on `dp[i+1][j-1]` or `dp[i][k]` + `dp[k+1][j]`.

### Longest Palindromic Subsequence O(n²)
```java
int[][] dp = new int[n][n];
for (int i = 0; i < n; i++) dp[i][i] = 1;  // every char is palindrome of length 1

// fill diagonally (increasing length)
for (int len = 2; len <= n; len++) {
    for (int i = 0; i <= n - len; i++) {
        int j = i + len - 1;
        if (s.charAt(i) == s.charAt(j))
            dp[i][j] = dp[i+1][j-1] + 2;
        else
            dp[i][j] = Math.max(dp[i+1][j], dp[i][j-1]);
    }
}
return dp[0][n-1];
```

### Palindrome Partitioning II (Min Cuts) O(n²)
```java
boolean[][] isPalin = new boolean[n][n];
// precompute palindromes
for (int len = 1; len <= n; len++)
    for (int i = 0; i <= n - len; i++) {
        int j = i + len - 1;
        isPalin[i][j] = (s.charAt(i) == s.charAt(j))
                        && (len <= 2 || isPalin[i+1][j-1]);
    }

int[] dp = new int[n];  // dp[i] = min cuts for s[0..i]
for (int i = 0; i < n; i++) {
    if (isPalin[0][i]) { dp[i] = 0; continue; }
    dp[i] = i;  // worst case: cut every char
    for (int j = 1; j <= i; j++)
        if (isPalin[j][i])
            dp[i] = Math.min(dp[i], dp[j-1] + 1);
}
return dp[n-1];
```

**🔗 LeetCode Problems:**

| # | Problem | Difficulty | Use Approach |
|---|---------|:----------:|:------------:|
| LC 132 | [Palindrome Partitioning II](https://leetcode.com/problems/palindrome-partitioning-ii/)   | 🔴 Hard   | isPalin + min cuts |
| LC 312 | [Burst Balloons](https://leetcode.com/problems/burst-balloons/)                           | 🔴 Hard   | Interval DP last balloon |
| LC 516 | [Longest Palindromic Subsequence](https://leetcode.com/problems/longest-palindromic-subsequence/) | 🟡 Medium | Diagonal fill |
| LC 647 | [Palindromic Substrings](https://leetcode.com/problems/palindromic-substrings/)           | 🟡 Medium | Expand around center |
| LC 664 | [Strange Printer](https://leetcode.com/problems/strange-printer/)                         | 🔴 Hard   | Interval DP merge |

---

## DP Decision Guide

```
One sequence / single value range?  → DP 1D
  ├── Linear (Fibonacci-style)       → dp[i] depends on dp[i-1], dp[i-2]
  ├── Max subarray                   → Kadane's
  ├── Knapsack                       → 0/1 (backwards) or Unbounded (forwards)
  └── LIS                            → O(n²) DP or O(n log n) patience sort

Two sequences / grid?               → DP 2D
  ├── Two strings                    → LCS / Edit Distance template
  ├── Grid paths                     → from top + from left
  └── Palindromes / intervals        → expand by length, dp[i][j] → dp[i+1][j-1]

Cannot write recurrence directly?
  → Draw the dp table on paper, fill small cases by hand, spot the pattern
```

---

*← Back to [Graph & Tree](04_Graph_Tree_Patterns.md) | Next: [Advanced DS →](06_Advanced_DS_Patterns.md)*
