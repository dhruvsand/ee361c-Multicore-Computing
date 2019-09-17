
import java.util.Set;
import java.util.concurrent.locks.Lock;

import static org.junit.Assert.assertTrue;

public class insertThread extends Thread{
	public static int thread_id = 0;
	public int id;
	private FineGrainedRBTree rbTree;
	private Set<Integer> values;
	private boolean insertingUniqueValues;

	public insertThread(FineGrainedRBTree rbTree, Set<Integer> values,boolean insertingUniqueValues) {
		this.id = thread_id++;
		this.rbTree = rbTree;
		this.values = values;
		this.insertingUniqueValues=insertingUniqueValues;
	}

	
	@Override
	public void run() {
		for (int value : values) {
			System.out.println("Thread "+id+" add "+ value);
				if(insertingUniqueValues)
					assertTrue("Couldnt add "+value,this.rbTree.Add(value));
				else
					this.rbTree.Add(value);


		}
	}
}
