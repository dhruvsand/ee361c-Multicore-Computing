Run TestTrees.java, which contains main.

Implemented fine grained lock based implementation of Concurrent RedBlack Trees. Based off
Concurrent Red-Black Trees
Franck van Breugel
Department of Electrical Engineering and Computer Science, York University 4700 Keele Street, Toronto, Ontario, Canada, M3J 1P3
January 23, 2015. Sometimes search used to deadlock and insert used to give a null pointer exception but problem was never detected in the last 100ish runs. 

For test cases, we initially try to add unique elements and assert that they are added successfully. Then
we do a search and insert of other elements concurrently to see if both can be efficiently run together.
Also search is done on the previous unique elements to check if the previously inserted elements are
found.

There are variables in the main method of TestTrees.java that can be modified:
Modify run num to run the tests multiple times
Modify num treads to change the concurrent threads
Modify insert_nodes_per_threads to change how many elements the threads add
Modify the display_tree to see a visual representation of the rbtree which looks decent with a small
number of nodes, ugly with a large number of nodes, and will immensely slow down the machine if a
large amount of trees are being drawn.

The insert thread takes an argument which specifies whether the elements added are unique to the tree. If they are, then the test is run; otherwise, it is not.
In the search threads the test is always run.


Will print:

	* Number of threads used.
	* Nodes inserted by each thread.
	* Time for all insert operations to end.


Example:

 -------- Insertion Test --------
Each thread inserts: 2 nodes
Lock-Based Threads:
Thread 2 add 2
Thread 0 add 0
Thread 4 add 4
Thread 2 add 3
Thread 4 add 5
Thread 0 add 1
Lock-Based RBTree uses 4.9591225E7 ns
 -------- Search Test --------
Each thread search 10000 times
Thread 1 add 69369
Thread 1 add 52011
Thread 3 add 98770
Thread 3 add 42716
Thread 5 add 59001
Thread 5 add 20090
Thread 1 search 2
Thread 0 search 0
Thread 2 search 4
Thread 0 search 1
Thread 1 search 3
Thread 2 search 5
Lock-Based RBTree uses: 4.0 ms
Disconnected from the target VM, address: '127.0.0.1:55781', transport: 'socket'

Process finished with exit code 0


