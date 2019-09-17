package lfbst;

import java.util.ArrayList;

class findThread extends Thread {

    private LockFreeBST bst;
    ArrayList<Integer> F;
    Node result;

    public findThread(LockFreeBST bst, ArrayList<Integer> F) {
    	this.bst = bst;
    	this.F = F;
    }

    public void run() {
		for (int f : F) {
		    try {
		    	result = bst.find(f);
		    	if(result != null) {
			    	System.out.printf("Thread " + this.getId() + ": find(" + f + ") returns true \n Tree leaves: " + bst.getLeaves() + "\n");
		    	}
		    	else {
		    		System.out.printf("Thread " + this.getId() + ": find(" + f + ") returns false \n Tree leaves: " + bst.getLeaves() + "\n");
		    	}

		    } catch (NullPointerException e) {
		    	System.out.println("findThread: NullPointerException");
		    	System.exit(1);
		    }
		}
    }
}