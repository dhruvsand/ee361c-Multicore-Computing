package parallel_rbtree;

public class DeleteThread extends Thread{
public static int thread_id =100 ;
	
	public int id;
	private RedBlackTree rbTree;
	private int num;
	private int nodes;
	
	public DeleteThread(RBTree rbTree, int num, int nodes){
		this.id = thread_id++;
		this.rbTree = (RedBlackTree) rbTree;
		this.num = num;
		this.nodes = nodes;
	}
	
	
	@Override
	public void run(){
		for(int i = 0; i < 10000; i++){	
			if(this.rbTree.delete(i)){
				System.out.println("\nThread "+id+" deleted "+i);
				System.out.println("LevelOrder");
				rbTree.printLevelOrder(rbTree.root);
			}
		}
	}
}
