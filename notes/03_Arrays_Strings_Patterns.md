# 03 — Arrays & Strings Patterns

> Covers: Sliding Window · Two Pointers · Prefix Sum · Binary Search · HashMap · Monotonic Stack

---

## Table of Contents
- [1. Sliding Window (Fixed Size)](#1-sliding-window-fixed-size)
- [2. Sliding Window (Dynamic Size)](#2-sliding-window-dynamic-size)
- [3. Two Pointers (Opposite Ends)](#3-two-pointers-opposite-ends)
- [4. Two Pointers (Fast & Slow)](#4-two-pointers-fast--slow)
- [5. Prefix Sum](#5-prefix-sum)
- [6. HashMap / HashSet](#6-hashmap--hashset)
- [7. Binary Search](#7-binary-search)
- [8. Monotonic Stack](#8-monotonic-stack)

---

## 1. Sliding Window (Fixed Size)

**Recognize:** "subarray/substring of **size K**", "max/min of K consecutive elements"

**Core idea:** Build first window manually. Then slide — add `nums[right]`, remove `nums[right - k]`.

### Approach 1 — Brute Force O(n·k)
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
```java
int windowSum = 0, maxSum = 0;

for (int i = 0; i < k; i++)           // build first window
    windowSum += nums[i];
maxSum = windowSum;

for (int right = k; right < nums.length; right++) {
    windowSum += nums[right];          // add new right element
    windowSum -= nums[right - k];      // remove leftmost element
    maxSum = Math.max(maxSum, windowSum);
}
return maxSum;
```

### Approach 3 — Fixed Window + HashMap (Distinct Elements) O(n)
```java
Map<Integer, Integer> window = new HashMap<>();
int windowSum = 0, maxSum = 0, left = 0;

for (int right = 0; right < nums.length; right++) {
    window.merge(nums[right], 1, Integer::sum);
    windowSum += nums[right];

    if (right - left + 1 == k) {
        if (window.size() == k)              // all distinct check
            maxSum = Math.max(maxSum, windowSum);
        windowSum -= nums[left];
        window.merge(nums[left], -1, Integer::sum);
        if (window.get(nums[left]) == 0) window.remove(nums[left]);
        left++;
    }
}
return maxSum;
```

### Approach 4 — Deque-based Window Maximum O(n)
Use when you need **max or min inside every window position**.
```java
Deque<Integer> deque = new ArrayDeque<>(); // stores INDICES
int[] result = new int[nums.length - k + 1];

for (int i = 0; i < nums.length; i++) {
    // remove indices that have fallen out of window
    while (!deque.isEmpty() && deque.peekFirst() < i - k + 1)
        deque.pollFirst();
    // maintain decreasing order (front = max)
    while (!deque.isEmpty() && nums[deque.peekLast()] < nums[i])
        deque.pollLast();
    deque.offerLast(i);
    if (i >= k - 1)
        result[i - k + 1] = nums[deque.peekFirst()];
}
return result;
```

**LeetCode Problems:**
- Max Average Subarray I — LC 643
- Contains Duplicate II — LC 219
- Find All Anagrams in a String — LC 438
- Sliding Window Maximum — LC 239

---

## 2. Sliding Window (Dynamic Size)

**Recognize:** "longest/shortest subarray with condition", "at most K distinct", "no repeating chars"

**Core idea:** `right` always expands. Shrink `left` only when window becomes **INVALID**.

### Approach 1 — Brute Force O(n²)
```java
int result = 0;
for (int i = 0; i < nums.length; i++) {
    Set<Integer> seen = new HashSet<>();
    for (int j = i; j < nums.length; j++) {
        seen.add(nums[j]);
        if (seen.size() <= k) result = Math.max(result, j - i + 1);
        else break;
    }
}
return result;
```

### Approach 2 — Expand / Shrink Template O(n) ✅ OPTIMAL
```java
Map<Integer, Integer> window = new HashMap<>();
int left = 0, result = 0;

for (int right = 0; right < nums.length; right++) {

    // ── 1. EXPAND — add right element ──
    window.merge(nums[right], 1, Integer::sum);

    // ── 2. SHRINK — fix violation (← ONLY THIS LINE CHANGES per problem) ──
    while (window.size() > k) {
        window.merge(nums[left], -1, Integer::sum);
        if (window.get(nums[left]) == 0) window.remove(nums[left]);
        left++;
    }

    // ── 3. RECORD ──
    result = Math.max(result, right - left + 1);
}
return result;
```

### Approach 3 — Minimum Window (shrink while VALID) O(n)
```java
Map<Character, Integer> need = new HashMap<>();
for (char c : t.toCharArray()) need.merge(c, 1, Integer::sum);

int left = 0, have = 0, required = need.size();
int minLen = Integer.MAX_VALUE, start = 0;
Map<Character, Integer> window = new HashMap<>();

for (int right = 0; right < s.length(); right++) {
    char c = s.charAt(right);
    window.merge(c, 1, Integer::sum);
    if (need.containsKey(c) && window.get(c).equals(need.get(c))) have++;

    while (have == required) {           // valid window → try to shrink
        if (right - left + 1 < minLen) { minLen = right - left + 1; start = left; }
        char lc = s.charAt(left++);
        window.merge(lc, -1, Integer::sum);
        if (need.containsKey(lc) && window.get(lc) < need.get(lc)) have--;
    }
}
return minLen == Integer.MAX_VALUE ? "" : s.substring(start, start + minLen);
```

### Shrink Condition Reference

| Problem | `while` shrink condition |
|---|---|
| At most K distinct elements | `window.size() > k` |
| No duplicate characters | `window.get(c) > 1` |
| Sum exceeds target | `sum > target` |
| At most K zeros (flip) | `zeroCount > k` |
| At most K replacements | `(right - left + 1) - maxFreq > k` |

**LeetCode Problems:**
- Fruit Into Baskets — LC 904
- Longest Substring Without Repeating Characters — LC 3
- Longest Substring with At Most K Distinct Characters — LC 340
- Minimum Window Substring — LC 76
- Longest Repeating Character Replacement — LC 424
- Max Consecutive Ones III — LC 1004
- Permutation in String — LC 567

---

## 3. Two Pointers (Opposite Ends)

**Recognize:** sorted array + pair/triplet sum, palindrome check, container with most water

**Core idea:** `left` starts at 0, `right` at n-1. Move `left→` when need bigger, `←right` when need smaller.

### Approach 1 — Brute Force O(n²)
```java
for (int i = 0; i < nums.length; i++)
    for (int j = i + 1; j < nums.length; j++)
        if (nums[i] + nums[j] == target) return new int[]{i, j};
```

### Approach 2 — Two Pointers (Sorted Array) O(n) ✅ OPTIMAL
```java
int left = 0, right = nums.length - 1;

while (left < right) {
    int sum = nums[left] + nums[right];
    if      (sum == target) return new int[]{left, right};
    else if (sum < target)  left++;    // need bigger  → move left right
    else                    right--;   // need smaller → move right left
}
return new int[]{-1, -1};
```

### Approach 3 — 3Sum (Fix one + two pointers) O(n²)
```java
Arrays.sort(nums);
List<List<Integer>> result = new ArrayList<>();

for (int i = 0; i < nums.length - 2; i++) {
    if (i > 0 && nums[i] == nums[i-1]) continue;   // skip duplicate fixed element
    int left = i + 1, right = nums.length - 1;

    while (left < right) {
        int sum = nums[i] + nums[left] + nums[right];
        if (sum == 0) {
            result.add(Arrays.asList(nums[i], nums[left], nums[right]));
            while (left < right && nums[left]  == nums[left+1])  left++;   // skip dups
            while (left < right && nums[right] == nums[right-1]) right--;  // skip dups
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

### Approach 5 — Trapping Rain Water O(n)
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

**LeetCode Problems:**
- Two Sum II — LC 167
- 3Sum — LC 15
- Container With Most Water — LC 11
- Valid Palindrome — LC 125
- Trapping Rain Water — LC 42
- Sort Colors (Dutch Flag) — LC 75

---

## 4. Two Pointers (Fast & Slow)

**Recognize:** linked list cycle, find middle, remove Nth from end, duplicate in array

**Core idea:** `slow` moves 1 step, `fast` moves 2 steps. Meeting = cycle. `fast` at end = `slow` at middle.

### Approach 1 — HashSet (O(n) space)
```java
Set<ListNode> seen = new HashSet<>();
while (head != null) {
    if (seen.contains(head)) return true;   // cycle!
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
    if (slow == fast) return true;   // cycle detected
}
return false;
```

### Approach 3 — Find Cycle Entry Point
```java
ListNode slow = head, fast = head;
while (fast != null && fast.next != null) {
    slow = slow.next; fast = fast.next.next;
    if (slow == fast) {
        slow = head;                         // reset one pointer to head
        while (slow != fast) { slow = slow.next; fast = fast.next; }
        return slow;                         // cycle entry node
    }
}
return null;
```

### Approach 4 — Find Middle of Linked List
```java
ListNode slow = head, fast = head;
while (fast != null && fast.next != null) {
    slow = slow.next;
    fast = fast.next.next;
}
return slow;   // slow points to middle
               // for even length, slow = second middle
```

### Approach 5 — Remove Nth Node From End O(n)
```java
ListNode dummy = new ListNode(0);
dummy.next = head;
ListNode fast = dummy, slow = dummy;

for (int i = 0; i <= n; i++) fast = fast.next;  // advance fast by n+1

while (fast != null) { slow = slow.next; fast = fast.next; }

slow.next = slow.next.next;   // skip the target node
return dummy.next;
```

**LeetCode Problems:**
- Linked List Cycle — LC 141
- Linked List Cycle II — LC 142
- Find the Duplicate Number — LC 287
- Middle of the Linked List — LC 876
- Remove Nth Node From End of List — LC 19
- Happy Number — LC 202

---

## 5. Prefix Sum

**Recognize:** "sum of subarray [i,j]" multiple times, "count subarrays with sum = K", "divisible by K"

**Core idea:** `prefix[i] = sum(nums[0..i-1])` → `sum(i,j) = prefix[j+1] - prefix[i]` in O(1).

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

### Approach 2 — Prefix Sum Array (Range Queries) O(1) query
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
prefixCount.put(0, 1);       // empty prefix has sum 0
int sum = 0, count = 0;

for (int num : nums) {
    sum += num;
    // if (sum - k) was seen before, those subarrays sum to k
    count += prefixCount.getOrDefault(sum - k, 0);
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
    sum = ((sum + num) % k + k) % k;  // handle negatives with +k
    count += modCount.getOrDefault(sum, 0);
    modCount.merge(sum, 1, Integer::sum);
}
return count;
```

### Approach 5 — 2D Prefix Sum (Grid Rectangle Queries) O(1) query
```java
int[][] prefix = new int[m+1][n+1];
for (int i = 1; i <= m; i++)
    for (int j = 1; j <= n; j++)
        prefix[i][j] = grid[i-1][j-1]
                     + prefix[i-1][j]
                     + prefix[i][j-1]
                     - prefix[i-1][j-1];

// sum of rectangle (r1,c1) to (r2,c2):
int rectSum = prefix[r2+1][c2+1]
            - prefix[r1][c2+1]
            - prefix[r2+1][c1]
            + prefix[r1][c1];
```

**LeetCode Problems:**
- Subarray Sum Equals K — LC 560
- Range Sum Query - Immutable — LC 303
- Product of Array Except Self — LC 238
- Continuous Subarray Sum — LC 523
- Subarray Sums Divisible by K — LC 974
- Count Number of Nice Subarrays — LC 1248

---

## 6. HashMap / HashSet

**Recognize:** duplicates, two sum (unsorted), anagrams, frequency problems, grouping

**Core idea:** Use map to store `value → index` for lookup, or `value → count` for frequency.

### Approach 1 — Two Sum HashMap O(n) ✅
```java
Map<Integer, Integer> seen = new HashMap<>();  // value → index

for (int i = 0; i < nums.length; i++) {
    int complement = target - nums[i];
    if (seen.containsKey(complement))
        return new int[]{seen.get(complement), i};
    seen.put(nums[i], i);
}
return new int[]{-1, -1};
```

### Approach 2 — Frequency Count O(n)
```java
// HashMap version
Map<Character, Integer> freq = new HashMap<>();
for (char c : s.toCharArray())
    freq.merge(c, 1, Integer::sum);

// int[] version (bounded chars — faster)
int[] freq = new int[26];
for (char c : s.toCharArray()) freq[c - 'a']++;
// verify anagram:
for (char c : t.toCharArray()) if (--freq[c - 'a'] < 0) return false;
return true;
```

### Approach 3 — Group Anagrams O(n·k log k)
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

### Approach 4 — Longest Consecutive Sequence O(n)
```java
Set<Integer> set = new HashSet<>();
for (int num : nums) set.add(num);

int longest = 0;
for (int num : set) {
    if (!set.contains(num - 1)) {        // only start sequences from the beginning
        int len = 1;
        while (set.contains(num + len)) len++;
        longest = Math.max(longest, len);
    }
}
return longest;
```

### Approach 5 — HashMap for Index (Track First/Last Seen) O(n)
```java
// Longest subarray with equal 0s and 1s (replace 0 with -1)
Map<Integer, Integer> firstSeen = new HashMap<>();
firstSeen.put(0, -1);    // prefix sum 0 seen at index -1
int sum = 0, maxLen = 0;

for (int i = 0; i < nums.length; i++) {
    sum += (nums[i] == 0 ? -1 : 1);
    if (firstSeen.containsKey(sum))
        maxLen = Math.max(maxLen, i - firstSeen.get(sum));
    else
        firstSeen.put(sum, i);
}
return maxLen;
```

**LeetCode Problems:**
- Two Sum — LC 1
- Group Anagrams — LC 49
- Valid Anagram — LC 242
- Top K Frequent Elements — LC 347
- First Unique Character in a String — LC 387
- Majority Element — LC 169
- Longest Consecutive Sequence — LC 128

---

## 7. Binary Search

**Recognize:** sorted array + search, "O(log n)" hinted, "minimum/maximum satisfying condition"

**Core idea:** `mid = left + (right - left) / 2` — never `(left + right) / 2` (overflow risk).

### Approach 1 — Linear Search O(n)
```java
for (int i = 0; i < nums.length; i++)
    if (nums[i] == target) return i;
return -1;
```

### Approach 2 — Classic Binary Search (Exact Value) O(log n) ✅
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

### Approach 3 — Left Boundary (First True) O(log n)
Find **leftmost** index where `condition(mid)` is true.
```java
int left = 0, right = nums.length;   // right is exclusive

while (left < right) {
    int mid = left + (right - left) / 2;
    if (condition(mid)) right = mid;   // might be answer, search left still
    else                left  = mid + 1;
}
return left;  // first index where condition is true
```

### Approach 4 — Right Boundary (Last True) O(log n)
```java
int left = 0, right = nums.length - 1;

while (left < right) {
    int mid = left + (right - left + 1) / 2;  // bias mid RIGHT to avoid infinite loop
    if (condition(mid)) left  = mid;           // mid works, might find better on right
    else                right = mid - 1;
}
return left;  // last index where condition is true
```

### Approach 5 — Binary Search on Answer O(log(range) · f(n)) ✅
Search the **answer range** instead of the array.
```java
int left = minPossibleAnswer, right = maxPossibleAnswer;

while (left < right) {
    int mid = left + (right - left) / 2;
    if (canAchieve(mid)) right = mid;    // mid works, try smaller
    else                 left  = mid + 1;
}
return left;

// Greedy check — can we achieve 'mid' as answer?
boolean canAchieve(int mid) {
    // Verify in O(n): is mid a feasible answer?
    return true; // or false
}
```

### Approach 6 — Rotated Sorted Array O(log n)
```java
int left = 0, right = nums.length - 1;

while (left <= right) {
    int mid = left + (right - left) / 2;
    if (nums[mid] == target) return mid;

    if (nums[left] <= nums[mid]) {             // LEFT half is sorted
        if (nums[left] <= target && target < nums[mid])
            right = mid - 1;
        else
            left  = mid + 1;
    } else {                                   // RIGHT half is sorted
        if (nums[mid] < target && target <= nums[right])
            left  = mid + 1;
        else
            right = mid - 1;
    }
}
return -1;
```

### Which Binary Search Template to Use

| Situation | Template | Loop condition |
|---|---|---|
| Find exact value | Classic | `left <= right` |
| Find first true | Left boundary | `left < right` |
| Find last true | Right boundary | `left < right` + mid bias +1 |
| Minimize feasible answer | Binary Search on Answer | `left < right` |

**LeetCode Problems:**
- Binary Search — LC 704
- Search in Rotated Sorted Array — LC 33
- Find Minimum in Rotated Sorted Array — LC 153
- Find Peak Element — LC 162
- Koko Eating Bananas — LC 875
- Capacity To Ship Packages Within D Days — LC 1011
- Median of Two Sorted Arrays — LC 4
- First Bad Version — LC 278

---

## 8. Monotonic Stack

**Recognize:** "next greater/smaller element", "daily temperatures", "histogram", "span/range"

**Core idea:**
- **Decreasing stack** → pop when `nums[i] > top` → finds **next greater**
- **Increasing stack** → pop when `nums[i] < top` → finds **next smaller**
- When you **pop**, the current element is the answer for the popped index.

### Approach 1 — Brute Force (Next Greater) O(n²)
```java
int[] result = new int[n];
for (int i = 0; i < n; i++) {
    result[i] = -1;
    for (int j = i + 1; j < n; j++)
        if (nums[j] > nums[i]) { result[i] = nums[j]; break; }
}
```

### Approach 2 — Monotonic Decreasing Stack (Next Greater) O(n) ✅
```java
Deque<Integer> stack = new ArrayDeque<>();  // stores INDICES
int[] result = new int[n];
Arrays.fill(result, -1);

for (int i = 0; i < n; i++) {
    while (!stack.isEmpty() && nums[stack.peek()] < nums[i])
        result[stack.pop()] = nums[i];   // nums[i] is next greater for popped
    stack.push(i);
}
return result;
```

### Approach 3 — Monotonic Increasing Stack (Next Smaller) O(n)
```java
Deque<Integer> stack = new ArrayDeque<>();
int[] result = new int[n];
Arrays.fill(result, -1);

for (int i = 0; i < n; i++) {
    while (!stack.isEmpty() && nums[stack.peek()] > nums[i])
        result[stack.pop()] = nums[i];   // nums[i] is next smaller for popped
    stack.push(i);
}
return result;
```

### Approach 4 — Circular Array (Next Greater II) O(n)
Loop twice over the array using `i % n`.
```java
Deque<Integer> stack = new ArrayDeque<>();
int[] result = new int[n];
Arrays.fill(result, -1);

for (int i = 0; i < 2 * n; i++) {
    while (!stack.isEmpty() && nums[stack.peek()] < nums[i % n])
        result[stack.pop()] = nums[i % n];
    if (i < n) stack.push(i);   // only push indices in first pass
}
return result;
```

### Approach 5 — Largest Rectangle in Histogram O(n)
```java
int[] heights = Arrays.copyOf(h, h.length + 1);  // append 0 sentinel to flush stack
Deque<Integer> stack = new ArrayDeque<>();
int maxArea = 0;

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
int[] left = new int[n], right = new int[n];
Deque<Integer> stack = new ArrayDeque<>();

// left[i] = number of subarrays where arr[i] is the minimum (left boundary)
for (int i = 0; i < n; i++) {
    while (!stack.isEmpty() && arr[stack.peek()] >= arr[i]) stack.pop();
    left[i] = stack.isEmpty() ? i + 1 : i - stack.peek();
    stack.push(i);
}
stack.clear();
// right[i] = number of subarrays where arr[i] is the minimum (right boundary)
for (int i = n - 1; i >= 0; i--) {
    while (!stack.isEmpty() && arr[stack.peek()] > arr[i]) stack.pop();
    right[i] = stack.isEmpty() ? n - i : stack.peek() - i;
    stack.push(i);
}

long ans = 0;
final long MOD = 1_000_000_007L;
for (int i = 0; i < n; i++)
    ans = (ans + (long) arr[i] * left[i] * right[i]) % MOD;
return (int) ans;
```

### Quick Reference

| Problem | Stack type | Pop when | Answer for popped |
|---|---|---|---|
| Next Greater Element | Decreasing | `nums[i] > top` | `nums[i]` |
| Next Smaller Element | Increasing | `nums[i] < top` | `nums[i]` |
| Daily Temperatures | Decreasing | `temp[i] > top` | `i - poppedIdx` |
| Largest Rectangle | Increasing | `height[i] < top` | `height * width` |

**LeetCode Problems:**
- Next Greater Element I — LC 496
- Next Greater Element II (Circular) — LC 503
- Daily Temperatures — LC 739
- Largest Rectangle in Histogram — LC 84
- Trapping Rain Water — LC 42
- Remove K Digits — LC 402
- Sum of Subarray Minimums — LC 907

---

*← Back to [Index](00_Index.md) | Next: [Graph & Tree Patterns →](04_Graph_Tree_Patterns.md)*
