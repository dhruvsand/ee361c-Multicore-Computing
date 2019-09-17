package q6.ReentrantLock;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class PIncrement implements Runnable{

    private int increment_by;
    private static int value;
    private static final ReentrantLock lock = new ReentrantLock();

    private PIncrement(int increment_by){
        this.increment_by = increment_by;
    }



    public static int parallelIncrement(int c, int numThreads){

        int increment_value = 1200000/numThreads;

        value =c;
        ArrayList<Thread> threads = new ArrayList<>();
        for(int i=0;i<numThreads;i++){
            if(i==numThreads-1)
                increment_value = increment_value + 1200000-numThreads*increment_value;
            Thread t = new Thread(new q6.ReentrantLock.PIncrement(increment_value));
            threads.add(t);
            t.start();
        }
        for(int i=0; i<numThreads;i++) {
            try {
                threads.get(i).join();
            } catch (Exception e) {
            }
        }
        return value;
    }

    @Override
    public void run() {
        for (int i = 0; i < increment_by; i++) {
//            atomicValue.getAndIncrement();
            lock.lock();
            value=value+1;
            lock.unlock();
        }
    }

}
