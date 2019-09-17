package lfbst;

import java.util.ArrayList;

class testDeleteThread extends Thread {

    private LockFreeBST bst;
    ArrayList<Integer> D;
    boolean result;

    public testDeleteThread(LockFreeBST bst, ArrayList<Integer> D) {
    	this.bst = bst;
		this.D = D;	
    }

    public void run() {
		for (int d : D) {
		    try {
		    	result = bst.delete(d);	
		    	//System.out.printf("Thread " + this.getId() + ": delete(" + d + ") returns " + result + "\n Tree leaves: " + bst.getLeaves() + "\n");
		    } catch (NullPointerException e) {
		    	System.out.println("testDeleteThread: NullPointerException");
		    	System.exit(1);
		    }
		}
    }
}