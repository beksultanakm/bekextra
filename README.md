In this project I compared a Binary Search Tree (BST) and a Hash Table. Both are written in Java by myself, without using the built-in HashMap or TreeMap. I tested insert, search and delete with 100, 1,000 and 10,000 elements.

Files:
BST.java - binary search tree
Hashtable.java - hash table
Benchmark.java - tests and time measuring
results.csv - results of the experiment

1.Implementation

BST.
Each node has a key,a left child and a right child.If the new key is smaller than the current node it goes left,if it is bigger it goes right.I wrote insert,search and delete with loops,not recursion,because with sorted data the tree gets 10,000 levels deep and recursion can give StackOverflowError.Delete has 3 cases:the node has no children (just remove it), one child (replace it with the child),two children (take the smallest key from the right subtree, copy it to this node and remove that smallest node).

Hash Table.
I used chaining.The table is an array,and each cell keeps a linked list of keys.The index is calculated by a simple hash function: `(key * 31 + 7) % table size`.If two keys get the same index (collision),they are put in the same list.The table starts with 16 cells.When it is more than 75% full,the size is doubled and all keys are placed again.

2.Time complexity

| Operation | BST best | BST worst | Hash best | Hash worst |
|---|---|---|---|---|
| Insert | O(log n) | O(n) | O(1) | O(n) |
| Search | O(log n) | O(n) | O(1) | O(n) |
| Delete | O(log n) | O(n) | O(1) | O(n) |

BST.
If the tree is balanced,its height is about log n,so every operation goes through log n nodes.The worst case is when keys are added in sorted order (1, 2, 3, ...).Then every key goes to the right and the tree becomes a line like a linked list. Height is n,so operations take O(n).

Hash Table.
If the hash function spreads keys well,every list is very short,so operations are O(1).The worst case is when all keys fall into one cell.Then it is one long list and search takes O(n).Resizing makes some inserts slow,but on average insert is still O(1).

3.Experiment

I ran the tests with n = 100, 1,000 and 10,000.I used two kinds of data: random order and sorted order (to see the worst case of BST).For every test I did 10 runs and took the average.Before the real measuring I did a warm-up, because Java is slow in the first runs (JIT).Time was measured with "System.nanoTime()".The numbers are the time for all n operations together,in milliseconds.

| Data | n | BST insert | BST search | BST delete | Hash insert | Hash search | Hash delete | BST height |
|---|---|---|---|---|---|---|---|---|
| random | 100 | 0.004 | 0.003 | 0.005 | 0.005 | 0.001 | 0.007 | 14 |
| random | 1,000 | 0.088 | 0.075 | 0.078 | 0.048 | 0.004 | 0.005 | 27 |
| random | 10,000 | 1.470 | 1.440 | 1.426 | 0.531 | 0.043 | 0.059 | 30 |
| sorted | 100 | 0.015 | 0.012 | 0.0004 | 0.003 | 0.0004 | 0.0006 | 100 |
| sorted | 1,000 | 1.440 | 1.394 | 0.004 | 0.042 | 0.005 | 0.007 | 1000 |
| sorted | 10,000 | 167.919 | 163.741 | 0.045 | 0.269 | 0.029 | 0.042 | 10000 |

4.Comparing the results

- With random data BST works fine.For n = 10,000 the height is only 30,which is close to log n.But the hash table is still faster:search is about 33 times faster (0.043 ms vs 1.44 ms).
- With sorted data BST is very slow.The height is equal to n (10,000),so the tree is just a line.Insert and search took about 165 ms,and the hash table did the same in less than 0.3 ms.This is the O(n) worst case in practice.
- BST delete is very fast on sorted data (0.045 ms).It happens because I delete keys in the order 1, 2, 3, ... and each time the deleted node is the root,which has no left child.So the program does not need to walk down the tree.It is not a real advantage of BST.
- The hash table is almost the same for random and sorted data.The time grows slowly when n grows,which matches O(1).
- For n = 100 the times are very small (thousandths of a millisecond),so the results are noisy and I would not make conclusions from them.

5.Why performance can be different in practice

- BST depends on the order of the data.If the tree is not balanced,it becomes slow.The hash table does not care about the order of keys,only about how good the hash function is.
- Hash table does a simple calculation and goes directly to the cell,but BST has to compare keys and go through many nodes. Nodes of a tree are in different places of memory,so it is also worse for the CPU cache.
- Hash table needs extra memory (empty cells,resizing).Sometimes insert is slow because of resizing.
- BST keeps keys sorted.So with BST we can easily get keys in order,find the minimum or maximum,or find a range.A hash table cannot do this.
- Time measuring in Java is not perfectly exact (JIT,garbage collector),so small differences between runs are normal.

6.Conclusion

If we only need fast insert,search and delete,the hash table is the better choice,because it is about O(1) and does not depend on the data order.BST is good when we need sorted order,but a simple BST is dangerous with sorted data,because it can become a list and take O(n).For this case a balanced tree (like AVL or Red-Black) is better.The experiment confirmed the theory:BST is fast on random data and very slow on sorted data,and the hash table is fast in both cases.
