package parallel_rbtree;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestRedBlackTree {

	
	public static void main(String[] args) throws InterruptedException {
		long start = 0, end = 0, duration = 0;
		int num_threads = 2;			// Change this variable to change number of threads.
		int insert_nodes_per_thread = 5;
		
		List<Thread> lock_free_threads = new ArrayList<Thread>();
		RedBlackTree lock_free_tree = new RedBlackTree();
		
		List<Integer> nodes_inserted = new ArrayList<Integer>();
		for (int i = 0; i < num_threads; i++) {
			int[] values = new int[insert_nodes_per_thread];
			for (int j = 0; j < insert_nodes_per_thread; j++) {
				Random rand = new Random();
				int  n = rand.nextInt(1000) + 1;
				nodes_inserted.add(n);
				values[j] = n;
			}
			lock_free_threads.add(new insertThread(lock_free_tree, values));
		}
		
		System.out.println("Lock-Free Threads:");
		System.out.println(num_threads + " thread(s)");
		System.out.println(insert_nodes_per_thread + " inserted node(s) per thread");
		start = System.currentTimeMillis();
		for (Thread thread : lock_free_threads) {
			thread.start();
		}
		for (Thread thread : lock_free_threads) {
			thread.join();
		}
		end = System.currentTimeMillis();
		duration = end - start;
		System.out.println("Lock-Free RBTree Insert uses " + (double) duration + " ms");
		System.out.println("Values inserted: "+ nodes_inserted);
		System.out.println("Height of tree: " + lock_free_tree.root.height());
		
		System.out.print("LevelOrder");
		lock_free_tree.printLevelOrder(lock_free_tree.root);
		System.out.println("\nInOrder");
		lock_free_tree.inOrder(lock_free_tree.root);
		
		System.out.println("\nPreOrder");
		lock_free_tree.preOrder(lock_free_tree.root);
	
		System.out.println("\nPostOrder");
		lock_free_tree.postOrder(lock_free_tree.root);
	
	
		System.out.println("\n\n -------- Search Test --------");
		System.out.println("Each thread search 10000 times");
		
		lock_free_threads.clear();
		for (int i = 0; i < num_threads; i++) {
			lock_free_threads.add(new SearchThread(lock_free_tree, num_threads, insert_nodes_per_thread));
		}
		start = System.currentTimeMillis();
		for (Thread thread : lock_free_threads) {
			thread.start();
		}
		for (Thread thread : lock_free_threads) {
			thread.join();
		}
		end = System.currentTimeMillis();
		duration = end - start;
		System.out.println("Lock-Free RBTree Search uses " + (double) duration + " ms");
		
		System.out.println("\n -------- Delete Test --------");
		System.out.println("Each thread attempts to delete 1-10000");
		
		lock_free_threads.clear();
		for (int i = 0; i < num_threads; i++) {
			lock_free_threads.add(new DeleteThread(lock_free_tree, num_threads, insert_nodes_per_thread));
		}
		start = System.currentTimeMillis();
		for (Thread thread : lock_free_threads) {
			thread.start();
		}
		for (Thread thread : lock_free_threads) {
			thread.join();
		}
		end = System.currentTimeMillis();
		duration = end - start;
		
		
		System.out.println("Lock-Free RBTree Delete uses " + (double) duration + " ms");
	

	}
}
