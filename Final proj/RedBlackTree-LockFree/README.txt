Run TestRedBlackTree, which contains main.

Implemented lock-free based implementation of Concurrent RedBlack Trees. Based off
"Lock-Free Red-Black Tress Using CAS" - Kim, Cameron, Graham, found at
(https://www.cs.umanitoba.ca/~hacamero/Research/RBTreesKim.pdf). Sometimes insert will deadlock and might need to restart, delete never has that problem.

Can change int num_threads to change number of threads used in insert, search, and delete operations.

Will print:

	* Number of threads used.
	* Nodes inserted by each thread.
	* Time for all insert operations to end.
	* LevelOrder representation of RedBlack Tree after insert
	* InOrder representation of RedBlack Tree after insert
	* PreOrder representation of RedBlack Tree after insert
	* PostOrder representation of RedBlack Tree after insert
`	* ThreadID found which integer in tree
	* ThreadID, corresponding Node deleted, and LevelOrder reperesentation after insert

Example:

Lock-Free Threads:
2 thread(s)
5 inserted node(s) per thread
Lock-Free RBTree Insert uses 1.0 ms
Values inserted: [310, 74, 163, 274, 699, 172, 482, 469, 474, 118]
Height of tree: 5
LevelOrder
Level1: 310Black 
Level2: 163Red 482Red 
Level3: 74Black 274Black 469Black 699Black 
Level4: 118Red 172Red 474Red 
InOrder
74Black 118Red 163Red 172Red 274Black 310Black 469Black 474Red 482Red 699Black 
PreOrder
310Black 163Red 74Black 118Red 274Black 172Red 482Red 469Black 474Red 699Black 
PostOrder
118Red 74Black 172Red 274Black 163Red 474Red 469Black 699Black 482Red 310Black 

 -------- Search Test --------
Each thread search 10000 times
Thread 101 found 74
Thread 101 found 118
Thread 101 found 163
Thread 101 found 172
Thread 101 found 274
Thread 101 found 310
Thread 101 found 469
Thread 101 found 474
Thread 101 found 482
Thread 101 found 699
Thread 100 found 74
Thread 100 found 118
Thread 100 found 163
Thread 100 found 172
Thread 100 found 274
Thread 100 found 310
Thread 100 found 469
Thread 100 found 474
Thread 100 found 482
Thread 100 found 699
Lock-Free RBTree Search uses 7.0 ms

 -------- Delete Test --------
Each thread attempts to delete 1-10000

Thread 100 deleted 74
LevelOrder
Level1: 310Black  
Level2: 163Red 482Red 
Level3: 118Black 274Black 469Black 699Black 
Level4: 172Red 474Red 

Thread 101 deleted 163
LevelOrder
Level1: 310Black 
Level2: 274Black 482Red 
Level3: 172Red 469Black 699Black 
Level4: 474Red 

Thread 101 deleted 172
LevelOrder
Level1: 310Black 
Level2: 274Black 482Red 
Level3: 469Black 699Black 
Level4: 474Red 

Thread 101 deleted 274
LevelOrder
Level1: 310Black 
Level2: 482Red 
Level3: 
Thread 100 deleted 118
LevelOrder

Level1: 310Black 
Level2: 482Red 
Level3: 469Black 699Black 
Level4: 474Red 469Black 
Thread 100 deleted 310
LevelOrder

Level1: 482Black 
Level2: 469Black 699Black 
Level3: 699Black 
Level4: 474Red 474Red 
Thread 100 deleted 469
LevelOrder

Level1: 482Black 
Level2: 699Black 
Thread 100 deleted 482
LevelOrder

Thread 101 deleted 474
LevelOrder

Level1: 699Black 
Thread 101 deleted 699
LevelOrder

Level1: 

699Black Lock-Free RBTree Delete uses 4.0 ms

