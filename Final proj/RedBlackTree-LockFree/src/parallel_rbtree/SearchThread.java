package parallel_rbtree;

public class SearchThread extends Thread{
public static int thread_id =100 ;
	
	public int id;
	private RBTree rbTree;
	private int num;
	private int nodes;
	
	public SearchThread(RBTree rbTree, int num, int nodes){
		this.id = thread_id++;
		this.rbTree = rbTree;
		this.num = num;
		this.nodes = nodes;
	}
	
	
	@Override
	public void run(){
		for(int i = 0; i < 10000; i++){			
			Integer result = this.rbTree.search(i);
			if(result != null && result != Integer.MIN_VALUE){
				System.out.println("Thread "+id+" found "+i);
			}
		}
	}
}
