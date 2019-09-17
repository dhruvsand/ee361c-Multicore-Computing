package lfbst;

import java.util.ArrayList;
import java.util.Random;

public class Test {

    public static void main (String[] args) {

		// variables for tests
		int numThreads = 5;
		int numOpsPerThread = 5;
		int min = 1;
		int max = 10;
		
		LockFreeBST bst = new LockFreeBST();
		Random random = new Random();
		long start, end;
		
		System.out.println("\n\n---------- Insertion Test ----------");
		
		bst = new LockFreeBST();

		int r;
		
		ArrayList<Thread> threads = new ArrayList<Thread>();
		ArrayList<Integer> insertedKeys = new ArrayList<Integer>();
		for(int x = 0; x < numThreads; x++) {
			ArrayList<Integer> values = new ArrayList<Integer>();
			for(int y = 0; y < numOpsPerThread; y++) {
				r = random.nextInt(max) + min;
				insertedKeys.add(r);
				values.add(r);
			}
			threads.add(new testInsertThread(bst, values));
		}
		
		System.out.println("Keys being inserted: " + insertedKeys + "\nStarting Insertion Test");
		start = System.nanoTime();
		for(Thread t: threads) {
			t.start();
		}
		for(Thread t: threads) {
			try {
				t.join();
			} catch (InterruptedException e) {
				System.out.println("Interrupted Exception");
			}
		}
		end = System.nanoTime();
		System.out.println("Insertion Test Finished: " + (end - start) + " ns");
		System.out.println("Resulting Leaves: " + bst.getLeaves());
		
		
		System.out.println("\n\n---------- Find Test ----------");
		
		threads = new ArrayList<Thread>();
		ArrayList<Integer> findKeys = new ArrayList<Integer>();
		for(int x = 0; x < numThreads; x++) {
			ArrayList<Integer> values = new ArrayList<Integer>();
			for(int y = 0; y < numOpsPerThread; y++) {
				r = random.nextInt(max) + min;
				findKeys.add(r);
				values.add(r);
			}
			threads.add(new findThread(bst, values));
		}
		
		System.out.println("Keys in Find Test: " + findKeys);
		start = System.nanoTime();
		for(Thread t: threads) {
			t.start();
		}
		for(Thread t: threads) {
			try {
				t.join();
			} catch (InterruptedException e) {
				System.out.println("Interrupted Exception");
			}
		}
		end = System.nanoTime();
		System.out.println("Find Test Finished: " + (end - start) + " ns");
		System.out.println("Resulting Leaves: " + bst.getLeaves());
		
		System.out.println("\n\n---------- Delete Test ----------");
		
		threads = new ArrayList<Thread>();
		ArrayList<Integer> deletedKeys = new ArrayList<Integer>();
		for(int x = 0; x < numThreads; x++) {
			ArrayList<Integer> values = new ArrayList<Integer>();
			for(int y = 0; y < numOpsPerThread; y++) {
				r = random.nextInt(max) + min;
				deletedKeys.add(r);
				values.add(r);
			}
			threads.add(new testDeleteThread(bst, values));
		}
		
		System.out.println("Leaves being deleted: " + deletedKeys);
		System.out.println("Leaves before Test: " + bst.getLeaves() + "\nDeletion Test Started");
		start = System.nanoTime();
		for(Thread t: threads) {
			t.start();
		}
		for(Thread t: threads) {
			try {
				t.join();
			} catch (InterruptedException e) {
				System.out.println("Interrupted Exception");
			}
		}
		end = System.nanoTime();
		
		System.out.println("Deletion Test Finished: " + (end - start) + " ns");
		System.out.println("Leaves after deletion test: " + bst.getLeaves());
		
    }
}