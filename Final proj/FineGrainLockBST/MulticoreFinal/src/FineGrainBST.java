import java.util.concurrent.locks.ReentrantLock;

public class FineGrainBST {
	ConcurrentNode root;
	ReentrantLock rootLock;
	
	public FineGrainBST() {
		root = null;
		rootLock = new ReentrantLock();
	}
	
	//traverse down the tree based on the data value
	//if equivalent data value is detected, do not store
	//once location is found (null location at the end of tree), data is inserted
	//need to make sure that the prevNode is always locked, so when a child is added
	//its parent is in the tree
	public boolean insert(int data) {
		
		ConcurrentNode dataNode = new ConcurrentNode(data);
		ConcurrentNode currentNode = null;
		ConcurrentNode prevNode = null;
		int compare = 0;
		boolean done = false;
		
		rootLock.lock();
		if(root != null) {
			currentNode = root;
			currentNode.lock();
			rootLock.unlock();
			while(!done) {
				compare = currentNode.getData() - data;
				prevNode = currentNode;
				if(compare < 0) {
					currentNode = currentNode.getRight();
				} else if(compare > 0) {
					currentNode = currentNode.getLeft();
				} else if(compare == 0){
					currentNode.unlock();
					return false;
				}
				
				if(currentNode != null) {
					currentNode.lock();
					prevNode.unlock();
				} else {
					done = true;
				}
			}
			if(compare < 0) {
				prevNode.setRight(dataNode);
			}
			else {
				prevNode.setLeft(dataNode);
			}	
			prevNode.unlock();
		} else {
			root = dataNode;
			rootLock.unlock();
		}
		return true;
	}

//search for the data to be removed
//once found, find the replacement data to be inserted at the location
//need to keep the prevNode locked so that the insertion place is preserved
public int delete(int data) {
		
		ConcurrentNode currentNode = root;
		ConcurrentNode prevNode = root;
		int compare = 0;
		int insertLocation = 0;
		boolean done = false;
		rootLock.lock();
		
		if(currentNode != null) {
			currentNode.lock();
			compare = currentNode.getData() - data;
			insertLocation = compare;
			if(compare < 0) {
				currentNode = currentNode.getRight();
			} else if(compare > 0) {
				currentNode = currentNode.getLeft();
			} else {
				ConcurrentNode replacement = getReplacementNode(currentNode);
				root = replacement;
				if(replacement != null) {
					replacement.setLeft(currentNode.getLeft());
					replacement.setRight(currentNode.getRight());
				}
				currentNode.unlock();
				rootLock.unlock();
				return currentNode.getData();
			}
			currentNode.lock();
			rootLock.unlock();
			
			while(!done) {
				compare = currentNode.getData() - data;
				if(compare == 0) {
					ConcurrentNode replacement = getReplacementNode(currentNode);			
					if(insertLocation > 0)
						prevNode.setLeft(replacement);
					else
						prevNode.setRight(replacement);
					
					if(replacement != null) {
						replacement.setLeft(currentNode.getLeft());
						replacement.setRight(currentNode.getRight());
					}
					
					currentNode.unlock();
					prevNode.unlock();
					return currentNode.getData();

				} else {
					prevNode.unlock();
					prevNode = currentNode;
					if(compare < 0) {
						currentNode = currentNode.getRight();
						insertLocation = compare;
					} else if(compare > 0) {
						currentNode = currentNode.getLeft();
						insertLocation = compare;
					}
				}
				if(currentNode != null) {
					currentNode.lock();
				} else {
					done = true;
				}
			}
		} else {
			rootLock.unlock();
			return -1;
		}
		prevNode.unlock();
		return -1;
	}

//find the node to replace the deleted node, delete the replacement node for usage
//if left node of subtree is not null, find right-most element
//otherwise if right of subtree is not null, find left most element 
//specified element is the replacement, set link to replacement equal to its opposing child
//return element for replacement in delete method
//remove the replacement node from the original place by setting its parent's reference to one of its children
private ConcurrentNode getReplacementNode(ConcurrentNode subTree) {	
	ConcurrentNode currentNode = null;
	ConcurrentNode prevNode = subTree;
	
	if(subTree.getRight() != null) {
		currentNode = subTree.getRight();
		currentNode.lock();
		while(currentNode.getLeft() != null) {
			if(prevNode != subTree) {
				prevNode.unlock();
			}
			prevNode = currentNode;
			currentNode = currentNode.getLeft();
			currentNode.lock();
		}
		
		if(currentNode.getRight() != null) {
			currentNode.getRight().lock();
		}
		if(prevNode != subTree) {
			prevNode.setLeft(currentNode.getRight());
			prevNode.unlock();
		}
		else {
			prevNode.setRight(currentNode.getRight());
		}
		if(currentNode.getRight() != null) {
			currentNode.getRight().unlock();
		}
		currentNode.unlock();
		return currentNode;
	}
	else if(subTree.getLeft() != null) {
		currentNode = subTree.getLeft();
		currentNode.lock();
		while(currentNode.getRight() != null) {
			if(prevNode != subTree) {
				prevNode.unlock();
			}
			prevNode = currentNode;
			currentNode = currentNode.getRight();
			currentNode.lock();
		}
		if(currentNode.getLeft() != null) {
			currentNode.getLeft().lock();
		}
		if(prevNode != subTree) {
			prevNode.setRight(currentNode.getLeft());
			prevNode.unlock();	
		}			
		else {
			prevNode.setLeft(currentNode.getLeft());
		}
		if(currentNode.getLeft() != null) {
			currentNode.getLeft().unlock();
		}
		currentNode.unlock();
		return currentNode;
	}
	return null;
}

public void printTree() {
	printTreeRecurse(root);
}

private void printTreeRecurse(ConcurrentNode currentNode) {
	if(currentNode == null){
		return;
	}
	printTreeRecurse(currentNode.getLeft());
	System.out.print(currentNode.getData() + ", ");
	printTreeRecurse(currentNode.getRight());
}

public static void main(String[] args) {
	
	FineGrainBST tree = new FineGrainBST();
	int numThreads = Integer.parseInt(args[0]);
	int maxVal = Integer.parseInt(args[1]);
	int testNum = Integer.parseInt(args[2]);
	Thread[] it = new TreeThread[numThreads];
	for(int x=0; x<numThreads; x++) {
		TreeThread t = new TreeThread(tree, x, maxVal, testNum);
		it[x] = t;
		t.start();
	}
	boolean done = false;
	while(!done) {
		done = true;
		for(Thread t: it) {
			if(t.isAlive()) {
				done = false;
			}
		}
	}
	System.out.print("\nTree contains:");
	tree.printTree();
	
}
}
