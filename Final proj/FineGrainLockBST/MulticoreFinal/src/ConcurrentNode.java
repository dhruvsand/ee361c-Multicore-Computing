import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentNode {
	private int data;
	private ReentrantLock lock;
	private ConcurrentNode left;
	private ConcurrentNode right;
	
	public ConcurrentNode(int data) {
		this.data = data;
		left = null;
		right = null;
		lock = new ReentrantLock();
	}	
	public void lock() {
		lock.lock();
	}
	public void unlock() {
		lock.unlock();
	}
	public int getData() {
		return data;
	}
	public ConcurrentNode getLeft() {
		return left;
	}
	public void setLeft(ConcurrentNode n) {
		left = n;
	}
	public ConcurrentNode getRight() {
		return right;
	}
	public void setRight(ConcurrentNode n) {
		right = n;
	}
}
