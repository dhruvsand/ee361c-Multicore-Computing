package lfbst;

import java.util.ArrayList;

class testInsertThread extends Thread {

    private LockFreeBST bst;
    ArrayList<Integer> I;
    boolean result;

    public testInsertThread(LockFreeBST bst, ArrayList<Integer> I) {
    	this.bst = bst;
    	this.I = I;
    }

    public void run() {
		for (int i : I) {
		    try {
		    	result = bst.insert(i);
		    	//System.out.printf("Thread " + this.getId() + ": insert(" + i + ") returns " + result + "\n Tree leaves: " + bst.getLeaves() + "\n");
		    } catch (NullPointerException e) {
		    	System.out.println("testInsertThread: NullPointerException");
		    	System.exit(1);
		    }
		}
    }
}