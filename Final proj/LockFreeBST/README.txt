Lock-Free Binary Search Tree

This lock-free BST is leaf-oriented, which means that every internal node has exactly two children, and each leaf indicates a key currently in the dictionary.
This also means that internal nodes may or may not be included in the dictionary.
When a lock-free BST is initialized, it starts with a root with key Integer.MAX_VALUE and two nodes with keys Integer.MAX_VALUE - 1 and Integer.MAX_VALUE as its left and right children, respectively.
Therefore, the BST's "real" root will be the left child of the node with key Integer.MAX_VALUE - 1.

The main function in Test.java will run three tests.

The first test runs insert on a given number of threads, and inserts a given number of keys per thread.

The second test runs find on random numbers within the given key range.

The third test runs delete similarly to the insertion test.

These tests were also used to compare runtimes with the Lock-Based BST.
To compare runtimes, we ran three tests on 8 threads, each doing one insert operation, for 10, 100, 1000, or 10000 operations.
The results of our runtimes are attached in the folder.
