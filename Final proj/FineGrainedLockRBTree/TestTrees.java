
import java.util.*;

import static org.junit.Assert.assertEquals;

public class TestTrees {

	public static void main(String[] args) throws InterruptedException {

		int runNumber=1;
		int num_threads = 30;
		int insert_nodes_per_thread = 3;
		boolean visulize_locked_tree = false;
		boolean test_delete=false;
		boolean search_test=true;

		for (int k=0;k<runNumber;k++) {

			long start = 0, end = 0, duration = 0;
			// ---- customize ----

			// ---------
			List<Thread> locked_insert_threads = new ArrayList<Thread>();
			locked_insert_threads.clear();
			List<Thread> locked_insert_threads_2 = new ArrayList<Thread>();
			locked_insert_threads_2.clear();
			List<Thread> locked_search_threads = new ArrayList<Thread>();
			locked_search_threads.clear();

			List<Thread> locked_delete_threads = new ArrayList<Thread>();
			locked_insert_threads.clear();

			FineGrainedRBTree locked_tree = new FineGrainedRBTree();

			System.out.println(" -------- Insertion Test --------");
			System.out.println("Each thread inserts: " + insert_nodes_per_thread + " nodes");
			System.out.println("Lock-Based Threads:");

			int insertNum = 0;
			for (int i = 0; i < num_threads; i++) {
				int[] values = new int[insert_nodes_per_thread];
				Set<Integer> integerSet = new HashSet<>();
				Set<Integer> integerSetIncr = new HashSet<>();

				for (int j = 0; j < insert_nodes_per_thread; j++) {
					Random rand = new Random();
					int n = rand.nextInt(100000) + 1;
					values[j] = insertNum;
					integerSet.add(insertNum);
					insertNum += 1;
					integerSetIncr.add(n + 1);
				}
				locked_insert_threads.add(new insertThread(locked_tree, integerSet, true));
				locked_delete_threads.add(new deleteThread(locked_tree, integerSet, true));

				locked_insert_threads_2.add(new insertThread(locked_tree, integerSetIncr, false));
				locked_search_threads.add(new SearchThread(locked_tree, values));
			}
			start = System.nanoTime();
			for (Thread thread : locked_insert_threads) {
				thread.start();
			}
			for (Thread thread : locked_insert_threads) {
				thread.join();
			}

			end = System.nanoTime();
			duration = end - start;
			System.out.println("Lock-Based RBTree uses " + (double) duration + " ns");


			if (test_delete) {
				System.out.println(" -------- Deletion Test --------");
				System.out.println("Each thread inserts: " + insert_nodes_per_thread + " nodes");
				System.out.println("Lock-Based Threads:");
				start = System.nanoTime();
				for (Thread thread : locked_delete_threads) {
					thread.start();
				}
				for (Thread thread : locked_delete_threads) {
					thread.join();
				}

				end = System.nanoTime();
				duration = end - start;
				System.out.println("Lock-Based RBTree delete uses " + (double) duration + " ns");

			}


			if (visulize_locked_tree) {
				locked_tree.breadth(locked_tree.root);
				LockBasedRBTreeGUI locked_tree_gui = new LockBasedRBTreeGUI(locked_tree);
			}

			//assertEquals(num_threads * insert_nodes_per_thread, locked_tree.size.get());

			if (search_test) {

				System.out.println(" -------- Search Test --------");
				System.out.println("Each thread search 10000 times");
//		locked_threads.clear();
//		for (int i = 0; i < num_threads; i++) {
//			locked_threads.add(new SearchThread(locked_tree,num_threads, insert_nodes_per_thread));
//		}
				start = System.currentTimeMillis();

				for (Thread thread : locked_insert_threads_2) {
					thread.start();
				}
				for (Thread thread : locked_search_threads) {
					thread.start();
				}
				for (Thread thread : locked_insert_threads_2) {
					thread.join();
				}
				for (Thread thread : locked_search_threads) {
					thread.join();
				}
				duration = System.currentTimeMillis() - start;
				System.out.println("Lock-Based RBTree uses: " + (double) duration + " ms");
			}
		}

	}

}
