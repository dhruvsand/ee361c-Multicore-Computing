import org.junit.Test;

import java.util.Random;
import java.util.concurrent.locks.Lock;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


public class SearchThread extends Thread{
	public static int thread_id = 0;
	public int id;
	private FineGrainedRBTree rbTree;
	private int[] values;
	public SearchThread(FineGrainedRBTree rbTree, int[] values) {
		this.id = thread_id++;
		this.rbTree = rbTree;
		this.values = values;
	}
	@Test
	@Override
	public void run(){
		for (int value : values) {
			System.out.println("Thread "+id+" search "+value);
			assertTrue("Couldnt find "+value, this.rbTree.Contains(value));
			if(!this.rbTree.Contains(value)){
				System.out.println("Couldnt find "+value);
//				fail();
			}
		}
	}
}
