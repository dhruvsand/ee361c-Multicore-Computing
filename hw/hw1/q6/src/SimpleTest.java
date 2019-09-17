import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;

public class SimpleTest {

	@Test
	public void TestTournament() {
		int res = q6.Tournament.PIncrement.parallelIncrement(0, 8);
		assertTrue("Result is " + res + ", expected result is 1200000.", res == 1200000);
	}

	@Test
	public void TestAtomicInteger() {
    	int res = q6.AtomicInteger.PIncrement.parallelIncrement(0, 8);
    	assertTrue("Result is " + res + ", expected result is 1200000.", res == 1200000);
	}

	@Test
	public void TestSynchronized() throws InterruptedException {
    	int res = q6.Synchronized.PIncrement.parallelIncrement(0, 8);
    	assertTrue("Result is " + res + ", expected result is 1200000.", res == 1200000);
	}

	@Test
	public void TestReentrantLock() {
		int res = q6.ReentrantLock.PIncrement.parallelIncrement(0, 8);
		assertTrue("Result is " + res + ", expected result is 1200000.", res == 1200000);
	}
}