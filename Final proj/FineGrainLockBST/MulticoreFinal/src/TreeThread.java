public class TreeThread extends Thread {
	private FineGrainBST tree;
	private int offset;
	private int maxVal;
	private int testNum;
	
	public TreeThread(FineGrainBST tree, int offset, int maxVal, int testNum) {
		this.tree = tree;
		this.offset = offset;
		this.maxVal = maxVal;
		this.testNum = testNum;
	}
	
	public void run() {
		//random add and delete
		if(testNum == 0) {
			for(int x =0; x<maxVal; x++) {
				int add = ((int)(Math.random() * 5))+x;
				tree.insert(add * offset);
			}
			int deleted = tree.delete(5*offset);
		}
		//adding numbers with occasion overlap, repeatedly adding 0 to make sure only one appears
		else if(testNum == 1) {
			for(int x =0; x<maxVal; x++) {
				int add = x * offset;
				tree.insert(add);
			}
		}
		//adding numbers far apart concurrently
		else if(testNum == 2) {
			for(int x =0; x<maxVal; x++) {
				int add = x + (offset * 1000);
				tree.insert(add);
			}
		}
		
	}
}
