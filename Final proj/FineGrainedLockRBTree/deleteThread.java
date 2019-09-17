
import java.util.Set;

import static org.junit.Assert.assertTrue;

public class deleteThread extends Thread{
	public static int thread_id = 0;
	public int id;
	private FineGrainedRBTree rbTree;
	private Set<Integer> values;
	private boolean deletingingUniqueValues;

	public deleteThread(FineGrainedRBTree rbTree, Set<Integer> values, boolean insertingUniqueValues) {
		this.id = thread_id++;
		this.rbTree = rbTree;
		this.values = values;
		this.deletingingUniqueValues=insertingUniqueValues;
	}

	
	@Override
	public void run() {
		for (int value : values) {
			System.out.println("Thread "+id+" delete "+ value);
				if(deletingingUniqueValues)
					assertTrue("Couldnt delete "+value,this.rbTree.remove(value));
				else
					this.rbTree.remove(value);


		}
	}
}
