package lfbst;

import java.util.HashSet;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

public class Node {

    public static final int CLEAN = 0;
    public static final int DFLAG = 1;
    public static final int IFLAG = 2;
    public static final int MARK = 3;

    public int key;
    public AtomicStampedReference<Info> state = null;
    public AtomicReference<Node> left, right = null;
    public boolean isLeaf;

    public Node(int key) {
		this.key = key;
		isLeaf = true;
    }
    
    public Node(int key, Node left, Node right) {
		this.key = key;
		this.left = new AtomicReference<Node>(left);
		this.right = new AtomicReference<Node>(right);
		state = new AtomicStampedReference<Info>(null,CLEAN);
		isLeaf = false;
    }

    public boolean isBST(int lowerBound, int upperBound) {
		if (key != Integer.MAX_VALUE && key != Integer.MAX_VALUE - 1) {
		    if (key < lowerBound) {
		    	return false;
		    }
		    if (key >= upperBound) {
		    	return false;
		    }
		}
	
		if (!isLeaf) {
		    Node l = left.get();
		    Node r = right.get();
		    return l.isBST(lowerBound, key) && r.isBST(key, upperBound);
		}
		return true;
    }
    
    public HashSet<Integer> getLeaves() {
		HashSet<Integer> result = new HashSet<Integer>();
		if (isLeaf) {
		    if (key != Integer.MAX_VALUE && key != Integer.MAX_VALUE - 1)
			result.add(key);
		}
		else {
		    result.addAll(left.get().getLeaves());
		    result.addAll(right.get().getLeaves());
		}
		return result;
    }
    
    public int height() {
		int lh, rh;
    	if(isLeaf) {
    		return 1;
    	}
    	else {
    		lh = left.get().height();
    		rh = right.get().height();
    	}
    	if(lh > rh) {
    		return lh + 1;
    	}
    	else {
    		return rh + 1;
    	}
    }
    
    public void printTreelevels(Node r, int l) {
    	if(l == 1) {
    		System.out.printf(" %d ", key);
    	}
    	else if(l > 1){
    		if(right.get() != null && left.get() != null) {
    			left.get().printTreelevels(r, l - 1);
    			right.get().printTreelevels(r, l - 1);
    		}
    		else if(right.get() == null && left.get() != null) {
    			left.get().printTreelevels(r, l - 1);
    		}
    		else if(right.get() != null && left.get() == null) {
    			right.get().printTreelevels(r, l - 1);
    		}
    	}
    	return;
    } 
}
	

    
    
    
    
    
