# 01 — Foundations & Basics

> Master these before diving into any pattern. Every algorithm builds on these concepts.

---

## Table of Contents
- [B1. Big-O Complexity](#b1-big-o-complexity)
- [B2. Java Data Structures — When to Use What](#b2-java-data-structures--when-to-use-what)
- [B3. Array & String Operations](#b3-array--string-operations)
- [B4. Linked List — Node & Operations](#b4-linked-list--node--operations)
- [B5. Tree — Node & Traversals](#b5-tree--node--traversals)
- [B6. Graph — Representations & Setup](#b6-graph--representations--setup)
- [B7. Recursion & The Recursive Mindset](#b7-recursion--the-recursive-mindset)
- [B8. Sorting Quick Reference](#b8-sorting-quick-reference)
- [B9. Bit Manipulation Basics](#b9-bit-manipulation-basics)
- [B10. Math Essentials](#b10-math-essentials)

---

## B1. Big-O Complexity

### Notation Table

| Notation | Name | Example |
|---|---|---|
| O(1) | Constant | HashMap lookup, array index |
| O(log n) | Logarithmic | Binary search |
| O(n) | Linear | Single loop |
| O(n log n) | Linearithmic | Merge sort, heap sort |
| O(n²) | Quadratic | Nested loops |
| O(2ⁿ) | Exponential | Subsets, backtracking |
| O(n!) | Factorial | Permutations |

### Rules of Thumb
- Drop constants: `O(2n)` → `O(n)`
- Drop non-dominant terms: `O(n² + n)` → `O(n²)`
- Nested loops **multiply**: `O(n) × O(n)` = `O(n²)`
- Sequential steps **add**: `O(n) + O(m)` = `O(n + m)`
- Recursion with branching factor b and depth d → `O(bᵈ)`

### Input Size → Max Acceptable Complexity

| n (input size) | Max OK complexity | Pattern to use |
|---|---|---|
| n ≤ 20 | O(2ⁿ) / O(n!) | Backtracking, Bitmask DP |
| n ≤ 500 | O(n²) | DP 2D, Nested loops |
| n ≤ 10,000 | O(n²) borderline | Try O(n log n) |
| n ≤ 10⁵ | O(n log n) | Sort, Binary Search, Heap |
| n ≤ 10⁶ | O(n) | Sliding Window, HashMap, DP 1D |
| n ≤ 10⁹ | O(log n) | Binary Search only |

---

## B2. Java Data Structures — When to Use What

| Structure | Class | Add | Remove | Lookup | Use When |
|---|---|---|---|---|---|
| Dynamic Array | `ArrayList` | O(1) amortized | O(n) | O(1) | Ordered list, index access |
| Linked List | `LinkedList` | O(1) | O(1) | O(n) | Frequent insert/delete at ends |
| HashMap | `HashMap` | O(1) | O(1) | O(1) | Key→value, fast lookup |
| HashSet | `HashSet` | O(1) | O(1) | O(1) | Uniqueness / membership check |
| Stack | `Deque` (ArrayDeque) | O(1) | O(1) | O(1) | LIFO — DFS, monotonic stack |
| Queue | `Deque` (ArrayDeque) | O(1) | O(1) | O(1) | FIFO — BFS, level order |
| Min-Heap | `PriorityQueue` | O(log n) | O(log n) | O(1) peek | Top-K largest, Dijkstra |
| Max-Heap | `PriorityQueue(reverseOrder)` | O(log n) | O(log n) | O(1) peek | Top-K smallest, median |
| TreeMap | `TreeMap` | O(log n) | O(log n) | O(log n) | Sorted keys, range queries |
| TreeSet | `TreeSet` | O(log n) | O(log n) | O(log n) | Sorted unique elements |
| Deque | `ArrayDeque` | O(1) | O(1) | O(1) | Sliding window max/min |

### Quick Init Templates
```java
// ── ArrayList ──
List<Integer> list = new ArrayList<>();
list.add(x); list.get(i); list.size(); list.remove(i);

// ── HashMap ──
Map<Integer, Integer> map = new HashMap<>();
map.getOrDefault(key, 0);                           // safe get with default
map.merge(key, 1, Integer::sum);                    // increment frequency
map.computeIfAbsent(key, k -> new ArrayList<>()).add(val); // group by key
map.containsKey(k); map.put(k, v); map.remove(k);

// ── HashSet ──
Set<Integer> set = new HashSet<>();
Set<Integer> set = new HashSet<>(Arrays.asList(arr)); // from array
set.add(x); set.contains(x); set.remove(x);

// ── Stack (use Deque — never use Stack class) ──
Deque<Integer> stack = new ArrayDeque<>();
stack.push(x);    // add to top
stack.pop();      // remove from top
stack.peek();     // view top

// ── Queue (use Deque) ──
Deque<Integer> queue = new ArrayDeque<>();
queue.offer(x);   // add to back
queue.poll();     // remove from front
queue.peek();     // view front

// ── Min-Heap ──
PriorityQueue<Integer> minHeap = new PriorityQueue<>();

// ── Max-Heap ──
PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());

// ── Custom Comparator Heap ──
PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]); // by second element
PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0])); // overflow-safe

// ── TreeMap ──
TreeMap<Integer, Integer> tm = new TreeMap<>();
tm.firstKey(); tm.lastKey();
tm.floorKey(x);   // greatest key ≤ x
tm.ceilingKey(x); // smallest key ≥ x
```

---

## B3. Array & String Operations

```java
// ── Arrays ──
int[] arr = new int[n];
Arrays.fill(arr, -1);                              // fill all with -1
Arrays.fill(arr, i, j, 0);                        // fill [i, j) with 0
Arrays.sort(arr);                                  // O(n log n)
Arrays.sort(arr, 0, k);                            // sort subarray [0, k)
int[] copy = Arrays.copyOf(arr, arr.length);       // full copy
int[] copy = Arrays.copyOfRange(arr, i, j);        // copy [i, j)
int pos = Arrays.binarySearch(arr, target);        // sorted array → index or -(ip)-1
System.arraycopy(src, srcPos, dest, destPos, len); // fast array copy
Arrays.equals(a, b);                               // compare two arrays

// ── 2D Arrays ──
int[][] grid = new int[m][n];
int rows = grid.length, cols = grid[0].length;
int[][] copy = new int[m][n];
for (int[] row : grid) Arrays.fill(row, 0);        // fill 2D

// ── Strings ──
String s = "hello";
char c = s.charAt(i);                              // char at index
s.length();
s.substring(i, j);                                 // [i, j) exclusive
s.substring(i);                                    // [i, end]
s.indexOf("sub");                                  // first occurrence, -1 if missing
s.contains("sub");
s.startsWith("pre"); s.endsWith("suf");
s.toCharArray();                                    // String → char[]
String.valueOf(charArray);                          // char[] → String
s.trim();                                          // remove leading/trailing spaces
s.toLowerCase(); s.toUpperCase();
s.split(",");                                      // String[] by delimiter
s.replace('a', 'b');                               // replace char
s.equals(t); s.equalsIgnoreCase(t);
String.join("-", list);                            // join list with delimiter

// ── Reverse a string ──
String rev = new StringBuilder(s).reverse().toString();

// ── StringBuilder (use instead of + in loops) ──
StringBuilder sb = new StringBuilder();
sb.append("abc"); sb.append(c); sb.append(num);
sb.deleteCharAt(sb.length() - 1);                  // remove last char
sb.insert(i, c);                                   // insert at index
sb.reverse();
sb.toString();

// ── char ↔ int conversions ──
int val = c - '0';           // digit char '3' → int 3
int val = c - 'a';           // letter 'c' → 0-based index 2
char c  = (char)('a' + val); // index 2 → 'c'
Character.isDigit(c);
Character.isLetter(c);
Character.isAlphabetic(c);

// ── int[] as frequency map (bounded chars) ──
int[] freq = new int[26];
for (char ch : s.toCharArray()) freq[ch - 'a']++;
```

---

## B4. Linked List — Node & Operations

```java
// ── Node Definition ──
class ListNode {
    int val;
    ListNode next;
    ListNode(int val) { this.val = val; }
}

// ── ALWAYS use a DUMMY NODE to handle edge cases cleanly ──
ListNode dummy = new ListNode(0);
dummy.next = head;
ListNode curr = dummy;
// ... modify list ...
return dummy.next;   // new head

// ── Traverse ──
ListNode curr = head;
while (curr != null) {
    System.out.println(curr.val);
    curr = curr.next;
}

// ── Reverse (Iterative) ──
ListNode prev = null, cur = head;
while (cur != null) {
    ListNode next = cur.next;  // save next
    cur.next = prev;           // reverse pointer
    prev = cur;                // advance prev
    cur = next;                // advance cur
}
return prev;  // new head

// ── Reverse (Recursive) ──
ListNode reverse(ListNode head) {
    if (head == null || head.next == null) return head;
    ListNode newHead = reverse(head.next);
    head.next.next = head;   // reverse the link
    head.next = null;
    return newHead;
}

// ── Find Middle (Fast & Slow) ──
ListNode slow = head, fast = head;
while (fast != null && fast.next != null) {
    slow = slow.next;
    fast = fast.next.next;
}
// slow is now the middle

// ── Detect Cycle (Floyd's) ──
ListNode slow = head, fast = head;
while (fast != null && fast.next != null) {
    slow = slow.next;
    fast = fast.next.next;
    if (slow == fast) return true;  // cycle!
}
return false;

// ── Delete a node (prev pointer needed) ──
prev.next = prev.next.next;

// ── Common edge cases to handle ──
// • head == null
// • single node list
// • even vs odd length (for middle)
```

---

## B5. Tree — Node & Traversals

```java
// ── Node Definition ──
class TreeNode {
    int val;
    TreeNode left, right;
    TreeNode(int val) { this.val = val; }
}

// ══════════════════════════════════════
// RECURSIVE TRAVERSALS
// ══════════════════════════════════════

// Inorder: Left → Root → Right  (gives SORTED order for BST)
void inorder(TreeNode root, List<Integer> result) {
    if (root == null) return;
    inorder(root.left, result);
    result.add(root.val);       // ← process here
    inorder(root.right, result);
}

// Preorder: Root → Left → Right  (used to serialize tree)
void preorder(TreeNode root, List<Integer> result) {
    if (root == null) return;
    result.add(root.val);       // ← process here
    preorder(root.left, result);
    preorder(root.right, result);
}

// Postorder: Left → Right → Root  (used to delete tree, evaluate expressions)
void postorder(TreeNode root, List<Integer> result) {
    if (root == null) return;
    postorder(root.left, result);
    postorder(root.right, result);
    result.add(root.val);       // ← process here
}

// ══════════════════════════════════════
// ITERATIVE TRAVERSALS
// ══════════════════════════════════════

// Iterative Inorder
List<Integer> inorderIterative(TreeNode root) {
    List<Integer> result = new ArrayList<>();
    Deque<TreeNode> stack = new ArrayDeque<>();
    TreeNode curr = root;
    while (curr != null || !stack.isEmpty()) {
        while (curr != null) { stack.push(curr); curr = curr.left; }  // go left
        curr = stack.pop();
        result.add(curr.val);   // process
        curr = curr.right;      // go right
    }
    return result;
}

// Level Order (BFS) — returns list of levels
List<List<Integer>> levelOrder(TreeNode root) {
    List<List<Integer>> result = new ArrayList<>();
    if (root == null) return result;
    Queue<TreeNode> queue = new LinkedList<>();
    queue.offer(root);
    while (!queue.isEmpty()) {
        int size = queue.size();
        List<Integer> level = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            TreeNode node = queue.poll();
            level.add(node.val);
            if (node.left  != null) queue.offer(node.left);
            if (node.right != null) queue.offer(node.right);
        }
        result.add(level);
    }
    return result;
}

// ══════════════════════════════════════
// COMMON TREE HELPERS
// ══════════════════════════════════════

// Height / Depth
int height(TreeNode root) {
    if (root == null) return 0;
    return 1 + Math.max(height(root.left), height(root.right));
}

// Count Nodes
int count(TreeNode root) {
    if (root == null) return 0;
    return 1 + count(root.left) + count(root.right);
}

// Check if Balanced
boolean isBalanced(TreeNode root) {
    return checkHeight(root) != -1;
}
int checkHeight(TreeNode node) {
    if (node == null) return 0;
    int left = checkHeight(node.left);
    if (left == -1) return -1;
    int right = checkHeight(node.right);
    if (right == -1) return -1;
    if (Math.abs(left - right) > 1) return -1;
    return 1 + Math.max(left, right);
}

// ══════════════════════════════════════
// BST OPERATIONS
// ══════════════════════════════════════

// Insert
TreeNode insert(TreeNode root, int val) {
    if (root == null) return new TreeNode(val);
    if (val < root.val) root.left  = insert(root.left,  val);
    else                root.right = insert(root.right, val);
    return root;
}

// Search
boolean search(TreeNode root, int val) {
    if (root == null) return false;
    if (root.val == val) return true;
    return val < root.val ? search(root.left, val) : search(root.right, val);
}

// Lowest Common Ancestor (BST)
TreeNode lcaBST(TreeNode root, int p, int q) {
    if (p < root.val && q < root.val) return lcaBST(root.left, p, q);
    if (p > root.val && q > root.val) return lcaBST(root.right, p, q);
    return root;  // split point = LCA
}

// Lowest Common Ancestor (Binary Tree)
TreeNode lca(TreeNode root, TreeNode p, TreeNode q) {
    if (root == null || root == p || root == q) return root;
    TreeNode left  = lca(root.left,  p, q);
    TreeNode right = lca(root.right, p, q);
    if (left != null && right != null) return root;  // p and q on different sides
    return left != null ? left : right;
}
```

---

## B6. Graph — Representations & Setup

```java
// ══════════════════════════════════════
// ADJACENCY LIST (most common)
// ══════════════════════════════════════
int n = 5;
List<List<Integer>> graph = new ArrayList<>();
for (int i = 0; i < n; i++) graph.add(new ArrayList<>());

// Undirected edge
graph.get(u).add(v);
graph.get(v).add(u);

// Directed edge (u → v only)
graph.get(u).add(v);

// Weighted adjacency list (store as int[]{neighbor, weight})
List<List<int[]>> wGraph = new ArrayList<>();
for (int i = 0; i < n; i++) wGraph.add(new ArrayList<>());
wGraph.get(u).add(new int[]{v, weight});
wGraph.get(v).add(new int[]{u, weight});  // undirected

// Build from edge list input
int[][] edges = {{0,1},{1,2},{2,3}};
for (int[] e : edges) {
    graph.get(e[0]).add(e[1]);
    graph.get(e[1]).add(e[0]);   // remove for directed
}

// ══════════════════════════════════════
// ADJACENCY MATRIX (dense graphs)
// ══════════════════════════════════════
int[][] matrix = new int[n][n];
matrix[u][v] = 1;        // or weight
// O(V²) space — only use when V ≤ 1000

// ══════════════════════════════════════
// GRID AS IMPLICIT GRAPH
// ══════════════════════════════════════
int[][] dirs4 = {{0,1},{0,-1},{1,0},{-1,0}};           // 4-directional
int[][] dirs8 = {{0,1},{0,-1},{1,0},{-1,0},
                 {1,1},{1,-1},{-1,1},{-1,-1}};          // 8-directional

boolean inBounds(int r, int c, int rows, int cols) {
    return r >= 0 && r < rows && c >= 0 && c < cols;
}

// ══════════════════════════════════════
// VISITED TRACKING
// ══════════════════════════════════════
boolean[] visited = new boolean[n];      // for graph
boolean[][] seen   = new boolean[m][n];  // for grid

// ══════════════════════════════════════
// USEFUL GRAPH FACTS
// ══════════════════════════════════════
// Tree = connected graph with n nodes and n-1 edges (no cycle)
// DAG  = Directed Acyclic Graph (use for topological sort)
// BFS  = shortest path in UNWEIGHTED graph
// Dijkstra = shortest path in WEIGHTED graph (non-negative weights)
// Bellman-Ford = shortest path with NEGATIVE weights
```

---

## B7. Recursion & The Recursive Mindset

```
Every recursive function needs exactly 3 things:
  1. BASE CASE    — when to stop (prevents infinite recursion)
  2. RECURSIVE CASE — call self with a STRICTLY SMALLER problem
  3. RETURN VALUE — what to bubble back up to the caller
```

```java
// ── Generic Template ──
ReturnType solve(State state) {
    // 1. Base case
    if (baseCondition) return baseValue;

    // 2. Recurse on smaller sub-problem
    ReturnType sub = solve(smallerState);

    // 3. Combine and return
    return combine(sub, currentValue);
}

// ── Example: sum of array ──
int sum(int[] arr, int i) {
    if (i == arr.length) return 0;        // base: nothing left
    return arr[i] + sum(arr, i + 1);      // recurse + combine
}

// ── Example: power ──
double pow(double x, int n) {
    if (n == 0) return 1;
    if (n < 0)  return 1.0 / pow(x, -n);
    if (n % 2 == 0) { double half = pow(x, n/2); return half * half; }  // O(log n)
    return x * pow(x, n - 1);
}
```

### Common Recursion Patterns

| Pattern | What it does | Example |
|---|---|---|
| `return f(n-1) + f(n-2)` | Two-branch, linear depth | Fibonacci |
| `return 1 + f(node.child)` | Bubble up result | Tree depth |
| `for each choice: f(...)` | Explore all options | Backtracking |
| `return merge(f(left), f(right))` | Divide & Conquer | Merge Sort |
| `memo[n] = f(n-1) + f(n-2)` | Cache + recurse | Memoized DP |

### Stack Depth Warning
- Call stack depth = O(recursion depth)
- For `n = 10^5` recursive calls → StackOverflowError
- Fix: convert to iterative with explicit stack, or increase JVM stack size

---

## B8. Sorting Quick Reference

```java
// ── Primitive array (fastest, Dual-Pivot Quicksort) ──
Arrays.sort(arr);                                         // O(n log n)

// ── Object array with comparator ──
Arrays.sort(arr, (a, b) -> a[0] - b[0]);                 // sort by 1st element asc
Arrays.sort(arr, (a, b) -> b[1] - a[1]);                 // sort by 2nd element desc
Arrays.sort(arr, (a, b) -> a[0] != b[0]
             ? a[0] - b[0] : a[1] - b[1]);               // multi-key sort

// ── ALWAYS prefer Integer.compare to avoid overflow ──
Arrays.sort(arr, (a, b) -> Integer.compare(a, b));        // safe ascending
Arrays.sort(arr, (a, b) -> Integer.compare(b, a));        // safe descending

// ── List sort ──
Collections.sort(list);
list.sort((a, b) -> Integer.compare(a, b));

// ── Sort chars in a string ──
char[] arr = s.toCharArray();
Arrays.sort(arr);
String sorted = new String(arr);                          // use as anagram key

// ── Sort by absolute value ──
Arrays.sort(arr, (a, b) -> Math.abs(a) - Math.abs(b));

// ── Reverse sort ──
Arrays.sort(arr, Collections.reverseOrder());             // Integer[] only, not int[]
```

### When to Sort First
- **Two Pointers** → always sort first
- **Binary Search** → input must be sorted
- **Backtracking with duplicates** → sort to skip duplicates easily
- **Greedy** → usually sort by some criterion first

---

## B9. Bit Manipulation Basics

```java
// ── Basic Operators ──
x & y    // AND  — both bits must be 1
x | y    // OR   — at least one bit is 1
x ^ y    // XOR  — bits differ
~x       // NOT  — flip all bits
x << k   // left shift  = x * 2^k
x >> k   // right shift = x / 2^k (arithmetic, preserves sign)
x >>> k  // unsigned right shift (fills 0 from left)

// ── Common Tricks ──
x & 1               // 1 if odd, 0 if even
x & (x - 1)         // clear lowest set bit  → x & (x-1) == 0 means power of 2
x | (1 << k)        // set k-th bit
x & ~(1 << k)       // clear k-th bit
(x >> k) & 1        // get value of k-th bit (0 or 1)
x ^ x               // 0  (XOR with itself cancels)
x ^ 0               // x  (XOR with 0 is identity)
a ^ b ^ b           // a  (XOR cancels pairs → find single/missing element)
-x == ~x + 1        // two's complement negation

// ── Useful Library Methods ──
Integer.bitCount(x)       // number of set bits (popcount)
Integer.highestOneBit(x)  // highest power of 2 that fits
Integer.numberOfTrailingZeros(x)
Integer.toBinaryString(x)

// ── Enumerate ALL Subsets via Bitmask — O(2ⁿ · n) ──
for (int mask = 0; mask < (1 << n); mask++) {
    List<Integer> subset = new ArrayList<>();
    for (int i = 0; i < n; i++)
        if ((mask >> i & 1) == 1)    // i-th bit set = include nums[i]
            subset.add(nums[i]);
    // process subset
}

// ── XOR trick: find single element in array where all others appear twice ──
int result = 0;
for (int num : nums) result ^= num;
return result;   // all pairs cancel, only single remains

// ── Find missing number in [0..n] ──
int missing = n;
for (int i = 0; i < n; i++) missing ^= i ^ nums[i];
return missing;
```

---

## B10. Math Essentials

```java
// ── Integer Limits ──
Integer.MAX_VALUE   // 2^31 - 1 =  2,147,483,647
Integer.MIN_VALUE   // -2^31    = -2,147,483,648
Long.MAX_VALUE      // ~9.2 × 10^18  (use when overflow risk)
// Tip: whenever multiplying two ints, cast to long first: (long) a * b

// ── Modular Arithmetic (counting problems mod 10^9+7) ──
final int MOD = 1_000_000_007;
long result = ((long) a % MOD * (b % MOD)) % MOD;         // multiply
long result = ((a - b) % MOD + MOD) % MOD;                // subtract (prevent negative)
long result = (a + b) % MOD;                              // add

// ── GCD & LCM ──
int gcd(int a, int b) { return b == 0 ? a : gcd(b, a % b); }  // Euclidean
int lcm(int a, int b) { return a / gcd(a, b) * b; }            // avoid overflow

// ── Fast Exponentiation (Binary Exponentiation) O(log exp) ──
long power(long base, long exp, long mod) {
    long result = 1;
    base %= mod;
    while (exp > 0) {
        if ((exp & 1) == 1) result = result * base % mod;  // odd exponent
        base = base * base % mod;
        exp >>= 1;
    }
    return result;
}

// ── Common Math Functions ──
Math.abs(x)
Math.min(a, b); Math.max(a, b);
Math.sqrt(n);   (int) Math.sqrt(n);  // integer sqrt
Math.pow(base, exp);                 // returns double
Math.log(n);    Math.log10(n);       Math.log(n) / Math.log(2);  // log base 2
Math.floor(x);  Math.ceil(x);        Math.round(x);

// ── Ceiling Division (without float) ──
int ceil = (a + b - 1) / b;   // equivalent to Math.ceil(a / b)

// ── Number of Digits ──
int digits = (int) Math.log10(n) + 1;  // for n > 0

// ── Check Perfect Square ──
boolean isPerfectSquare(int n) {
    int sq = (int) Math.sqrt(n);
    return sq * sq == n;
}

// ── Sieve of Eratosthenes — All Primes up to n — O(n log log n) ──
boolean[] isPrime = new boolean[n + 1];
Arrays.fill(isPrime, true);
isPrime[0] = isPrime[1] = false;
for (int i = 2; (long) i * i <= n; i++)
    if (isPrime[i])
        for (int j = i * i; j <= n; j += i)
            isPrime[j] = false;

// ── Digit Sum ──
int digitSum(int n) {
    int sum = 0;
    while (n > 0) { sum += n % 10; n /= 10; }
    return sum;
}

// ── Reverse a Number ──
int reverse(int n) {
    int rev = 0;
    while (n != 0) { rev = rev * 10 + n % 10; n /= 10; }
    return rev;
}
```

---

*← Back to [Index](00_Index.md)*
